package com.example.mca15a.rtanalyzer;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.text.style.TtsSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.view.View.OnClickListener;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Jabir on 22-03-18.
 */

public class ChangePassWordFragment extends Fragment{
    View myView;
    EditText uname,oldPwd,newPwd,cnfmPwd;
    Button submit;
    SharedPreferences sharedPreferences;
    String URL= "http://10.0.2.2/RTAnalyzer/route.php";
    public static ArrayList<String> credentials;
    JSONParser jsonParser = new JSONParser();
    @Nullable
//    public void submit(View v){
//        Toast.makeText(getActivity(),"clicked",Toast.LENGTH_LONG).show();
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
         myView= inflater.inflate(R.layout.content_password_change,container,false);
       // return super.onCreateView(inflater, container, savedInstanceState);
        uname=myView.findViewById(R.id.uname);
        oldPwd=myView.findViewById(R.id.oldPwd);
        newPwd=myView.findViewById(R.id.newPwd);
        cnfmPwd=myView.findViewById(R.id.cnfmPwd);
            submit=myView.findViewById(R.id.btnSubmit);
        sharedPreferences = getActivity().getSharedPreferences("credentials", MODE_PRIVATE);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(uname.getText().toString().trim().equals(sharedPreferences.getString("username",""))){
                    if(oldPwd.getText().toString().equals(sharedPreferences.getString("password",""))){
                        if(newPwd.getText().toString().equals(cnfmPwd.getText().toString())){
                           new PwdChanger().execute("1",uname.getText().toString(),newPwd.getText().toString());
                            Log.d("messaaage:","password changingggggggggg");
                        }
                        else{

                             Snackbar.make(getView(), "Passwords should match", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                        }
                    }
                    else{
                        Snackbar.make(getView(), "Incorrect Password", Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();

                    }
                }
                else {
                    Snackbar.make(getView(), "Incorrect Username", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
            }
        });
        return myView;
    }


    private class PwdChanger extends AsyncTask<String, String, JSONObject> {

        @Override

        protected void onPreExecute() {

            super.onPreExecute();

        }
        @Override
        protected JSONObject doInBackground(String... args) {
            String type= args[0];
            String uname= args[1];
            String newPwd= args[2];
            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("type",type));
            params.add(new BasicNameValuePair("uname",uname));
            params.add(new BasicNameValuePair("newpwd",newPwd));
            //params.add(null);
//          try {
            JSONObject json = jsonParser.makeHttpRequest(URL, "POST", params);
            //  Log.d("names","inside doin background");
            //  jsonArray= json.getJSONArray("place");
            return json;
//          }catch (JSONException e){
//              e.printStackTrace();
//          }
//          return
        }
        protected void onPostExecute(JSONObject result) {
            // dismiss the dialog once product deleted
            //Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
              Log.d("messaaage:","inside onpostexecute");
            try {
                if (result != null) {
                   // Toast.makeText(getActivity(), result.getString("message"), Toast.LENGTH_LONG).show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage(result.getString("message"))
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //do things
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
//                    getActivity().getFragmentManager().beginTransaction().replace(R.id.pwd_change,new HomeFragment()).commit();
                    getActivity().getFragmentManager().beginTransaction().replace(R.id.content_home,new HomeFragment()).commit();

                } else {
                    Toast.makeText(getActivity(), "Unable to change Password", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
