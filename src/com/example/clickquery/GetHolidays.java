package com.example.clickquery;

import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import com.example.clickquery.JSONParser;


public class GetHolidays extends Activity {
    ListView list;
    TextView ver;
    TextView name;
    TextView api;
    Button Btngetdata;
    ArrayList<HashMap<String, String>> oslist = new ArrayList<HashMap<String, String>>();
    //URL to get JSON Array
    private static String url = "http://dev.mob-z.com/clickquery/index.php?q=5";
    //JSON Node Names
    private static final String TAG_OS = "products";
    private static final String TAG_NAME = "name";
    private static final String TAG_TIME = "pid";

    JSONArray android = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        oslist = new ArrayList<HashMap<String, String>>();
        new JSONParse().execute();
        
        

    }
    private class JSONParse extends AsyncTask<String, String, JSONObject> {
         private ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

             name = (TextView)findViewById(R.id.name);

            pDialog = new ProgressDialog(GetHolidays.this);
            pDialog.setMessage("Loading ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        @Override
        protected JSONObject doInBackground(String... args) {
            JSONParser jParser = new JSONParser();
            // Getting JSON from URL
            JSONObject json = jParser.getJSONFromUrl(url);
            return json;
        }
         @Override
         protected void onPostExecute(JSONObject json) {
             pDialog.dismiss();
             try {
                    // Getting JSON Array from URL
                    android = json.getJSONArray(TAG_OS);
                    for(int i = 0; i < android.length(); i++){
                    JSONObject c = android.getJSONObject(i);
                    // Storing  JSON item in a Variable
                    //String ver = c.getString(TAG_VER);
                    final String name = c.getString(TAG_NAME);
                    final String dtime = c.getString(TAG_TIME);
                    //String api = c.getString(TAG_API);
                    // Adding value HashMap key => value
                    HashMap<String, String> map = new HashMap<String, String>();
                    //map.put(TAG_VER, ver);
                    map.put(TAG_NAME, name);
                    map.put(TAG_TIME, dtime);
                    oslist.add(map);
                    list=(ListView)findViewById(R.id.lvMain);
                    ListAdapter adapter = new SimpleAdapter(GetHolidays.this, oslist,
                            R.layout.list_result_holiday,
                            new String[] { TAG_NAME, TAG_TIME}, new int[] {
                                    R.id.name, R.id.time});
        
                    list.setAdapter(adapter);
                    }
            } catch (JSONException e) {
                e.printStackTrace();
            }             
         }

    }
}