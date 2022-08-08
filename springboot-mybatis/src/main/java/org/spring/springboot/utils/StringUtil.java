package org.spring.springboot.utils;


import cn.hutool.json.JSONObject;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Clob;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

	public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final String DATE_FORMAT = "yyyy-MM-dd";
	public static SimpleDateFormat datePattern = new SimpleDateFormat("yyyy-MM-dd");
	public static SimpleDateFormat dateTimePattern = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static final Long nd = 1000 * 60 * 60 * 24l;
	private static String PERCENTAGE_PATTERN = "###0.00%";
	public static String NORMAL_DOUBLE_PATTERN = "###0.00";

	/**
	 * 格式化百分比
	 * 
	 * @param percentage
	 * @return
	 */
	public static String formatPerenctage(double percentage) {
		if (percentage == 0) {
			return "0.00%";
		}
		return formatDicimal(percentage, PERCENTAGE_PATTERN);
	}

	public static String formatDicimal(double percentage, String pattern) {
		DecimalFormat f = new DecimalFormat(pattern);
		return f.format(percentage);
	}

	/**
	 * 左加零
	 * 
	 * @param defaultStr
	 * @param len
	 * @return
	 */
	public static String lappendZero(String defaultStr, Integer len) {
		if (len <= 0 || defaultStr.length() >= len) {
			return defaultStr;
		}
		int index = defaultStr.length();
		StringBuffer sb = new StringBuffer();
		while (index < len) {
			sb.append("0");
			index++;
		}
		sb.append(defaultStr);
		return sb.toString();
	}

	/**
	 * @Title: nullToStr @Description: 为null返回String空,并且将空格去掉 @param @param
	 * sourceStr @param @return 设定文件 @return String 返回类型 @throws
	 */

	public static String nullToStr(String sourceStr) {
		if ((sourceStr == null) || sourceStr.equals("") || sourceStr.equals("null")) {
			sourceStr = "";
		}
		return sourceStr.trim();
	}

	public static String nullToStr(Object sourceStr) {
		String str = "";
		if (sourceStr == null || sourceStr.equals("null")) {
			str = "";
		} else
			str = sourceStr.toString();
		return str.trim();
	}

	public static boolean equals(String text1, String text2) {
		return text1 == null && text2 == null || text1 != null && text1.equals(text2);
	}

	public static Double parseDouble(String text) {
		try {
			return Double.parseDouble(text);
		} catch (Exception e) {
			return null;
		}
	}

	public static Long parseLong(String text) {
		try {
			return Long.parseLong(text);
		} catch (Exception e) {
			return null;
		}
	}

	public static Integer parseInteger(String text) {
		try {
			return Integer.parseInt(text);
		} catch (Exception e) {
			return null;
		}
	}

	public static Short parseShort(String text) {
		try {
			return Short.parseShort(text);
		} catch (Exception e) {
			return null;
		}
	}

	/***
	 * 根据时间和格式进行格式化日期
	 */
	public static String formatDate(String strDate, String pattern) {
		String sReturn = "";
		try {
			if (strDate.equals(""))
				return sReturn;
			String sMonth = "", sDay = "", sHour = "", sMinute = "", sSecond = "";
			DateFormat df = new SimpleDateFormat(DATE_TIME_FORMAT);
			df.parse(strDate);
			Calendar c = df.getCalendar();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH) + 1;
			int day = c.get(Calendar.DATE);
			int hour = c.get(Calendar.HOUR_OF_DAY);
			int minute = c.get(Calendar.MINUTE);
			int second = c.get(Calendar.SECOND);

			if (month < 10)
				sMonth = "0" + month;
			else
				sMonth = "" + month;
			if (day < 10)
				sDay = "0" + day;
			else
				sDay = "" + day;
			if (hour < 10)
				sHour = "0" + hour;
			else
				sHour = "" + hour;
			if (minute < 10)
				sMinute = "0" + minute;
			else
				sMinute = "" + minute;
			if (second < 10)
				sSecond = "0" + second;
			else
				sSecond = "" + second;

			if (pattern.equals("yyyy-MM-dd"))
				sReturn = year + "-" + sMonth + "-" + sDay;
			else if (pattern.equals("yyyyMMdd")) {
				sReturn = year + sMonth + sDay;
			} else
				sReturn = year + "-" + sMonth + "-" + sDay + " " + sHour + ":" + sMinute + ":" + sSecond;

		} catch (Exception e) {

		}
		return sReturn;
	}

	/***
	 * 根据时间和格式进行格式化日期
	 */
	public static String getInvoDate() {
		String sReturn = "";
		try {
			SimpleDateFormat tempDate = new SimpleDateFormat("yyMM");
			sReturn = tempDate.format(new Date());

		} catch (Exception e) {

		}
		return sReturn;
	}

	/**
	 * 传入时间字符串，返回所有月份数组
	 * 
	 * @author zhuanglt
	 * @param start
	 *            2012-03
	 * @param end
	 *            2012-06
	 * @return
	 */
	public static List getAllMonths(String start, String end) {
		String splitSign = "-";
		String regex = "\\d{4}" + splitSign + "(([0][1-9])|([1][012]))"; // 判断YYYY-MM时间格式的正则表达式
		if (!start.matches(regex) || !end.matches(regex))
			return new ArrayList();

		List<String> list = new ArrayList<String>();
		if (start.compareTo(end) > 0) {
			// start大于end日期时，互换
			String temp = start;
			start = end;
			end = temp;
		}

		String temp = start; // 从最小月份开始
		while (temp.compareTo(start) >= 0 && temp.compareTo(end) <= 0) {
			list.add(temp); // 首先加上最小月份,接着计算下一个月份
			String[] arr = temp.split(splitSign);
			int year = Integer.valueOf(arr[0]);
			int month = Integer.valueOf(arr[1]) + 1;
			if (month > 12) {
				month = 1;
				year++;
			}
			if (month < 10) {// 补0操作
				temp = year + splitSign + "0" + month;
			} else {
				temp = year + splitSign + month;
			}
		}
		return list;
	}

	public static List getAllDays(String d1, String d2) {
		SimpleDateFormat Formate = new SimpleDateFormat("yyyy-MM-dd");
		List result = new ArrayList();
		try {
			Date date1 = Formate.parse(d1);
			Date date2 = Formate.parse(d2);

			while (date1.before(date2)) {
				result.add(Formate.format(date1));
				date1.setTime(date1.getTime() + 60 * 60 * 24 * 1000);
			}
			return result;
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return new ArrayList();
	}

	public static List getAllDay(String d1, String d2) {
		SimpleDateFormat Formate = new SimpleDateFormat("yyyy-MM-dd");
		List result = new ArrayList();
		try {
			Date date1 = Formate.parse(d1);
			Date date2 = Formate.parse(d2);

			while (date1.before(date2)) {
				result.add(Formate.format(date1));
				date1.setTime(date1.getTime() + 60 * 60 * 24 * 1000);
			}
			result.add(Formate.format(date1));
			return result;
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return new ArrayList();
	}

	/**
	 * 数据库字段转为字符串，对象为空返回空字符串
	 * 
	 * @param o
	 * @return
	 */
	public static String mapFieldToString(Object o) {
		return o == null ? "" : o.toString();
	}

	/**
	 * @Title: getDynamicCo @Description: TODO返回四位随机码 @param @return
	 * 设定文件 @return String 返回类型 @throws
	 */
	public static String getDynamicCo() {
		String dynamicCo = "";
		Random random = new Random();
		for (int i = 0; i < 4; i++) {
			String rand = String.valueOf(random.nextInt(10));
			dynamicCo += rand;
		}
		return dynamicCo;
	}

	public static String formatDateTime(Date date) {
		if (date == null) {
			return "";
		}
		return dateTimePattern.format(date);
	}

	public static String formatDate(Date date) {
		if (date == null) {
			return "";
		}
		return datePattern.format(date);
	}

	public static void JsonWriter(String jsonNmae, Object jsonValue, HttpServletResponse response) throws IOException {
		JSONObject beanJSON = new JSONObject();
		beanJSON.put("result", true);
		beanJSON.put(jsonNmae, jsonValue);
		response.setContentType("text/json;charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().println(beanJSON.toString());
		response.getWriter().close();
	}

	public static boolean isNotBlank(String text) {
		return text != null && text.trim().length() > 0 && !text.equals("null");
	}

	public static boolean isBlank(String text) {
		return !isNotBlank(text);
	}

	public static boolean isNotBlank(Object text) {
		return text != null && text.toString().trim().length() > 0 && !text.toString().equals("null");
	}

	public static boolean isBlank(Object text) {
		return !isNotBlank(text);
	}

	public static String getTime() {
		SimpleDateFormat tempDate = new SimpleDateFormat("yyyyMMddHHmmss");
		return tempDate.format(new Date());
	}

	public static Date formatDate(String dateTime) throws ParseException {
		SimpleDateFormat tempDate = new SimpleDateFormat("yyyyMMddHHmmss");
		return tempDate.parse(dateTime);
	}

	public static Date formatLongDate(String dateTime) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.parse(dateTime);
	}

	/**
	 * 检验输入是否为正确的日期格式(不含秒的任何情况),严格要求日期正确性,格式:yyyy-MM-dd HH:mm
	 * 
	 * @param sourceDate
	 * @return
	 */
	public static boolean checkDate(String sourceDate, String format) {
		if (sourceDate == null) {
			return false;
		}
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(format);
			dateFormat.setLenient(false);
			dateFormat.parse(sourceDate);
			return true;
		} catch (Exception e) {
		}
		return false;
	}

	public static Date formatShortTime(String dateTime) throws ParseException {
		return datePattern.parse(dateTime);
	}

	public static Date getDay(Date date) throws Exception {
		return new Date(date.getTime() + nd);
	}

	public static String DateTOStringForShort(Date date) {
		return datePattern.format(date);
	}

	public final static String clob2String(Clob clob) {
		if (clob == null) {
			return null;
		}

		StringBuffer sb = new StringBuffer(65535);// 64K
		Reader clobStream = null;
		try {
			clobStream = clob.getCharacterStream();
			char[] b = new char[60000];// 每次获取60K
			int i = 0;
			while ((i = clobStream.read(b)) != -1) {
				sb.append(b, 0, i);
			}
		} catch (Exception ex) {
			sb = null;
		} finally {
			try {
				if (clobStream != null)
					clobStream.close();
			} catch (Exception e) {
			}
		}
		if (sb == null)
			return null;
		else
			return sb.toString();
	}

	public final static boolean compareAnswer(String[] answerA, String[] answerB) {
		if (answerA == null || answerB == null)
			return false;
		if (answerA.length != answerB.length)
			return false;
		for (int i = 0; i < answerA.length; i++) {
			boolean isEqual = false;
			for (int j = 0; j < answerB.length; j++) {
				if (answerA[i].equals(answerB[j])) {
					isEqual = true;
				}
			}
			if (!isEqual) {
				return false;
			}
		}
		return true;
	}

	public static String getIpAddr(HttpServletRequest request) {
		String ipAddress = null;
		ipAddress = request.getHeader("x-forwarded-for");
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getRemoteAddr();
			if (ipAddress.equals("127.0.0.1")) {
				// 根据网卡取本机配置的IP
				InetAddress inet = null;
				try {
					inet = InetAddress.getLocalHost();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
				if (inet != null)
					ipAddress = inet.getHostAddress();
			}

		}
		// 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
		if (ipAddress != null && ipAddress.length() > 15) {
			if (ipAddress.indexOf(",") > 0) {
				ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
			}
		}
		return ipAddress;
	}

//	public static boolean userInfoRoleHasRole(List<DbUserInfoRole> userInfoRoleList, Long roleId) {
//		if (userInfoRoleList != null) {
//			for (DbUserInfoRole userInfoRole : userInfoRoleList) {
//				if (userInfoRole.getRoleId().equals(roleId))
//					return true;
//			}
//		}
//		return false;
//	}

	/*
	 * 判断数组是否为空
	 */
	public static boolean isBlankArray(String[] s) {
		if (s == null)
			return true;
		if (s.length == 1 && isBlank(s[0]))
			return true;
		return false;

	}

	public static boolean isNotBlankArray(String[] s) {
		return !isBlankArray(s);
	}


	/**
	 * 将字符串数组转换成以逗号隔开的字符串 例如{"A","B","C"}转换后变成 A,B,C
	 * 
	 * @param strArray
	 * @return
	 */
	public static String strArrayToStr(String[] strArray) {
		StringBuffer strBuf = new StringBuffer();
		if (null != strArray && strArray.length > 0) {
			for (String temp : strArray) {
				strBuf.append(temp + ",");
			}
			return strBuf.substring(0, strBuf.length() - 1);
		}
		return null;
	}

	private static List<Double> subtotalFill(List<Double> total, int len) {
		for (int i = 0; i < len; i++) {
			total.add(0d);
		}
		return total;
	}

	public static String getEname(String name) {
		HanyuPinyinOutputFormat pyFormat = new HanyuPinyinOutputFormat();
		pyFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		pyFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		pyFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
		try {
			return PinyinHelper.toHanyuPinyinString(name, pyFormat, "");
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 将String数组转成以|连接的String.
	 * 
	 * @Title: arrayToString
	 * @Description:
	 * @author: xiefengsong@xdf.cn
	 * @data : 2014-10-28 上午10:48:19
	 */
	public static String arrayToString(String[] array) {
		if (array != null && array.length > 0) {
			StringBuffer resultBuff = new StringBuffer();
			for (String str : array) {
				resultBuff.append(str + "|");
			}
			return resultBuff.toString().substring(0, resultBuff.length() - 1);
		}
		return null;
	}

	/**
	 * @Title: chineseToString
	 * @Description: 将汉字字符串转为拼音,如果为非汉字则保留原样.
	 * @author: xiefengsong@xdf.cn
	 * @data : 2014-12-29 上午09:38:18
	 */
	public static String chineseToString(String chinese) {
		if (isNotBlank(chinese)) {
			StringBuffer pinyinName = new StringBuffer();
			char[] nameChar = chinese.toCharArray();
			HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
			defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
			defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
			defaultFormat.setVCharType(HanyuPinyinVCharType.WITH_U_UNICODE);
			String[] transferPinyinArray = null;
			String transferPinyin = null;
			for (int i = 0; i < nameChar.length; i++) {
				if (nameChar[i] > 128) {
					try {
						transferPinyinArray = PinyinHelper.toHanyuPinyinStringArray(nameChar[i], defaultFormat);
						if (transferPinyinArray != null && transferPinyinArray.length > 0) {
							transferPinyin = transferPinyinArray[0];
						} else {
							transferPinyin = nameChar[i] + "";
						}
						pinyinName.append(transferPinyin);
					} catch (BadHanyuPinyinOutputFormatCombination e) {
						e.printStackTrace();
					}
				} else {
					pinyinName.append(nameChar[i]);
				}
			}
			// 如果朋友吕,绿之类的.将ü替换成v.
			return pinyinName.toString().replace("ü", "v");
		}
		return "";
	}

	/**
	 * 验证输入的邮箱格式是否符合
	 * 
	 * @param email
	 * @return 是否合法
	 */
	public static boolean emailFormat(String email) {
		boolean tag = true;
		final String pattern1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
		final Pattern pattern = Pattern.compile(pattern1);
		final Matcher mat = pattern.matcher(email);
		if (!mat.find()) {
			tag = false;
		}
		return tag;
	}

	public static String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;

		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "/n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();

	}

	/**
	 * 将数组转成db用的 '1','2','3'格式
	 * 
	 * @param flag 表示带引号的字符串 false 表示没有
	 */
	public static String StringToDBstr(List<String> strs, boolean flag) {

		String temp = "";
		if (!flag) {
			if (strs != null && strs.size() > 0) {
				for (int i = 0; i < strs.size(); i++) {
					temp = temp + strs.get(i);
					if (i < strs.size() - 1) {
						temp = temp + ",";
					}
				}
			}
		} else {
			if (strs != null && strs.size() > 0) {
				for (int i = 0; i < strs.size(); i++) {
					temp = temp + "'" + strs.get(i) + "'";
					if (i < strs.size() - 1) {
						temp = temp + ",";
					}
				}
			}
		}
		return temp;
	}

	public static boolean isNumber(String str) {// 判断整型
		return str.matches("[\\d]+");
	}

	public static boolean isDecimalsNumber(String str) {// 判断小数，与判断整型的区别在与d后面的小数点（红色）
		return str.matches("[\\d.]+");
	}


	public static final String createDeleteSqlByList(String tableName, List<Map<String, Object>> mapList, String keyId,
			boolean isSplite) {
		StringBuilder res = new StringBuilder();
		if (StringUtil.isBlank(keyId) || StringUtil.isBlank(tableName)) {
			return "";
		}
		String spliteStr = "";
		if (isSplite) {
			spliteStr = "|-1-|" + "\r\n";
		}
		if (mapList != null && mapList.size() > 0) {
			for (int i = 0; i < mapList.size(); i++) {
				Map<String, Object> tempMap = mapList.get(i);
				// 复合主键
				String[] list = keyId.split(",");
				String temp = "";
				for (int j = 0; j < list.length; j++) {
					// System.out.println(list[i]);
					if (j < list.length - 1) {
						temp = temp + list[j] + "=" + tempMap.get(list[j]) + " and ";
					} else {
						temp = temp + list[j] + "=" + tempMap.get(list[j]);
					}
				}
				res.append(" delete from " + tableName + " where " + temp + "; " + spliteStr);
			}
		} else {
			return "";
		}

		return res.toString();
	}


	public static final String createUpdateSqlByList(String tableName, List<Map<String, Object>> mapList, String keyId,
			boolean isSplite, String updateValue, boolean updateIsStringOrInt) {
		StringBuilder res = new StringBuilder();
		if (StringUtil.isBlank(keyId) || StringUtil.isBlank(tableName)) {
			return "";
		}
		String spliteStr = "";
		if (isSplite) {
			spliteStr = "|-1-|" + "\r\n";
		}
		if (mapList != null && mapList.size() > 0) {
			for (int i = 0; i < mapList.size(); i++) {
				Map<String, Object> tempMap = mapList.get(i);
				// 复合主键
				String[] list = keyId.split(",");
				String temp = "";
				String update = "";
				for (int j = 0; j < list.length; j++) {
					// System.out.println(list[i]);
					if (j < list.length - 1) {
						temp = temp + list[j] + "=" + tempMap.get(list[j]) + " and ";
					} else {
						temp = temp + list[j] + "=" + tempMap.get(list[j]);
					}
				}
				if (updateIsStringOrInt) {
					// 是string
					update = updateValue + "= '" + tempMap.get(updateValue) + "' ";
				} else {
					// 否字符串 int
					update = updateValue + "=" + tempMap.get(updateValue);
				}
				if (StringUtil.isBlank(update)) {
					continue;
				}
				res.append(" update " + tableName + " set " + update + "  where " + temp + "; " + spliteStr);
			}
		} else {
			return "";
		}

		return res.toString();
	}

	/*
	 * 适应ios sqlite3的转义字符
	 */
	public static final String checkStr(String str) {
		/*
		 * 
		 * / -> // ' -> '' [ -> /[ ] -> /] % -> /% & -> /& _ -> /_ ( -> /( ) ->
		 * /)
		 */
		str = str.replace("/", "//");
		str = str.replace("'", "''");
		str = str.replace("[", "/[");
		str = str.replace("]", "/]");

		str = str.replace("%", "/%");
		str = str.replace("&", "/&");
		str = str.replace("_", "/_");
		str = str.replace("(", "/(");
		str = str.replace(")", "/)");

		return str;
	}

	/*
	 * 小数转成比例
	 */
	public static Map<String, Object> decimalsToRatio(String num) {
		if (StringUtil.isBlank(num)) {
			return null;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		double f = Double.parseDouble(num);
		BigDecimal b = new BigDecimal(f);
		double f1 = 100 * b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		int hundred = (int) (f1);// Integer.parseInt(String.valueOf(f1));
		int div = maxCommonDivisor(hundred, 100);

		int n1 = hundred / div;
		int n2 = 100 / div;
		map.put("one", n1);
		map.put("two", n2);
		System.out.println(n1 + "    " + n2);
		return map;
	}

	/*
	 * 递归法求最大公约数
	 */

	public static int maxCommonDivisor(int m, int n) {
		if (m < n) {// 保证m>n,若m<n,则进行数据交换
			int temp = m;
			m = n;
			n = temp;
		}
		if (m % n == 0) {// 若余数为0,返回最大公约数
			return n;
		} else { // 否则,进行递归,把n赋给m,把余数赋给n
			return maxCommonDivisor(n, m % n);
		}
	}

	public static <T> List<T> castList(Object obj, Class<T> clazz)
	{
		List<T> result = new ArrayList<T>();
		if(obj instanceof List<?>)
		{
			for (Object o : (List<?>) obj)
			{
				result.add(clazz.cast(o));
			}
			return result;
		}
		return null;
	}

	/**
	 * 去除字符串两端特定符号
	 *
	 * @param srcStr 字符串
	 * @param splitter 特定符号
	 * @return 过滤后的字符串
	 */
	public static String trimBothEndsChars(String srcStr, String splitter) {
		String regex = "^" + splitter + "*|" + splitter + "*$";
		return srcStr.replaceAll(regex, "");
	}
}
