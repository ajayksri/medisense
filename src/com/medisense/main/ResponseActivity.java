package com.medisense.main;

import java.util.ArrayList;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;


import android.app.AlertDialog;
import android.app.ListActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;


import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ResponseActivity extends ListActivity {
    
    ArrayList<String> listItems=new ArrayList<String>();
    ArrayAdapter<String> adapter;
    public int userid;
    RequestParams params = new RequestParams();
    public static final String URL = "http://192.168.108.193/medisenseGetNumPrescriptions.php";
    
    public void showNoPresError() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Sorry...");
        alertDialog.setMessage("No Prescription uploaded. Please upload a prescription first!");
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
        setContentView(R.layout.activity_response);
        
        adapter=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                listItems);
        setListAdapter(adapter);
        
        Bundle bundle = getIntent().getExtras();
        
        int id = bundle.getInt("maxid", 1);
        userid = bundle.getInt("userid", 0);
        
        AsyncHttpClient client = new AsyncHttpClient();
        params.put("userid", Integer.toString(userid));
        //params.put("pid", Integer.toString(pid));
        
        client.get(URL, params, new AsyncHttpResponseHandler() {
            
            @Override
            public void onSuccess(String response) {
                // Hide Progress Dialog
                
                if (response.equals("0")) {
                    showNoPresError();
                }
                
                for (int i = 1; i <= Integer.parseInt(response); i++) {
                    adapter.add("Prescription ID : " + i);
                }
                //adapter.add(response);
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
        
        
            
    }
    
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Intent myIntent = new Intent(ResponseActivity.this, ChemistListActivity.class);
        myIntent.putExtra("userid", userid);
        myIntent.putExtra("pid", position + 1);
        ResponseActivity.this.startActivity(myIntent);
        
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
