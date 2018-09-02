package utils;

import com.example.skyworthclub.jigsawgame.JigsawActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by skyworthclub on 2018/8/3.
 */

public class GameUtil {

    public static List<ItemBean> mItemBeans = new ArrayList<>();
    public static ItemBean blankBitmap;

    /**
     * 生成随机的item
     */
    public static void getPuzzleGenerator(){
        int type = JigsawActivity.TYPE;
        int index = 0;
        //随机打乱顺序
        for (int i=0; i < mItemBeans.size(); i++){
            index = (int)(Math.random() * (type * type));
            swapItems(mItemBeans.get(index), blankBitmap);
        }

        List<Integer> data = new ArrayList<>();
        for (int i=0; i<mItemBeans.size(); i++){
            data.add(mItemBeans.get(i).getBitmapId());
        }

        //判断生成是否有解
        if (canSolve(data))
            return;
        else
            getPuzzleGenerator();
    }

    /**
     * 交换两个itemBean
     * @param from
     * @param blank
     */
    public static void swapItems(ItemBean from, ItemBean blank){
        ItemBean tempItemBean = new ItemBean();

        //交换bitmapId
        tempItemBean.setBitmapId(from.getBitmapId());
        from.setBitmapId(blank.getBitmapId());
        blank.setBitmapId(tempItemBean.getBitmapId());

        //交换bitmap
        tempItemBean.setBitmap(from.getBitmap());
        from.setBitmap(blank.getBitmap());
        blank.setBitmap(tempItemBean.getBitmap());

        blankBitmap = from;
    }

    /**
     * 该数据是否有解
     * @param data
     * @return
     */
    private static boolean canSolve(List<Integer> data){
        //获取空白的id
        int blankId = blankBitmap.getItemId();
        //可行性原则
        if (data.size() % 2 == 1){
            return getInversion(data)%2 == 0;
        }
        else {
            //从下往上数，空格位于奇数行
            if (((blankId - 1)/ JigsawActivity.TYPE) % 2 == 1){
                return getInversion(data)%2 == 0;
            }
            else {
                //从下往上数，空格位于偶数行
                return getInversion(data)%2 == 1;
            }
        }
    }

    /**
     * 计算倒置和算法
     * @param data
     * @return
     */
    private static int getInversion(List<Integer> data){
        int inversion = 0;
        int inversionCount = 0;
        for (int i=0; i<data.size(); i++){
            for (int j=i+1; j<data.size(); j++){
                int index = data.get(i);
                if (data.get(j)!=0 && data.get(j)<index)
                    inversionCount++;
            }

            inversion += inversionCount;
            inversionCount = 0;
        }

        return inversion;
    }

    /**
     * 判断点击的item是否能够移动
     * @param position
     * @return
     */
    public static boolean isMoveable(int position){
        int type = JigsawActivity.TYPE;
        //获取空格item
        int blankId = GameUtil.blankBitmap.getItemId()-1;
        //不同行相差为type
        if (Math.abs(blankId - position) == type)
            return true;
        //相同行相差为1
        if ((blankId/type == position/type) && Math.abs(blankId-position)==1)
            return true;

        return false;
    }

    /**
     * 判断拼图是否成功
     * @return
     */
    public static boolean isSuccess(){
        int type = JigsawActivity.TYPE;
        for (ItemBean itemBean : mItemBeans){
            if (itemBean.getBitmapId() != 0 && itemBean.getBitmapId() == itemBean.getItemId())
                continue;
            else if (itemBean.getBitmapId() == 0 && itemBean.getItemId() == type*type)
                continue;
            else
                return false;
        }

        return true;
    }

}
