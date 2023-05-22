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
import android.view.View;
import android.widget.Toast;

import com.example.myrecyclerviewexample.API.Connector;
import com.example.myrecyclerviewexample.base.BaseActivity;
import com.example.myrecyclerviewexample.base.CallInterface;
import com.example.myrecyclerviewexample.model.Oficio;
import com.example.myrecyclerviewexample.model.Usuario;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

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

                    List<Usuario> usuarios;
                    Usuario usuario;
                    @Override
                    public void doInBackground() {

                        usuarios = Connector.getConector().getAsList(Usuario.class,"usuarios");
                        Connector.getConector().delete(Usuario.class, "/usuarios/" + position);

                    }

                    @Override
                    public void doInUI() {
                        hideProgress();
                        myRecyclerViewAdapter.notifyItemRemoved(position);
                        usuario = usuarios.get(position);
                        Snackbar.make(recyclerView, "Deleted " + usuario.getNombre(), Snackbar.LENGTH_LONG)
                                .setAction("Undo", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        showProgress();
                                        executeCall(new CallInterface() {
                                            @Override
                                            public void doInBackground() {
                                                Connector.getConector().post(Usuario.class, usuario, "/usuarios");
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
                    boolean operationSuccessful = false;
                    String operationToast = "";

                    if (result.getResultCode() == Activity.RESULT_OK) {
                        myRecyclerViewAdapter.notifyDataSetChanged();

                        Intent data = result.getData();
                        if (data != null) {
                            operationSuccessful = data.getBooleanExtra("addSuccessful", false);
                            operationToast = data.getStringExtra("operationToast");
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

        Intent intent = new Intent(getApplicationContext(), UserFormActivity.class);
        intent.putExtra("mode", UserFormActivity.MODE.UPDATE.toString());
        intent.putExtra("user", u);
        intent.putExtra("oficio", u.getOficio());
        detailActivityLauncher.launch(intent);

//        Toast.makeText(this,"Clic en " + u.getOficio(),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void doInBackground() {

        usuarioList = Connector.getConector().getAsList(Usuario.class,"usuarios");
        oficioList = Connector.getConector().getAsList(Oficio.class, "oficios");

    }

    @Override
    public void doInUI() {
        hideProgress();
        myRecyclerViewAdapter.setUsuarios(usuarioList);
    }
}