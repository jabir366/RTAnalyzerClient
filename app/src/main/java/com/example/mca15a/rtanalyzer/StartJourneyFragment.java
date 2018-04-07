package com.example.mca15a.rtanalyzer;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
/**
 * Created by jabr on 29/3/18.
 */

public class StartJourneyFragment extends Fragment {
    View myView;
    private Spinner spinner;
    private TextView startDate;
    private EditText startTime,source,destination,duration;
    private int hour, minute;
    private Button sjourney;
    String URL= "http://10.0.2.2/RTAnalyzer/route.php";
    String selected_route;
    public static ArrayList<String> route;
    JSONParser jsonParser = new JSONParser();
    private static final String TAG = "MainActivity";
    private DatePickerDialog.OnDateSetListener nDateSetListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.activity_start_journey, container, false);
        //turn super.onCreateView(inflater, container, savedInstanceState);
        spinner = myView.findViewById(R.id.spinner1);
        source=myView.findViewById(R.id.source);
        destination=myView.findViewById(R.id.destination);
        duration=myView.findViewById(R.id.duration);
        startDate = myView.findViewById(R.id.sdate);
        sjourney = myView.findViewById(R.id.sjourney);
        startTime = myView.findViewById(R.id.stime);
//        datepickerlistener
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DATE);
                DatePickerDialog dialog = new DatePickerDialog(
                        getActivity(),
                        android.R.style.Theme_Holo_Dialog_MinWidth,
                        nDateSetListener,
                        year, month, day
                );
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        //timepickerlistener
        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePic();
            }
        });
        nDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);

                String date =year + "-" + month + "-" + day;
                startDate.setText(date);
            }
        };
        //============================================== spinner
        route = new ArrayList<String>();
      //  route.add("pattom");
//        route.add("chaka");
//        route.add("nedumangad");
//        route.add("attingal");
        //  ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,R.array.places,R.layout.support_simple_spinner_dropdown_item);

        spinner.setPrompt("Select Route");
        //spinner.setSelection(0);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, route);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        Router router=new Router();
        router.execute("0");
        Log.d("from oncreateview","entered");
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String text = parent.getItemAtPosition(position).toString();
                spinner.setSelection(position);
                selected_route=text;
               // Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
            // ================================================= spinner
        });
        sjourney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(getActivity(), "starttt", Toast.LENGTH_SHORT).show();
                Router router=new Router();
                router.execute("2",source.getText().toString(),destination.getText().toString(),startDate.getText().toString(),startTime.getText().toString(),duration.getText().toString(),selected_route);
            }
        });
        return myView;
    }

    void timePic() {
        final Calendar cal = Calendar.getInstance();
        hour = cal.get(Calendar.HOUR);
        minute = cal.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                startTime.setText(hourOfDay + ":" + minute);
            }
        }, hour, minute, false);
        timePickerDialog.show();
    }
    private class Router extends AsyncTask<String, String, JSONObject> {

        @Override

        protected void onPreExecute() {

            super.onPreExecute();

        }
        @Override
        protected JSONObject doInBackground(String... args) {
            String type= args[0];
            //JSONArray jsonArray;
            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("type", type));
            if(type.equals("2")){
                String source=args[1];
                String destination=args[2];
                String startDate=args[3];
                String startTime=args[4];
                String duration=args[5];
                String selected_route=args[6];
                params.add(new BasicNameValuePair("source",source));
                params.add(new BasicNameValuePair("destination",destination));
                params.add(new BasicNameValuePair("start_date",startDate));
                params.add(new BasicNameValuePair("start_time",startTime));
                params.add(new BasicNameValuePair("duration",duration));
                params.add(new BasicNameValuePair("selected_route",selected_route));
            }
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
                 JSONArray detType=result.getJSONArray("type");
                 JSONObject detobj=detType.getJSONObject(0);
                 if(detobj.getString("tp").equals("0")) {
                     //Log.d("wwwwwwwwwwe:",detobj.getString("tp"));
                     Log.d("messaaage:", result.toString());
                     JSONArray jsonArray = result.getJSONArray("place");
                     for (int i = 0; i < jsonArray.length(); i++) {
                         JSONObject object = jsonArray.getJSONObject(i);
                         Log.d("route_name", object.getString("route_name"));
                         StartJourneyFragment.route.add(object.getString("route_name"));
                     }
                 }
                 else if(detobj.getString("tp").equals("2")){
                     //Log.d("wwwwwwwwwwe:",detobj.getString("tp"));
                 }
               if (result != null) {
                    Log.d("messaaage:",result.toString());
                    Toast.makeText(getActivity(),"jsonlength"+result.length(),Toast.LENGTH_LONG).show();
//                    navigate(result);
//                    clear();
                } else {
                    Toast.makeText(getActivity(), "Unable to retrieve route", Toast.LENGTH_LONG).show();
//                    clear();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}

