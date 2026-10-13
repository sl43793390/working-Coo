package com.sl.util;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import com.sl.app.component.util.ComponentUtil;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;

public class Util {

	
	private static final Logger log = LoggerFactory.getLogger(Util.class);

	private static final String USERS_CONFIG = "users.properties";

	/**
	 * 判断文件的编码格式
	 * 
	 * @param fileName
	 *            :file
	 * @return 文件编码格式
	 * @throws Exception
	 */
	public static String getFileEncode(File fileName) throws Exception {
		BufferedInputStream bin = new BufferedInputStream(new FileInputStream(fileName));
		int p = (bin.read() << 8) + bin.read();
		String code = null;

		switch (p) {
		case 0xefbb:
			code = "UTF-8";
			break;
		case 0xfffe:
			code = "Unicode";
			break;
		case 0xfeff:
			code = "UTF-16BE";
			break;
		default:
			code = "GBK";
		}
		IoUtil.close(bin);
		return code;
	}

	/**
	 * 查询配置文件中的用户
	 */
	public static Map<String, String> getUsers() {
		Map<String, String> users = new HashMap<String, String>();
		List<String> readLines = getUsersConfigFile();
		for (String e : readLines) {
			if (e.trim().equals("")) {
				continue;
			} else if (e.contains("=")) {
				users.put(e.split("=")[0], e.split("=")[1]);
			}
		}

		return users;
	}

	public static List<String> getUsersAsLine() {
		return getUsersConfigFile();
	}

