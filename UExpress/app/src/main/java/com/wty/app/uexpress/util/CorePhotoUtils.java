package com.wty.app.uexpress.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.util.DisplayMetrics;

import com.wty.app.uexpress.util.imageaware.ImageSize;
import com.wty.app.uexpress.util.imageaware.ImageViewAware;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 与业务无关的 Photo工具
 */
public class CorePhotoUtils {

    /**
     * 读取图片属性
     *
     * @param path ：图片绝对路径
     * @return degree ：旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
                default:
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return degree;
    }

    /**
     * 保存bitmap到sd卡filePath文件中 如果有，则删除
     *
     * @param bitmap
     * @param filePath :图片绝对路径
     * @return boolean :是否成功
     */
    public static boolean saveBitmap2file(Bitmap bitmap, String filePath) {
        if (bitmap == null) {
            return false;
        }
        //压缩格式
        CompressFormat format = CompressFormat.JPEG;
        int quality = 100;
        OutputStream stream = null;
        File file = new File(filePath);
        File dir = file.getParentFile();
        if (!dir.exists()) {
            dir.mkdirs();//创建父目录
        }
        if (file.exists()) {
            file.delete();
        }

        try {
            stream = new FileOutputStream(filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bitmap.compress(format, quality, stream);
    }

    /**
     * 不加载图片的前提下获得图片的宽高
     *
     * @param path 图片本地路径
     * @return int[0]宽、int[1]高
     */
    public static int[] getImageWidthHeight(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();

        /**
         * 最关键在此，把options.inJustDecodeBounds = true;
         * 这里再decodeFile()，返回的bitmap为空，但此时调用options.outHeight时，已经包含了图片的高了
         */
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options); // 此时返回的bitmap为null
        /**
         *options.outHeight为原始图片的高
         */
        return new int[]{options.outWidth, options.outHeight};
    }

    /**
     * Defines target size for image aware view. Size is defined by target
     * parameters or device display dimensions.<br />
     */
    public static ImageSize defineTargetSizeForView(ImageViewAware imageAware, Context context) {
        ImageSize maxImageSize = getMaxImageSize(context);

        int width = imageAware.getWidth();
        if (width <= 0) {
            width = maxImageSize.getWidth();
        }

        int height = imageAware.getHeight();
        if (height <= 0) {
            height = maxImageSize.getHeight();
        }
        return new ImageSize(width, height);
    }

    /**
     * 获取屏幕宽高
     *
     * @param context
     * @return
     */
    public static ImageSize getMaxImageSize(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();

        int width = 0;
        if (width <= 0) {
            width = displayMetrics.widthPixels;
        }
        int height = 0;
        if (height <= 0) {
            height = displayMetrics.heightPixels;
        }
        return new ImageSize(width, height);
    }

    /**
     * 根据Imageview 的宽、高和本地图片的大小 动态调整加载的图片大小
     *
     * @param targetWidth  Imageview 的宽
     * @param targetHeight Imageview的高
     * @param localPath    图片的本地路径
     * @return 调整后的宽高 int[0]宽 int[1]高
     */
    public static int[] adjustWidthAndHeight(int targetWidth, int targetHeight, String localPath) {
        int[] result = {100, 100};
        if (localPath != "") {
            //得到本地图片的原始路径
            int[] params = CorePhotoUtils.getImageWidthHeight(localPath);
            //获得图片的旋转角度
            int degree = CorePhotoUtils.readPictureDegree(localPath);

            if (params[0] <= 0) {
                return result;
            }
            int sourceMinLength;//显示view的最短边长
            //view 宽>高
            if (targetWidth > targetHeight) {
                sourceMinLength = params[0] > params[1] ? targetHeight : targetWidth;
            } else {
                sourceMinLength = params[0] > params[1] ? targetHeight : targetWidth;
            }
            //最短边最大值不超过600
            sourceMinLength = sourceMinLength > 600 ? 600 : sourceMinLength;
            //最短边最小值不小于100
            sourceMinLength = sourceMinLength < 100 ? 100 : sourceMinLength;

            //图片本身 宽 > 高
            if (params[0] > params[1]) {
                //压缩后的图片高度
                targetHeight = params[1] > sourceMinLength ? sourceMinLength : params[1];
                //压缩后的图片宽度
                targetWidth = (targetHeight * ((params[0] * 100) / params[1])) / 100;
            } else {//高 > 宽
                targetWidth = params[0] > sourceMinLength ? sourceMinLength : params[0];
                targetHeight = targetWidth * ((params[1] * 100) / params[0]) / 100;
            }

            //竖图,但是却 宽 > 高
            if (degree % 180 > 0 && (params[0] > params[1])) {
                result[0] = targetHeight;
                result[1] = targetWidth;
            } else {
                result[0] = targetWidth;
                result[1] = targetHeight;
            }
        }
        return result;
    }
}
