package com.wty.app.uexpress.data.entity;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.orhanobut.logger.Logger;
import com.wty.app.uexpress.BuildConfig;
import com.wty.app.uexpress.http.HttpUtil;
import com.wty.app.uexpress.http.HttpUtilCore;
import com.wty.app.uexpress.http.WebServiceException;

/**
 * @author wty
 * 基础响应实体
 **/
public abstract class BaseResponseEntity {

	protected HttpUtilCore.Method method = HttpUtilCore.Method.Post;

	protected abstract String createArgs(Object... params);

	protected abstract String makeUrl();

    public String requestJson(Object... params){
        String json = null;
        try {
            json = requestHttp(params);
            //打印请求数据
			if(BuildConfig.DEBUG){
				Logger.d(" entityName = :%s \n url = %s \n args = %s \n",this.getClass().getSimpleName(),makeUrl(),createArgs(params));
            	Logger.json(json);
			}
        } catch (WebServiceException e){
			e.printStackTrace();
			Logger.e(e," entityName = :%s \n url = %s \n args = %s \n error = %s",this.getClass().getSimpleName(),
					makeUrl(),
					createArgs(params),
					e.getMessage());
		} catch (Exception e) {
            e.printStackTrace();
            Logger.e(e," entityName = :%s \n url = %s \n args = %s \n error = %s",this.getClass().getSimpleName(),
                    makeUrl(),
                    createArgs(params),
                    e.getMessage());
        }
        return json;
    }

	/**
	 * @Desc  请求数据 post
	 **/
	private String requestHttp(Object... params) throws Exception{
		String json;
		String url = makeUrl();
		//只捕抓http内部exception
		if(method== HttpUtilCore.Method.Get){
			String args = createArgs(params);
			json = HttpUtil.getToService(url+args);
		}else{
			String args = createArgs(params);
			json = HttpUtil.postToService(url, args);
		}
		return json;
	}

	public interface OnResponseListener<T extends BaseResponseEntity>{
		void onSuccess(String json, T response);
		void onTimeout();
	}

	/**
	 * gson解析数据
	 **/
	public String handleResponse(String json,OnResponseListener onResponseListener){
			if (json != null && !json.equals("")) {
                BaseResponseEntity response ;
                try {
                    response = new Gson().fromJson(json, this.getClass());
                }catch (JsonSyntaxException e){
                    e.printStackTrace();
					return "解析JSON失败！请查看原数据";
                }

				if(onResponseListener!=null){
					onResponseListener.onSuccess(json,response);
				}
				return "200";
			}else{
	        	if(onResponseListener!=null){
					onResponseListener.onTimeout();
				}
	        	return "获取不到任何数据，请查看服务器!";
	        }
	}
}
