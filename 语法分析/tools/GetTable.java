package tools;

import java.util.ArrayList;

import domain.First;
import domain.Follow;

public class GetTable {
	static String[] VT;
	static ArrayList<First> firsts;
	static ArrayList<Follow> follows;

	public GetTable(String[] VT, ArrayList<First> firsts, ArrayList<Follow> follows) {
		GetTable.VT = VT;
		GetTable.firsts = firsts;
		GetTable.follows = follows;
	}

	public static int[][] getTable() {
		// 初始化分析表
		int rowNum = VT.length;
		int colNum = firsts.size();
		int[][] table = new int[colNum][rowNum];
		// 首先全部置为3,有一个初始值方便后期判断
		for (int i = 0; i < colNum; i++) {
			for (int j = 0; j < rowNum; j++) {
				table[i][j] = 3;
			}
		}

		// 在此约定, 0 为 ε , 1 为可以推导, -1 表示出错， -2 表示该行终结符的follow集合，用于错误处理
		// 开始填写预测分析表
		for (int i = 0; i < firsts.size(); i++) {
			ArrayList<String> firstValues = firsts.get(i).getValue();
			for (int j = 0; j < VT.length; j++) {
				// 情况1:if A->α [a in First(A)] 则 [A,a] 那个位置为 1
				if (firstValues.contains(VT[j])) {
					table[i][j] = 1;
				}
				// 情况2:if ε 属于 First(A) 且 b 属于Follow(A) ,则 [A,b]那个位置为 0
				if (firstValues.contains("~")) {
					ArrayList<String> followValues = follows.get(i).getValue();
					if (followValues.contains(VT[j])) {
						table[i][j] = 0;
					}
				}
				// 出错管理
				// 如果栈顶为A 当前单词为a 查表为空,此时若a属于Follow(A),将A出栈
				// ERROR1:此时将查不到的位置置为-1
				// ERROR2:其余情况置为-2
				for (int i1 = 0; i1 < firsts.size(); i1++) {
					for (int j1 = 0; j1 < VT.length; j1++) {
						if (table[i1][j1] != 3) {
							continue;
						}
						ArrayList<String> followValues = follows.get(i1).getValue();
						if (followValues.contains(VT[j1])) {
							table[i1][j1] = -1;
						} else {
							table[i1][j1] = -2;
						}
					}
				}
			}
		}

		return table;
	}

}
