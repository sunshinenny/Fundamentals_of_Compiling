package tools;

import java.util.ArrayList;

import domain.First;
import domain.Follow;
import domain.KeyValue;

public class GetFollow {
	static ArrayList<KeyValue> generative;
	static String[] VN;
	static ArrayList<First> firsts;

	public GetFollow(ArrayList<KeyValue> generative, String[] VN, ArrayList<First> firsts) {
		GetFollow.generative = generative;
		GetFollow.VN = VN;
		GetFollow.firsts = firsts;
	}

	public static ArrayList<Follow> getFollow() {

		ArrayList<Follow> follows = new ArrayList<Follow>();
		for (KeyValue keyValue : generative) {
			// 定义临时的Follow对象
			Follow tempFollow = new Follow();
			ArrayList<String> items = new ArrayList<String>();
			String key = keyValue.getKey();
			// 将"#"加入到Start非终结符中
			if (key.equals("E")) {
				items.add("#");
			}
			// 开始逐行寻找此非终结符出现的前后符号,根据定义生成它的初步Follow集合
			for (KeyValue getFollowOrigin : generative) {
				String leftKey = getFollowOrigin.getKey();
				String value = getFollowOrigin.getValue();
				// 在产生式右侧寻找左侧非终结符,进而获取到前后符号
				int address = value.indexOf(key);
				// 如果找到了该非终结符
				if (address != -1) {
					// 判断下一个符号是否存在,如果不存在将会导致数组越界的报错
					if ((address + 1) == value.length()) {
						// 根据A->αB [将Follow(A)添加到Follow(B)中]
						items.add(leftKey);
						tempFollow.setKey(key);
						tempFollow.setValue(items);
						follows.add(tempFollow);
						continue;
					} else {
						// 寻找它后面的符号,定义为nextString
						String nextString = String.valueOf(value.charAt(address + 1));
						// 判断nextString是不是终结符
						if (VNJduge(nextString)) {
							// 如果是,判断它能不能推出空
							// 如果可以,则根据A->αB [将Follow(A)添加到Follow(B)中]
							for (KeyValue getNull : generative) {
								if (nextString.equals(getNull.getKey())) {
									if (getNull.getValue().equals("~")) {
										items.add(leftKey);
										break;
									}
								}
							}

							// 如果推不出空,则根据A->αBβ [将First(β)\ε添加到Follow(B)]
							for (First itemFirst : firsts) {
								String firstKey = itemFirst.getKey();
								if (nextString.equals(firstKey)) {
									for (String s : itemFirst.getValue()) {
										if (s.equals("~")) {
											continue;
										} else {
											items.add(s);
										}
									}
								}
							}
						} else {
							// 如果是终结符:根据A->αBβ [将β添加到Follow(B)]
							items.add(nextString);
						}
					}
				}
			}
			tempFollow.setKey(key);
			tempFollow.setValue(items);
			follows.add(tempFollow);
		}

		/**
		 * 此时因为遍历得到了很多重复的元素\n 所以开始去重\n
		 * 并且顺便将follow集合中的占位符,例如Follow(E)中包含了Follow(T),将T替换为Follow集合
		 */

		// 去除重复元素
		ArrayList<String> keys = new ArrayList<String>();
		ArrayList<Follow> newFollows = new ArrayList<Follow>();
		for (Follow follow : follows) {
			if (!keys.contains(follow.getKey())) {
				// 将该字符保存下来,以便于下一次遍历判断
				keys.add(follow.getKey());
				// 如果不存在,将其集合内的value去重后加入新的集合 准备输出
				ArrayList<String> newValues = new ArrayList<String>();
				for (String s : follow.getValue()) {
					if (!newValues.contains(s)) {
						// 判断右边的集合内有没有左边的key,如果有 集合中删去
						if (s.equals(follow.getKey())) {
							continue;
						}
						newValues.add(s);
					} else {
						continue;
					}
				}
				Follow tempFollow = new Follow();
				tempFollow.setKey(follow.getKey());
				tempFollow.setValue(newValues);
				newFollows.add(tempFollow);
			} else {
				continue;
			}
			// 如果存在,则跳过
		}

		// 第一遍处理结束,此时将Follow集右边的非终结符转为对应的Follow集合
		int flag = 1;
		while (flag == 1) {
			for (int i = 0; i < newFollows.size(); i++) {
				Follow follow = newFollows.get(i);
				ArrayList<String> values = follow.getValue();
				ArrayList<String> newValues = new ArrayList<String>();
				for (String s : values) {
					// 判断是不是非终结符
					if (VNJduge(s)) {
						// 将其替换为对应的Follow集合
						for (Follow follow2 : newFollows) {
							String key = follow2.getKey();
							if (s.equals(key)) {
								for (String tempString : follow2.getValue()) {
									if (!newValues.contains(tempString)) {
										newValues.add(tempString);
									}
								}
							}
						}
					} else {
						newValues.add(s);
					}
				}
				newFollows.get(i).setValue(newValues);
			}
			for (Follow follow : newFollows) {
				ArrayList<String> values = follow.getValue();
				for (String s : values) {
					if (VNJduge(s)) {
						break;
					} else {
						flag = 0;
					}
				}
			}

		}

		return newFollows;
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
}
