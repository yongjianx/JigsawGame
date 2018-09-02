package com.example.skyworthclub.jigsawgame;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import adapters.GridViewAdapter;
import utils.ScreenUtil;

public class LauchActivity extends AppCompatActivity {
    //返回码，本地图库
    private static final int RESULT_IMAGE = 100;
    //IMAGE_TYPE
    private static final String IMAGE_TYPE = "image/*";

    //相机返回码
    private static final int RESULT_CAMERA = 200;
    //系统相机
    private static String TEMP_IMAGE_PATH;

    private GridView mGridView;
    private float mGridViewWidth;
    private float mGridViewHeight;
    private int[] mResPic;
    private List<Bitmap> mPicList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lauch);

        //相机拍照存放的路径
        TEMP_IMAGE_PATH = Environment.getExternalStorageDirectory().getPath()+"temp.png";
        init();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){
            if(requestCode == RESULT_IMAGE && data != null){
                //相册
                Cursor cursor = this.getContentResolver().query(data.getData(),
                        null, null, null, null);
                cursor.moveToFirst();
                String imagePath = cursor.getString(cursor.getColumnIndex("_data"));
                Log.e("TAG", "return...");
            }
            else if (requestCode == RESULT_CAMERA){
                //相机

            }
        }

    }

    private void init(){
        mGridView = findViewById(R.id.gridView);

        mResPic = new int[]{R.drawable.pic1, R.drawable.pic2, R.drawable.pic3, R.drawable.pic4,
                R.drawable.pic5, R.drawable.pic6, R.drawable.pic7, R.drawable.pic1,
                R.drawable.pic2, R.drawable.pic3, R.drawable.pic4, R.drawable.pic5,
                R.drawable.pic6, R.drawable.pic7, R.drawable.pic1, R.drawable.more};
        for (int i=0; i<mResPic.length; i++){
            final Bitmap bitmap = BitmapFactory.decodeResource(getResources(), mResPic[i]);
            mPicList.add(bitmap);
        }

        //GridView布局、点击事件
        mGridView.post(new Runnable() {
            @Override
            public void run() {
                mGridViewWidth = ScreenUtil.getWidth(mGridView, 4);
                mGridViewHeight = ScreenUtil.getHeight(mGridView, 4);

                mGridView.setAdapter(new GridViewAdapter(LauchActivity.this, mPicList,
                        mGridViewWidth, mGridViewHeight, 4));

                mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (position == mPicList.size()-1){
                            showDialogCustom();
                        }
                        else {
                            final Intent intent = new Intent(LauchActivity.this, JigsawActivity.class);
                            //图片路径
                            intent.putExtra("picSelectedId", mResPic[position]);
                            startActivity(intent);
                        }
                    }
                });
            }
        });

    }

    /**
     * 弹出选择相册还是相机的对话框
     */
    private void showDialogCustom(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(LauchActivity.this);
        builder.setTitle("Dialog...");
        builder.setMessage("choose which one to go to...");
        builder.setPositiveButton("相册", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_TYPE);
                startActivityForResult(intent, RESULT_IMAGE);
            }
        });
        builder.setNegativeButton("相机", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Uri uri = Uri.fromFile(new File((TEMP_IMAGE_PATH)));
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(intent, RESULT_CAMERA);
            }
        });

        builder.show();
    }


}
