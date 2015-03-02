package com.medisense.main;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.support.v7.app.ActionBarActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

    public static final int GET_FROM_GALLERY = 3;

    protected static final int GET_FROM_CAMERA = 2;
    
    public static final String PREFS_NAME = "MyPrefsFile";
    public static final int GET_ID = 1;
    public static final String URL = "http://192.168.108.193/medisense.php";
    public static final String URL_ID = "http://192.168.108.193/medisenseGetId.php";

    
    RequestParams params = new RequestParams();
    String imgPath, fileName;
    Bitmap bitmap;
    String encodedString;
    
    public static int pid = 0;
    public int myId = 0;
    
    private Uri fileUri;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        myId = settings.getInt("id", 0);
        
        

        
        setContentView(R.layout.activity_main);
        
        if (myId == 0) {
            getAndStoreId();
        }
    }
    
    public void getAndStoreId() {
        params.put("action", GET_ID);
        
        AsyncHttpClient client = new AsyncHttpClient();
        
        client.get(URL_ID, new AsyncHttpResponseHandler() {
            // When the response returned by REST has Http
            // response code '200'
            @Override
            public void onSuccess(String response) {
                // Hide Progress Dialog
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putInt("id", Integer.parseInt(response));
                editor.commit();


                showFirstTimeUser(response);
                myId = Integer.parseInt(response);
                
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
                showSucOrFail("Failure");
                
                    Toast.makeText(getApplicationContext(),
                            "Requested resource not found",
                            Toast.LENGTH_LONG).show();
                
            }
        });
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
        /*if (pid == 0) {
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
        } else {*/
            Intent myIntent = new Intent(MainActivity.this, ResponseActivity.class);
            myIntent.putExtra("maxid", pid);
            myIntent.putExtra("userid", myId);
            MainActivity.this.startActivity(myIntent);
        //}

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
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    
                    fileUri = getOutputMediaFileUri();
              
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
              
                    // start the image capture Intent
                    startActivityForResult(intent, GET_FROM_CAMERA);
                    //startActivityForResult(new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE), GET_FROM_CAMERA);
                    
                } else if (items[item].equals("Choose from Library")) {
                    startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
                    
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
});
builder.show();
        
    }
    
    public Uri getOutputMediaFileUri() {
        return Uri.fromFile(getOutputMediaFile());
    }
  
    /**
     * returning image
     */
    private static File getOutputMediaFile() {
  
        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "Medisense images");
  
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                
                return null;
            }
        }
  
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;

        mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" + timeStamp + ".jpg");


        return mediaFile;
    }

    @SuppressLint("NewApi")
    @SuppressWarnings("deprecation")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        
        
        try {
            // When an Image is picked
            if ((requestCode == GET_FROM_GALLERY) && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data
 
                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };
 
                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();
 
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgPath = cursor.getString(columnIndex);
                cursor.close();
                
                // Get the Image's file name
                String fileNameSegments[] = imgPath.split("/");
                fileName = fileNameSegments[fileNameSegments.length - 1];
                // Put file name in Async Http Post Param which will used in Php web app
                params.put("filename", fileName);
                params.put("userid", Integer.toString(myId));
                
                if (imgPath != null && !imgPath.isEmpty()) {
                    
                    // Convert image to String using Base64
                    encodeImagetoString();
                } else {
                    Toast.makeText(
                            getApplicationContext(),
                            "You must select image from gallery before you try to upload",
                            Toast.LENGTH_LONG).show();
                }
 
            } else if ((requestCode == GET_FROM_CAMERA) && resultCode == RESULT_OK) {
                
                imgPath = fileUri.getPath();
                
                
                String fileNameSegments[] = imgPath.split("/");
                fileName = fileNameSegments[fileNameSegments.length - 1];
                // Put file name in Async Http Post Param which will used in Php web app
                params.put("filename", fileName);
                params.put("userid", Integer.toString(myId));
                
                if (imgPath != null && !imgPath.isEmpty()) {
                    
                    // Convert image to String using Base64
                    encodeImagetoString();
                } else {
                    Toast.makeText(
                            getApplicationContext(),
                            "You must select image from gallery before you try to upload",
                            Toast.LENGTH_LONG).show();
                }
 
            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }
        
        
    }
    
    public void encodeImagetoString() {
        new AsyncTask<Void, Void, String>() {
 
            protected void onPreExecute() {
 
            };
 
            @Override
            protected String doInBackground(Void... params) {
                BitmapFactory.Options options = null;
                options = new BitmapFactory.Options();
                options.inSampleSize = 3;
                bitmap = BitmapFactory.decodeFile(imgPath,
                        options);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                // Must compress the Image to reduce image size to make upload easy
                bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
                byte[] byte_arr = stream.toByteArray();
                // Encode Image to String
                encodedString = Base64.encodeToString(byte_arr, 0);
                return "";
            }
 
            @Override
            protected void onPostExecute(String msg) {
                
                params.put("image", encodedString);
                // Trigger Image upload
                triggerImageUpload();
            }
        }.execute(null, null, null);
    }
     
    public void triggerImageUpload() {
        makeHTTPCall();
        
        
    }
    
    public void showFirstTimeUser(String generatedId) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Registered...");
        alertDialog.setMessage("Your user ID is : " + generatedId + ".");
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        pid = Integer.parseInt(generatedId);
        alertDialog.setIcon(R.drawable.ic_launcher);
        alertDialog.show();
    }
    
    public void showSucOrFail(String generatedId) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Success...");
        alertDialog.setMessage("Your request ID is : " + generatedId + ". You will be notified once we get the response");
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        pid = Integer.parseInt(generatedId);
        alertDialog.setIcon(R.drawable.ic_launcher);
        alertDialog.show();
    }
 
    // Make Http call to upload Image to Php server
    public void makeHTTPCall() {
            
        AsyncHttpClient client = new AsyncHttpClient();
        // Don't forget to change the IP address to your LAN address. Port no as well.
        client.post(URL,
                params, new AsyncHttpResponseHandler() {
                    // When the response returned by REST has Http
                    // response code '200'
                    @Override
                    public void onSuccess(String response) {
                        // Hide Progress Dialog
                        
                        showSucOrFail(response);
                        
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
                        if (statusCode == 404) {
                            showSucOrFail("400");
                            Toast.makeText(getApplicationContext(),
                                    "Requested resource not found",
                                    Toast.LENGTH_LONG).show();
                        }
                        // When Http response code is '500'
                        else if (statusCode == 500) {
                            showSucOrFail("500");
                            Toast.makeText(getApplicationContext(),
                                    "Something went wrong at server end",
                                    Toast.LENGTH_LONG).show();
                        }
                        // When Http response code other than 404, 500
                        else {
                            showSucOrFail("Unknown");
                            Toast.makeText(
                                    getApplicationContext(),
                                    "Error Occured \n Most Common Error: \n1. Device not connected to Internet\n2. Web App is not deployed in App server\n3. App server is not running\n HTTP Status code : "
                                            + statusCode, Toast.LENGTH_LONG)
                                    .show();
                        }
                    }
                });
    }
}
