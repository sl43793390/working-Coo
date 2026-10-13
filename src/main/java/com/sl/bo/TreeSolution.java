package com.sl.bo;

import java.util.HashMap;
import java.util.Map;

/**
 * 给定先序遍历和中序遍历，生成二叉树
 * Definition for a binary tree node. public class TreeNode { int val; TreeNode
 * left; TreeNode right; TreeNode(int x) { val = x; } }
 */
public class TreeSolution {
	private Map<Integer, Integer> d = new HashMap<>();
	private int[] preorder;
	private int[] midorder;

	public TreeNode<Integer> buildTree(int[] preorder, int[] midorder) {
		int n = midorder.length;
		for (int i = 0; i < n; ++i) {
			d.put(midorder[i], i);
		}
		this.preorder = preorder;
		this.midorder = midorder;
		return dfs(0, 0, n);
	}
//	{1,2,3,4,5,6,7,8}, new int[] {3,2,4,1,6,5,7,8});
	private TreeNode<Integer> dfs(int i, int j, int n) {
		if (n < 1) {
			return null;
		}
		int k = d.get(preorder[i]);
		int l = k - j;
		TreeNode<Integer> root = new TreeNode<Integer>(preorder[i]);
		root.left = dfs(i + 1, j, l);
		root.right = dfs(i + 1 + l, k + 1, n - l - 1);
		return root;
	}
}