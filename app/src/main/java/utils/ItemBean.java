package utils;

import android.graphics.Bitmap;

/**
 * Created by skyworthclub on 2018/8/3.
 */

public class ItemBean {

    private int mItemId;
    private int mBitmapId;
    private Bitmap mBitmap;

    public ItemBean(){

    }

    public ItemBean(int mItemId, int mBitmapId, Bitmap mBitmap){
        this.mItemId = mItemId;
        this.mBitmapId = mBitmapId;
        this.mBitmap = mBitmap;
    }

    public void setItemId(int mItemId){
        this.mItemId = mItemId;
    }

    public int getItemId(){
        return mItemId;
    }

    public void setBitmapId(int mBitmapId){
        this.mBitmapId = mBitmapId;
    }

    public int getBitmapId(){
        return mBitmapId;
    }

    public void setBitmap(Bitmap mBitmap){
        this.mBitmap = mBitmap;
    }

    public Bitmap getBitmap(){
        return mBitmap;
    }
}
