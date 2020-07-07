package cn.wdy07.server.handler.qualifier;

import cn.wdy07.model.Message;
import cn.wdy07.server.handler.MessageHandlerQualifier;

/**
 * ConversationTypeQualifier、MessageType1Qualifier、MessageType2Qualifier的父类，使用模板模式
 * 
 * @author taylor
 */
public abstract class SingleTypeQualifier implements MessageHandlerQualifier {
	private int[] types = null;
	private int type;

	public SingleTypeQualifier(int qualifiedType) {
		this.type = qualifiedType;
	}

	public SingleTypeQualifier(int[] qualifiedType) {
		this.types = qualifiedType;
	}

	@Override
	public boolean qualify(Message message) {
		int type = getTypeInMessage(message);
		if (types == null)
			return type == this.type;

		for (int t : types)
			if (t == type)
				return true;

		return false;
	}

	/**
	 * 获取用来比较的类型，子类提供
	 * 
	 * @param message
	 * @return
	 */
	protected abstract int getTypeInMessage(Message message);
}
