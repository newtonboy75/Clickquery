package com.example.clickquery;

import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.example.clickquery.JSONParser;
import android.view.ViewGroup;


public class MainActivity extends Activity {
    ListView list;
    TextView ver;
    TextView name;
    TextView api;
    Button Btngetdata;
    ArrayList<HashMap<String, String>> oslist = new ArrayList<HashMap<String, String>>();
    //URL to get JSON Array
    private static String url = "http://dev.mob-z.com/clickquery/index.php";
    //JSON Node Names
    private static final String TAG_OS = "products";
    //private static final String TAG_VER = "ver";
    private static final String TAG_NAME = "name";
    private static final String TAG_HOLIDAY = "holiday";
    private static final String TAG_CURTOTAL = "curtotal";
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
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Loading ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        @Override
        protected JSONObject doInBackground(String... args) {
            JSONParser jParser = new JSONParser();
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
                    final String holiday = c.getString(TAG_HOLIDAY);
                    final String curtotal = c.getString(TAG_CURTOTAL);
                    //String api = c.getString(TAG_API);
                    // Adding value HashMap key => value
                    HashMap<String, String> map = new HashMap<String, String>();
                    //map.put(TAG_VER, ver);
                    map.put(TAG_NAME, name);
                    map.put(TAG_HOLIDAY, holiday);
                    map.put(TAG_CURTOTAL, curtotal);
                    oslist.add(map);
                    list=(ListView)findViewById(R.id.lvMain);
                    ListAdapter adapter = new SimpleAdapter(MainActivity.this, oslist,
                            R.layout.list_result,
                            new String[] { TAG_NAME}, new int[] {
                                    R.id.name}){
                    	public View getView(int position, View convertView, ViewGroup parent){
                    	    convertView = getLayoutInflater().inflate(R.layout.list_result, null);
                    	    TextView tt = (TextView)convertView.findViewById(R.id.name);
                    	    TextView ttt = (TextView)convertView.findViewById(R.id.holiday);
                    	    
                    	    tt.setText(oslist.get(+position).get("name"));
                    	    if(position == 2){        	    	
                    	    	if(holiday.equals("none")){
                    	    		ttt.setVisibility(View.GONE);                    	    		
                    	    	}else{
                    	    		ttt.setText(holiday);
                        	    	ttt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.reminder, 0, 0, 0);
                    	    	}
                    	    	
                    	    }else if(position == 3){
                    	    	ttt.setText("Current Subscriber Total: " + curtotal);
                    	    	ttt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.subs, 0, 0, 0);
                    	    }
                    	    else{
                    	    	ttt.setVisibility(View.GONE);
                    	    }
                    	    return convertView;
                    	} 
                    };
        
                    list.setAdapter(adapter);
                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {
                        	if(position == 0){
                            	Intent myIntent = new Intent(MainActivity.this, GetSubscribersAll.class);
                            	MainActivity.this.startActivity(myIntent);                    		
                        	}else if(position == 1){	
                            	Intent myIntent = new Intent(MainActivity.this, GetUserTimez.class);
                            	MainActivity.this.startActivity(myIntent);                       		
                        	}else if(position == 2){
                        		Intent myIntent = new Intent(MainActivity.this, GetHolidays.class);
                            	MainActivity.this.startActivity(myIntent); 
                        	}else if(position == 3){
                        		Intent myIntent = new Intent(MainActivity.this, GetNumSubs.class);
                            	MainActivity.this.startActivity(myIntent); 
                        	}else{
                        		Toast.makeText(MainActivity.this, "You Clicked at "+oslist.get(+position).get("name"), Toast.LENGTH_SHORT).show();
                        	}

                        }
                    });
                    }
            } catch (JSONException e) {
                e.printStackTrace();
            }             
         }

    }
}