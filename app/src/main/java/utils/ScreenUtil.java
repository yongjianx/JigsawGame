package utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.GridView;

/**
 * Created by skyworthclub on 2018/7/30.
 */

public class ScreenUtil {

    /**
     * @param context
     * @return
     * pixels = dps * (density / 160)
     * metrics.density = density / 160//metrics.density与density是不同的东西
     */
    public static DisplayMetrics getScreenSize(Context context){
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        Display display = windowManager.getDefaultDisplay();
        display.getMetrics(metrics);//metrics.widthPixel, metrics.heightPixel以px为单位
        return metrics;
    }

    /**
     * 获取屏幕密度
     * @param context
     * @return
     */
    public static float getDeviceDensity(Context context){
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);

        return metrics.density;
    }

    /**
     * GridView的宽度
     * @return
     */
    public static float getWidth(GridView gridView, int type){
        final float width = gridView.getWidth()-gridView.getHorizontalSpacing()*(type-1)
                -gridView.getPaddingLeft()-gridView.getPaddingRight();

        return width;
    }

    /**
     * GridView的高度
     * @return
     */
    public static float getHeight(GridView gridView, int type){
        final float height = gridView.getHeight()-gridView.getVerticalSpacing()*(type-1)
                -gridView.getPaddingBottom()-gridView.getPaddingTop();
        Log.e("TAG", "height="+gridView.getHeight());

        return height;
    }
}
