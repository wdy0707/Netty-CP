package cn.wdy07.server.test;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class RabbitmqSendder {
	public static void main(String[] args) throws IOException, TimeoutException {
		func1();
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
}
