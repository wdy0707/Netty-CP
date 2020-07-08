package cn.wdy07.model.header;

import cn.wdy07.model.BaseType;

/**
 * @author wdy
 * @create 2020-06-20 23:48
 */
public enum ConversationType implements BaseType {

	ONE_WAY, LOGIN, LOGOUT, PRIVATE, GROUP, SYSTEM, HEARTBEAT,

	CHATROOM, PUBLIC_SERVICE;
}
