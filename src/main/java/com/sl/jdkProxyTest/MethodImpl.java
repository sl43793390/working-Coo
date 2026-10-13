package com.sl.jdkProxyTest;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 接口中每个方法的动态实现逻辑
 * 
 * @author Administrator
 *
 */
public class MethodImpl<T> {
	private final Class<T> mapperInterface;
	private final Method method;

	public MethodImpl(Class<T> mapperInterface, Method method) {
		this.mapperInterface = mapperInterface;
		this.method = method;
	}

	public Object execute(Object[] args) {
		Object result = null;
		System.out.println(method.getReturnType());
		System.out.println(method.getName());

		System.out.println("执行sql,参数："+args);
		if (method.getReturnType().getSimpleName().equals("String")) {
			return  "String 类型结果";
		}
			Class<?> class1 = method.getReturnType();
//			然后遍历类的所有属性，把得到的数据设置进去并返回即可实现返回类型的匹配
			try {
				result = class1.getDeclaredConstructor().newInstance();
				Field declaredField = class1.getDeclaredField("userName");
				declaredField.setAccessible(true);
				declaredField.set(result, "用户1");
			} catch (ReflectiveOperationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return result;

	}
}
