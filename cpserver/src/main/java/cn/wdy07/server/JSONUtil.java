package cn.wdy07.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONUtil {
	
	private static ThreadLocal<ObjectMapper> mapper = ThreadLocal.withInitial(() -> new ObjectMapper() );
	
	public static String toJSONString(Object o) {
		try {
			return mapper.get().writeValueAsString(o);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static <T> T parse(String jsonString, Class<T> clazz) {
		try {
			return mapper.get().readValue(jsonString, clazz);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
