package tutorial1;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class ProducerDemoWithCallBack {
    public static void main(String[] args) {

        final Logger logger = LoggerFactory.getLogger(ProducerDemoWithCallBack.class);

        String bootstrapServers = "127.0.0.1:9092";

        //Create the Producer Properties

        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        //Create the Producer

        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties);


        //create a producer record

        for (int i = 0; i <= 10; i++) {
            String topic = "first_topic";
            String value = "hello world" + Integer.toString(i);
            ProducerRecord<String, String> producerRecord =
                    new ProducerRecord<String, String>(topic, value);


            //send Data  -->ASYNCHRONOUS

            producer.send(producerRecord, new Callback() {
                public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                    //executes every time a record is successfully sent or thrown an exception
                    if (e == null) {
                        logger.info("Received new MetaData. \n" +
                                "Topic: \t" + recordMetadata.topic() + "\t" +
                                "Partition : \t" + recordMetadata.partition() + "\t" +
                                "Offset : \t" + recordMetadata.offset() + "\t" +
                                "TimeStamp : \t" + recordMetadata.timestamp()
                        );

                    } else {
                        logger.error("Error occurred while Producing:" + e);
                    }
                }
            });
        }
        //flush data

        producer.flush();
// flush and close producer

        producer.close();

    }

}
