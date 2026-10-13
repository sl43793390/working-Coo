package com.sl.bo;

public class MatrixPair {

	private Integer row;
	private Integer col;
	private char alphbet;
	
	
	public MatrixPair(Integer row, Integer col) {
		super();
		this.row = row;
		this.col = col;
	}
	public MatrixPair(Integer row, Integer col, char alphbet) {
		super();
		this.row = row;
		this.col = col;
		this.alphbet = alphbet;
	}
	public MatrixPair(int row, int col, char alphbet) {
		super();
		this.row = row;
		this.col = col;
		this.alphbet = alphbet;
	}
	public MatrixPair() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @return the row
	 */
	public Integer getRow() {
		return row;
	}
	/**
	 * @param row the row to set
	 */
	public void setRow(Integer row) {
		this.row = row;
	}
	/**
	 * @return the col
	 */
	public Integer getCol() {
		return col;
	}
	/**
	 * @param col the col to set
	 */
	public void setCol(Integer col) {
		this.col = col;
	}
	/**
	 * @return the alphbet
	 */
	public char getAlphbet() {
		return alphbet;
	}
	/**
	 * @param alphbet the alphbet to set
	 */
	public void setAlphbet(char alphbet) {
		this.alphbet = alphbet;
	}
	
	
}
