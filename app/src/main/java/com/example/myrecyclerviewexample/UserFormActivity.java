package com.example.myrecyclerviewexample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

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
    private Button btnSave;
    private Button btnCancelar;
    private Button btnCrear;
    private TextInputEditText tietApellidos;
    private TextInputEditText tietNombre;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userform);

        MODE mode = MODE.valueOf(getIntent().getExtras().getString("mode"));

        btnSave = findViewById(R.id.btnSave);
        btnCrear = findViewById(R.id.btnCrear);
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
                btnCrear.setVisibility(View.GONE);
                break;
            case CREATE:
                btnSave.setVisibility(View.GONE);
                break;

        }

        btnCancelar.setOnClickListener(
                view -> finish()
        );

        btnSave.setOnClickListener(
                v-> {
                    showProgress();
                    executeCall(new CallInterface() {
                        @Override
                        public void doInBackground() {
                            Intent i = new Intent();
                        }

                        @Override
                        public void doInUI() {
                            hideProgress();

                        }
                    });
                }
        );

        btnCrear.setOnClickListener(
                v -> {
                    showProgress();
                    executeCall(new CallInterface() {
                        @Override
                        public void doInBackground() {

                        }

                        @Override
                        public void doInUI() {
                            hideProgress();
                        }
                    });
                }
        );

    }

}