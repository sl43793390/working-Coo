package com.sl.bo;

public class Mypair {
	private Integer length;
	private String result;

	public Mypair(Integer length, String result) {
		super();
		this.length = length;
		this.result = result;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public void update(Integer length, String result) {
		this.length = length;
		this.result = result;
	}

	@Override
	public String toString() {
		return "Mypair [length=" + length + ", result=" + result + "]";
	}

}