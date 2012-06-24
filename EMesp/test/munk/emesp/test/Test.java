package munk.emesp.test;

import munk.emesp.*;
import munk.emesp.visitor.CollapseConstantsVisitor;

public class Test {


	public static void main(String[] args) throws ExpressionParseException {
		FunctionMap fm = new FunctionMap();
		fm.loadDefaultFunctions();
		Expression ex = ExpressionParser.parse("(y - x - z^3)*(cos(y*x) * tan(x))" , fm);
		Expression deriv = ex.getDerivative("x");
		Expression simple = deriv.accept(CollapseConstantsVisitor.getInstance());
		
		System.out.println(ex);
		System.out.println(deriv);
		System.out.println(simple);
	}


}