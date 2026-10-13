package com.test;

import javax.crypto.SecretKey;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.digest.SM3;

public class TestSm {

	public static void main(String[] args) {
		String sm3 = SmUtil.sm3("admin");
		System.out.println(sm3.toUpperCase());
	}

}
