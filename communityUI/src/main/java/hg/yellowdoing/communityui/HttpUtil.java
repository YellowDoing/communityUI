package hg.yellowdoing.communityui;


import android.os.Handler;
import android.os.Message;
import okio.BufferedSink;

import java.io.IOException;

/**
 * Created by YellowDoing on 2017/10/18.
 */

public class HttpUtil {

/*
    private OkHttpClient okHttpClient;
    private FormBody.Builder formBodyBuilder;
    private Request.Builder requestBuilder;

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


    //在此处可配置用户Id信息
    public HttpUtil addUserId(){
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




    *//**
     * 上传Json的Post请求
     *//*
    public void post(Callback callback, final String json) {

        final MyHandler handler = new MyHandler(callback);

        final RequestBody requestBody = new RequestBody() {
            @Override
            public MediaType contentType() {
                return MediaType.parse("application/json");
            }

            @Override
            public void writeTo(BufferedSink bufferedSink) throws IOException {
                bufferedSink.write(json.getBytes());
            }
        };

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = okHttpClient.newCall(requestBuilder.post(requestBody).build()).execute();
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
    }*/
}