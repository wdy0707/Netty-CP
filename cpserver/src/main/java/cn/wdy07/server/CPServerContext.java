package cn.wdy07.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.wdy07.server.config.CPServerConfigurator;
import cn.wdy07.server.config.DefaultCPServerConfigurator;

public class CPServerContext {
	private static CPServerContext context = new CPServerContext();
	private static final Logger logger = LoggerFactory.getLogger(CPServerContext.class);
	
	public static CPServerContext getContext() {
		return context;
	}
	
	private CPServerContext() {
		
	}
	
	private CPServerConfigurator configurator;

	void setConfigurator(CPServerConfigurator configurator) {
		this.configurator = configurator;
		logger.info("use {}", configurator.getClass().getSimpleName());
	}
	
	public CPServerConfigurator getConfigurator() {
		if (configurator == null) {
			synchronized (this) {
				if (configurator == null) {
					configurator = new DefaultCPServerConfigurator();
					logger.info("use {}", DefaultCPServerConfigurator.class.getSimpleName());
				}
			}
		}
		
		return configurator;
	}
}
