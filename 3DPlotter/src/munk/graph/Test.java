package munk.graph;

import java.util.regex.Matcher;
import java.util.regex.Pattern;




public class Test {

	private static Pattern PATTERN = Pattern.compile("([xyz]) *=([^=]+)$");
	public static void main(String[] args) {
		Matcher m = PATTERN.matcher("z = y");
		boolean matches = m.matches();
		
		System.out.println(matches);
	}
	

}