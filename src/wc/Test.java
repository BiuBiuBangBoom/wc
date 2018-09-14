package wc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Pattern;

public class Test {
	public static void main(String[] args) throws IOException {
		int emptyLineCount = 0;
		int codeLineCount = 0;
		int comLineCount = 0;
		File file = new File("D:\\\\123.java");
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line;
		while((line = br.readLine()) != null) {
			if(Pattern.compile("^\\s*\\S?\\s*$").matcher(line).matches())
				emptyLineCount++;
			else if(line.contains("//"))
				comLineCount++;
			else
				codeLineCount++;
		}
		System.out.println("空行:" + emptyLineCount);
		System.out.println("代码行:" + codeLineCount);
		System.out.println("注释行:" + comLineCount);
		br.close();
		return;
}
}
