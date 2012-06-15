package munk.emesp.test;

import munk.emesp.*;

public class Test {


	public static void main(String[] args) throws ExpressionParseException {
		String expr = "sin(x) + cos(x)*tan(x)";
		double max = 10E5;

		long start = System.currentTimeMillis();
		FunctionMap fm2 = new FunctionMap();
		fm2.loadDefaultFunctions();
		
		munk.emesp.Expression newEx = ExpressionParser.parse(expr, fm2);
		
		VariableValues vm2 = new VariableValues();
		
		for (double i = 0; i < max; i++) {
			vm2.setValue("x", i);
			
			newEx.eval(vm2);
		}
		
		System.out.println(System.currentTimeMillis() - start);
		System.out.println(newEx.getDerivative("x"));

	}


}