package 预测分析表;

import java.util.ArrayList;

import domain.First;
import domain.Follow;
import domain.KeyValue;
import tools.Analyze;
import tools.GetFirst;
import tools.GetFollow;
import tools.GetGenerative;
import tools.GetTable;

public class main {

	// 定义非终结符集合[其中 ε 由 ~ 代替 , E'由 e 代替,T'由 t 代替]
	static String VN[] = { "E", "e", "T", "t", "F" };
	// 定义句子
	static String Fa[] = { "Te", "+Te", "~", "Ft", "*Ft", "~", "(E)", "i" };
	// 定义产生式
	static String source[] = { "E->Te", "e->+Te", "e->~", "T->Ft", "t->*Ft", "t->~", "F->(E)", "F->i" };
	// 定义终结符集合[其中 ε 由 ~ 代替 , E'由 e 代替]
//	static String VT[] = { "i", "+", "*", "(", ")", "#" };
	static String VT[] = { "i", "(", ")", "*", "+", "#" };

	public static void main(String[] args) {
		// 1. 绑定产生式
		new GetGenerative(source, Fa);
		ArrayList<KeyValue> generative = GetGenerative.getGenetative();
		printGenerative(generative);// 输出产生式以便于查看是否生成正确
		// 2. 构建First集合
		new GetFirst(generative, VN);
		ArrayList<First> firsts = GetFirst.getFirst();
		printFirst(firsts);// 输出First集合以便于查看是否生成正确
		// 3. 构建Follow集合
		new GetFollow(generative, VN, firsts);
		ArrayList<Follow> follows = GetFollow.getFollow();
		printFollow(follows);
		// 4. 构建预测分析表
		new GetTable(VT, firsts, follows);
		int[][] table = GetTable.getTable();
		printTable(table);// 输出预测分析表以便于查看是否生成正确

		// 5. 开始分析
		// 定义分析的句子
		String sentence = "i+i#";
		new Analyze(VN, VT, Fa, generative, firsts, follows, table, sentence);
		Analyze.analyze();
	}

	/**
	 * 以下方法目的为:输出产生式,first集合,follow集合
	 */

	public static void printGenerative(ArrayList<KeyValue> generative) {
		for (KeyValue keyValue : generative) {
			System.out.println(keyValue.getKey() + "->" + keyValue.getValue());
		}
	}

	public static void printFirst(ArrayList<First> firsts) {
		for (First items : firsts) {
			System.out.print("First(" + items.getKey() + ")={");
			for (String tempString : items.getValue()) {
				System.out.print(tempString + " ");
			}
			System.out.println("}");
		}
	}

	public static void printFollow(ArrayList<Follow> follows) {
		for (Follow items : follows) {
			System.out.print("Follow(" + items.getKey() + ")={");
			for (String tempString : items.getValue()) {
				System.out.print(tempString + " ");
			}
			System.out.println("}");
		}
	}

	public static void printTable(int[][] table) {
		for (String vt : VT) {
			System.out.print(vt + "\t");
		}
		System.out.println();
		for (int[] row : table) {
			for (int n : row) {
				System.out.print(n + "\t");
			}
			System.out.println();
		}
//		System.out.println(Arrays.deepToString(table));
	}

}
