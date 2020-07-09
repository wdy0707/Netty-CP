package kryo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import cn.wdy07.model.Message;
import cn.wdy07.model.MessageHeader;
import cn.wdy07.model.header.ClientType;
import cn.wdy07.model.header.ContentSubType;
import cn.wdy07.model.header.ConversationType;
import cn.wdy07.model.header.MessageType;
import cn.wdy07.model.header.StatusSubType;

public class KryoTest {

	public static void main(String[] args) {
		Kryo kryo = new Kryo();
		
		Message m = buildMessage();
		
		
		
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        Output output = new Output(byteArray);

        kryo.writeObjectOrNull(output, m ,m.getClass());
        output.flush();//待优化(AOP)
        output.close();

        byte[] objBytes = byteArray.toByteArray();
        System.out.println(Arrays.toString(objBytes));
        
        
        Input input = new Input(objBytes);
        Message tObj =  kryo.readObjectOrNull(input, Message.class);
        input.close();
        try {
            byteArray.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(tObj);
	}

	private static Message buildMessage() {
		Message message = new Message();
		MessageHeader header = new MessageHeader();
		header.setConversationType(ConversationType.GROUP);
		header.setMessageType(MessageType.CONTENT);
//		header.setMessageType2(ContentSubType.FILE);
		header.setMessageType2(StatusSubType.READ_RECEIPT_RESPONSE);
		header.setTargetId("targetId");
		header.setUserId("userId");
		message.setHeader(header);
		
		message.setContent(null);
		
		return message;
	}
}
