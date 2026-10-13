package com.sl.jdkProxyTest;

import java.lang.reflect.Proxy;
/**
 * 每个接口都有一个对应的invocationHandler的实现
 * @author Administrator
 *
 * @param <T>
 */
public class ProxyFactoryTest<T> {
	  private final Class<T> mapperInterface;

	public ProxyFactoryTest(Class<T> mapperInterface) {
		this.mapperInterface = mapperInterface;
	}

	 public T newInstance(ProxyImplHandler proxyHandler) {
		    return (T) Proxy.newProxyInstance(ProxyImplHandler.class.getClassLoader(),new Class[] { mapperInterface }, proxyHandler);
	 }
	  
}
