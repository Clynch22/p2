package com.example.solution_color;

//REMEBER TO CHANGE THE BACKGROUND TO THE CURRENT IMAGE EACH TIME
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity  {
    private ImageView currentPicture;
    private ImageView defaultPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent myIntent = new Intent(this, SettingsActivity.class);
                startActivity(myIntent);
                break;
            case R.id.action_share:
                doShare();
                break;
            default:
                break;
        }
        return true;
    }
    public void savePreferences(String subject, String message){
        SharedPreferences settings = getSharedPreferences("PrefFile",MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
    }
    public void doShare(){ //DOESNT SEND IMAGE WILL CORRECTLY SET THE DEFAULT TEXTS
        Intent message = new Intent();
        message.setAction(Intent.ACTION_SEND);
        message.putExtra(android.content.Intent.EXTRA_SUBJECT, Constants.DEF_SUBJECT_TEXT);
        message.putExtra(android.content.Intent.EXTRA_TEXT, Constants.DEF_MESSAGE_TEXT);
        message.putExtra(Intent.EXTRA_STREAM, getUri());
        message.setType("image/jpeg");
        startActivity(Intent.createChooser(message,"send"));

    }

    private static Uri getUri(){
        return Uri.fromFile(getStoFile());
    }
    private static File getStoFile(){
        File storage = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "P2_skel");
        File storagePath = new File(storage.getPath());
        return storagePath;
    }
    public void takePicture(View view){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,getUri());
        startActivityForResult(intent, Constants.TAKE_PICTURE);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == Constants.RESULT_OK){

        }
        else if(resultCode == Constants.RESULT_CANCELED){

        }
    }
}

