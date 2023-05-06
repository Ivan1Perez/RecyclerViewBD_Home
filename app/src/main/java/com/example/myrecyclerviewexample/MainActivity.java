package com.example.myrecyclerviewexample;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.myrecyclerviewexample.base.BaseActivity;
import com.example.myrecyclerviewexample.base.CallInterface;
import com.example.myrecyclerviewexample.model.Model;
import com.example.myrecyclerviewexample.model.Usuario;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener, CallInterface {

    private RecyclerView recyclerView;
    private MyRecyclerViewAdapter myRecyclerViewAdapter;
    private ActivityResultLauncher<Intent> detailActivityLauncher;

    private boolean isUpdating;

    private FloatingActionButton addUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler);
        addUser = findViewById(R.id.addUser);
        isUpdating = false;


        myRecyclerViewAdapter = new MyRecyclerViewAdapter(this);
        myRecyclerViewAdapter.setOnClickListener(this);
        recyclerView.setAdapter(myRecyclerViewAdapter);

        LinearLayoutManager myLinearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(myLinearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, RecyclerView.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        detailActivityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    boolean addSuccessful;

                    if(result.getResultCode()== Activity.RESULT_OK){
                        myRecyclerViewAdapter.setUsuarios(Model.getInstance().getUsuarios());
                        myRecyclerViewAdapter.notifyDataSetChanged();

                        addSuccessful = getIntent().getBooleanExtra("addSuccessful", true);

                        if(addSuccessful){
                            Toast.makeText(this,"Usuario añadido con éxito",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(this,"Error al añadir el usuario",Toast.LENGTH_SHORT).show();
                        }

                    }
                    else{
                        Toast.makeText(this,"Cancelado por el usuario",Toast.LENGTH_SHORT).show();
                    }
                }
        );

        addUser.setOnClickListener(v -> {
            Intent i = new Intent(this, UserFormActivity.class);
            i.putExtra("mode",UserFormActivity.MODE.CREATE.toString());
            detailActivityLauncher.launch(i);
        });

        showProgress();
        executeCall(this);
    }

    @Override
    public void onClick(View view) {
        Usuario u = Model.getInstance().getUsuarios().get(recyclerView.getChildAdapterPosition(view));

        Intent intent = new Intent(getApplicationContext(),UserFormActivity.class);
        intent.putExtra("mode",UserFormActivity.MODE.UPDATE.toString());
        intent.putExtra("user",u);
        detailActivityLauncher.launch(intent);

//        Toast.makeText(this,"Clic en " + u.getOficio(),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void doInBackground() {

        Model.getInstance().getUsuarios();
        Model.getInstance().getOficios();

    }

    @Override
    public void doInUI() {
        hideProgress();
        List<Usuario> usuarioList = Model.getInstance().getUsuarios();
        myRecyclerViewAdapter.setUsuarios(usuarioList);
    }
}