	public static List<String> getConfigFileAsLineByClasspathResource(String fileName) {
		ClassPathResource res = new ClassPathResource(fileName);
		try (InputStream in = res.getInputStream()) {
			// readLines(InputStream, String charsetName, T) 已废弃，换 Charset 重载
			ArrayList<String> readLines2 = IoUtil.readLines(in, java.nio.charset.StandardCharsets.UTF_8, new ArrayList<String>());
			return readLines2;
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		return null;
	}

	public static List<String> getUserGuide() {
		ClassPathResource res = new ClassPathResource("userGuide.txt");
		try (InputStream in = res.getInputStream()) {
			ArrayList<String> readLines2 = IoUtil.readLines(in, java.nio.charset.StandardCharsets.UTF_8, new ArrayList<String>());
			return readLines2;
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		return null;
	}

	/**
	 * 将用户保存到配置文件中，覆盖模式
	 * 
	 * @param lines
	 */
	public static void saveUsers(List<String> lines) {
		// 移除空白行
		ListIterator<String> listIterator = lines.listIterator();
		while (listIterator.hasNext()) {
			String str = listIterator.next();
			if (str.trim().equals("")) {
				listIterator.remove();
			}
		}
		if (null == lines || lines.isEmpty()) {
			return;
		}
		String path = System.getProperty("user.dir") + File.separator + USERS_CONFIG;
		// 写入
		File file = new File(path);
		FileUtil.writeLines(lines, file, "UTF-8");

	}

	public static List<String> getUsersConfigFile() {
		List<String> configs = new ArrayList<String>();
		List<String> defaultUser = getDefaultUser();
		configs.addAll(defaultUser);
		String path = System.getProperty("user.dir") + File.separator + USERS_CONFIG;
		File file = new File(path);
		if (!FileUtil.exist(file)) {
			log.error("请在jar包同级路径下放置users.properties文件，以便登录使用，格式：用户名=密码,默认用户admin");
			log.error(path);
			return configs;
		} else {
			configs.addAll(defaultUser);
			List<String> readLines = FileUtil.readLines(file, Charset.forName("UTF-8"));
			configs.addAll(readLines);
			return readLines;
		}
	}

	/**
	 * 写入用户配置文件，普通路径为##后缀 ssh配置为##ssh后缀
	 * 
	 * @param lines
	 * @throws IOException
	 */
	public static void saveUserConfigToFile(String line) {
		if (null == line) {
			return;
		}
		String path = System.getProperty("user.dir") + File.separator + ComponentUtil.getCurrentUserName() + ".properties";
		try {
			if (!FileUtil.exist(new File(path))) {
				// 不存在，创建
				File file = new File(path);
				file.createNewFile();
			}
		} catch (IOException e) {
			log.error("创建文件失败{}", e.getMessage());
			e.printStackTrace();
		}
		// 写入
		FileUtil.appendString(System.lineSeparator() + line, path, Charset.forName("UTF-8"));
	}

	/**
	 * 查询用户配置文件，普通路径为##后缀 ssh配置为##ssh后缀
	 * 
	 * @param lines
	 * @throws IOException
	 */
	public static List<String> getUserConfig() {
		List<String> configs = new ArrayList<String>();
		String path = System.getProperty("user.dir") + File.separator + ComponentUtil.getCurrentUserName() + ".properties";
		File file = new File(path);
		if (!FileUtil.exist(file)) {
			return configs;
		} else {
			List<String> readLines = FileUtil.readLines(file, Charset.forName("UTF-8"));
			return readLines;
		}
	}

	/**
	 * 检查用户配置文件是否存在某个key 存在返回true
	 * 
	 * @param lines
	 * @throws IOException
	 */
	public static boolean checkUserConfig(String key) {
		String path = System.getProperty("user.dir") + File.separator + ComponentUtil.getCurrentUserName() + ".properties";
		File file = new File(path);
		if (!FileUtil.exist(file)) {
		} else {
			List<String> readLines = FileUtil.readLines(file, Charset.forName("UTF-8"));
			for (String l : readLines) {
				if (!l.trim().equals("") && l.contains("=")) {
					if (l.split("=")[0].equals(key)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 读取服务器列表配置
	 * 
	 * @return
	 */
	public static List<String> getRemoteServerList() {
		List<String> configFileAsLine = getConfigFileAsLineByClasspathResource("remoteServerList.conf");

		ListIterator<String> listIterator = configFileAsLine.listIterator();
		while (listIterator.hasNext()) {
			String type = listIterator.next();
			if (type.startsWith("#") || type.trim().equals("") || !type.contains("=") || type.endsWith("=path")) {
				listIterator.remove();
			}
		}
		return configFileAsLine;
	}

	/**
	 * 读取classpath下的默认用户
	 * 
	 * @return
	 */
	public static List<String> getDefaultUser() {
		List<String> configFileAsLine = getConfigFileAsLineByClasspathResource("users.properties");

		ListIterator<String> listIterator = configFileAsLine.listIterator();
		while (listIterator.hasNext()) {
			String type = listIterator.next();
			if (type.startsWith("#") || type.trim().equals("") || !type.contains("=") || type.endsWith("=path")) {
				listIterator.remove();
			}
		}
		return configFileAsLine;
	}

	/**
	 * 查询配置服务器列表
	 */
	public static List<String> getServerList() {
		List<String> serverList = new ArrayList<String>();
		List<String> configFileAsLine = getConfigFileAsLineByClasspathResource("remoteServerList.conf");
		for (String e : configFileAsLine) {
			if (e.startsWith("#") || e.trim().equals("")) {
				continue;
			} else if (e.contains("=")) {
				serverList.add(e.trim());
			}
		}
		return serverList;
	}

	/**
	 * 查询文件后缀配置
	 */
	public static List<String> getFileSuffix() {
		List<String> suffixList = new ArrayList<String>();
		List<String> configFileAsLine = getConfigFileAsLineByClasspathResource("fileSuffix.conf");
		for (String e : configFileAsLine) {
			if (e.startsWith("#") || e.trim().equals("")) {
				continue;
			} else {
				suffixList.add(e.trim());
			}
		}
		return suffixList;
	}

	/**
	 * 原RequestDateConvert类中的方法
	 * 
	 * @param inDate
	 * @return
	 */
	public static Integer getMonth(String inDate) {
		String s = "0";
		if (inDate.equals("Jan")) {
			s = "01";
		} else if (inDate.equals("Feb")) {
			s = "02";
		} else if (inDate.equals("Mar")) {
			s = "03";
		} else if (inDate.equals("Apr")) {
			s = "04";
		} else if (inDate.equals("May")) {
			s = "05";
		} else if (inDate.equals("Jun")) {
			s = "06";
		} else if (inDate.equals("Jul")) {
			s = "07";
		} else if (inDate.equals("Aug")) {
			s = "08";
		} else if (inDate.equals("Sep")) {
			s = "09";
		} else if (inDate.equals("Oct")) {
			s = "10";
		} else if (inDate.equals("Nov")) {
			s = "11";
		} else if (inDate.equals("Dec")) {
			s = "12";
		}
		return Integer.parseInt(s);
	}
//	四月
	public static String getMonthNumber(String month) {
		if (null == month) {
			return "";
		}
		String res = "";
		switch (month) {
		case "一月" -> res = "01";
		case "二月" -> res = "02";
		case "三月" -> res = "03";
		case "四月" -> res = "04";
		case "五月" -> res = "05";
		case "六月" -> res = "06";
		case "七月" -> res = "07";
		case "八月" -> res = "08";
		case "九月" -> res = "09";
		case "十月" -> res = "10";
		case "十一月" -> res = "11";
		case "十二月" -> res = "12";
		default -> res = "";
		}
		return res;
	}

	private static ThreadLocal<DateFormat> threadLocalDateTime = new ThreadLocal<DateFormat>() {
		@Override
		protected DateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
		}
	};

	private static ThreadLocal<DateFormat> threadLocalDate = new ThreadLocal<DateFormat>() {
		@Override
		protected DateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
		}
	};

	public static Date parseDate(String dateStr) throws ParseException {
		if (StrUtil.isBlank(dateStr)) {
			return null;
		}
		return threadLocalDate.get().parse(dateStr);
	}

	public static String formatDate(Date date) {
		if (date != null) {
			return threadLocalDate.get().format(date);
		} else {
			return "";
		}
	}

	public static Date parseDateTime(String dateStr) throws ParseException {
		if (StrUtil.isBlank(dateStr)) {
			return null;
		}
		return threadLocalDateTime.get().parse(dateStr);
	}

	public static String formatDateTime(Date date) {
		if (date != null) {
			return threadLocalDateTime.get().format(date);
		} else {
			return "";
		}
	}

	/**
	 * 用于将list进行分页
	 * 
	 * @param sourceList
	 * @param pageSize
	 * @return
	 */
	public static <E> List<List<E>> splitListToPage(List<E> sourceList, Integer pageSize) {
		List<List<E>> pageResult = new ArrayList<List<E>>();
		if (null == sourceList || sourceList.isEmpty()) {
			return pageResult;
		}
		List<E> sub = new ArrayList<E>();
		for (E e : sourceList) {
			if (sub.size() >= pageSize) {
				pageResult.add(sub);
				sub = new ArrayList<E>();
			}
			sub.add(e);
		}
		if (!sub.isEmpty()) {
			pageResult.add(sub);
		}
		return pageResult;
	}

	public static <T> List<List<T>> zip(List<T>... lists) {
		List<List<T>> zipped = new ArrayList<List<T>>();
		for (List<T> list : lists) {
			for (int i = 0, listSize = list.size(); i < listSize; i++) {
				List<T> list2;
				if (i >= zipped.size())
					zipped.add(list2 = new ArrayList<T>());
				else
					list2 = zipped.get(i);
				list2.add(list.get(i));
			}
		}
		return zipped;
	}

	/**
	 * 执行一条命令
	 * @param cmd
	 * @return
	 */
	public static String executeLinuxCmd(String cmd) {
		System.out.println("got cmd job : " + cmd);
		try {
			// Runtime.exec(String) 已废弃，换 ProcessBuilder（exec 内部本来就是按空白切分的）
			ProcessBuilder pb = new ProcessBuilder(cmd.trim().split("\\s+"));
			Process process = pb.start();
			InputStream in = process.getInputStream();
			BufferedReader bs = new BufferedReader(new InputStreamReader(in));
			StringBuffer out = new StringBuffer();
			bs.lines().forEach(e -> out.append(e));
			System.out.println("job result [" + out.toString() + "]");
			in.close();
			 process.waitFor();
			process.destroy();
			return "success";
		} catch (IOException e) {
			log.error(ExceptionUtils.getStackTrace(e));
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 打开一个连接，
	 * 
	 * @param commands
	 * @return
	 */
	public static List<String> executeNewFlow(List<String> commands) {
		List<String> results = new ArrayList<String>();
		try {
			// Runtime.exec(String, String[], File) 已废弃，换 ProcessBuilder
			Process proc = new ProcessBuilder("/bin/bash").start();
			BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(proc.getOutputStream())), true);
			for (String line : commands) {
				out.println(line);
			}
			// out.println("cd /home/test");
			// out.println("pwd");
			// out.println("rm -fr /home/proxy.log");
			out.println("exit");// 这个命令必须执行，否则in流不结束。
			String rspLine = "";
			while ((rspLine = in.readLine()) != null) {
//				System.out.println(rspLine);
				results.add(rspLine);
			}
			proc.waitFor();
			in.close();
			out.close();
			proc.destroy();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return results;
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
	
	public static Date locaDateTimeToDate(LocalDateTime localdate) {
	        ZoneId zoneId = ZoneId.systemDefault();
	        ZonedDateTime zonedDateTime =localdate.atZone(zoneId);
	        Instant instant = zonedDateTime.toInstant();
		return Date.from(instant);
	}
	
	/**
	 * 任意 Date 转 Instant。
	 * java.sql.Date.toInstant() 会直接抛 UnsupportedOperationException（它只有日期没有时刻），
	 * 必须先经 getTime()；java.sql.Timestamp 不受此限，但统一走 getTime() 两种都安全。
	 * MyBatis 的 Map 结果里 DATE 列拿到的是 java.sql.Date，实体字段拿到的是 Timestamp，
	 * 所以涉及 Map 结果的日期转换必须走这里，不能直接 toInstant()。
	 */
	public static Instant dateToInstant(Date date) {
		return null == date ? null : Instant.ofEpochMilli(date.getTime());
	}

	public static LocalDate dateToLocalDate(Date date) {
		ZoneId systemDefault = ZoneId.systemDefault();
		LocalDate ofInstant = LocalDate.ofInstant(dateToInstant(date), systemDefault);
		return ofInstant;
	}

	public static LocalDateTime dateToLocalDateTime(Date date) {
		ZoneId systemDefault = ZoneId.systemDefault();
		LocalDateTime ofInstant = LocalDateTime.ofInstant(dateToInstant(date), systemDefault);
		return ofInstant;
	}
	
	public static boolean isSameDay(LocalDateTime t1,LocalDateTime t2) {
		int dayOfYear = t1.getDayOfYear();
		int dayOfYear2 = t2.getDayOfYear();
		return dayOfYear == dayOfYear2;
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
