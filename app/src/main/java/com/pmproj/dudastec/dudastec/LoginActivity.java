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

import Domain.DataBase;
import Domain.User;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button btnRegister = findViewById(R.id.login_btn_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(myIntent);
            }
        });

        Button btnLogin = findViewById(R.id.login_btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText txtUsername = findViewById(R.id.login_etxt_nombre);
                EditText txtPassword = findViewById(R.id.login_etxt_password);
                String username = txtUsername.getText().toString();
                String password = txtPassword.getText().toString();

                //esto es para ocultar el teclado
                InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(txtPassword.getWindowToken(), 0);

                txtUsername.setText("");
                txtPassword.setText("");

                User userTemp = User.getInstance();

                if(username.equals("admin") && password.equals("admin")){
                    userTemp.setNickname("admin");
                    Intent myIntent = new Intent(LoginActivity.this, AdminMainActivity.class);
                    startActivity(myIntent);
                }else {
                    DataBase db = new DataBase( getApplicationContext());
                    Cursor c = db.getUsuario(username);
                    if (c.getCount()>0 && c.getString(2).equalsIgnoreCase(password)){
                        User.getInstance().setNickname(username);
                        if(c.getString(3).equalsIgnoreCase("0")){
                            User.getInstance().setTipo("0");
                            Intent myIntent = new Intent(LoginActivity.this, UserMainActivity.class);
                            startActivity(myIntent);
                        }else if (c.getString(3).equalsIgnoreCase("1")){
                            User.getInstance().setTipo("1");

                        }
                    }else{
                        Snackbar.make(v, "Contraseña o Usuario incorrectos", Snackbar.LENGTH_LONG)
                                .show();
                    }
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        //Include the code here
        return;
    }
}
