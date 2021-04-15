package com.worryfree.search.listener;

import com.worryfree.search.config.RabbitMQConfig;
import com.worryfree.search.service.ESManagerService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GoodsDelListener {

    @Autowired
    private ESManagerService esManagerService;

    @RabbitListener(queues = RabbitMQConfig.SEARCH_DEL_QUEUE)
    public void resviceMessage(String spuId){
        System.out.println("删除索引库监听类,接收到的spuId:  "+spuId);

        esManagerService.delDataBySpuId(spuId);
    }
}
