package com.wty.app.uexpress.util;

import java.util.Locale;

/**
 * @author wty
 *  图片路径处理工具
 */

public class CoreImageURLUtils {

    public static String getUri(String imagePath){
        switch (ImageScheme.ofUri(imagePath)){

            case FILE:
            case CONTENT:
            case HTTP:
            case HTTPS:
                return imagePath;
            case HEADIMG:
                return getUriFromHeadImg(imagePath);
            case ASSETS:
                return  getUriFromAssets(imagePath);
            default:
                return "";
        }
    }

    /**
     *  处理 headimg:// 开头的路径（适用于picasso）
     *  headimg://livingthing.jpg   -->   file:///android_asset/headimg/livingthing.jpg
     */
    private static String getUriFromHeadImg(String imagePath) {
        return "file:///android_asset/headimg/" + ImageScheme.HEADIMG.crop(imagePath);
    }

    private static String getUriFromAssets(String imagePath) {
        return "file:///android_asset/"+ ImageScheme.ASSETS.crop(imagePath);
    }


    public enum ImageScheme{

        HEADIMG("headimg"),HTTP("http"), HTTPS("https"), FILE("file"), CONTENT("content"),
        ASSETS("assets"), UNKNOWN("");

        private String uriPrefix;

        ImageScheme(String scheme){
            this.uriPrefix = scheme + "://";
        }

        public static ImageScheme ofUri(String uri){
            if (uri != null){
                for (ImageScheme s : values()){
                    if (s.belongsTo(uri)){
                            return s;
                    }
                }
            }
            return UNKNOWN;
        }

        public String getUriPrefix(){
                return uriPrefix;
            }

        /**
         * 判断 Uri 开头字段是否满足
         * @param uri
         */
        private boolean belongsTo(String uri){
            return uri.toLowerCase(Locale.US).startsWith(uriPrefix);
        }

        /**
         * @Desc 去掉前缀
         **/
        public String crop(String uri){
                return uri.substring(uriPrefix.length());
            }

        /**
         * @Desc 组装前缀
         **/
        public String wrap(String path){
                return uriPrefix + path;
            }
        }
}
