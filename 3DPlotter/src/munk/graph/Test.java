package munk.graph;

import org.nfunk.jep.*;

import de.congrace.exp4j.*;




public class Test {


	public static void main(String[] args) throws UnknownFunctionException, UnparsableExpressionException, ParseException {
		String expr = "x^2 + y^2 - 1";
		Calculable calc = new ExpressionBuilder(expr)
		.withVariable("x", 0)
		.withVariable("y", 0)
		.build();
		
		double limit = 10E5;
		long start = System.currentTimeMillis();
		for (double x = 0; x < limit; x++) {
			calc.setVariable("x", x);
			calc.setVariable("y", x);

			double result1 = calc.calculate();
		}
		
		System.out.println(System.currentTimeMillis() - start);
		
		start = System.currentTimeMillis();
		JEP jep = new JEP();
		jep.addStandardConstants();
		jep.addStandardFunctions();
		jep.addVariable("x", 0);
		jep.addVariable("y", 0);
		jep.addVariable("z", 0);
		Node node = jep.parse(expr);
		
		for (double x = 0; x < limit; x++) {
			jep.setVarValue("x", x);
			jep.setVarValue("y", x);
			
			double result = (double) jep.evaluate(node);
		}
		
		System.out.println(System.currentTimeMillis() - start);
	}
	

}