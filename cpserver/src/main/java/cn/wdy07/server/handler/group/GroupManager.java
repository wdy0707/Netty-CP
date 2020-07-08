package cn.wdy07.server.handler.group;

import java.util.Set;

/**
 * 查询群组用户，群组信息保存在应用服务器，需要和应用服务器进行同步，该类也必须是单例的
 * 
 * @author taylor
 *
 */
public interface GroupManager {
	Set<Member> getAllMember(String groupId);
}
