package utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import com.example.skyworthclub.jigsawgame.JigsawActivity;
import com.example.skyworthclub.jigsawgame.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by skyworthclub on 2018/8/3.
 */

public class ImageUtil {

    private static ItemBean itemBean;

    public static void createInitBitmaps(int type, Bitmap picSelected, Context context){
        //先把mItemBeans清空
        GameUtil.mItemBeans.clear();

        Bitmap bitmap = null;
        List<Bitmap> bitmapList = new ArrayList<>();

        //每个item的宽高
        int itemWidth = picSelected.getWidth() / type;
        int itemHeight = picSelected.getHeight() / type;
        for (int i=0; i<type; i++){
            for (int j=0; j<type; j++){

                bitmap = Bitmap.createBitmap(picSelected, j*itemWidth, i*itemHeight,
                        itemWidth, itemHeight);
                bitmapList.add(bitmap);

                itemBean = new ItemBean(i*type+j+1, i*type+j+1, bitmap);
                GameUtil.mItemBeans.add(itemBean);
            }
        }

        //保存最后一个图片在拼图完成式填充
        JigsawActivity.mLastBitmap = bitmapList.get(type*type-1);

        //移除最后一个item
        bitmapList.remove(type*type-1);
        GameUtil.mItemBeans.remove(type*type-1);

        Bitmap blankBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.blank);
        blankBitmap = resizeBitmap(itemWidth, itemHeight, blankBitmap);

        bitmapList.add(blankBitmap);
        GameUtil.mItemBeans.add(new ItemBean(type*type, 0, blankBitmap));
        GameUtil.blankBitmap = GameUtil.mItemBeans.get(type*type-1);

    }

    /**
     * 处理图片，放大缩小到合适位置
     * @param newWidth
     * @param newHeight
     * @param bitmap
     * @return
     */
    public static Bitmap resizeBitmap(float newWidth, float newHeight, Bitmap bitmap){
        Matrix matrix = new Matrix();
        matrix.postScale(newWidth / bitmap.getWidth(), newHeight / bitmap.getHeight());

        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return newBitmap;
    }
}
