package guru.learningjournal.kafka.examples;

import guru.learningjournal.kafka.examples.serde.JsonSerializer;
import guru.learningjournal.kafka.examples.types.PosInvoice;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Properties;

public class PosProducer {
    private static final Logger logger = LogManager.getLogger();

    public static void main(String[] args) {
        if (args.length < 2) {
            logger.error("Not enough parameters !");
            return;
        }
        String topicName = args[0];
        Integer NumOfThrds = Integer.parseInt(args[1]);
        Integer speed = Integer.parseInt(args[2]);

        Properties props = new Properties();

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092,localhost:9093,localhost:9094");
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "POSProducer");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        KafkaProducer<Integer, PosInvoice> producer = new KafkaProducer<Integer, PosInvoice>(props);

        Thread[] dispatchers = new Thread[NumOfThrds];
        logger.info("Starting dispatcher threads...");
        for (int i = 0; i < NumOfThrds; i++) {
            dispatchers[i] = new Thread(new Dispatcher(producer, topicName, speed, i));
            dispatchers[i].start();
        }

        try {
            for (Thread t : dispatchers) {
                t.join();
            }
        } catch (InterruptedException e) {
            logger.error("Smthing goes wrong...");
            e.printStackTrace();
        }
    }
}
