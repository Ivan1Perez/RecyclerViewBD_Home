package com.example.myrecyclerviewexample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.myrecyclerviewexample.base.BaseActivity;
import com.example.myrecyclerviewexample.base.CallInterface;
import com.example.myrecyclerviewexample.model.Model;
import com.example.myrecyclerviewexample.model.Oficio;
import com.example.myrecyclerviewexample.model.Usuario;
import com.google.android.material.textfield.TextInputEditText;

import java.io.Serializable;

public class UserFormActivity extends BaseActivity {

    public static enum MODE implements Serializable {
        UPDATE,CREATE
    }

    private Usuario usuario;
    private Button btnUpdate;
    private Button btnCancelar;
    private Button btnAdd;
    private TextInputEditText tietApellidos;
    private TextInputEditText tietNombre;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userform);

        MODE mode = MODE.valueOf(getIntent().getExtras().getString("mode"));

        btnUpdate = findViewById(R.id.btnUpdate);
        btnAdd = findViewById(R.id.btnAdd);
        btnCancelar = findViewById(R.id.btnCancelar);
        tietNombre = findViewById(R.id.tietNombre);
        tietApellidos = findViewById(R.id.tietApellidos);
        spinner = findViewById(R.id.spinner);

        ArrayAdapter<Oficio> myAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                Model.getInstance().getOficios());
        spinner.setAdapter(myAdapter);

        switch (mode){
            case UPDATE:
                usuario = (Usuario) getIntent().getExtras().getSerializable("user");
                tietNombre.setText(usuario.getNombre());
                tietApellidos.setText(usuario.getApellidos());
                btnAdd.setVisibility(View.GONE);
                break;
            case CREATE:
                btnUpdate.setVisibility(View.GONE);
                break;

        }

        btnCancelar.setOnClickListener(
                view -> finish()
        );

        btnUpdate.setOnClickListener(
                v-> {
                    showProgress();
                    executeCall(new CallInterface() {
                        @Override
                        public void doInBackground() {

                        }

                        @Override
                        public void doInUI() {
                            hideProgress();
                            finish();
                        }
                    });
                }
        );

        btnAdd.setOnClickListener(
                v -> {
                    if(tietNombre.getText().length()==0 || tietApellidos.getText().length()==0){
                        Toast.makeText(this,"Por favor, rellene los dos campos.",Toast.LENGTH_SHORT).show();
                    }else{
                        showProgress();
                        executeCall(new CallInterface() {
                            @Override
                            public void doInBackground() {
                                boolean addSuccessful;
                                Intent i = new Intent();
                                String nombre = tietNombre.getText().toString();
                                String apellidos = tietApellidos.getText().toString();
                                Oficio oficio = (Oficio) spinner.getSelectedItem();

                                Usuario usuario = new Usuario(nombre, apellidos, oficio.getIdOficio());
                                addSuccessful = Model.getInstance().addUsuario(usuario);
                                i.putExtra("addSuccessful", addSuccessful);

                                setResult(RESULT_OK,i);

                            }

                            @Override
                            public void doInUI() {
                                hideProgress();
                                finish();
                            }
                        });
                    }

                }
        );

    }

}