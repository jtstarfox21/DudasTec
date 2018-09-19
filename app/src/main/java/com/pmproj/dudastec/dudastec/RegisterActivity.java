package cr.ac.itcr.dudastec;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import Domain.DataBase;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button btnCancel = findViewById(R.id.register_btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent( RegisterActivity.this, LoginActivity.class);
                startActivity(myIntent);
            }
        });

        Button btnRegister = findViewById(R.id.register_btn_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etxtNick = findViewById(R.id.register_etxt_nombre);
                EditText etxtPass = findViewById(R.id.register_etxt_password);
                EditText etxtConfirmPass = findViewById(R.id.register_etxt_passwordconfirm);

                String nickname = etxtNick.getText().toString();
                String password = etxtPass.getText().toString();
                String confirmPass = etxtConfirmPass.getText().toString();

                //esto es para ocultar el teclado
                InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(etxtNick.getWindowToken(), 0);

                if (nickname.equals("")){
                    Snackbar.make(v, "Favor llenar el campo de nickname", Snackbar.LENGTH_LONG)
                            .show();
                    return;
                }
                if (password.equals("")){
                    Snackbar.make(v, "Favor llenar el campo de contraseña", Snackbar.LENGTH_LONG)
                            .show();
                    return;
                }
                if (confirmPass.equals("")){
                    Snackbar.make(v, "Favor llenar el campo de confirmar contraseña", Snackbar.LENGTH_LONG)
                            .show();
                    return;
                }
                if(!password.equals(confirmPass)){
                    Snackbar.make(v, "Las contraseñas deben ser iguales", Snackbar.LENGTH_LONG)
                            .show();
                    return;
                }

                DataBase db = new DataBase(getApplicationContext());
                Cursor c = db.getUsuario(nickname);
                if (c.getCount()>0){
                    Snackbar.make(v, "Nickname en uso", Snackbar.LENGTH_LONG)
                            .show();
                    etxtNick.setText("");
                }
                else{
                    db.insertarUsuario(nickname,password,"0");  //tipo 0 es usuario
                    Toast toast1 =
                            Toast.makeText(getApplicationContext(),
                                    "Usuario agregado", Toast.LENGTH_SHORT);
                    toast1.show();
                    Intent myIntent = new Intent( RegisterActivity.this, LoginActivity.class);
                    startActivity(myIntent);
                }

            }
        });
    }
}
