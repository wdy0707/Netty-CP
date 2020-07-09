package cn.wdy07.server.protocol;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import cn.wdy07.model.Protocol;
import cn.wdy07.server.exception.UnsupportedProtocolException;

/**
 * 记录服务器支持的协议，只有注册了的协议才会支持
 * @author taylor
 *
 */
public class SupportedProtocol {
	private static SupportedProtocol supportedProtocol = new SupportedProtocol();
	
	private List<ProtocolHandlerNode> supportedProtocols = new ArrayList<ProtocolHandlerNode>();
	private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	
	private SupportedProtocol() {
		
	}
	
	public static SupportedProtocol getInstance() {
		return supportedProtocol;
	}
	
	public boolean support(String protocol) {
		try {
			lock.readLock().lock();
			for (ProtocolHandlerNode node : supportedProtocols)
				if (node.getProtocol().name().equals(protocol))
					return true;
			return false;
		} finally {
			lock.readLock().unlock();
		}
	}
	
	public boolean support(Protocol protocol) {
		try {
			lock.readLock().lock();
			for (ProtocolHandlerNode node : supportedProtocols)
				if (node.getProtocol() == protocol)
					return true;
			return false;
		} finally {
			lock.readLock().unlock();
		}
		
	}
	
	public Protocol getOneSupportedProtocol(List<Protocol> protocols) {
		Protocol protocol = null;
		try {
			lock.readLock().lock();
			for (Protocol p : protocols) {
				if (support(p)) {
					if (protocol == null || protocol.ordinal() > p.ordinal())
						protocol = p;
				}
			}
		} finally {
			lock.readLock().unlock();
		}
		return protocol;
	}
	
	public void register(Protocol protocol, ProtocolCodec codec) {
		try {
			lock.writeLock().lock();
			if (support(protocol))
				throw new IllegalArgumentException("protocol " + protocol.name() + "is registered");
			
			supportedProtocols.add(new ProtocolHandlerNode(protocol, codec));
		} finally {
			lock.writeLock().unlock();
		}
	}
	
	public void register(ProtocolHandlerNode node) {
		try {
			lock.writeLock().lock();
			if (support(node.getProtocol()))
				throw new IllegalArgumentException("protocol " + node.getProtocol().name() + "is registered");
			
			supportedProtocols.add(node);
		} finally {
			lock.writeLock().unlock();
		}
	}
	
	public ProtocolCodec getCodec(Protocol protocol) {
		try {
			lock.readLock().lock();
			for (ProtocolHandlerNode node : supportedProtocols)
				if (node.getProtocol() == protocol)
					return node.getCodec();
			
			throw new UnsupportedProtocolException("protocol " + protocol.name() + " is not support");
		} finally {
			lock.readLock().unlock();
		}
	}
	
	public ProtocolCodec getCodec(String protocol) {
		try {
			lock.readLock().lock();
			for (ProtocolHandlerNode node : supportedProtocols)
				if (node.getProtocol().name().equals(protocol))
					return node.getCodec();
			
			throw new UnsupportedProtocolException("protocol " + protocol + " is not support");
		} finally {
			lock.readLock().unlock();
		}
	}
	
	/*
	 * 这样写可以运行时动态注册新的协议
	 */
	public List<ProtocolHandlerNode> getAllCodec() {
		try {
			lock.readLock().lock();
			return new ArrayList<ProtocolHandlerNode>(supportedProtocols);
		} finally {
			lock.readLock().unlock();
		}
	}
	
}
