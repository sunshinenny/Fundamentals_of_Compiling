package tools;

import java.util.Stack;

public class stackView {

	public stackView() {
	}
	/**
	 * 正向输出Stack
	 * @param stack
	 */
	public static String returnStack(Stack<String> stack) {
		String theStack = "";

		for (int i = 0; i < stack.size(); i++) {
			String tmp = stack.get(i);
			theStack += tmp;
		}
		return theStack;
	}

	/**
	 * 反向输出
	 * 
	 * @param stack
	 */
	public static String returnReserveStack(Stack<String> stack) {
		String theStack = "";

		for (int i = stack.size()-1; i >= 0; i--) {
			String tmp = stack.get(i);
			theStack += tmp;
		}
		return theStack;
	}
}
