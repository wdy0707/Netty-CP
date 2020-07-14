package cn.wdy07.server.protocol;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import cn.wdy07.model.Protocol;
import cn.wdy07.server.exception.UnsupportedProtocolException;

/**
 * 记录服务器支持的协议，只有注册了的协议才会支持
 * @author taylor
 *
 */
public class SupportedProtocol {
	private static final Protocol defaultProtocol = Protocol.privatee;
	private HashMap<Protocol, ProtocolCodec> codecs = new HashMap<Protocol, ProtocolCodec>();
	private Set<ProtocolCodec> copy;
	private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	private boolean registeredNewProtocol = false;
	
	public boolean support(String protocol) {
		Protocol p = Protocol.valueOf(Protocol.class, protocol);
		
		try {
			lock.readLock().lock();
			return codecs.containsKey(p);
		} finally {
			lock.readLock().unlock();
		}
	}
	
	public boolean support(Protocol protocol) {
		try {
			lock.readLock().lock();
			return codecs.containsKey(protocol);
		} finally {
			lock.readLock().unlock();
		}
		
	}
	
	public Protocol getOneSupportedProtocol(List<Protocol> protocols) {
		Protocol protocol = null;
		try {
			lock.readLock().lock();
			if (codecs.size() == 0)
				throw new IllegalArgumentException("no support protocol");
			
			for (Protocol p : protocols) {
				if (support(p))
					if (protocol == null || p.ordinal() < protocol.ordinal())
						protocol = p;
				
			if (protocol == null && support(defaultProtocol))
				protocol = defaultProtocol;
			
			if (protocol == null)
				protocol = codecs.keySet().toArray(Protocol.values())[0];
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
			
			codecs.put(protocol, codec);
			registeredNewProtocol = true;
		} finally {
			lock.writeLock().unlock();
		}
	}
	
	public void register(ProtocolHandlerNode node) {
		try {
			lock.writeLock().lock();
			if (support(node.getProtocol()))
				throw new IllegalArgumentException("protocol " + node.getProtocol().name() + "is registered");
			
			codecs.put(node.getProtocol(), node.getCodec());
			registeredNewProtocol = true;
		} finally {
			lock.writeLock().unlock();
		}
	}
	
	public ProtocolCodec getCodec(Protocol protocol) {
		try {
			lock.readLock().lock();
			if (!support(protocol))
				throw new UnsupportedProtocolException("protocol " + protocol.name() + " is not support");
			return codecs.get(protocol);
		} finally {
			lock.readLock().unlock();
		}
	}
	
	public ProtocolCodec getCodec(String protocol) {
		Protocol p = Protocol.valueOf(Protocol.class, protocol);
		try {
			lock.readLock().lock();
			if (!support(protocol))
				throw new UnsupportedProtocolException("protocol " + protocol + " is not support");
			return codecs.get(p);
		} finally {
			lock.readLock().unlock();
		}
	}
	
	public Set<ProtocolCodec> getAllCodec() {
		if (copy == null || registeredNewProtocol) {
			try {
				lock.readLock().lock();
				copy = new HashSet<ProtocolCodec>(codecs.values());
			} finally {
				lock.readLock().unlock();
			}

		}
		return copy;
	}
}
