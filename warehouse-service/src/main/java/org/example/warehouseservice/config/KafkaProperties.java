package org.example.warehouseservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.*;

@ConfigurationProperties("kafka")
@Component
@Data
public class KafkaProperties {
    private Topics topics = new Topics();
    private Producer producer = new Producer();
    private Consumer consumer = new Consumer();

    @Data
    public static class Topics {
        OrderCreatedDlt orderCreatedDlt = new OrderCreatedDlt();
        OrderCreated orderCreated = new OrderCreated();
        @Data
        public static class OrderCreatedDlt {
            private String name;
            private int partitions;
            private int replicas;

        }
        @Data
        public static class OrderCreated {
            private String name;
            private int partitions;
            private int replicas;

        }
    }

    @Data
    public static class Producer {
        private String bootstrapServer;
        private String keySerializer;
        private String valueSerializer;
        private LinkedHashMap<String, String> additionalProperties;


    }

    @Data
    public static class Consumer {
        private String groupId;
        private String bootstrapServer;
        private String keyDeserializer;
        private String valueDeserializer;
        private Long fixedBackOffInterval;
        private Long fixedBackOffMaxAttempts;
        private LinkedHashMap<String, String> additionalProperties;
    }
}
