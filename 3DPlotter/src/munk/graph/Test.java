package munk.graph;

import java.util.regex.Matcher;
import java.util.regex.Pattern;




public class Test {

	private static Pattern PATTERN = Pattern.compile("( *([xyz]) *=([^=]+)$)|(([^=]+)= *([xyz]) *)");
	public static void main(String[] args) {
		
		
		Matcher m = PATTERN.matcher("2 + cos(x) = x");
		
		System.out.println(m.matches());
		
	}
	

}