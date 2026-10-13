package com.sl.bo;

import java.util.Stack;

/**
 * 用两个栈实现一个队列，单线程版
 * @author Administrator
 *
 */
public class StackAndQueue {

	private Stack<Integer> inStack = new Stack<>();
	private Stack<Integer> outStack = new Stack<>();
	
	private volatile boolean flag = true;
	public Integer getOne() {
		if (outStack.isEmpty()) {
			if (inStack.isEmpty()) {
				return -1;
			}
			//做转移操作
			transferInToOut();
		}
		return outStack.pop();
	}

	private void transferInToOut() {
		System.out.println("转换===");
		int size = inStack.size();
		for (int i = 0; i < size; i++) {
			outStack.addElement(inStack.pop());
		}
	}
	
	public boolean pushOne(Integer one) {
		inStack.addElement(one);
		return true;
	}
}
