package cn.wdy07.server.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import cn.wdy07.model.Message;
import cn.wdy07.model.MessageContent;
import cn.wdy07.model.content.LoginRequestMessageContent;
import cn.wdy07.model.content.TextMessageContent;
import cn.wdy07.model.header.ClientType;
import cn.wdy07.model.header.ConversationType;
import cn.wdy07.server.JSONUtil;
import cn.wdy07.server.protocol.message.MessageBuilder;

public class JacksonTest {
	
	public static void main(String[] args) throws JsonProcessingException {
//		TextMessageContent content = new TextMessageContent("abc");
//		ObjectMapper mapper = new ObjectMapper();
//		String json = JSONUtil.toJSONString(content);
//		
//		System.out.println(json);
//		MessageContent c = JSONUtil.parse(json, MessageContent.class);
//		System.out.println(c);
		
		func1();
	}
	
	private static void func1() {
		LoginRequestMessageContent loginMessageContent = new LoginRequestMessageContent();
		loginMessageContent.setClientType(ClientType.AndroidPad);
		Message message = MessageBuilder.create()
				.convesationType(ConversationType.LOGIN)
				.userId("12345")
				.content(loginMessageContent)
				.build();
		message.setContent(loginMessageContent);
		
		String jsonString = JSONUtil.toJSONString(message);
		System.out.println(jsonString);
		
		Message m = JSONUtil.parse(jsonString, Message.class);
		System.out.println(m);
	}
}
