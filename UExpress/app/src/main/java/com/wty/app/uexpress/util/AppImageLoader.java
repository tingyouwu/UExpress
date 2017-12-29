package com.wty.app.uexpress.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.text.TextUtils;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.wty.app.uexpress.R;
import com.wty.app.uexpress.util.imageaware.ImageSize;
import com.wty.app.uexpress.util.imageaware.ImageViewAware;
import com.wty.app.uexpress.util.imageaware.PicassoTransformation;

import java.io.File;

/**
 *  图片加载工具类
 */

public class AppImageLoader {

    public static void displayImage(Context context,String uri, ImageView imageView) {
        ImageViewAware imageViewAware = new ImageViewAware(imageView);
        ImageSize targetSize = CorePhotoUtils.defineTargetSizeForView(imageViewAware, context.getApplicationContext());

        //图片路径适配处理
        String newUri = CoreImageURLUtils.getUri(uri);
        if(TextUtils.isEmpty(newUri)){
            return;
        }
        //本地路径
        if (urlIsLocal(newUri)) {

            //调整图片显示的 宽高
            int[] widthHeight = CorePhotoUtils.adjustWidthAndHeight(targetSize.getWidth()
                    ,targetSize.getHeight(), CoreImageURLUtils.ImageScheme.FILE.crop(newUri));

            Picasso.with(context.getApplicationContext())
                    .load(new File(CoreImageURLUtils.ImageScheme.FILE.crop(newUri)))
                    .error(R.mipmap.img_fail)
                    .placeholder(R.drawable.bg_icon_background)
                    .resize(widthHeight[0],widthHeight[1])
                    .config(Bitmap.Config.RGB_565)
                    .into(imageView);
        } else {//加载非本地图片
            Picasso.with(context.getApplicationContext())
                    .load(newUri)
                    .error(R.mipmap.img_fail)
                    .placeholder(R.drawable.bg_icon_background)
                    .transform(new PicassoTransformation(targetSize.getWidth(), targetSize.getHeight()))
                    .stableKey(uri)
                    .config(Bitmap.Config.RGB_565)
                    .into(imageView);
        }
    }

    public static void displayImage(Context context,int resourId, ImageView imageView) {
        Picasso.with(context.getApplicationContext())
                .load(resourId)
                .error(R.mipmap.img_fail)
                .placeholder(R.drawable.bg_icon_background)
                .config(Bitmap.Config.RGB_565)
                .into(imageView);
    }

    /**
     * 判断图片路径是否是本地路径
     * @param imagePath 图片路径
     * @return
     */
    public static boolean urlIsLocal(String imagePath) {
        return imagePath.contains(Environment.getExternalStorageDirectory().toString());
    }
}
