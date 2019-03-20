package main;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

public class main {
	/**
	 * 此程序是通过将文件的字符读取到字符数组中去，然后遍历数组，将读取的字符进行 分类并输出
	 * 
	 * @author SunShinenny
	 *
	 */
//	ArrayList<String> wordTable=new ArrayList<String>();
	// 定义HashMap存储标识符
	static HashMap<Integer, String> wordMap = new HashMap<Integer, String>();
	static HashMap<Integer, String> numberMap = new HashMap<Integer, String>();
	static ArrayList<String> errorList = new ArrayList<String>();
	static String comment1 = "";
	static String comment2 = "";
	static int lineFlag = 1;// 默认第一行

	// 定义int类型的flag存储标识符的序号
	static int wordFlag = 1;
	static int numberFlag = 1;

	public static class WordAnalyze {
		private String keyWord[] = { "int", "char", "float", "void", "const", "for", "if", "else", "then", "while",
				"switch", "break", "begin", "end" };
		private char ch;

		// 判断是否是关键字
		int isKey(String str) {
			for (int i = 0; i < keyWord.length; i++) {
				if (keyWord[i].equals(str))
					return i + 1;// 直接return单词代码
			}
			return -1;
		}

		// 判断是否是字母
		boolean isLetter(char letter) {
			if ((letter >= 'a' && letter <= 'z') || (letter >= 'A' && letter <= 'Z'))
				return true;
			else
				return false;
		}

		// 判断是否是数字
		boolean isDigit(char digit) {
			if (digit >= '0' && digit <= '9')
				return true;
			else
				return false;
		}

