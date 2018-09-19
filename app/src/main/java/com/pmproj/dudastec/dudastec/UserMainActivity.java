package cr.ac.itcr.dudastec;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import Domain.DataBase;
import Domain.User;

public class UserMainActivity extends AppCompatActivity {
    private ArrayAdapter<String> adapter;
    private ArrayList<String> auxArray;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);

        auxArray = new ArrayList<>();
        DataBase db = new DataBase(getApplicationContext());
        Cursor c = db.getMateriasSeguidas(User.getInstance().getNickname());
        c.moveToFirst();
        while (c.getCount() > 0) {
            if (c.isAfterLast()) {
                break;
            }
            auxArray.add(c.getString(0));
            c.moveToNext();
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, auxArray);
        listView = findViewById(R.id.UserMain_listView);
        listView.setAdapter(adapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final int position = i;

                AlertDialog.Builder dialog = new AlertDialog.Builder(UserMainActivity.this);
                dialog.setTitle("Importante");
                dialog.setMessage("¿Dejar de seguir?");
                dialog.setCancelable(false);
                dialog.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String nickSeleccionado = listView.getItemAtPosition(position).toString();
                        DataBase db = new DataBase(getApplicationContext());
                        db.deleteMateriaSeguida(User.getInstance().getNickname(),nickSeleccionado);
                        refresh();
                    }
                });
                dialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                    }
                });
                dialog.show();
                Vibrator v = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                v.vibrate(30);

                return false;
            }
        });
    }

    public void refresh(){
        auxArray.clear();
        DataBase db = new DataBase( getApplicationContext());
        Cursor c = db.getMateriasSeguidas(User.getInstance().getNickname());
        c.moveToFirst();
        while(c.getCount() > 0){
            if (c.isAfterLast()){
                break;
            }
            auxArray.add(c.getString(0));
            c.moveToNext();
        }
        ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
    }


    //esto fue para amarrar el menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = this.getMenuInflater();
        menuInflater.inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_logout: {
                Intent myIntent = new Intent(UserMainActivity.this, LoginActivity.class);
                startActivity(myIntent);
                return true;
            }
            case R.id.menu_searchMaterias: {
                Intent myIntent = new Intent(UserMainActivity.this, UserBandListActivity.class);
                startActivity(myIntent);
                return true;
            }
            case R.id.menu_searchContenido: {
                Intent myIntent = new Intent(UserMainActivity.this, UserSearchContentActivity.class);
                startActivity(myIntent);
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
