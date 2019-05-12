package tools;

import java.util.ArrayList;

import domain.First;
import domain.KeyValue;

public class GetFirst {
	static ArrayList<KeyValue> generative;
	static String[] VN;

	public GetFirst(ArrayList<KeyValue> generative, String[] VN) {
		// TODO Auto-generated constructor stub
		GetFirst.generative = generative;
		GetFirst.VN = VN;
	}

	public static ArrayList<First> getFirst() {
		// 定义first的集合 firsts
		ArrayList<First> firsts = new ArrayList<First>();
		// 开始遍历产生式
		for (KeyValue gValue : generative) {
			String key1 = gValue.getKey();
			ArrayList<String> items = new ArrayList<String>();
			for (KeyValue gValue2 : generative) {
				String key2 = gValue2.getKey();
				if (key1.equals(key2)) {
					String value = gValue2.getValue();
					String judge = String.valueOf(value.charAt(0));
					items.add(judge);
				}
				// 遍历产生式,寻找终结符,找到之后将其加入到 E 的First集合中
			}
			int flag = 0;// 如果一直为0 表明没有相同的元素存在
			for (First first : firsts) {
				if (first.getKey().equals(key1)) {
					// 如果为True 表明有相同的元素存在
					flag = 1;
				} else
					continue;
			}
			// ArrayList去重
			if (flag == 0) {
				First firstTemp = new First();
				firstTemp.setKey(key1);
				firstTemp.setValue(items);
				firsts.add(firstTemp);
			} else {
				continue;
			}
		}

		// 替换掉First集合中的非终结符
		int vnFlag = 1;
		while (vnFlag == 1) {
			// 情况1:根据A->Bx First(B)\ε=First(A)得到最终的First集合
			for (int i = 0; i < firsts.size(); i++) {
				First first = firsts.get(i);
				String v = first.getValue().get(0);// 得到形如{T}或者{+}的形式
				if (VNJduge(v)) {
					// 如果属于非终结符
					for (First first2 : firsts) {
						String v1 = first2.getKey();
						if (v1.equals(v)) {
							// 情况2:赋值->First集\ε
							ArrayList<String> temp = new ArrayList<String>();
							for (String s : first2.getValue()) {
								if (s.equals("~")) {
									continue;
								} else {
									temp.add(s);
								}
							}
							// 赋值给当前的First集合
							firsts.get(i).setValue(temp);
						}
					}
				}
				// 检测First集合是否为终结符集
				for (First lastJudge : firsts) {
					String v1 = lastJudge.getValue().get(0);// 得到形如{T}或者{+}的形式
					if (VNJduge(v1)) {
						// 没有转换完,则继续循环
						break;
					} else {
						// 转换结束,停止循环
						vnFlag = 0;
					}
				}
			}
		}
		return firsts;
	}

	/**
	 * 判断这个符号是不是非终结符
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
}
