package com.example.mca15a.rtanalyzer;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.EditText;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
//import kotlinx.android.synthetic.main.activity_main*;

public class MainActivity extends AppCompatActivity {


    EditText editEmail, editPassword, editName, editOName, editMobile;
    Button btnSignIn, btnRegister;
    //ProgressBar pb;

    String URL = "http://10.0.2.2/RTAnalyzer/index.php";
    //String URL = "http://192.168.7.201/RTAnalyzer/index.php";

    SharedPreferences sharedPreferences;
    JSONParser jsonParser = new JSONParser();

    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editEmail = (EditText) findViewById(R.id.editEmail);
        editName = (EditText) findViewById(R.id.editName);
        editPassword = (EditText) findViewById(R.id.editPassword);
        editOName = findViewById(R.id.editOName);
        editMobile = findViewById(R.id.editMobile);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        btnRegister = (Button) findViewById(R.id.btnRegister);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // if(editName.getText().isEmpty())
                Toast.makeText(getApplicationContext(),"maaaaaaaaaaaaaaa",Toast.LENGTH_LONG);
                AttemptLogin attemptLogin = new AttemptLogin();
                attemptLogin.execute(editName.getText().toString(), editPassword.getText().toString(),""," "," ");
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (i == 0) {
                    i = 1;
                    editEmail.setVisibility(View.VISIBLE);
                    editOName.setVisibility(View.VISIBLE);
                    editMobile.setVisibility(View.VISIBLE);
                    btnSignIn.setVisibility(View.GONE);
                    btnRegister.setText("CREATE ACCOUNT");
                } else {

                    btnRegister.setText("REGISTER");
                    editEmail.setVisibility(View.GONE);
                    editOName.setVisibility(View.GONE);
                    editMobile.setVisibility(View.GONE);
                    btnSignIn.setVisibility(View.VISIBLE);
                    i = 0;

                    AttemptLogin attemptLogin = new AttemptLogin();
                    attemptLogin.execute(editName.getText().toString(), editPassword.getText().toString(), editEmail.getText().toString(), editOName.getText().toString(), editMobile.getText().toString());
                }

            }
        });


    }

    private class AttemptLogin extends AsyncTask<String, String, JSONObject> {

        private void clear() {
            ViewGroup group = (ViewGroup) findViewById(R.id.linearLayout);
            for (int i = 0, count = group.getChildCount(); i < count; ++i) {
                View view = group.getChildAt(i);
                //Log.d("count", ""+i);
                if (view instanceof EditText) {
                    ((EditText) view).setText("");
                }
            }
        }

        private void navigate(JSONObject result) throws JSONException {
            if (result.getString("action").equalsIgnoreCase("login") && (result.getInt("success") == 1)) {
                sharedPreferences = getSharedPreferences("credentials", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("username", editName.getText().toString());
                editor.putString("password", editPassword.getText().toString());
                editor.apply();
                //Log.d("messaaage","inside navigate");
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            }
        }

        @Override

        protected void onPreExecute() {

            super.onPreExecute();

        }

        @Override

        protected JSONObject doInBackground(String... args) {
            String mobile = args[4];
            String oname = args[3];
            String email = args[2];
            String password = args[1];
            String name = args[0];

            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("username", name));
            params.add(new BasicNameValuePair("password", password));
            if (email.length() > 0)
                params.add(new BasicNameValuePair("email", email));
            params.add(new BasicNameValuePair("OName", oname));
            params.add(new BasicNameValuePair("mobile", mobile));

            JSONObject json = jsonParser.makeHttpRequest(URL, "POST", params);

            return json;
        }

        protected void onPostExecute(JSONObject result) {
            // dismiss the dialog once product deleted
            //Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();

            try {
                if (result != null) {
                    Toast.makeText(getApplicationContext(), result.getString("message"), Toast.LENGTH_LONG).show();
                    navigate(result);
                    clear();
                } else {
                    Toast.makeText(getApplicationContext(), "Unable to retrieve any data from server", Toast.LENGTH_LONG).show();
                    clear();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
