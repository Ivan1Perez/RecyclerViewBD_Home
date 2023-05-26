package com.example.myrecyclerviewexample;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.myrecyclerviewexample.API.Connector;
import com.example.myrecyclerviewexample.base.BaseActivity;
import com.example.myrecyclerviewexample.base.CallInterface;
import com.example.myrecyclerviewexample.model.Oficio;
import com.example.myrecyclerviewexample.model.Usuario;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MainActivity extends BaseActivity implements View.OnClickListener, CallInterface {

    private RecyclerView recyclerView;
    private MyRecyclerViewAdapter myRecyclerViewAdapter;
    private ActivityResultLauncher<Intent> detailActivityLauncher;
    private FloatingActionButton addUser;
    private List<Usuario> usuarioList;
    private List<Oficio> oficioList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usuarioList = new ArrayList<>();
        oficioList = new ArrayList<>();

        recyclerView = findViewById(R.id.recycler);
        addUser = findViewById(R.id.addUser);

        myRecyclerViewAdapter = new MyRecyclerViewAdapter(this);
        myRecyclerViewAdapter.setOnClickListener(this);
        recyclerView.setAdapter(myRecyclerViewAdapter);

        LinearLayoutManager myLinearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(myLinearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, RecyclerView.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                showProgress();
                executeCall(new CallInterface() {

                    Usuario usuario;
                    @Override
                    public void doInBackground() {

                        usuario = usuarioList.get(position);
                        Log.d("posicion",String.valueOf(position));
                        Log.d("usuarioList size",String.valueOf(usuarioList.size()));
                        Connector.getConector().delete(Usuario.class, "usuarios/" + usuario.getIdUsuario());
                        usuarioList.remove(position);

                    }

                    @Override
                    public void doInUI() {
                        hideProgress();
                        myRecyclerViewAdapter.notifyItemRemoved(position);

                        Snackbar.make(recyclerView, "Deleted " + usuario.getNombre(), Snackbar.LENGTH_LONG)
                                .setAction("Undo", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        showProgress();
                                        executeCall(new CallInterface() {
                                            @Override
                                            public void doInBackground() {
                                                Connector.getConector().post(Usuario.class, usuario, "usuarios");
                                                usuarioList.add(position,usuario);
                                            }

                                            @Override
                                            public void doInUI() {
                                                hideProgress();
                                                myRecyclerViewAdapter.notifyItemInserted(position);
                                            }
                                        });
                                    }
                                }).show();
                    }
                });

            }
        });

        itemTouchHelper.attachToRecyclerView(recyclerView);

        detailActivityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {

                    if (result.getResultCode() == Activity.RESULT_OK) {
                        boolean operationSuccessful = false;
                        String operationToast;
                        String operationCase;
                        Usuario usuarioRecibido;

                        operationToast = "Error al actualizar el usuario";

                        Intent data = result.getData();
                        if (data != null) {
                            operationSuccessful = data.getBooleanExtra("addSuccessful", false);
                            operationToast = data.getStringExtra("operationToast");
                            operationCase = data.getStringExtra("operationCase");

                            if(operationCase.equals("updateUser")){
                                usuarioRecibido = (Usuario) data.getSerializableExtra("usuarioActualizado");

                                //Borramos el usuario desactualizado de la tabla de usuarios y añadimos el actualizado en
                                //su posición.
                                int index = usuarioList.indexOf(usuarioRecibido);
                                if (index != -1) {
                                    usuarioList.remove(index);
                                    usuarioList.add(index, usuarioRecibido);
                                }
                            }else{
                                usuarioRecibido = (Usuario) data.getSerializableExtra("usuarioAdded");
                                usuarioList.add(usuarioRecibido);
                            }

                            myRecyclerViewAdapter.notifyDataSetChanged();

                        }

                        if (operationSuccessful) {
                            Toast.makeText(this, operationToast, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, operationToast, Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(this, "Cancelado por el usuario", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        addUser.setOnClickListener(v -> {
            Intent i = new Intent(this, UserFormActivity.class);
            i.putExtra("mode", UserFormActivity.MODE.CREATE.toString());
            detailActivityLauncher.launch(i);
        });

        showProgress();
        executeCall(this);
    }

    @Override
    public void onClick(View view) {
        Usuario u = usuarioList.get(recyclerView.getChildAdapterPosition(view));

        //Para pasar el oficio primero recogemos la lista de oficios ya creada en 'myRecyclerView'
        Oficio o = myRecyclerViewAdapter.getOficios().get(u.getIdOficio()-1);

        Intent intent = new Intent(getApplicationContext(), UserFormActivity.class);
        intent.putExtra("mode", UserFormActivity.MODE.UPDATE.toString());
        intent.putExtra("oficio", o);
        intent.putExtra("user", u);
        detailActivityLauncher.launch(intent);

//        Toast.makeText(this,"Clic en " + u.getOficio(),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void doInBackground() {

        usuarioList = Connector.getConector().getAsList(Usuario.class,"usuarios");
//        oficioList = Connector.getConector().getAsList(Oficio.class, "oficios");

    }

    @Override
    public void doInUI() {
        hideProgress();
        myRecyclerViewAdapter.setUsuarios(usuarioList);
    }

}