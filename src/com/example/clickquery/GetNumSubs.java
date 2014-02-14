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
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.example.clickquery.JSONParser;


public class GetNumSubs extends Activity {
    ListView list;
    TextView ver;
    TextView name;
    TextView api;
    TableRow tr;
    TableLayout mTable;
    TextView textView;
    
    ArrayList<HashMap<String, String>> oslist = new ArrayList<HashMap<String, String>>();
    //URL to get JSON Array
    private static String url = "http://dev.mob-z.com/clickquery/index.php?q=21";
    //JSON Node Names
    private static final String TAG_OS = "products";
    private static final String TAG_NAME = "name";
    private static final String TAG_TIME = "pid";

    JSONArray android = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_result_totals);
        //ImageView image = (ImageView) findViewById(R.id.imageLogo);
        oslist = new ArrayList<HashMap<String, String>>();
        new JSONParse().execute();
        mTable = (TableLayout)findViewById(R.id.TableLayout01);
        //mTable.addView(addRow("Date", "New Subscribers"));
    }
    
    
          private TableRow addRow(String dname, String dpid) {

             tr = new TableRow(GetNumSubs.this);
                        
             final TextView textView = new TextView(this);            
             textView.setLayoutParams(new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, 60));
             MarginLayoutParams marginParams = new MarginLayoutParams(textView.getLayoutParams());
             marginParams.setMargins(20, 1, 20, 1);
             textView.setText(""+dname);
             textView.setTextSize(16);
             textView.setPadding(20, 5, 5, 5);
             
             tr.addView(textView);
             
             final TextView textView2 = new TextView(this);
             textView2.setLayoutParams(new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, 60));
             MarginLayoutParams marginParams2 = new MarginLayoutParams(textView2.getLayoutParams());
             marginParams2.setMargins(20, 1, 20, 1);
             textView2.setText(""+dpid);
             textView2.setTextSize(16); 
             textView2.setPadding(125, 5, 5, 5);
             tr.addView(textView2);
             return tr;
         }   
    
    private class JSONParse extends AsyncTask<String, String, JSONObject> {
         private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            name = (TextView)findViewById(R.id.name);

            pDialog = new ProgressDialog(GetNumSubs.this);
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
             
             try {
                    // Getting JSON Array from URL
                    android = json.getJSONArray(TAG_OS);
                    for(int i = 0; i < android.length(); i++){
                    JSONObject c = android.getJSONObject(i);
                    // Storing  JSON item in a Variable
                    //String ver = c.getString(TAG_VER);
                    String name = c.getString(TAG_NAME);
                    String pid = c.getString(TAG_TIME);

                    //me
                    System.out.println("Table Row values are   "
                            + name+" - "+pid);
                    mTable.addView(addRow(pid, name));
                    //me
                    

                    }
                    pDialog.dismiss();
            } catch (JSONException e) {
                e.printStackTrace();
            } 
             
         }

    }
}