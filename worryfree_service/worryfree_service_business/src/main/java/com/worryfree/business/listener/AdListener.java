package com.worryfree.business.listener;


import com.worryfree.business.config.RabbitMQConfig;
import okhttp3.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.SQLOutput;

@Component
public class AdListener {

    @RabbitListener(queues = RabbitMQConfig.AD_UPDATE_QUEUE)
    public void recevieMessage(String message){

        System.out.println("接收到的消息为:"+message);

        OkHttpClient okHttpClient = new OkHttpClient();
        String url = "http://172.16.153.2/ad_update?position=" + message;

        Request request = new Request.Builder().url(url).build();
        Call newCall = okHttpClient.newCall(request);
        newCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("请求失败");
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                System.out.println("请求成功：" + response.message());
            }
        });

    }

}
