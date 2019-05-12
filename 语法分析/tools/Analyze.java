package tools;

import java.util.ArrayList;
import java.util.Stack;

import domain.First;
import domain.Follow;
import domain.KeyValue;

public class Analyze {
	static String[] VN;
	static String[] VT;
	static String[] Fa;
	static ArrayList<KeyValue> generative;
	static ArrayList<First> firsts;
	static ArrayList<Follow> follows;
	static int[][] table;
	static String sentence;

	public Analyze(String[] VN, String[] VT, String[] Fa, ArrayList<KeyValue> generative, ArrayList<First> firsts,
			ArrayList<Follow> follows, int[][] table, String sentence) {
		Analyze.VN = VN;
		Analyze.VT = VT;
		Analyze.Fa = Fa;
		Analyze.generative = generative;
		Analyze.firsts = firsts;
		Analyze.follows = follows;
		Analyze.table = table;
		Analyze.sentence = sentence;
	}

	public static void analyze() {
		// sentenceStack 句子的栈
		Stack<String> sentenceStack = new Stack<String>();
		// 逆向入栈
		for (int i = sentence.length() - 1; i >= 0; i--) {
			String item = String.valueOf(sentence.charAt(i));
			sentenceStack.add(item);
		}
		new stackView();

		// 非终结符组成的栈
		Stack<String> vnStack = new Stack<String>();
		// 初始化vnStack为#E
		vnStack.add("#");
		vnStack.add("E");

		System.out.println("栈\t句子\t动作");
		// 开始分析
		// 非终结符栈 逆序获取A和句子第一个字母a一起查表
		while (vnStack.size() != 0) {
			String msg = "";
			String begin = stackView.returnStack(vnStack);
			String vnTop = vnStack.pop();// 取出栈顶元素并赋值给nowVN

			// 如果当前的值是非终结符,进入到查表操作
			if (VNJduge(vnTop)) {
				// 此时取出的vnStack为E,句子为i
				String strTop = sentenceStack.peek();// peek方法,我就看看 不取出来

				int topAddress = arrayToString(VN).indexOf(vnTop);
				int strAddress = arrayToString(VT).indexOf(strTop);
				// 如果查询到的结果为1
				if (table[topAddress][strAddress] == 1) {
					// 遍历产生式,获取到E->Te这种类型的式子,将右侧逆序入栈
					int outFlag = 0;
					for (KeyValue keyValue : generative) {
						outFlag++;
						String key = keyValue.getKey();
						String right = keyValue.getValue();
						if (right.equals("~")) {
							continue;
						} else if (key.equals(vnTop)) {
							// 查看第一个是不是终结符,如果是,看看是否属于该非终结符的First集
							String firstStr = String.valueOf(right.charAt(0));
							if (arrayToString(VT).indexOf(firstStr) != -1) {
								// 不等于-1代表第一个值为终结符
								// 此处判断包含二义性的产生式哪一个更适合用来匹配
								int flag = 0;
								for (KeyValue keyValuex : generative) {
									String keyx = keyValuex.getKey();
									String rightx = keyValuex.getValue();
									if (key.equals(keyx)) {
										String firstStrx = String.valueOf(rightx.charAt(0));
										if (firstStrx.equals(strTop)) {
											break;
										}
									}
									flag++;
								}
								if (flag == outFlag - 1) {
									for (int i = right.length() - 1; i >= 0; i--) {
										// 将产生式逆序入栈
										vnStack.add(String.valueOf(right.charAt(i)));
									}
									msg = key + "->" + right;
									break;
								}
							} else {
								// 第一个值为非终结符
								for (int i = right.length() - 1; i >= 0; i--) {
									// 将产生式逆序入栈
									vnStack.add(String.valueOf(right.charAt(i)));
								}
								msg = key + "->" + right;
								break;
							}
						}
					}

				} else if (table[topAddress][strAddress] == 0) {
					// 查表为0,则表明可以推导出空
					msg = vnTop + "->ε";
				}else if (table[topAddress][strAddress] ==  -1) {
					msg= "E1:"+vnTop+"出栈";
				}else if (table[topAddress][strAddress] ==  -2) {
					msg =sentenceStack.peek()+"不属于follow("+vnTop+"),句子指针下移";
					System.out.println(begin + "\t" + stackView.returnReserveStack(sentenceStack) + "\t" + msg);
					sentenceStack.pop();
					continue;
				}
			} else {
				// 如果是终结符,则进入匹配或者报错
				// 此时如果取出的是 i 和句子的第一个单词匹配
				String senTop = sentenceStack.peek();
				if (vnTop.equals(senTop)) {
					sentenceStack.pop();
					msg = "匹配 " + senTop;
				} else {
					msg = "匹配不成功,跳过该单词";
				}
			}
			System.out.println(begin + "\t" + stackView.returnReserveStack(sentenceStack) + "\t" + msg);
		}
		if (sentenceStack.size()!=0) {
			System.out.println("匹配出错,请查看报错");
		}
		if (vnStack.size() == 0) {
			System.out.println("匹配结束");
		} 
	}

	/**
	 * 判断这个符号是不是终结符
	 * 
	 * @param judge
	 * @return
	 */
	public static boolean VNJduge(String judge) {
		boolean result = false;
		for (String string : VN) {
			if (string.equals(judge)) {
				result = true;
			}
		}

		return result;
	}

	public static String arrayToString(String[] str) {
		String temp = "";
		for (String string : str) {
			temp += string;
		}
		return temp;

	}
}
