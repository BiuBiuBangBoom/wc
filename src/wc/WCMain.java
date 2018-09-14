package wc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WCMain {
	private List<File> files = null;

	public static void main(String[] args) throws IOException {
		WCMain wc = new WCMain();
		Scanner sc = new Scanner(System.in);
		System.out.println("程序使用参数：");
		System.out.println("-c <文件名> ，统计文件字符数。");
        System.out.println("-w <文件名> ，统计文件单词数。");
        System.out.println("-l <文件名> ，统计文件行数数。");
        System.out.println("-s <文件路径> ，递归处理目录下的文件。");
        System.out.println("-a <文件名> ，统计文件代码行，空白行，注释行。");
        System.out.println("使用举例：");
        System.out.println("wc.exe -c -w -l -a D:\\111.java 或者 wc.exe -s D:\\123");
		while (sc.hasNextLine()) {
			String com = sc.nextLine();
			if (com.equals("exit"))
				break;
			String reg = "^wc\\.exe\\s(-[acwl]\\s){1,4}[A-Z]:\\\\\\\\[^\\s]*\\.java$|^wc\\.exe\\s-s\\s[A-Z]:\\\\\\\\[^\\s]*";//用正则检验输入格式
			Pattern pa = Pattern.compile(reg);
			Matcher m = pa.matcher(com);
			if (!m.matches())
				System.out.println("输入格式错误，请重新输入");
			else {
				int index = com.lastIndexOf(" ");
				String filepath = com.substring(index + 1);//解析输入的文件地址参数
				File f = new File(filepath);
				if (f.exists()) {
					if (f.isDirectory()) {
						wc.files = new ArrayList<>();
						wc.getFolders(filepath);
						if(wc.files.isEmpty()) {
							System.out.println("目录内无文件符合");
							continue;
						}
						for (File aFile : wc.files) {
							System.out.println("**********************************");
							System.out.println("文件名:" + aFile.getName());//执行-s操作
							String aFilePath = aFile.getAbsolutePath();
							wc.wordsCount(aFilePath);
							wc.charCount(aFilePath);
							wc.linesCount(aFilePath);
							wc.advancedCount(aFilePath);
						}
					} else {
						System.out.println("**********************************");
						System.out.println("文件名:" + f.getName());
						if (Pattern.compile("-w").matcher(com).find())
							wc.wordsCount(filepath);
						if (Pattern.compile("-c").matcher(com).find())
							wc.charCount(filepath);
						if (Pattern.compile("-l").matcher(com).find())
							wc.linesCount(filepath);
						if (Pattern.compile("-a").matcher(com).find())
							wc.advancedCount(filepath);
					}
				} else
					System.out.println("路径不存在");
			}
		}
		sc.close();
	}

	public void getFolders(String filepath) {
		File file = new File(filepath);
		File[] folders = file.listFiles();
		if (folders.length == 0)
			return;
		else
			for (File f : folders) {
				if (f.isDirectory())
					getFolders(f.getAbsolutePath());//递归文件夹
				else if (f.getName().endsWith("java"))
					files.add(f);
			}
	}

	public void wordsCount(String filepath) throws IOException {
		int count = 0;
		File file = new File(filepath);
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line;
		Pattern pa = Pattern.compile("[\u4e00-\u9fa5]");
		while((line = br.readLine()) != null) {
			String[] str = line.split("\\W+");
			for(String word : str) {
				if(!word.equals("") && !pa.matcher(word).find())//检查是否有中文或空字符
						count++;
			}
		}
		System.out.println("词数:" + count);
		br.close();
		return;
	}

	public void charCount(String filepath) throws IOException {
		int count = 0;
		File file = new File(filepath);
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line1, line2;
		while((line1 = br.readLine()) != null) {
			line2 = line1.replaceAll("\\s+", "");//将空白符删去
			count += line2.length();
		}
		System.out.println("字符数:" + count);
		br.close();
		return;
	}

	public void linesCount(String filepath) throws IOException {
		int count = 0;
		File file = new File(filepath);
		BufferedReader br = new BufferedReader(new FileReader(file));
		while(br.readLine() != null)
			count++;
		System.out.println("行数:" + count);
		br.close();
		return;
	}

	public void advancedCount(String filepath) throws IOException {
		int emptyLineCount = 0;
		int codeLineCount = 0;
		int comLineCount = 0;
		File file = new File(filepath);
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line;
		while((line = br.readLine()) != null) {
			if(Pattern.compile("^\\s*\\S?\\s*$").matcher(line).matches())//检验空行
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
