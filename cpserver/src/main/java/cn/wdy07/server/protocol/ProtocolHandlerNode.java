package cn.wdy07.server.protocol;

public class ProtocolHandlerNode {
	private String protocolName;
	private ProtocolHanlder handler;

	public ProtocolHandlerNode(String name, ProtocolHanlder handler) {
		this.protocolName = name;
		this.handler = handler;
	}

	public String getProtocolName() {
		return protocolName;
	}

	public void setProtocolName(String name) {
		this.protocolName = name;
	}

	public ProtocolHanlder getHandler() {
		return handler;
	}

	public void setHandler(ProtocolHanlder handler) {
		this.handler = handler;
	}
	
	
}
	
