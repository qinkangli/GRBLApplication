package com.example.grblapplication;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Picture;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.Manifest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MakeGcodeActivity extends AppCompatActivity implements View.OnClickListener {


    private static String TAG = "MakeGcodeActivity";
    private ImageView mImg;
    private static final int IMAGE = 3;

    private List<Integer> mDatas = new ArrayList<Integer>(Arrays.asList(
            R.drawable.test, R.drawable.test1, R.drawable.test2, R.drawable.test3,
            R.drawable.test4, R.drawable.test5, R.drawable.test6));
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);//隐藏状态栏
        getSupportActionBar().hide();//隐藏标题栏
        setContentView(R.layout.activity_make_gcode);

        Button FindImage = (Button) findViewById(R.id.FindImage);
        Button Opposite = (Button) findViewById(R.id.Opposite);
        Button Mirror = (Button) findViewById(R.id.Mirror);
        Button CreatGcode = (Button) findViewById(R.id.CreatGCode);

        mImg = (ImageView) findViewById(R.id.image);


        FindImage.setOnClickListener(this);
        Opposite.setOnClickListener(this);
        Mirror.setOnClickListener(this);
        CreatGcode.setOnClickListener(this);





    }



    //加载图片
    private void displayImage(String imaePath){
       if(imaePath != null){
           Bitmap bitmap = BitmapFactory.decodeFile(imaePath);
           mImg.setImageBitmap(bitmap);
       }else {
           Toast.makeText(this,"fail to get image",Toast.LENGTH_SHORT).show();
       }
    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode, Intent data){
        if (requestCode == IMAGE && resultCode == RESULT_OK && data != null){
            handleImageOnKitKat(data);
        }
    }

    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this,uri)){
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android,providers.media.documents".equals(uri.getAuthority())){
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID+"="+id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }else if ("com.android.providers.downloads.document".equals(uri.getAuthority())){
                Uri contntUri = ContentUris.withAppendedId(Uri.parse("Content://downloads/public_downloads"),Long.valueOf(docId));
                imagePath = getImagePath(contntUri,null);
            }

        }else if("content".equalsIgnoreCase(uri.getScheme())){
            imagePath = getImagePath(uri,null);
        }else if ("file".equalsIgnoreCase(uri.getScheme())){
            imagePath = getImagePath(uri,null);
        }
        displayImage(imagePath);
    }

    private String getImagePath(Uri uri, String selection) {
        String path= null;
        Cursor cursor = getContentResolver().query(uri,null,selection,null,null);
        if (cursor != null){
            if(cursor.moveToFirst()){
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }


    @Override
    public void onClick(View v) {


        switch (v.getId()){
            case R.id.FindImage:


                if(ContextCompat.checkSelfPermission(MakeGcodeActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                }else {
                    Intent intent = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, IMAGE);

                }

                break;

            case R.id.Opposite:


                break;

            case R.id.Mirror:

                break;

            case R.id.CreatGCode:


                break;

        }


    }
}
