package tools;

import java.util.ArrayList;

import domain.KeyValue;

public class GetGenerative {
	static String[] source;
	static String[] Fa;

	public GetGenerative(String[] source, String[] Fa) {
		GetGenerative.source = source;
		GetGenerative.Fa = Fa;
	}

	/**
	 * 绑定产生式
	 * 
	 * @param source
	 * @param Fa
	 * @return
	 */
	public static ArrayList<KeyValue> getGenetative() {
		ArrayList<KeyValue> generative = new ArrayList<KeyValue>();
		for (String item : source) {
			String vn = String.valueOf(item.charAt(0));
			String line = item.substring(3);
			for (String temp : Fa) {
				if (line.equals(temp)) {
					// 存入形如 "Te":"E" 的键值对
					KeyValue tempKeyValue = new KeyValue();
					tempKeyValue.setKey(vn);
					tempKeyValue.setValue(temp);
					generative.add(tempKeyValue);
					break;
				}
			}
		}
		return generative;
	}
}
