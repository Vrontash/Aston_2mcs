package org.example.orderservice.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties("kafka")
@Component
@Getter
@Setter
public class KafkaProperties {
    private Topics topics = new Topics();
    private Producer producer = new Producer();

    @Getter
    @Setter
    public static class Topics {
        OrderCreated orderCreated = new OrderCreated();

        @Getter
        @Setter
        public static class OrderCreated {
            private String name;
            private int partitions;
            private int replicas;

        }
    }

    @Getter
    @Setter
    public static class Producer {
        private String bootstrapServer;
        private String keySerializer;
        private String valueSerializer;
        private String batchSize;
        private String lingerMs;
        private String compressionType;
    }

}
