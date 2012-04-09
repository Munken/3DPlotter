package munk.graph;

import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class Test {


	public static void main(String[] args) {
		Pattern PATTERN = Pattern.compile("([^=]+)=([^=]+)$");
		Matcher m = PATTERN.matcher("x^2 + cos(x) + sin(y) = y");
		boolean matches = m.matches();
		
		System.out.println(matches);
		System.out.println(m.group(1) + " || " + m.group(2));
		
		String result = m.group(1) + "-(" + m.group(2) + ")";
		System.out.println(result);
	}
	

}