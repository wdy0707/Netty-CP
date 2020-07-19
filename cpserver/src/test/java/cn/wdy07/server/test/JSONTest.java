package cn.wdy07.server.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.alibaba.fastjson.JSON;

import cn.wdy07.model.header.ClientType;
import cn.wdy07.server.user.Client;
import cn.wdy07.server.user.ClientBuilder;
import cn.wdy07.server.user.User;
import redis.clients.jedis.Jedis;

public class JSONTest {
	public static void main(String[] args) {
//		Jedis jedis = new Jedis("192.168.99.100", 6379);
//		jedis.set("user1", "abcde");
//		System.out.println(jedis.lrange("user", 0, -1));
		
//		System.out.println(JSON.toJSONString(new String[] {"a", "b"}));
		
//		new JSONTest().test();
		
		Jedis jedis = new Jedis("192.168.99.100", 6379);
		User user = new User();
		user.setUserId("user1");
		List<Client> clients = new ArrayList<Client>();
		clients.add(ClientBuilder.create().channel(null).userId("user1").clientType(ClientType.AndroidPad).build());
		user.setOnlineClient(clients);
		jedis.rpush("user1", JSON.toJSONString(user));
		jedis.rpush("user1", JSON.toJSONString(user));
		
	}
	
	@Test
	public void test() {
		User user = new User();
		user.setUserId("user1");
		List<Client> clients = new ArrayList<Client>();
		clients.add(ClientBuilder.create().channel(null).userId("user1").clientType(ClientType.AndroidPad).build());
		user.setOnlineClient(clients);
		System.out.println(user);
		String out = JSON.toJSONString(new User[] {user});
		System.out.println(out);
		System.out.println(JSON.parseArray(out, User.class));
	}
}
