package com.example.agenda_sqlite;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;


/**
 * Created by
 */

public class Agregar extends Activity {
    DB usuarios;
    String accion="nuevo";
    String id="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agregar);
        mostrardatos();
    }

    public void mostrardatos(){
        try {
            Bundle bundle = getIntent().getExtras();
            accion = bundle.getString("accion");
            if (accion.equals("modificar")) {
                id = bundle.getString("id");

                String user[] = bundle.getStringArray("user");

                TextView tempVal = (TextView) findViewById(R.id.txtNombre);
                tempVal.setText(user[0].toString());

                tempVal = (TextView) findViewById(R.id.txtDireccion);
                tempVal.setText(user[1].toString());

                tempVal = (TextView) findViewById(R.id.txtTelefono);
                tempVal.setText(user[2].toString());
            }
        }catch (Exception e){
            Toast.makeText(Agregar.this, "Error: "+ e.getMessage().toString(), Toast.LENGTH_LONG).show();
        }
    }

    public void guardar_amigo(View v){
        try{
            TextView tempVal = (TextView)findViewById(R.id.txtNombre);
            String nom = tempVal.getText().toString();

            tempVal = (TextView)findViewById(R.id.txtDireccion);
            String dir = tempVal.getText().toString();

            tempVal = (TextView)findViewById(R.id.txtTelefono);
            String tel = tempVal.getText().toString();

            usuarios = new DB(Agregar.this, "",null,1);
            usuarios.guardarUsuario(nom, dir, tel, accion, id);

            Toast.makeText(Agregar.this, "Listo, amigo registrado con exito", Toast.LENGTH_LONG).show();

            Intent imostrar= new Intent(Agregar.this, Agenda.class);
            startActivity(imostrar);
        }catch(Exception ex){
            Toast.makeText(Agregar.this, "Error: "+ ex.getMessage().toString(), Toast.LENGTH_LONG).show();
        }
    }
}
