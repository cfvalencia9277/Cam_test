package com.brangerbriz.test_cam_stickers;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.os.Environment.*;

public class MainActivity extends Activity {


    private static final int CAMERA_REQUEST = 1888;
    private static final int SELECT_SINGLE_PICTURE = 101;
    private ImageView imageView;
    public static final String IMAGE_TYPE = "image/*";


    int photoCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.imageView = (ImageView) this.findViewById(R.id.imageView1);
        Button photoButton = (Button) this.findViewById(R.id.button1);
        Button bringImageButton = (Button) this.findViewById(R.id.btn_pick_single_image);

        photoButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,MediaStore.Images.Media.EXTERNAL_CONTENT_URI.getPath());
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });
        bringImageButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {

                // in onCreate or any event where your want the user to
                // select a file
                Intent intent = new Intent();
                intent.setType(IMAGE_TYPE);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,
                        getString(R.string.select_picture)), SELECT_SINGLE_PICTURE);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {




        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            photoCount++;
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            File outFile = new File(Environment.getExternalStorageDirectory(), photoCount+".jpeg");
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(outFile);
                photo.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(requestCode ==  SELECT_SINGLE_PICTURE && resultCode == RESULT_OK){

            Uri selectedImageUri = data.getData();
            try {
                imageView.setImageBitmap(new UserPicture(selectedImageUri, getContentResolver()).getBitmap());
            } catch (IOException e) {
                Log.e(MainActivity.class.getSimpleName(), "Failed to load image", e);
            }
        }
    }
}