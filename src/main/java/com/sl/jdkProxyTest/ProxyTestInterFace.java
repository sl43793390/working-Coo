package com.sl.jdkProxyTest;

import com.sl.entity.User;

//第一步：定义接口及对应的方法
public interface ProxyTestInterFace {

	String test(String input);

	public User getUser(String idUser);
}
