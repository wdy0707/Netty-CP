一个可以用的版本，客户端使用控制台指令发送消息、接收显示消息

服务端支持：
	私聊
	群聊
	心跳
	离线消息
	支持同一用户登录多个客户端

服务端：运行CPServer.java
客户端：运行ClientTest.java

群组关系
group1: user1, user2, user3
group2: user1, user2, user4
group3: user1, user3, user5

命令
	login
	// logout 功能不正确
	private
	group
	heartbeat
	
login|userId|clientType
	clientType为数字：
		0	Unknown
		1	AndroidPhone
		2	AndroidPad
		3	ApplePhone
		4	ApplePad
		5	Windows
		6	Mac
		7	Web
	login必须最先使用

private|sender|receiver|content

group|sender|groupId|content
	groupId即group1、group2、group3

heartbeat|userId

请正确操作！