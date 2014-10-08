package com.medisense.main;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ChemistListActivity extends ListActivity {
    
    ArrayList<String> listItems=new ArrayList<String>();
    ArrayAdapter<String> adapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chemistlist);
        
        adapter=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                listItems);
        setListAdapter(adapter);


        adapter.add("Subhash Medicos : 9812376567");
        adapter.add("Gupta Medicos : 9678723765");
        adapter.add("Shukla Medicos : 9674625467");
        adapter.add("Ajay Medicos : 9784562346");

            
    }
    
    protected void onListItemClick(ListView l, View v, int position, long id) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle((position == 0 ? "Subhash Medicos" : (position == 1 ? "Gupta Medicos" : (position == 2 ? "Shukla Medicos" : "Ajay Medicos"))));
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
