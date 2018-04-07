package com.example.mca15a.rtanalyzer;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

public class StartJourneyActivity extends AppCompatActivity {
    private Spinner spinner;
    private TextView displayDate;
    private EditText txtTime;
    private int hour,minute;
    private Button sjourney;
    String URL= "http://10.0.2.2/RTAnalyzer/route.php";
    public static ArrayList<String> route;
    JSONParser jsonParser = new JSONParser();
    private DatePickerDialog.OnDateSetListener nDateSetListener;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_journey);
        spinner=findViewById(R.id.spinner1);
        displayDate=findViewById(R.id.sdate);
        sjourney=findViewById(R.id.sjourney);
        txtTime=findViewById(R.id.stime);
//        datepickerlistener
        displayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal= Calendar.getInstance();
                int year=cal.get(Calendar.YEAR);
                int month=cal.get(Calendar.MONTH);
                int day=cal.get(Calendar.DATE);
                DatePickerDialog dialog=new DatePickerDialog(
                        StartJourneyActivity.this,
                        android.R.style.Theme_Holo_Dialog_MinWidth,
                        nDateSetListener,
                        year,month,day
                );
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        //timepickerlistener
        txtTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePic();
            }
        });
        nDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                //Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);

                String date = month + "/" + day + "/" + year;
                displayDate.setText(date);
            }
        };
        //============================================== spinner
        route=new ArrayList<String>();
        route.add("pffff");
        route.add("cddddda");
       // route.add("nedumangad");
       // route.add("attingal");
      //  ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,R.array.places,R.layout.support_simple_spinner_dropdown_item);
       ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,route);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setPrompt("Select Route");
        spinner.setAdapter(adapter);
        //asynctask to load route names from database
        Router router=new Router();
        router.execute("route");
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String text=parent.getItemAtPosition(position ).toString();
                Toast.makeText(parent.getContext(),text,Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
            // ================================================= spinner
        });
        sjourney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(StartJourneyActivity.this,"starttt",Toast.LENGTH_SHORT).show();
            }
        });
    }
    void timePic(){
        final Calendar cal=Calendar.getInstance();
        hour=cal.get(Calendar.HOUR);
        minute=cal.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog=new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                txtTime.setText(hourOfDay+":"+minute);
            }
        },hour,minute,false);
        timePickerDialog.show();
    }


private class Router extends AsyncTask<String, String, JSONObject> {

        @Override

        protected void onPreExecute() {

            super.onPreExecute();

        }
        @Override
        protected JSONObject doInBackground(String... args) {
            String name= args[0];
            JSONArray jsonArray;
            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("type", name));
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
          //  log.d
            try {
            JSONArray jsonArray=result.getJSONArray("place");
            for(int i=0;i<jsonArray.length();i++){
                JSONObject object=jsonArray.getJSONObject(i);
                Log.d("route_name",object.getString("route_name"));
                StartJourneyActivity.route.add(object.getString("route_name"));

            }


                if (result != null) {
                    Toast.makeText(getApplicationContext(),"jsonlength"+result.length(),Toast.LENGTH_LONG).show();
//                    navigate(result);
//                    clear();
                } else {
                    Toast.makeText(getApplicationContext(), "Unable to retrieve route", Toast.LENGTH_LONG).show();
//                    clear();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
