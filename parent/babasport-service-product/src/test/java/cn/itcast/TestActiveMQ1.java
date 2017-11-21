package cn.itcast;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * 消息发送方
 * 
 * @author Administrator
 *
 */
public class TestActiveMQ1 {

	public static void main(String[] args) throws JMSException {

		// 连接工厂
		ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
		activeMQConnectionFactory.setBrokerURL("tcp://192.168.56.101:61616");
		activeMQConnectionFactory.setUserName("admin");
		activeMQConnectionFactory.setPassword("admin");

		// 连接对象
		Connection connection = activeMQConnectionFactory.createConnection();
		connection.start();

		//创建一个session
        //第一个参数:是否支持事务，如果为true，则会忽略第二个参数，被jms服务器设置为SESSION_TRANSACTED
        //第二个参数为false时，paramB的值可为Session.AUTO_ACKNOWLEDGE，Session.CLIENT_ACKNOWLEDGE，DUPS_OK_ACKNOWLEDGE其中一个。
        //Session.AUTO_ACKNOWLEDGE为自动确认，客户端发送和接收消息不需要做额外的工作。哪怕是接收端发生异常，也会被当作正常发送成功。
        //Session.CLIENT_ACKNOWLEDGE为客户端确认。客户端接收到消息后，必须调用javax.jms.Message的acknowledge方法。jms服务器才会当作发送成功，并删除消息。
        //DUPS_OK_ACKNOWLEDGE允许副本的确认模式。一旦接收方应用程序的方法调用从处理消息处返回，会话对象就会确认消息的接收；而且允许重复确认。
		Session session = connection.createSession(false,
				Session.AUTO_ACKNOWLEDGE);

		// 消息目的地（消息队列名称）
		Destination destination = session.createQueue("maoIds");

		// 消息生产者
		MessageProducer producer = session.createProducer(destination);

		// 设置不持久化
		producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

		// 创建消息内容对象
		TextMessage message = session.createTextMessage("测试发送的消息"+System.currentTimeMillis());

		// 发送消息
		producer.send(message);
		
		connection.close();

	}

}
