package com.example.nguyentrungcong.ocrdemo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v7.app.AppCompatCallback;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nguyentrungcong.createfilefromassets.CreateFileFromAssets;
import com.example.nguyentrungcong.tackingpicture.CaptureActivity;
import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends Activity implements AppCompatCallback {

    private AppCompatDelegate delegate;
    TessBaseAPI baseApi;
    Button btn_camera;
    TextView tv_capture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        delegate = AppCompatDelegate.create(this, this);
        delegate.onCreate(savedInstanceState);
        delegate.setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        delegate.setSupportActionBar(toolbar);
        initialize();
    }
    public void initialize(){
        btn_camera = (Button) findViewById(R.id.btn_camera);
        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MainActivity.this, CaptureActivity.class), 10);
            }
        });
        tv_capture = (TextView) findViewById(R.id.tv_capture);
        //Init TessTwoApi
        File dir = new File(getFilesDir().getPath()+"/tessdata");
        if(!dir.exists()){
            dir.mkdir();
        }
        CreateFileFromAssets.getInstance().initialize(MainActivity.this).CreateFileFromPath("tessdata");
        File f = new File(getFilesDir().getPath()+"/tessdata/eng.traineddata");
        Log.d("TAg,", getFilesDir().getPath() + "/tessdata/eng.traineddata");
        baseApi = new TessBaseAPI();
        baseApi.init(getFilesDir().getPath(), "eng");
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10){
            if (resultCode == RESULT_OK){
                byte[] datas = data.getExtras().getByteArray("data");
                baseApi.setImage(BitmapFactory.decodeByteArray(datas, 0, datas.length));
                tv_capture.setText(baseApi.getUTF8Text());
            }
        }
    }

    //Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSupportActionModeStarted(ActionMode mode) {

    }

    @Override
    public void onSupportActionModeFinished(ActionMode mode) {

    }

    @Nullable
    @Override
    public ActionMode onWindowStartingSupportActionMode(ActionMode.Callback callback) {
        return null;
    }
}
