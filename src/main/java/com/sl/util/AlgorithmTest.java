package com.sl.util;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sl.bo.MatrixPair;
import com.sl.bo.Mypair;

import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.RandomUtil;

public class AlgorithmTest {

	
	private static final Logger log = LoggerFactory.getLogger(AlgorithmTest.class);

	public static void main(String[] args) {
	
//		int[] arr = new int[] {1,3,5,7,9,13,15,20,34};
//		int binarySearch = binarySearch0(arr, 144);
//		System.out.println(binarySearch);
//		System.out.println(isCircleNumber(11211));
//		Integer[] reverseArray = reverseArray(new Integer [] {1,2});
//		for (int i = 0; i < reverseArray.length; i++) {
//			System.out.println(reverseArray[i]);
//		}
//		printSnakeColumn("qwerrtyuiosdfa", 3);
//		q t y d f 
//		w r u s a
//		e r i o 
		
//		for (int i = 0; i <100; i++) {
//			int random7 = getRandom7(0, 7);
//			System.out.println(random7);
//		}
//		String str = "mr  sl ijj   ";
//		String replaced = getReplaced(str);
//		System.out.println(replaced);
//		Integer[] a = new Integer[] {1,3,5,7,9};
//		Integer[] b = new Integer[] {2,4,6,8,10};
//		
//		Integer[] combineSortedArry = combineSortedArry(a, b);
//		System.out.println(combineSortedArry);
		
//		String longestShareSubStr = getLongestShareSubStr("abcdefghighklksa", "abcdeflkjigslksa");
//		System.out.println(longestShareSubStr);
		
//		Integer [][] arr = new Integer[][] {{1,3,5,7,9,11},
//											{12,14,15,17,21,23},
//											{23,24,25,27,29,32}};
//		MatrixPair checkIfExsits = checkIfExsits(arr, 29);
//		System.out.println(checkIfExsits);
		
//		TreeSolution so = new TreeSolution();
//		TreeNode<Integer> buildTree = so.buildTree(new int[] {1,2,3,4,5,6,7,8}, new int[] {3,2,4,1,6,5,7,8});
//		System.out.println(buildTree);
		
//		StackAndQueue st = new StackAndQueue();
//		for (int i = 0; i < 20; i++) {
//			st.pushOne(i);
//			if (i == 10) {
//				st.getOne();
//			}
//		}
//		Integer calculateWaJump = calculateWaJump(7);
//		System.out.println(calculateWaJump);
		
//		boolean checkExists = checkExists(new char[][] {{'a','b','c'},{'d','e','f'},{'g','h','j'}}, "adg");
//		System.out.println(checkExists);
		
//		Integer number = getNumber(new char [1][3], 0);
//		System.out.println(number);
//		Integer splitNumberForMultiMax = splitNumberForMultiMax(12);
//		System.out.println(splitNumberForMultiMax);
//		int hammingWeight = hammingWeight(100);
//		System.out.println(hammingWeight);
		
		
	}
	
//	多级双向链表中，除了指向下一个节点和前一个节点指针之外，它还有一个子链表指针，可能指向单独的双向链表。这些子列表也可能会有一个或多个自己的子项，
//	依此类推，生成多级数据结构，如下面的示例所示。
//	给定位于列表第一级的头节点，请扁平化列表，即将这样的多级双向链表展平成普通的双向链表，使所有结点出现在单级双链表中。

	

	
	
	
	
	
	
	public static int trap(int[] height) {
	    if (height.length == 0) return 0;
	    int left = 0;
	    int right = height.length-1;
	    int leftMax = 0;
	    int rightMax = 0;
	    int result = 0;
	    while (left <= right) {
	      if (leftMax < rightMax) {
	        result += leftMax - height[left] > 0 ?
	            leftMax - height[left] : 0;
	        leftMax = Math.max(leftMax, height[left]);
	        left++;
	      } else {
	        result += rightMax - height[right] > 0 ?
	            rightMax - height[right] : 0;
	        rightMax = Math.max(rightMax, height[right]);
	        right--;
	      }
	    }
	    return result;
	  }
//	编写一个函数，输入是一个无符号整数（以二进制串的形式），返回其二进制表达式中数字位数为 '1' 的个数（也被称为 汉明重量).）。
//	•请注意，在某些语言（如 Java）中，没有无符号整数类型。在这种情况下，输入和输出都将被指定为有符号整数类型，并且不应影响您的实现，因为无论整数是有符号的还是无符号的，其内部的二进制表示形式都是相同的。
	 public static int hammingWeight(int n) {
        int ans = 0;
        while (n != 0) {
            n &= n - 1;
            ++ans;
        }
        return ans;
    }
	
//	给你一根长度为 n 的绳子，请把绳子剪成整数长度的 m 段（m、n都是整数，n>1并且m>1），每段绳子的长度记为 k[0],k[1]...k[m-1] 。请问 k[0]*k[1]*...*k[m-1] 可能的最大乘积是多少？例如，当绳子的长度是8时，我们把它剪成长度分别为2、3、3的三段，此时得到的最大乘积是18。
//	示例 1：
//	输入: 2输出: 1解释: 2 = 1 + 1, 1 × 1 = 1
//	示例 2:
//	输入: 10输出: 36解释: 10 = 3 + 3 + 4, 3 × 3 × 4 = 36
//	提示：
//	•2 <= n <= 58

