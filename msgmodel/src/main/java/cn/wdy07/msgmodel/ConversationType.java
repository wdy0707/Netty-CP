package cn.wdy07.msgmodel;

/**
 * @author wdy
 * @create 2020-06-20 23:48
 */
public enum ConversationType {
	
    ONE_WAY(1), /*无需应答的消息*/
    
    LOGIN(2), /*登录*/
    
    LOGOUT(3), 
    
    HEARTBEAT(4), /*心跳*/
	
    // 单聊
	PRIVATE(5),
	
	// 群聊
	GROUP(6),
	
	// 聊天室
	CHATROOM(7),
	
	// 客服
	CUSTOMER_SERVICE(8),
	
	// 系统消息
	SYSTEM(9),
	
	// 公众服务
	PUBLIC_SERVICE(10);
	

    private byte value;

    ConversationType(int value) {
        this.value = (byte) value;
    }

    public byte getValue() {
        return value;
    }
}
