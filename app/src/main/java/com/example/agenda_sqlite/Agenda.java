package com.example.agenda_sqlite;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.database.Cursor;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class Agenda extends Activity {
    DB db;
    public Cursor c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agenda);
        obtenerDatos();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflate = getMenuInflater();
        inflate.inflate(R.menu.menu_agenda, menu);

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
        c.moveToPosition( info.position );
        menu.setHeaderTitle(c.getString(1));
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnxModificar:
                try {
                    String user[] = {
                            c.getString(1),
                            c.getString(2),
                            c.getString(3)
                    };
                    Bundle bundle = new Bundle();
                    bundle.putString("accion", "modificar");
                    bundle.putString("id", c.getString(0));
                    bundle.putStringArray("user", user);

                    Intent iusuario = new Intent(Agenda.this, Agregar.class);
                    iusuario.putExtras(bundle);
                    startActivity(iusuario);
                }
                catch (Exception e){
                    Toast.makeText(Agenda.this, "Error: "+ e.getMessage().toString(), Toast.LENGTH_LONG).show();
                }
                return true;

            case R.id.mnxEliminar:
                AlertDialog confirmacion = eliminar();
                confirmacion.show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }

    }

    public AlertDialog eliminar(){
        AlertDialog.Builder confirmacion = new AlertDialog.Builder(Agenda.this);

        confirmacion.setTitle( c.getString(1) );
        confirmacion.setMessage("Esta Seguro de Eliminar el registgro?");

        confirmacion.setPositiveButton("Si", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
// TODO Auto-generated method stub
                db.eliminarUsuario( c.getString(0) );
                dialog.cancel();
                Toast.makeText(Agenda.this, "El registro se elimino satisfactoriamente.", Toast.LENGTH_LONG).show();
            }
        });

        confirmacion.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override

            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                Toast.makeText(Agenda.this, "Accion cancelado por el usuario.",        Toast.LENGTH_LONG).show();
            }
        } );
        return confirmacion.create();
    }

    public void obtenerDatos(){
        db = new DB(Agenda.this, "", null, 1);
        c = db.consultarUsuarios();
        if(c.moveToFirst()){ //hay registro que mostrar

//hacer referencia al ListView
            ListView ltsUser = (ListView)findViewById(R.id.ltsAmigos);

//crear un array de tipo list
            final ArrayList<String> alUsers = new ArrayList<String>();

//crear una array de tipo adaptador
            final ArrayAdapter<String> aaUsers = new ArrayAdapter<String>(Agenda.this, android.R.layout.simple_list_item_1, alUsers);
            ltsUser.setAdapter(aaUsers);

            do{
                alUsers.add(c.getString(1));
            }while(c.moveToNext());
            aaUsers.notifyDataSetChanged();

            registerForContextMenu(ltsUser);//hacemos referencia al listview para que muestre el menu.
        }else{
            //no hay registros que mostrar
            Toast.makeText(Agenda.this, "No hay Registros que mostrar ",  Toast.LENGTH_LONG).show();
        }
    }

    public  void registrar_amigos(View v){
        Intent iagregar = new Intent(Agenda.this, Agregar.class);
        startActivity(iagregar);
    }
}
