package com.sl.jdkProxyTest;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import cn.hutool.core.util.ClassUtil;

/**
 * 使用方法：
 * 1、扫描指定包下得所有接口
 * 2、循环每个接口的方法放入到一个map中：Map<Method, MethodImpl> map = new HashMap<Method, MethodImpl>();
 * 3、通过接口创建工厂、处理器：face = new ProxyFactoryTest<>(ProxyTestInterFace.class);
 * 						proxyImplHandler = new ProxyImplHandler<>(ProxyTestInterFace.class, map);
 * 4、使用工厂创建具体的代理对象：face.newInstance(proxyImplHandler);
 * 5、最后使用返回的代理对象调用各个接口的方法；
 * 6、重点是要写methodImpl类的逻辑，其它基本变化不大。
 * @author Administrator
 * @param <T>
 *
 */
public class Test<T> {

	public static Map<Class<?>, Object> maps = new HashMap<>(); 
	public static void main(String[] args) {
		//加载指定包下的所有接口
		Set<Class<?>> scanPackage = ClassUtil.scanPackage("com.sl.jdkProxyTest");
		for (Class<?> class1 : scanPackage) {
			if (class1.isInterface()) {
				generateInstanceByInterface(class1);//生成代理对象
				
			}
		}
//		调用代理对象的方法；
		ProxyTestInterFace instance = (ProxyTestInterFace) getInstance(ProxyTestInterFace.class);
		instance.test("testa");
		instance.getUser("user");
		ProxyTestInterFace2 instance2 = (ProxyTestInterFace2) getInstance(ProxyTestInterFace2.class);
		instance2.testinter2("testa2");
		instance2.getUserInter2("user2");
		
	}
	private static Object getInstance(Class<?> classz) {
		return maps.get(classz);
	}

	private static <T> Object generateInstanceByInterface(Class<T> interface1) {
		Map<Method, MethodImpl> map = new HashMap<Method, MethodImpl>();
		Method[] methods = interface1.getMethods();
		for (int i = 0; i < methods.length; i++) {
			MethodImpl impl = new MethodImpl<>(interface1, methods[i]);
			map.put(methods[i], impl);
		}
//		创建生成代理类的工厂
		ProxyFactoryTest face = new ProxyFactoryTest<>(interface1);
		//每个接口对应一个接口处理器及对应的方法和处理逻辑的实现，其中method中存放的就是方法的实际执行实现，最终由代理对象调用。
		ProxyImplHandler proxyImplHandler = new ProxyImplHandler<>(interface1, map);
//		创建代理对象
		Object newInstance = face.newInstance(proxyImplHandler);
		maps.put(interface1, newInstance);
		return newInstance;
//		调用接口的方法，此时执行的就是method中的实现逻辑。
//		System.out.println(newInstance);
//		System.out.println("调用结果："+newInstance.test("sssss"));
//		System.out.println(newInstance.getUser("sssss"));
//		System.out.println(newInstance.test("sldk"));
		
	}

	private static void singleClassTest() {
		Map<Method, MethodImpl> map = new HashMap<Method, MethodImpl>();
		Method[] methods = ProxyTestInterFace.class.getMethods();
		for (int i = 0; i < methods.length; i++) {
			MethodImpl impl = new MethodImpl<>(ProxyTestInterFace.class, methods[i]);
			map.put(methods[i], impl);
		}
//		创建生成代理类的工厂
		ProxyFactoryTest<ProxyTestInterFace> face = new ProxyFactoryTest<>(ProxyTestInterFace.class);
		//每个接口对应一个接口处理器及对应的方法和处理逻辑的实现，其中method中存放的就是方法的实际执行实现，最终由代理对象调用。
		ProxyImplHandler<ProxyTestInterFace> proxyImplHandler = new ProxyImplHandler<>(ProxyTestInterFace.class, map);
//		创建代理对象
		ProxyTestInterFace newInstance = face.newInstance(proxyImplHandler);
//		调用接口的方法，此时执行的就是method中的实现逻辑。
//		System.out.println(newInstance);
//		System.out.println("调用结果："+newInstance.test("sssss"));
//		System.out.println(newInstance.getUser("sssss"));
//		System.out.println(newInstance.test("sldk"));
	}
}
