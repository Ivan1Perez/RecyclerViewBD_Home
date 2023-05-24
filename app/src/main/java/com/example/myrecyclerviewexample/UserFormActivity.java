package com.example.myrecyclerviewexample;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.myrecyclerviewexample.API.Connector;
import com.example.myrecyclerviewexample.base.BaseActivity;
import com.example.myrecyclerviewexample.base.CallInterface;
import com.example.myrecyclerviewexample.model.Oficio;
import com.example.myrecyclerviewexample.model.Usuario;
import com.google.android.material.textfield.TextInputEditText;

import java.io.Serializable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
    protected ExecutorService executor = Executors.newSingleThreadExecutor();
    protected Handler handler = new Handler(Looper.getMainLooper());

    protected void executeCall(CallInterface callInterface){
        executor.execute(() -> {
            callInterface.doInBackground();
            handler.post(() -> {
                callInterface.doInUI();
            });
        });
    }

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


        executeCall(new CallInterface() {
            ArrayAdapter<Oficio> myAdapter;
            @Override
            public void doInBackground() {
                myAdapter = new ArrayAdapter<>(
                        UserFormActivity.this,
                        android.R.layout.simple_spinner_item,
                        Connector.getConector().getAsList(Oficio.class, "oficios")
                );
            }

            @Override
            public void doInUI() {
                spinner.setAdapter(myAdapter);
            }
        });


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
                    if(tietNombre.getText().length()==0 || tietApellidos.getText().length()==0){
                        Toast.makeText(this,"Por favor, rellene los dos campos.",Toast.LENGTH_SHORT).show();
                    }else{
                        showProgress();
                        executeCall(new CallInterface() {
                            @Override
                            public void doInBackground() {
                                Object apiResponse;
                                boolean addSuccessful = false;
                                String operationToast;
                                Intent i = new Intent();
                                String nombre = tietNombre.getText().toString();
                                String apellidos = tietApellidos.getText().toString();
                                Oficio oficio = (Oficio) spinner.getSelectedItem();

                                Usuario usuarioModificado = new Usuario(usuario.getIdUsuario(), nombre, apellidos, oficio.getIdOficio());

                                apiResponse = Connector.getConector().put(Usuario.class, usuarioModificado, "/usuarios");

                                if(apiResponse!=null){
                                    operationToast = "Usuario actualizado con éxito";
                                    addSuccessful = true;
                                }else{
                                    operationToast = "Error al actualizar el usuario";
                                }

                                i.putExtra("addSuccessful", addSuccessful);
                                i.putExtra("operationToast", operationToast);

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

        btnAdd.setOnClickListener(
                v -> {
                    if(tietNombre.getText().length()==0 || tietApellidos.getText().length()==0){
                        Toast.makeText(this,"Por favor, rellene los dos campos.",Toast.LENGTH_SHORT).show();
                    }else{
                        showProgress();
                        executeCall(new CallInterface() {
                            @Override
                            public void doInBackground() {
                                Object apiResponse;
                                boolean addSuccessful = false;
                                String operationToast;
                                Intent i = new Intent();
                                String nombre = tietNombre.getText().toString();
                                String apellidos = tietApellidos.getText().toString();
                                Oficio oficio = (Oficio) spinner.getSelectedItem();

                                Usuario usuario = new Usuario(nombre, apellidos, oficio.getIdOficio());
                                apiResponse = Connector.getConector().post(Usuario.class, usuario, "/usuarios");

                                if(apiResponse!=null){
                                    operationToast = "Usuario añadido con éxito";
                                    addSuccessful = true;
                                }else{
                                    operationToast = "Error al añadir el usuario";
                                }

                                i.putExtra("addSuccessful", addSuccessful);
                                i.putExtra("operationToast", operationToast);

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