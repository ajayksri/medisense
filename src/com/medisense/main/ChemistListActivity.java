package com.medisense.main;

import java.util.ArrayList;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ChemistListActivity extends ListActivity {
    
    ArrayList<String> listItems=new ArrayList<String>();
    ArrayAdapter<String> adapter;
    RequestParams params = new RequestParams();
    public static final String URL = "http://192.168.108.193/medisenseResponse.php";
    
    
    public void showNoResError() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Sorry...");
        alertDialog.setMessage("No Responses yet!");
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                finish();
            }
        });
        
        alertDialog.setIcon(R.drawable.ic_launcher);
        alertDialog.show();
        
        
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chemistlist);
        
        adapter=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                listItems);
        setListAdapter(adapter);
        
        AsyncHttpClient client = new AsyncHttpClient();
        
        Bundle bundle = getIntent().getExtras();
        
        int userid = bundle.getInt("userid", 1);
        int pid = bundle.getInt("pid", 1);
        
        params.put("userid", Integer.toString(userid));
        params.put("pid", Integer.toString(pid));
        
        client.get(URL, params, new AsyncHttpResponseHandler() {
            
            @Override
            public void onSuccess(String response) {
                // Hide Progress Dialog

                if (response.equals("None")) {
                    showNoResError();
                } else {

                    String rs[] = response.split("#");

                    for (int i = 0; i < rs.length; i++) {
                        adapter.add(rs[i]);
                    }
                }
                //showSucOrFail(response);

                Toast.makeText(getApplicationContext(), response,
                        Toast.LENGTH_LONG).show();

            }

            // When the response returned by REST has Http
            // response code other than '200' such as '404',
            // '500' or '403' etc
            @Override
            public void onFailure(int statusCode, Throwable error,
                    String content) {
                // Hide Progress Dialog
                
                // When Http response code is '404'
                //showSucOrFail("Failure");
                
                    Toast.makeText(getApplicationContext(),
                            "Requested resource not found",
                            Toast.LENGTH_LONG).show();
                
            }
        });


        /*adapter.add("Subhash Medicos : 9812376567");
        adapter.add("Gupta Medicos : 9678723765");
        adapter.add("Shukla Medicos : 9674625467");
        adapter.add("Ajay Medicos : 9784562346");*/

            
    }
    
    protected void onListItemClick(ListView l, View v, int position, long id) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle((CharSequence) l.getItemAtPosition(position));
        //alertDialog.setTitle((position == 0 ? "Subhash Medicos" : (position == 1 ? "Gupta Medicos" : (position == 2 ? "Shukla Medicos" : "Ajay Medicos"))));
        alertDialog.setMessage("We have all the medicines available. Please call us for home delivery...");
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        }); 
        alertDialog.show();

        super.onListItemClick(l, v, position, id);
      }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
