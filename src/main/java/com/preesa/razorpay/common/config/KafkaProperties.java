package com.preesa.razorpay.common.config;

import com.preesa.razorpay.common.enums.EventAggregateType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "app.kafka")
@Getter
@Setter
public class  KafkaProperties{
        Map<String,String> topics = new HashMap<>();

    public String topicFor(EventAggregateType aggregateType){
        String topic= topics.get(aggregateType.name().toLowerCase());

        if(topic== null){
            throw new IllegalStateException("No Kafka topic is configured for aggregateType: "+aggregateType.name());
        }
        return  topic;
    }
}
