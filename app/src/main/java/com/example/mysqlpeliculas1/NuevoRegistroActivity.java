package com.example.mysqlpeliculas1;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;


public class NuevoRegistroActivity extends AppCompatActivity {
    private EditText edt_reg_email, edt_reg_clave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_registro);

        edt_reg_email = (EditText)findViewById(R.id.edt_login_email);
        edt_reg_clave = (EditText)findViewById(R.id.edt_login_clave);

        //entrar en la base de datos
    }

    public void registro(View view) {
        String email = String.valueOf(edt_reg_email.getText()).trim();
        String password = String.valueOf(edt_reg_clave.getText());


    }
}