package org.example.orderservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties("kafka")
@Component
@Data
public class KafkaProperties {
    private Topics topics = new Topics();
    private Producer producer = new Producer();

    @Data
    public static class Topics {
        OrderCreated orderCreated = new OrderCreated();
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
        private String batchSize;
        private String lingerMs;
        private String compressionType;
    }

}
