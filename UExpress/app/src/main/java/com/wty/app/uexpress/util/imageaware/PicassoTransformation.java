package com.wty.app.uexpress.util.imageaware;

import android.graphics.Bitmap;
import android.util.Log;

import com.squareup.picasso.Transformation;

/**
 *  picasso 图片显示转换
 */

public class PicassoTransformation implements Transformation {
    //图片压缩后的最短边长
    private int targetWidth;
    //图片压缩后的最短边长
    private int targetHeight;

    public PicassoTransformation(int targetWidth, int targetHeight){
        this.targetWidth = targetWidth;
        this.targetHeight = targetHeight;
    }



    @Override
    public Bitmap transform(Bitmap source) {
//        Log.i("test","source.getHeight()="+source.getHeight()+",source.getWidth()="+source.getWidth()
//                +",targetWidth="+targetWidth+" ,targetHeight="+targetHeight);

        if(source.getWidth()==0){
            return source;
        }
        int sourceMinLength;//显示view的最短边长
        // view 宽>高
        if (targetWidth > targetHeight){
            sourceMinLength = source.getWidth() > source.getHeight()? targetHeight:targetWidth;
        }else {
            sourceMinLength = source.getWidth() > source.getHeight()?targetHeight:targetWidth;
        }
        //最短边最大值不超过600
        sourceMinLength = sourceMinLength > 600?600:sourceMinLength;
        //最短边最小值不小于100
        sourceMinLength = sourceMinLength <100 ? 100:sourceMinLength;

        //图片本身 宽 > 高
        if (source.getWidth()>source.getHeight()){
            //压缩后的图片高度
            targetHeight = source.getHeight() > sourceMinLength?sourceMinLength:source.getHeight();
            //压缩后的图片宽度
            targetWidth = (targetHeight*((source.getWidth()*100)/source.getHeight()))/100;
        }else {//高 > 宽
            targetWidth = source.getWidth() > sourceMinLength?sourceMinLength:source.getWidth();
            targetHeight = targetWidth * ((source.getHeight()*100)/source.getWidth())/100;
        }

//        Log.i("test","finalWidth="+targetWidth+" ,finalHeight="+targetHeight);

        //如果图片小于设置的宽度，则返回原图
        if(source.getWidth()<targetWidth){
            return source;
        }else{
            if (targetHeight != 0 && targetWidth != 0) {
                Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);
                if (result != source) {
                    // Same bitmap is returned if sizes are the same
                    source.recycle();
                }
                return result;
            } else {
                return source;
            }
        }
    }


    @Override
    public String key() {
        return "transformation=" + targetWidth;
    }
}


