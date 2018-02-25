package com.ozankimence.sehirkitabm;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Main2Activity extends AppCompatActivity {
    ImageView imageView;
    Button button;
    EditText editText;
    Bitmap bitmap;
    static SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        imageView = findViewById(R.id.i1);
        button = findViewById(R.id.b1);
        editText = findViewById(R.id.e1);

        Intent intent = getIntent();
        String getText =intent.getStringExtra("text");
        editText.setText(getText);
        imageView.setImageBitmap(MainActivity.selectedImage);

    }

    @TargetApi(Build.VERSION_CODES.M)
    public void selectImage(View view) {
        if(checkCallingOrSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},2);
        }else{
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent,1);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1 && resultCode==RESULT_OK && data!=null) {
            Uri uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),uri);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void saveInfo(View view) {
        String getText = editText.getText().toString();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,50,byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();

        try {
            db= this.openOrCreateDatabase("SEHIRDATABASE",MODE_PRIVATE,null);
            db.execSQL("CREATE TABLE IF NOT EXISTS SEHIR (text VARCHAR,image BLOB)");
            String sqlInsert = "INSERT INTO SEHIR (text,image) VALUES (?,?)";
            SQLiteStatement statement = db.compileStatement(sqlInsert);
            statement.bindString(1,getText);
            statement.bindBlob(2,byteArray);
            statement.execute();

        }
        catch (Exception e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
    }
}
