package com.medisense.main;

import java.util.ArrayList;


import android.app.ListActivity;

import android.content.Intent;
import android.os.Bundle;


import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ResponseActivity extends ListActivity {
    
    ArrayList<String> listItems=new ArrayList<String>();
    ArrayAdapter<String> adapter;
    
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
        
        for (int i = 0; i < id; i++) {
            adapter.add("Prescription ID : " + i);
        }
            
    }
    
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Intent myIntent = new Intent(ResponseActivity.this, ChemistListActivity.class);
        
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
