package cn.wdy07.server.test;


import java.util.ArrayList;

import cn.wdy07.server.user.Client;
import cn.wdy07.server.user.User;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

public class EhcacheTest {
	public static void main(String[] args) {

		CacheManager cacheManager = CacheManager.create("./src/main/resources/ehcache.xml");
		Cache cache = cacheManager.getCache("usercache");
		User user1 = new User();
		user1.setUserId("user1");
		cache.put(new Element("user1", user1));
		
		System.out.println(cache.get("user1").getObjectValue());
		user1.setOnlineClient(new ArrayList<Client>());
		System.out.println(cache.get("user1").getObjectValue());
	}
}
