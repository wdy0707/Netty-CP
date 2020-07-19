package cn.wdy07.server.test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import cn.wdy07.model.header.ClientType;

public class EnumTest {

	public static void main(String[] args) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Class<? extends Enum<?>> clazz = ClientType.class;
		Method method = clazz.getDeclaredMethod("valueOf", String.class);
		System.out.println(method.invoke(null, "AndroidPhone"));
	}

}
