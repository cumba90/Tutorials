package guru.learningjournal.kafka.examples;

import guru.learningjournal.kafka.examples.datagenerator.InvoiceGenerator;
import guru.learningjournal.kafka.examples.types.PosInvoice;
import org.apache.kafka.clients.producer.KafkaProducer;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Dispatcher implements Runnable{
    private static final Logger logger = LogManager.getLogger();
    private KafkaProducer<Integer, PosInvoice> producer;
    private String topicName;
    private Integer speed;
    private  Integer trdNo = 0;

    public Dispatcher(KafkaProducer<Integer, PosInvoice> producer, String topicName, Integer speed, Integer trdNo) {
        this.producer = producer;
        this.topicName = topicName;
        this.speed = speed;
        this.trdNo = trdNo;
    }

    public void run () {
        logger.info("Start processing thread " + this.trdNo);
        int counter = 0;
        while (true) {
            try {
                PosInvoice invoice = InvoiceGenerator.getInstance().getNextInvoice();
                producer.send(new ProducerRecord<>(topicName, null, invoice));
                counter++;
                Thread.sleep(speed);
            } catch (InterruptedException e) {
                e.printStackTrace();
                logger.error(this.trdNo + " has been interrupted with errors.");
                break;
            }
        }
        logger.info("Message sending to the tipic " + this.topicName + " has been finished. Messages sended " + counter);
    }
}
