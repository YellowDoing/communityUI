package bmob.yellowdoing.com.bmobcommunity;

import android.os.Handler;
import android.os.Message;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by YellowDoing on 2017/11/14.
 */

public class HttpUtil {

    private OkHttpClient okHttpClient;
    private FormBody.Builder formBodyBuilder;
    private Request.Builder requestBuilder;
    private RequestBody mRequestBody;

    public static HttpUtil Builder() {
        return new HttpUtil();
    }

    private HttpUtil() {
        okHttpClient = new OkHttpClient();
        requestBuilder = new Request.Builder();
    }

    public HttpUtil url(String url) {
        requestBuilder.url(url);
        return this;
    }

    public HttpUtil add(String key, String value) {
        if (formBodyBuilder == null)
            formBodyBuilder = new FormBody.Builder();
        formBodyBuilder.add(key, value);
        return this;
    }


    public HttpUtil addHeader(String key, String value) {
        requestBuilder.addHeader(key, value);
        return this;
    }

    public void post(Callback callback) {
        final MyHandler handler = new MyHandler(callback);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = okHttpClient.newCall(requestBuilder.post(formBodyBuilder.build()).build()).execute();
                    Message message = new Message();
                    message.obj = response.body().string();
                    handler.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public interface Callback {
        void getResponse(String response);
    }

    private static class MyHandler extends Handler {

        private HttpUtil.Callback callback;

        MyHandler(HttpUtil.Callback callback1) {
            this.callback = callback1;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            callback.getResponse((String) msg.obj);
        }
    }

}