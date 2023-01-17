package fr.ensma.lias.javarabbitmqapi;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;

public class RabbitMQConsumer extends DefaultConsumer {
    private RabbitMQBasicReceiver receiver;

    public RabbitMQConsumer( RabbitMQBasicReceiver receiver ) {
        super( receiver.getChannel() );
        this.receiver = receiver;
    }

    @Override
    public Channel getChannel() {
        return receiver.getChannel();
    }

}
