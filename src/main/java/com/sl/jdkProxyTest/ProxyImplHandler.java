package com.sl.jdkProxyTest;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

import org.apache.ibatis.reflection.ExceptionUtil;

/**
 * 每个接口对应一个handler对象，因此创建代理对象时传入各自的proxyImpl
 * 
 * @author Administrator
 *
 * @param <T>
 */
public class ProxyImplHandler<T> implements InvocationHandler {

	private final Class<T> mapperInterface;
	private final Map<Method, MethodImpl> methodCache;

	public ProxyImplHandler(Class<T> mapperInterface, Map<Method, MethodImpl> methodCache) {
		this.mapperInterface = mapperInterface;
		this.methodCache = methodCache;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		// 首先处理Object类型的toString调用，跳过；
		if (Object.class.equals(method.getDeclaringClass())) {
			try {
				return method.invoke(this, args);
			} catch (Throwable t) {
				throw ExceptionUtil.unwrapThrowable(t);
			}
		}
		// 开始处理接口方法的实现逻辑，该逻辑可参考mybatisMapperProxy将方法作为Key,实际逻辑作为value，可实现根据方法调用具体的逻辑
		// 返回类型根据接口定义的类型确定
		System.out.println("代理方法执行开始============");
		MethodImpl<?> cachedMapperMethod = cachedMapperMethod(method);
		return cachedMapperMethod.execute(args);
	}

	/**
	 * 每个方法对应一个methodImpl,其中的execute方法是根据配置进行加载的。这样就会不同方法有不同执行结果
	 * @param method
	 * @return
	 */
	private MethodImpl<T> cachedMapperMethod(Method method) {
		MethodImpl<T> mapperMethod = methodCache.get(method);
		if (mapperMethod == null) {
			mapperMethod = new MethodImpl<T>(mapperInterface, method);
			methodCache.put(method, mapperMethod);
		}
		return mapperMethod;
	}
}
