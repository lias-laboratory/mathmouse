package fr.ensma.lias.javarabbitmqapi;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Classe paramétrable, permettant d'écrire un object dans un tableau d'octet et inversement. Une
 * fois la classe instanciés, elle ne peut lire et écrire que le type d'object sur lequel elle a été
 * instanciée. Il faut donc une instance par type d'objet à sérialiser.
 * 
 * @author ponchatc
 *
 * @param <T>
 */
public class RabbitMQObjectManager<T> {
    private Logger logger;

    public RabbitMQObjectManager() {
        logger = LoggerFactory.getLogger( getClass() );
        logger.info( this.toString() );
    }

    public T fromBytes( byte[] body ) {
        T obj = null;
        try {
            logger.debug( "instantiating a ByteArrayInputStreamObject from byte array..." );
            ByteArrayInputStream bis = new ByteArrayInputStream( body );
            ObjectInputStream ois = new ObjectInputStream( bis );
            logger.debug( "instantiating new object from ObjectInputStream..." );
            obj = (T) ois.readObject();
            ois.close();
            bis.close();
            logger.debug( "instantiation process completed successfully." );
        } catch ( IOException | ClassNotFoundException e ) {
            logger.error(
                    "an error occured while reading new byte array, object type may not match class object type." );
            e.printStackTrace();
        }
        return obj;
    }

    public byte[] toBytes( T object ) {
        byte[] bytes;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream( baos );
            oos.writeObject( object );
            oos.flush();
            oos.reset();
            bytes = baos.toByteArray();
            oos.close();
            baos.close();
        } catch ( IOException e ) {
            bytes = new byte[] {};
            logger.error( "Unable to write to output stream", e );
        }
        return bytes;
    }

}
