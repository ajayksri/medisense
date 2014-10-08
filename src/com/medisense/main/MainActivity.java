package com.medisense.main;

import android.support.v7.app.ActionBarActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;


import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends ActionBarActivity {

    public static final int GET_FROM_GALLERY = 3;

    protected static final int GET_FROM_CAMERA = 0;
    
    public static int id = 0;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
    
    public void uploadPrescription(View view) {
        Intent myIntent = new Intent(MainActivity.this, UploadPrescriptionActivity.class);
        MainActivity.this.startActivity(myIntent);
        Log.d("Android : ", "upload prescription clicked");
    }
    
    public void responsesClicked(View view) {
        if (id == 0) {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Sorry...");
            alertDialog.setMessage("No Prescription uploaded. Please upload a prescription first!");
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
            
            alertDialog.setIcon(R.drawable.ic_launcher);
            alertDialog.show();
        } else {
            Intent myIntent = new Intent(MainActivity.this, ResponseActivity.class);
            myIntent.putExtra("maxid", id);
            MainActivity.this.startActivity(myIntent);
        }

    }
    
    public void pickImage(View view) {
        //startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
    
        final CharSequence[] items = { "Take Photo", "Choose from Library",
        "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    startActivityForResult(new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE), GET_FROM_CAMERA);
                    
                } else if (items[item].equals("Choose from Library")) {
                    startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
                    
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
});
builder.show();
        
    }
    
    @SuppressWarnings("deprecation")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Success...");
        alertDialog.setMessage("Your request ID is : " + id + ". You will be notified once we get the response");
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        id++;
        alertDialog.setIcon(R.drawable.ic_launcher);
        alertDialog.show();
    }
}
