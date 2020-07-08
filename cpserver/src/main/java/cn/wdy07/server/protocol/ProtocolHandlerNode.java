package cn.wdy07.server.protocol;

import cn.wdy07.model.Protocol;

public class ProtocolHandlerNode {
	private Protocol protocol;
	private ProtocolCodec codec;

	public ProtocolHandlerNode(Protocol protocol, ProtocolCodec codec) {
		this.protocol = protocol;
		this.setCodec(codec);
	}

	public Protocol getProtocol() {
		return protocol;
	}

	public void setProtocol(Protocol protocol) {
		this.protocol = protocol;
	}

	public ProtocolCodec getCodec() {
		return codec;
	}

	public void setCodec(ProtocolCodec codec) {
		this.codec = codec;
	}
}
	
