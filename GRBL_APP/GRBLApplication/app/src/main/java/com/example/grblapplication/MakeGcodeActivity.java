package com.example.grblapplication;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Picture;
import android.media.Image;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MakeGcodeActivity extends AppCompatActivity implements View.OnClickListener {


    private static String TAG = "MakeGcodeActivity";
    private ImageView mImg;
    private static final int IMAGE = 3;
    private  String PicturePath= null;

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
           //将图片转化成BMP格式再变化成黑白形式
           Bitmap newmap = convertToBlackWhite(bitmap);
           mImg.setImageBitmap(newmap);
           saveBmp(newmap);

       }else {
           Toast.makeText(this,"fail to get image",Toast.LENGTH_SHORT).show();
       }
    }


    public void MakeGcode(String BmpPath){

        //生成G代码的NC文件
        FileOutputStream outputStream = null;
        BufferedWriter writer = null;
        File mfile = new File("/storage/emulated/0//GRBL_Application","text.nc");
        if (!mfile.exists()){
            try {
                mfile.createNewFile();

                outputStream = new FileOutputStream(mfile);
                writer =  new BufferedWriter(new OutputStreamWriter(outputStream));
                writer.write("M5\n");
                writer.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        if(BmpPath != null){




        }else {
            Toast.makeText(this,"fail to get image",Toast.LENGTH_SHORT).show();
        }

    }



    /**
     * 将Bitmap存为 .bmp格式图片
     * @param bitmap
     */
    public void saveBmp(Bitmap bitmap) {
        // 位图大小
        int nBmpWidth = bitmap.getWidth();
        int nBmpHeight = bitmap.getHeight();
        // 图像数据大小
        // int bufferSize = nBmpHeight * (nBmpWidth * 3 + nBmpWidth % 4);
        int bufferSize = nBmpHeight * (nBmpWidth * 4 + nBmpWidth % 5);
        try {
            // 存储文件名
            // String filename = savePhoto();
            String filename =  savePicturePath("GRBL_Application");
            File file = new File(filename);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fileos = new FileOutputStream(filename);
            // bmp文件头
            int bfType = 0x4d42;
            long bfSize = 14 + 40 + bufferSize;
            int bfReserved1 = 0;
            int bfReserved2 = 0;
            long bfOffBits = 14 + 40;
            // 保存bmp文件头
            writeWord(fileos, bfType);
            writeDword(fileos, bfSize);
            writeWord(fileos, bfReserved1);
            writeWord(fileos, bfReserved2);
            writeDword(fileos, bfOffBits);
            // bmp信息头
            long biSize = 40L;
            long biWidth = nBmpWidth;
            long biHeight = nBmpHeight;
            int biPlanes = 1;
            int biBitCount = 32;

            long biCompression = 0L;
            long biSizeImage = 0L;
            long biXpelsPerMeter = 0L;
            long biYPelsPerMeter = 0L;
            long biClrUsed = 0L;
            long biClrImportant = 0L;
            // 保存bmp信息头
            writeDword(fileos, biSize);
            writeLong(fileos, biWidth);
            writeLong(fileos, biHeight);
            writeWord(fileos, biPlanes);
            writeWord(fileos, biBitCount);
            writeDword(fileos, biCompression);
            writeDword(fileos, biSizeImage);
            writeLong(fileos, biXpelsPerMeter);
            writeLong(fileos, biYPelsPerMeter);
            writeDword(fileos, biClrUsed);
            writeDword(fileos, biClrImportant);
            // 像素扫描
            byte bmpData[] = new byte[bufferSize];
            //int wWidth = (nBmpWidth * 3 + nBmpWidth % 4);
            int wWidth = (nBmpWidth * 4 + nBmpWidth % 5);
            for (int nCol = 0, nRealCol = nBmpHeight - 1; nCol < nBmpHeight; ++nCol, --nRealCol)
                for (int wRow = 0, wByteIdex = 0; wRow < nBmpWidth; wRow++, wByteIdex += 4) {
                    int clr = bitmap.getPixel(wRow, nCol);
                    bmpData[nRealCol * wWidth + wByteIdex] = (byte) Color.blue(clr);
                    bmpData[nRealCol * wWidth + wByteIdex + 1] = (byte) Color.green(clr);
                    bmpData[nRealCol * wWidth + wByteIdex + 2] = (byte) Color.red(clr);
                    bmpData[nRealCol * wWidth + wByteIdex+3] = (byte) Color.alpha(0xff);
                }

            fileos.write(bmpData);
            fileos.flush();
            fileos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }



    }


    /**
     * 将彩色图转换为黑白图
     * @return 返回转换好的位图
     */
    public static Bitmap convertToBlackWhite(Bitmap bmp) {
        int width = bmp.getWidth(); // 获取位图的宽
        int height = bmp.getHeight(); // 获取位图的高
        int[] pixels = new int[width * height]; // 通过位图的大小创建像素点数组

        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        int alpha = 0xFF << 24;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int grey = pixels[width * i + j];

                int red = ((grey & 0x00FF0000) >> 16);
                int green = ((grey & 0x0000FF00) >> 8);
                int blue = (grey & 0x000000FF);

                grey = (int) (red * 0.3 + green * 0.59 + blue * 0.11);
                grey = alpha | (grey << 16) | (grey << 8) | grey;
                pixels[width * i + j] = grey;
            }
        }
        Bitmap newBmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

        newBmp.setPixels(pixels, 0, width, 0, 0, width, height);

        Bitmap resizeBmp = ThumbnailUtils.extractThumbnail(newBmp, 560, 560);
        return resizeBmp;

    }



    protected void writeWord(FileOutputStream stream, int value) throws IOException {
        byte[] b = new byte[2];
        b[0] = (byte) (value & 0xff);
        b[1] = (byte) (value >> 8 & 0xff);
        stream.write(b);
    }

    protected void writeDword(FileOutputStream stream, long value) throws IOException {
        byte[] b = new byte[4];
        b[0] = (byte) (value & 0xff);
        b[1] = (byte) (value >> 8 & 0xff);
        b[2] = (byte) (value >> 16 & 0xff);
        b[3] = (byte) (value >> 24 & 0xff);
        stream.write(b);
    }

    protected void writeLong(FileOutputStream stream, long value) throws IOException {
        byte[] b = new byte[4];
        b[0] = (byte) (value & 0xff);
        b[1] = (byte) (value >> 8 & 0xff);
        b[2] = (byte) (value >> 16 & 0xff);
        b[3] = (byte) (value >> 24 & 0xff);
        stream.write(b);
    }


    public String savePicturePath(String photoName) {
        String mFilePath = Environment.getExternalStorageDirectory().getPath();// 获取SD卡路径
        String fpath = "";
        File file = new File(mFilePath + File.separatorChar + photoName);
        if (!file.exists()) {
            file.mkdirs();//创建文件夹
        }
        fpath = mFilePath + File.separatorChar + File.separatorChar + photoName;
        file = new File(fpath);
        if (!file.exists()) {
            file.mkdirs();
        }
        long picName = System.currentTimeMillis();
        PicturePath = fpath + File.separatorChar + picName + ".bmp";
        file = null;
        return PicturePath;
    }


    public String saveNcFilePath() {
        File NCFile = new File("/storage/emulated/0//GRBL_Application//NC_File");

        if (!NCFile.exists()) {
            //创建文件
            NCFile.mkdirs();
            //给一个吐司提示，显示创建成功
            Log.e(TAG,"文件创建成功");
        }
        return "svbe";

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
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, IMAGE);

                }

                break;

            case R.id.Opposite:


                break;

            case R.id.Mirror:

                break;

            case R.id.CreatGCode:
                MakeGcode(PicturePath);
                saveNcFilePath();
                Log.e(TAG,PicturePath);



                break;

        }


    }
}
