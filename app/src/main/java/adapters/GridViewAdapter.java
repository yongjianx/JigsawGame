package adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.skyworthclub.jigsawgame.JigsawActivity;

import java.util.List;

import utils.ScreenUtil;

/**
 * Created by skyworthclub on 2018/8/1.
 */

public class GridViewAdapter extends BaseAdapter {
    private Context context;
    private List<Bitmap> picList;
    private float width;
    private float height;
    private int type;

    public GridViewAdapter(Context context, List<Bitmap> picList, float width, float height, int type){
        this.context = context;
        this.picList = picList;
        this.width = width;
        this.height = height;
        this.type = type;
    }

    @Override
    public int getCount() {
        return picList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return picList.get(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ImageView imageView = null;
        final int ImageViewWidth = (int) width / type;
        final int ImageViewHight = (int) height / type;

        if (convertView == null){
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(ImageViewWidth, ImageViewHight));
            //设置显示比例类型
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        }
        else {
            imageView = (ImageView) convertView;
        }

        imageView.setBackgroundColor(Color.BLACK);
        imageView.setImageBitmap(picList.get(position));

        return imageView;
    }

}
