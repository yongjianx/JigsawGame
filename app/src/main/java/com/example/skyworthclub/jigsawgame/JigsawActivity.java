package com.example.skyworthclub.jigsawgame;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import adapters.GridViewAdapter;
import utils.GameUtil;
import utils.ImageUtil;
import utils.ItemBean;
import utils.ScreenUtil;

public class JigsawActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView mDiffTextView;
    private TextView mTimeTextView;
    private TextView mStepTextView;
    private GridView mGridView;
    private Button mBtnOriginalImg;
    private Button mBtnReset;
    private Button mBtnBack;
    private GridViewAdapter mAdapter;
    private PopupWindow mPopupWindow;

    private float density;
    private float mWidth;
    private float mHeight;

    //图片横向、竖向个数
    public static int TYPE;
    //图片的id
    private int mResourceId;
    private Bitmap mBitmap;

    private List<Bitmap> mBitmapList = new ArrayList<>();
    //空缺的图片
    public static Bitmap mLastBitmap;

    private Timer mTimer;
    private TimerTask mTimerTask;
    private int mTimeCount;
    private int mStepCount;
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    mTimeTextView.setText(mTimeCount+"");
                    mTimeCount++;
                    break;

                case 1:

                    break;
                    default:
                        break;
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e("TAG", "onCreate()...");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jigsaw);
        init();
        TYPE = 3;
        density = ScreenUtil.getDeviceDensity(this);

        mBtnOriginalImg.setOnClickListener(this);
        mBtnReset.setOnClickListener(this);
        mBtnBack.setOnClickListener(this);

        //选择难度
        mDiffTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View rootView = LayoutInflater.from(JigsawActivity.this).inflate(R.layout.activity_jigsaw, null);
                popupShow(rootView);
            }
        });

        //记时
        mTimer = new Timer(true);
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                Message msg = new Message();
                msg.what = 0;
                mHandler.sendMessage(msg);
            }
        };
        mTimer.schedule(mTimerTask,0, 1000);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //获取上一个Activity传过来的数据
        final Intent intent = getIntent();
        if (intent != null) {
            mResourceId = intent.getIntExtra("picSelectedId", 0);
        }

        mGridView.post(new Runnable() {
            @Override
            public void run() {
                Log.e("TAG", "gridView.post()...");
                mWidth = ScreenUtil.getWidth(mGridView, TYPE);
                mHeight = ScreenUtil.getHeight(mGridView, TYPE);

                mBitmap = BitmapFactory.decodeResource(getResources(), mResourceId);
                mBitmap = ImageUtil.resizeBitmap(mWidth, mWidth, mBitmap);
                initGridView(TYPE);

                mAdapter = new GridViewAdapter(JigsawActivity.this, mBitmapList, mWidth, mHeight, TYPE);
                mGridView.setAdapter(mAdapter);

                //GridView的Item的点击事件
                mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (GameUtil.isMoveable(position)){
                            mStepCount++;
                            mStepTextView.setText(mStepCount+"");
                            GameUtil.swapItems(GameUtil.mItemBeans.get(position), GameUtil.blankBitmap);

                            //先清除数据源
                            mBitmapList.clear();
                            for (ItemBean itemBean : GameUtil.mItemBeans){
                                mBitmapList.add(itemBean.getBitmap());
                            }

                            //拼图成功，将原来空白的图片补上
                            if (GameUtil.isSuccess()){
                                mBitmapList.remove(mBitmapList.size()-1);
                                mBitmapList.add(mLastBitmap);

                                mTimer.cancel();//停止记时
                                mTimerTask = null;
                                //设置GridView不可再点击
                                mGridView.setOnItemClickListener(null);
                            }
                            mAdapter.notifyDataSetChanged();//更新
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        mTimeCount = 0;
        mStepCount = 0;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.original_img:
                break;

            case R.id.reset:
                initGridView(TYPE);
                mAdapter.notifyDataSetChanged();
                break;

            case R.id.back:
                JigsawActivity.this.finish();
                break;
            default:
                break;
        }
    }

    private void init(){
        mDiffTextView = findViewById(R.id.difficulty);
        mTimeTextView = findViewById(R.id.time);
        mStepTextView = findViewById(R.id.stepNumber);
        mGridView = findViewById(R.id.gridView);
        mBtnOriginalImg = findViewById(R.id.original_img);
        mBtnReset = findViewById(R.id.reset);
        mBtnBack = findViewById(R.id.back);
    }

    private void initGridView(int type){
        //设置列数为TYPE
        mGridView.setNumColumns(type);
        mTimeCount = 0;
        mStepCount = 0;
        mStepTextView.setText(mStepCount+"");
        ImageUtil.createInitBitmaps(type, mBitmap, JigsawActivity.this);
        GameUtil.getPuzzleGenerator();
        mBitmapList.clear();
        for (ItemBean itemBean : GameUtil.mItemBeans){
            mBitmapList.add(itemBean.getBitmap());
        }
    }

    /**
     * PopupWindow，选择难度
     * @param view
     */
    private void popupShow(View view){
        View mPopupView = LayoutInflater.from(this).inflate(R.layout.popupwindow_view, null);
        final EditText editText = mPopupView.findViewById(R.id.editText);
        Button btn_cancel = mPopupView.findViewById(R.id.cancel);
        Button btn_confirm = mPopupView.findViewById(R.id.confirm);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
            }
        });
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = editText.getText().toString().trim();
                if (!s.equals("")) {
                    TYPE = Integer.parseInt(s);
                    if (TYPE == 0 || TYPE == 1){
                        TYPE = 3;
                        Toast.makeText(JigsawActivity.this, "cann`t be 0 or 1!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    mDiffTextView.setText(s + " * " + s);

                    initGridView(TYPE);
//                    mWidth = mWidth - 4*(TYPE-3);
//                    mHeight = mHeight - 4*(TYPE-3);
                    mAdapter = new GridViewAdapter(JigsawActivity.this, mBitmapList, mWidth, mHeight, TYPE);
                    mGridView.setAdapter(mAdapter);
                }
                mPopupWindow.dismiss();
            }
        });

        mPopupWindow = new PopupWindow(mPopupView, 400*(int)density, 250*(int)density);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        //透明背景
        Drawable drawable = new ColorDrawable(Color.TRANSPARENT);
        mPopupWindow.setBackgroundDrawable(drawable);

        //获取位置
        int[] location = new int[2];
        view.getLocationOnScreen(location);

        mPopupWindow.showAtLocation(view, Gravity.CENTER,0, 0);
    }
}
