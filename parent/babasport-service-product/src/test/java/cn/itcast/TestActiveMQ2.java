package cn.itcast;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * 消息消费方
 * 
 * @author Administrator
 *
 */
public class TestActiveMQ2 {

	public static void main(String[] args) throws JMSException, InterruptedException {

		// 连接工厂
		ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
		activeMQConnectionFactory.setBrokerURL("tcp://192.168.56.101:61616");
		activeMQConnectionFactory.setUserName("admin");
		activeMQConnectionFactory.setPassword("admin");

		// 连接对象
		Connection connection = activeMQConnectionFactory.createConnection();
		connection.start();

		// 获得session
		Session session = connection.createSession(Boolean.FALSE,
				Session.AUTO_ACKNOWLEDGE);

		// 消息目的地（消息队列名称）
		Destination destination = session.createQueue("maoIds");

		// 消息消费方
		MessageConsumer consumer = session.createConsumer(destination);

		while (true) {
			Thread.sleep(3000);
			// 接收消息
			TextMessage message = (TextMessage) consumer.receive();
			System.out.println("打印消息内容：" + message.getText());
			message.acknowledge();
		}

	}

}
