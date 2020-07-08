package cn.wdy07.server.handler.group;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 不与应用服务器通信，直接定义几个群组成员
 * 
 * @author taylor
 *
 */
public class SimpleLocalGroupManager implements GroupManager {
	ConcurrentHashMap<String, Set<Member>> members = new ConcurrentHashMap<String, Set<Member>>();
	{
		members.put("group1", new HashSet<Member>(Arrays.asList(new Member("user1"), new Member("user2"), new Member("user3"))));
		members.put("group2", new HashSet<Member>(Arrays.asList(new Member("user1"), new Member("user2"), new Member("user4"))));
		members.put("group3", new HashSet<Member>(Arrays.asList(new Member("user1"), new Member("user3"), new Member("user5"))));
	}
	
	private static SimpleLocalGroupManager manager = new SimpleLocalGroupManager();
	
	private SimpleLocalGroupManager() {
	
	}
	
	public static SimpleLocalGroupManager getInstance() {
		return manager;
	}
	
	
	@Override
	public Set<Member> getAllMember(String groupId) {
		return members.get(groupId);
	}
	
}
