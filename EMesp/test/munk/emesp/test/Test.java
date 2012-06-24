package munk.emesp.test;

import munk.emesp.*;
import munk.emesp.exceptions.ExpressionParseException;
import munk.emesp.visitor.CollapseConstantsVisitor;

public class Test {


	public static void main(String[] args) throws ExpressionParseException {
		FunctionMap fm = new FunctionMap();
		fm.loadDefaultFunctions();
		Expression ex = ExpressionParser.parse("tan(u^2) + t" , fm);
		
		
		for (String str : ex.getVariableNames()) {
			System.out.println(str);
		}
	}


}