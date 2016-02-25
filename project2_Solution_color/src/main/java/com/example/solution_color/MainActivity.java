package com.example.solution_color;

//REMEBER TO CHANGE THE BACKGROUND TO THE CURRENT IMAGE EACH TIME
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.library.bitmap_utilities.BitMap_Helpers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity  {
    private String curPPath;
    private ImageView bg;
    private File PROCESSED_FILE;
   Drawable bMap;
    Bitmap theBitMap1;
    Bitmap theBitMap2;
    Bitmap bnw;
    Bitmap colorBitMap;

    int screenheight;
    int screenwidth;
    String[] perms = {"android.permission.READ_EXTERNAL_STORAGE"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this,perms, 200); //MAGIC NUMBERS NEED TO FIX
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        DisplayMetrics metrics = this.getResources().getDisplayMetrics();
        screenheight = metrics.heightPixels;
        screenwidth = metrics.widthPixels;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        bg = (ImageView)findViewById(R.id.imageView2);


    }
    @Override

    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults){

        switch(permsRequestCode){

            case 200:

                boolean readAccepted = grantResults[0]==PackageManager.PERMISSION_GRANTED;



                break;

        }

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
            case R.id.action_colorize:
                doColorize();
                break;
            case R.id.action_reset:
                doReset();
                break;
            case R.id.action_sketchy:
                doBNW();
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
    public void doReset(){
        bg.setImageResource(R.drawable.gutters);
        bg.setScaleType(ImageView.ScaleType.FIT_CENTER);
        bg.setScaleType(ImageView.ScaleType.FIT_XY);
    }
    public void doBNW(){
        bMap = new BitmapDrawable(getResources(),curPPath);
        theBitMap1  = BitMap_Helpers.copyBitmap(bMap);
        bnw =  BitMap_Helpers.thresholdBmp(theBitMap1, Constants.randColor);
        bg.setImageBitmap(bnw);
    }
    public void doColorize(){

        bMap = new BitmapDrawable(getResources(),curPPath);
        theBitMap1  = BitMap_Helpers.copyBitmap(bMap);
        theBitMap2= BitMap_Helpers.copyBitmap(bMap);
        bnw =  BitMap_Helpers.thresholdBmp(theBitMap1, Constants.randColor);
        colorBitMap = BitMap_Helpers.colorBmp(theBitMap2, Constants.floatOf);
        BitMap_Helpers.thresholdBmp(theBitMap1, Constants.randColor);
       BitMap_Helpers.colorBmp(theBitMap2, Constants.floatOf);
        BitMap_Helpers.merge(colorBitMap, bnw);
       bg.setImageBitmap(colorBitMap);
    }
    public void doShare(){ //NEED TO SET TEXTS
        Intent shareIntent = new Intent();
        File f = new File(curPPath);
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(f));
        shareIntent.setType("image/jpeg");
        startActivity(Intent.createChooser(shareIntent, "Send to"));

    }



    public void takePicture(View view){
        Intent imageIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        String name = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
        File imagesFolder = new File(name);



        if (!imagesFolder.mkdirs()) {
            if (!imagesFolder.exists()) { Toast.makeText(this, "Folder error", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        File image = new File(imagesFolder, "JPEG_" + timeStamp + ".jpg");
        Uri uriSavedImage = Uri.fromFile(image);
        curPPath = image.getAbsolutePath();
        PROCESSED_FILE = image;
        imageIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
        startActivityForResult(imageIntent, Constants.TAKE_PICTURE);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){


            bg.setImageBitmap(Camera_Helpers.loadAndScaleImage(curPPath,screenheight, screenwidth));



    }
}