	public static Integer splitNumberForMultiMax(Integer num) {
		if (num <3) {
			if (num <= 0) {
				return -1;
			}
			return 1;
		}
		if (num >58) {
			return -1;
		}
		int result = 1;
		if (num%3 == 0) {
			int tmp = num / 3;
			for (int i = 0; i < tmp; i++) {
				result *= 3;
			}
		}else if (num%3 == 1) {
			int tmp = num / 3;
			for (int i = 0; i < tmp-1; i++) {
				result *= 3;
			}
			result *=4;
		}else if (num%3 == 2) {
			int tmp = num / 3;
			for (int i = 0; i < tmp; i++) {
				result *= 3;
			}
			result *=2;
		}
		return result;
	}
	
//	地上有一个m行n列的方格，从坐标 [0,0] 到坐标 [m-1,n-1] 。一个机器人从坐标 [0, 0] 的格子开始移动，它每次可以向左、右、上、下移动一格（不能移动到方格外），也不能进入行坐标和列坐标的数位之和大于k的格子。例如，当k为18时，机器人能够进入方格 [35, 37] ，因为3+5+3+7=18。但它不能进入方格 [35, 38]，因为3+5+3+8=19。请问该机器人能够到达多少个格子？
//			示例 1：
//			输入：m = 2, n = 3, k = 1输出：3
//			示例 2：
//			输入：m = 3, n = 1, k = 0输出：1
//			提示：
//			•1 <= n,m <= 100
//			•0 <= k <= 20

	public static Integer getNumber(char [][] arr,Integer k) {
		int count = 0;
		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < arr[i].length; j++) {
				if (i + j <= k) {
					count ++;
				}else {
					continue;
				}
			}
		}
		return count;
	}
	
//	给定一个 m x n 二维字符网格 board 和一个字符串单词 word 。如果 word 存在于网格中，返回 true ；否则，返回 false 。
//	单词必须按照字母顺序，通过相邻的单元格内的字母构成，其中“相邻”单元格是那些水平相邻或垂直相邻的单元格。同一个单元格内的字母不允许被重复使用。

	public static boolean checkExists(char[][] cs,String word) {
		int col = cs[0].length;
		char[] charArray = word.toCharArray();
		int index = 0;
		for (int i = 0; i < col; i++) {//列匹配模式
			index = 0;
			for (int row = 0; row < cs.length; row++) {
				char str = cs[row][i];
				if (index < word.length() && Character.compare(str, charArray[index]) == 0) {
					index ++;
				}
			}
			if (index == word.length()) {
				return true;
			}
		}
		for (int i = 0; i < cs.length; i++) {
			index = 0;
			for (int j = 0; j < cs[i].length; j++) {
				char str = cs[i][j];
				if (index < word.length() && Character.compare(str, charArray[index]) == 0) {
					index ++;
				}
			}
			if (index == word.length()) {
				return true;
			}
		}
		return false;
		
	}

	
//	用两个栈实现一个队列。队列的声明如下，请实现它的两个函数 appendTail 和 deleteHead ，分别完成在队列尾部插入整数和在队列头部删除整数的功能。(若队列中没有元素，deleteHead 操作返回 -1 )
//	见StackAndQueue

//	输入某二叉树的前序遍历和中序遍历的结果，请构建该二叉树并返回其根节点。
//	假设输入的前序遍历和中序遍历的结果中都不含重复的数字。
//	具体步骤如下：
//	1、先利用前序遍历找根节点：前序遍历的第一个数，就是根节点的值；
//	2、在中序遍历中找到根节点的位置 pos，则 pos 左边是左子树的中序遍历，右边是右子树的中序遍历；
//	3、假设左子树的中序遍历的长度是 k，则在前序遍历中，根节点后面的 k 个数，是左子树的前序遍历，剩下的数是右子树的前序遍历；
//	4、有了左右子树的前序遍历和中序遍历，我们可以先递归创建出根节点，然后再递归创建左右子树，再将这两颗子树接到根节点的左右位置；
//	细节1： 如何在中序遍历中对根节点快速定位？
//	见TreeSolution
	