		// 词法分析
		void analyze(char[] chars) {
			String arr = "";
			for (int i = 0; i < chars.length; i++) {
				ch = chars[i];
				arr = "";
				if (ch == '\n') {
					lineFlag++;
				} else if (ch == ' ' || ch == '\t' || ch == '\r') {
				} else if (isLetter(ch)) {
					while (isLetter(ch) || isDigit(ch)) {
						arr += ch;
						ch = chars[++i];
					}
					// 回退一个字符
					i--;
					if (isKey(arr) != -1) {
						// 关键字
						System.out.println("(" + arr + "," + "_" + ")");
					} else {
						// 标识符
						System.out.println("(" + arr + "," + "ID:" + wordFlag + ")");
						// 添加到标识符表
						wordMap.put(wordFlag, arr);
						wordFlag++;

					}
				} else if (isDigit(ch)) {// 如果读到的第一个字符为数字,则继续下面的操作
					// 定义一个记录小数点的计数器
					// 如果小数点的数量为1 则正常识别为数字,如果大于1,将小数点识别为小数点输出,两侧的数字分别作为数字输出
					// 如果ch一直是数字 或者ch是一个小数点(并且下一个ch仍然是数字)
					while (isDigit(ch) || (ch == '.' && isDigit(chars[++i]))) {
						if (ch == '.') {
							i--;
						}
						// 游标往后走
						if (isDigit(chars[i])) {
							arr = arr + ch;
							ch = chars[++i];
						} else if (chars[i] == '.') {
							arr = arr + ".";
							ch = chars[++i];
						} else {
							ch = chars[++i];
						}
					}
					// 属于无符号常数
					System.out.println("(" + arr + "," + numberFlag + ")");
					numberMap.put(numberFlag, arr);
					numberFlag++;
					i -= 1;
				} else
					switch (ch) {
					// 分界符
					case '(':
						System.out.println("(" + ch + "," + 24 + ")");
						break;
					case ')':
						System.out.println("(" + ch + "," + 25 + ")");
						break;
					case '[':
						System.out.println("(" + ch + "," + 26 + ")");
						break;
					case ']':
						System.out.println("(" + ch + "," + 27 + ")");
						break;
					case ',':
						System.out.println("(" + ch + "," + 35 + ")");
						break;
					case ';':
						System.out.println("(" + ch + "," + 36 + ")");
						break;
					case '{':
						System.out.println("(" + ch + "," + 0 + ")");
						break;
					case '}':
						System.out.println("(" + ch + "," + 0 + ")");
						break;
					case '\'':
						System.out.println("(" + ch + "," + 0 + ")");
						break;
					case '.': {
						ch = chars[++i];
						if (ch == '.') {

						} else {
							errorList.add("多个小数点错误 所处行号:  " + lineFlag);
							i--;
						}
					}
						break;
					// 运算符
					case '=': {
						ch = chars[++i];
						if (ch == '=')
							System.out.println("(==" + "," + 30 + ")");
						else {
							System.out.println("(=" + "," + 45 + ")");
							i--;
						}
					}
						break;
					case '>': {
						ch = chars[++i];
						if (ch == '=')
							System.out.println("(>=" + "," + 30 + ")");
						else {
							System.out.println("(>" + "," + 30 + ")");
							i--;
						}
					}
						break;
					case '<': {
						ch = chars[++i];
						if (ch == '=')
							System.out.println("(<=" + "," + 30 + ")");
						else {
							System.out.println("(<" + "," + 30 + ")");
							i--;
						}
					}
						break;
					case '!': {
						ch = chars[++i];
						if (ch == '=')
							System.out.println("(!=" + "," + 30 + ")");
						else {
							System.out.println("(!" + "," + 44 + ")");
							i--;
						}
					}
						break;
					case '/': {
//						String comment1 = "";
//						String comment2 = "";
						ch = chars[++i];
						if (ch == '=')
							System.out.println("(/=" + "," + 37 + ")");
						else if (ch == '/') {
							// 指针继续往下,直到读取到换行符为止(或者到指定的长度为止)
							ch = chars[++i];
							int commentFlag1 = 0;
							while (ch != '\n') {
								if (commentFlag1 > 1024)
									break;// 如果读取的长度超过1024个字符,则放弃识别为备注
								comment1 += ch;// 拼接为备注内容
								ch = chars[++i];// 移动游标
								commentFlag1++;// 将统计Flag++
								if (ch == '\n')
									lineFlag++;
								continue;
							}
//							System.out.print("//" + comment1);
							comment1 += "\n";
						} else if (ch == '*') {
							// 指针继续往下,直到读取到*/为止(或者到指定的长度为止)
							ch = chars[++i];
							int commentFlag2 = 0;
							while (true) {
								if (chars[i] == '*' && chars[i + 1] == '/') {
									break;
								} else {
									if (chars[i] == '\n') {
										lineFlag++;
									}
									if (commentFlag2 > 1024)
										break;// 如果读取的长度超过1024个字符,则放弃识别为备注
									if (i == chars.length - 1) {
										String errorMsg = "发生错误: /*注释未结束   所处行号: " + lineFlag;
										errorList.add(errorMsg);
										break;
									}
									comment2 += ch;// 拼接为备注内容
									ch = chars[++i];// 移动游标
									commentFlag2++;// 将统计Flag++
									continue;
								}

							}
							// 继续移动游标到备注外
							i++;
//							System.out.println("/*" + comment2 + "*/");
							comment2 += "\n";

						} else {
							System.out.println("(/" + "," + 22 + ")");
							i--;
						}
					}
						break;
					case '+': {
						ch = chars[++i];
						if (ch == '=')
							System.out.println("(+=" + "," + 38 + ")");
						else {
							System.out.println("(+" + "," + 19 + ")");
							i--;
						}
					}
						break;
					case '-': {
						ch = chars[++i];
						if (ch == '=')
							System.out.println("(/=" + "," + 39 + ")");
						else {
							System.out.println("(-" + "," + 20 + ")");
							i--;
						}
					}
						break;
					case '*': {
						ch = chars[++i];
						if (ch == '=')
							System.out.println("(*=" + "," + 40 + ")");
						else {
							System.out.println("(*" + "," + 21 + ")");
							i--;
						}
					}
						break;
					case '%': {
						ch = chars[++i];
						if (ch == '=')
							System.out.println("(%=" + "," + 41 + ")");
						else {
							System.out.println("(%" + "," + 23 + ")");
							i--;
						}
					}
						break;
					case '|': {
						ch = chars[++i];
						if (ch == '|')
							System.out.println("(||" + "," + 42 + ")");
						else {
							i--;
						}
					}
						break;
					case '&': {
						ch = chars[++i];
						if (ch == '&')
							System.out.println("(&&" + "," + 43 + ")");
						else {
							i--;
						}
					}
						break;
					// 无识别
					default: {
//						System.out.println(ch + "\t" + "\t可能为非法字符");
						String errorMsg = "";
						errorMsg += ch + "\t" + "\t可能为非法字符  所处行号:  " + lineFlag;
						errorList.add(errorMsg);
					}
					}
			}
		}

		public static void main(String[] args) throws Exception {
			File file = new File("E:\\data.txt");// 定义一个file对象，用来初始化FileReader
			FileReader reader = new FileReader(file);// 定义一个fileReader对象，用来初始化BufferedReader
			int length = (int) file.length();
			// 这里定义字符数组的时候需要多定义一个,因为词法分析器会遇到超前读取一个字符的时候，如果是最后一个
			// 字符被读取，如果在读取下一个字符就会出现越界的异常
			char buf[] = new char[length + 1];
			reader.read(buf);
			reader.close();
//			System.out.println(buf.length);
			new WordAnalyze().analyze(buf);
			loopTable();
		}

		static void loopTable() {// 该方法不调用,用以存储一些暂时多余的代码
			// 遍历HashMap
			System.out.println("\n标识符表");
			for (Integer key : wordMap.keySet()) {
				System.out.println("Key: " + key + " Value: " + wordMap.get(key));
			}
			System.out.println("\n常数表");
			for (Integer key : numberMap.keySet()) {
				System.out.println("Key: " + key + " Value: " + numberMap.get(key));
			}
			System.out.println("\n//注释的内容为:\n" + comment1 + "\n");
			System.out.println("\n/*注释的内容为:\n" + comment2 + "\n");
			System.out.println("错误的个数为:" + errorList.size());
			System.out.println("错误为:");
			for (String msg : errorList) {
				System.out.println(msg);
			}
		}
	}
}
