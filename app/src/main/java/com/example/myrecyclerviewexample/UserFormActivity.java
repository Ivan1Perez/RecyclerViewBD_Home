package com.example.myrecyclerviewexample;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.myrecyclerviewexample.base.BaseActivity;
import com.example.myrecyclerviewexample.base.CallInterface;
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