package munk.graph;

import java.util.regex.Matcher;
import java.util.regex.Pattern;




public class Test {

	private static Pattern LHS_RHS = Pattern.compile("(^.*)=(.*$)");
	private static Pattern SEPARATION = Pattern.compile("^ *([xyz]) *=(?:(?!(?:(\\1|=))).)*$");
	
	private static Pattern PATTERN = Pattern.compile(" *([xyz]) *=([^=]+)$|([^=]+)= *([xyz]) *");
	
	public static void main(String[] args) {
		String str = "x + cos(z) + sin(z) = y";
		String var1 = "x";
		String var2 = "y";
		
		
		Matcher m = PATTERN.matcher(str);
		if (m.matches()) {
			String lhs = (m.group(1) != null) ? m.group(1).trim() : m.group(4);
			String rhs = (m.group(2) != null) ? m.group(2).trim() : m.group(3);
			
			if (lhs.equals("x")) {
				var1 = "z";
			}
			else if (lhs.equals("y")) {
				var2 = "z";
			}
			else {
			}
		}
		
		System.out.println(var1 + "  " + var2);
			
			
		
		
//		while (m.find()) {
//			System.out.println(m.group());
//		}
		
//		Matcher m = SEPARATION.matcher(str);
//		
//		for (int i = 0; i < 2; i++) {
//			
//			if (m.matches()) {
//				System.out.println("Succes " + i);
//				break;
//			} else if (i == 0){
//				m = LHS_RHS.matcher(str);
//				if (m.matches()) {
//					String input = m.group(2) + "=" + m.group(1);
//					m = SEPARATION.matcher(input);
//				}
//			} else {
//				break;
//			}
//		}
		
		
	}
	

}