//	在一个 n * m 的二维数组中，每一行都按照从左到右递增的顺序排序，每一列都按照从上到下递增的顺序排序。请完成一个高效的函数，输入这样的一个二维数组和一个整数，判断数组中是否含有该整数。
	 public static MatrixPair checkIfExsits(Integer [][] matrix,Integer target) {
		 for (int i = 0; i < matrix.length; i++) {
			//使用二分法查找是否在当前数组内
			 int j = binarySearch2(matrix[i], target);
			 if (matrix[i][j] == target) {
				 return new MatrixPair(i,j);
			}
		}
		 return null;
	 }
	 /**
	  * 如果查不到返回左指针的二分查找法
	  * @param a
	  * @param key
	  * @return
	  */
	 private static int binarySearch2(Integer[] a, Integer key) {
	    	int mid = 0,high = a.length - 1;
	    	int low = 0;
	    	if (low == high) {//如果长度为1；
	    		if (a[0] == key) {
					return 0;
				}else {
					return low;
				}
			}
	    	while (low < high) {
	    		mid = (low + high) >>>1;
	    		if (key > a[mid]) {
					low = mid + 1;
				}else if (key < a[mid]) {
					high = mid -1;
				}else {
					return mid;
				}
			}
	    	return low;
	    }
	/**
	 * 查找最长相同子串
	 * @param a
	 * @param b
	 * @return
	 */
	public static String getLongestShareSubStr(String a,String b) {
		char[] ca = a.toCharArray();
		char[] cb = b.toCharArray();
		Map<String, List<MatrixPair>> map = new HashMap<>();
//		char[][] metrix = new char[a.length()][b.length()];//行为a、列为b
		for (int i = 0; i < ca.length; i++) {
			
			for (int j = 0; j < cb.length; j++) {
				if (Character.compare(ca[i], cb[j]) == 0) {
					if (i > 0 && j > 0) {//获取左前方是否也是相同的
						List<MatrixPair> malist = map.get(ca[i-1]+cb[j-1]+"");
						if (null != malist) {
							malist.add(new MatrixPair(i, j, ca[i]));
							map.put(ca[i]+cb[j]+"", malist);
						}else {
							List<MatrixPair> list = new ArrayList<>();
							list.add(new MatrixPair(i, j, ca[i]));
							map.put(ca[i]+cb[j]+"", list);
						}
					}else {//如果是第一行
						List<MatrixPair> list = new ArrayList<>();
						list.add(new MatrixPair(i, j, ca[i]));
						map.put(ca[i]+cb[j]+"", list);
					}
				}
			}
		}
		Set<Entry<String,List<MatrixPair>>> entrySet = map.entrySet();
		List<MatrixPair> valueRes = null;
		int i = 0;
		for (Entry<String, List<MatrixPair>> entry : entrySet) {
			List<MatrixPair> value = entry.getValue();
			if (value.size() > i) {
				i = value.size();
				valueRes = value;
			}
		}
		StringBuffer buf = new StringBuffer();
		for (MatrixPair val : valueRes) {
			buf.append(val.getAlphbet());
		}
		return buf.toString();
	}

	/**
	 * 合并两个有序数组后仍然为有序数组
	 * @param a
	 * @param b
	 * @return
	 */
	public static Integer [] combineSortedArry(Integer [] a, Integer [] b) {
		int length = a.length > b.length ?a.length :b.length;
		Integer [] result = new Integer [a.length +b.length];
		int index = 0;
		for (int i = 0; i < length; i++) {
			if (i < a.length && i < b.length) {
				if (a[i] > b[i]) {
					result[index] = b[i];
					index ++;
					result[index] = a[i];
					index ++;
				}else {
					result[index] = a[i];
					index ++;
					result[index] = b[i];
					index ++;
				}
			}else if(i < a.length){
				result[index] = a[i];
				index ++;
			}else {
				result[index] = b[i];
				index ++;
			}
		}
		return result;
	}
	
	
	/**
	 * 将字符串中的空白处替换为%20
	 * @param ta
	 * @return
	 */
	public static String getReplaced(String ta) {
		char[] charArray = ta.toCharArray();
		StringBuffer bf = new StringBuffer();
		boolean flag = false;
		for (int i = 0; i < charArray.length; i++) {
			boolean blankChar = CharUtil.isBlankChar(charArray[i]);
			if (blankChar) {
				if (!flag) {//the previous one is not blank
					flag = true;
					bf.append("%20");
				}
				
			}else {
				flag = false;
				bf.append(charArray[i]);
			}
		}
		return bf.toString();
	}
	
	
	/**
	 * 生成给定区间的等概率值
	 * @param from
	 * @param to
	 * @return
	 */
	public static int getRandom7(int from, int to) {
		int length = to -from;
		int index = 1;
		int count = 1 << 1;
		while (count < length) {
			count = count << 1;
			index ++;
		}
		int result = 0;
		do {
			for (int i = 0; i < index; i++) {
				result |=  getZeroOrOne(1,5) << i;
			}
		}while (result > length);
		return result + from;
	}
	/**
	 * 根据给定的最大最小值获取等概率返回0/1
	 * @param min
	 * @param max
	 * @return
	 */
	public static byte getZeroOrOne(int min, int max) {
//		int min = 1;
//		int max = 5;
		int length = max -min +1;
		int mid = min + ((max - min )>> 1);
		boolean odd = (length & 1) != 0;//是否为奇数个
		int tmp = 0;
		do {
			tmp = getRandomInt(min, max);
		}while(odd && tmp == mid);
		return (byte) (tmp <= mid ? 0 :1); 
	}
	public static int getRandomInt(int min,int max) {
		int randomInt = RandomUtil.randomInt(min, max+1);
		return randomInt;
	}
	
	/**
	 * 返回蛇形打印形式qwerrrtyuiosdfa,此方法根据列来计数打印
	 * @param str
	 */
	public static void printSnakeColumn(String str,int row) {
		int column = str.length()/row + 1;
		char[] charArray = str.toCharArray();
		int index = 0;//用来指示那一列
		int c = 0;//取字符的下标
		while (index < column) {
			if (index % 2 == 0) {//奇数倒序，偶数正序
				for (int i = 0; i < row; i++) {
					if (c < charArray.length) {
						System.out.println(charArray[c]);
						c++;
					}
				}
			}else {//逆序
				int tmp = c+row-1;
				for (int i = 0; i < row; i++) {
					if (tmp < charArray.length) {
						System.out.println(charArray[tmp]);
					}
					tmp --;
					c++;
				}
			}
			index ++;
			System.out.println("======分割======");
		}
	}
	
	
	/**
	 * 数组翻转
	 * @param <T>
	 * @param arr
	 * @return
	 */
	public static <T>  T [] reverseArray(T [] arr) {
		T tmp = null;
		int j = arr.length - 1;
		for (int i = 0; i < arr.length; i++) {
			if (i > j) {
				break;
			}
			tmp = arr[i];
			arr[i] = arr[j];
			arr[j] = tmp;
			j --;
		}
		return arr;
	}
	
	/**
	 * 	
	 * 举个例子：1221 这个数字。
		- 通过计算 1221 / 1000， 得首位1
		- 通过计算 1221 % 10， 可得末位 1
		- 进行比较
		- 再将 22 取出来继续比较
	 * @param x
	 * @return
	 */
	public static boolean isCircleNumber(int x) {
		if (x < 0) {
			return false;
		}
		int div = 1;
		while(x/div >10) {
			div *= 10;
		}
		while (x >0) {
			int left = x/div;
			int right = x % 10;
			if (left != right) {
				return false;
			}
			x = x % div /10;
			div = div/100;
		}
		return true;
	}
	
	  // Like public version, but without range checks.
    private static int binarySearch(int[] a, int key) {
    	int low = 0,mid = 0,high = a.length - 1;
    	if (low == high) {//如果长度为1；
    		if (a[0] == key) {
				return 0;
			}else {
				return -1;
			}
		}
    	while (low < high) {
    		mid = (low + high) >>>1;
    		if (key > a[mid]) {
				low = mid + 1;
			}else if (key < a[mid]) {
				high = mid -1;
			}else {
				return mid;
			}
		}
    	return -1;
    }
	

	/**
	 * 从给定字符串中获取其中最长的不重复子串；
	 * 获取长度：pair.getLength()
	 * 获取子串：pair.getResult()
	 * @param args
	 * @return
	 */
	public static Mypair getLongestStrInDifference(String args) {
		char[] target = args.toCharArray();
		Mypair pair = new Mypair(-1, "");
		int l = 0;
		int r = 0;
		int[] count = new int[256];
		StringBuffer buf = new StringBuffer();
		while (l < target.length) {
			if (r < target.length && count[target[r]] == 0) {
				count[target[r]] ++;
				buf.append(target[r]);
				r ++;
			}else {
				count[target[l]] --;
				l++;
				if (pair.getLength() < r-l+1) {
					pair.update(r-l+1, buf.toString());
				}
				buf.delete(0, 1);
			}

		}
//		System.out.println(pair.toString());
		return pair;
	}
	
	public static boolean validateIfNotCorrect(String regex, String str){
        if (str == null || str.trim().equals("")) {
                return false;
        }
        Pattern pattern = Pattern.compile(regex);
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
	}
	/**
	* 验证输入的是否是纯数字，是返回true，不是返回false; 负数返回false；
	* @return
	*/
	
	public static  boolean validateNumberisNaN(String str){
	            if (validateIfNotCorrect("^\\d+(\\.\\d+)?(,\\d+(\\.\\d+)?)*(\\.\\d+)?$",str)) {
	                return true;
	            }
	    return false;
	}
	/**
	 * 验证输入的是否是纯数字，是返回true，包含正数和负数；
	 * @return
	 */
	
	public static  boolean isNumber(String str){
		if (validateIfNotCorrect("^-?\\d+(\\.\\d+)?(,\\d+(\\.\\d+)?)*(\\.\\d+)?$",str)) {
			return true;
		}
		return false;
	}
	
	public static Date locaDateToDate(LocalDate localdate) {
		ZoneId systemDefault = ZoneId.systemDefault();
		Instant instant = localdate.atStartOfDay().atZone(systemDefault).toInstant();
		Date from = Date.from(instant);
		return from;
	}
	
	public static LocalDate dateToLocalDate(Date date) {
		Instant instant = date.toInstant();
		ZoneId systemDefault = ZoneId.systemDefault();
		LocalDate ofInstant = LocalDate.ofInstant(instant, systemDefault);
		return ofInstant;
	}
	
	/**
	 * 执行python脚本
	 */
	public static void executePythonScript(String path) {
//       String path = "/data/logs/newblog/getbaidu.py";
        Process proc;
        try {
            // Runtime.exec(String) 已废弃，换 ProcessBuilder
            proc = new ProcessBuilder("python3", path).start();// 执行py文件
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                log.info(line);
            }
            in.close();
            proc.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
	}
	
	 public static List<DateInfo> getMonthDays(String date){
	        SimpleDateFormat yymm = new SimpleDateFormat("yyyy-MM");
	        SimpleDateFormat yymmdd = new SimpleDateFormat("yyyy-MM-dd");
	        Calendar calendar = Calendar.getInstance();
	        List<DateInfo> dateInfoList = new ArrayList<>();
	        try {
	            //年月转成Date 例：2021-05 ，目的获取本月的第一次
	            Date dt =yymm.parse(date);
	            String preFix = date.substring(0, 7);
//	            String ym = yymm.format(date);
	            do{
	                //当天日期
	                date = yymmdd.format(dt);
	                if (!date.startsWith(preFix)){ //如果不是本月 退出
	                    //判断本月1号是否星期天，如果不是星期天，取上个星期天的天数
	                    if (!dateInfoList.isEmpty()){
	                        DateInfo oneDate = dateInfoList.get(0);
	                        if (oneDate.getWeek() != 0){
	                            calendar.setTime(yymmdd.parse(oneDate.getDate()));
	                            for (int i = oneDate.getWeek() - 1; i > 0; i--) {
	                                calendar.add(calendar.DATE,-1);
	                                dt = calendar.getTime();
	                                date = yymmdd.format(dt);
	                                DateInfo dateInfo = new DateInfo();
	                                dateInfo.setDate(date);
	                                dateInfo.setWeek(calendar.get(Calendar.DAY_OF_WEEK)-1);
	                                dateInfo.setDay(calendar.get(Calendar.DATE));
	                                dateInfoList.add(0,dateInfo);
	                            }
	                        }
	                    }
	                    return dateInfoList;
	                }
	                calendar.setTime(dt);

	                //获取星期 和 日
	                DateInfo dateInfo = new DateInfo();
	                dateInfo.setDate(date);
	                dateInfo.setWeek(calendar.get(Calendar.DAY_OF_WEEK)-1);
	                dateInfo.setDay(calendar.get(Calendar.DATE));
	                dateInfoList.add(dateInfo);

	                //设置明天日期
	                calendar.add(calendar.DATE,1);
	                dt = calendar.getTime();
	            } while (true);


	        } catch (ParseException e) {
	            e.printStackTrace();
	        }
	        return null;
	    }
}
