package cn.wdy07.server;

import cn.wdy07.server.config.CPServerConfigurator;
import cn.wdy07.server.config.DefaultCPServerConfigurator;

public class CPServerContext {
	private static CPServerContext context = new CPServerContext();
	
	public static CPServerContext getContext() {
		return context;
	}
	
	private CPServerContext() {
		
	}
	
	private CPServerConfigurator configurator;

	void setConfigurator(CPServerConfigurator configurator) {
		this.configurator = configurator;
	}
	
	public CPServerConfigurator getConfigurator() {
		if (configurator == null) {
			synchronized (this) {
				if (configurator == null) {
					configurator = new DefaultCPServerConfigurator();
				}
			}
		}
		
		return configurator;
	}
}
