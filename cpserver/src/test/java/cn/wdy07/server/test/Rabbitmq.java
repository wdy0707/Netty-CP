package cn.wdy07.server.test;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.AMQP.Queue.DeclareOk;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class Rabbitmq {
	public static void main(String[] args) throws IOException, TimeoutException {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					func2();
				} catch (IOException | TimeoutException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	private static void func1() throws IOException, TimeoutException {
		// 1.创建连接工厂
		ConnectionFactory factory = new ConnectionFactory();
		// 2.配置服务器地址和连接信息
		factory.setHost("192.168.99.100");
		factory.setUsername("guest");
		factory.setPassword("guest");
		factory.setVirtualHost("/");
		// 3.获取连接
		Connection connection = factory.newConnection();
		// 4.创建信道
		Channel channel = connection.createChannel();
		// 5.声明一个类型为 type 的持久化的、非自动删除的交换器
		String exchangeName = "hello-exchange";
		channel.exchangeDeclare(exchangeName, BuiltinExchangeType.DIRECT, true, false, null);
		// 6.指明路由键
		String routingKey = "my-key";
		// 7.需要传递的数据
		byte[] messageBodyBytes = "Hello RabbiMQ!".getBytes();
		// 8.将消息发布到指定的交换机上,设置投递模式为2,对应模式名为persistent,代表消息会被持久化存储
		channel.basicPublish(exchangeName, routingKey, new AMQP.BasicProperties.Builder().deliveryMode(2).build(),
				messageBodyBytes);
		// 9.关闭信道
		channel.close();
		// 10.关闭连接
		connection.close();
	}

	private static void func2() throws IOException, TimeoutException {
		// 1.创建连接工厂
		ConnectionFactory factory = new ConnectionFactory();
		// 2.配置服务器地址和连接信息
		factory.setHost("192.168.99.100");
		factory.setUsername("guest");
		factory.setPassword("guest");
		factory.setVirtualHost("/");
		// 3.获取连接
		Connection connection = factory.newConnection();
		// 4.创建信道
		final Channel channel = connection.createChannel();
		// 5.声明一个类型为 type 的持久化的、非自动删除的交换器
		String exchangeName = "hello-exchange";
		channel.exchangeDeclare(exchangeName, BuiltinExchangeType.DIRECT, true, false, null);
		// 6.声明一个具名的、持久化的、非排他的、不自动删除的队列
		String queueName = "hello-queue";
		channel.queueDeclare(queueName, true, false, false, null);
		// 7.建立绑定关系
		String bindingKey = "my-key";
		channel.queueBind(queueName, exchangeName, bindingKey);
		final Set<String> set = new HashSet<String>(); 
		final Object lock = new Object();
		DeclareOk declareOk = channel.queueDeclarePassive(queueName);
		//获取队列中的消息个数
		int num = declareOk.getMessageCount();
		// 8.订阅并消费消息
		channel.basicConsume(queueName, false, "myConsumerTag", new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
					byte[] body) throws IOException {
				// 9.处理收到的消息
				set.add(new String(body));
				// 10.手动ACK
				long deliveryTag = envelope.getDeliveryTag();
				channel.basicAck(deliveryTag, false);
			}
		});
		
		lock.notify();
		
		System.out.println(set);

		// 11.这里为了观察结果，先不关闭连接
//		 channel.close();
//		 connection.close();
	}
}
