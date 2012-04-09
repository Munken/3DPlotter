package munk.graph;

import org.nfunk.jep.*;

import com.graphbuilder.math.*;



import de.congrace.exp4j.*;




public class Test {


	public static void main(String[] args) throws UnknownFunctionException, UnparsableExpressionException, ParseException {
		String expr = "x^2 + y^2 - 1";
		Calculable calc = new ExpressionBuilder(expr)
		.withVariable("x", 0)
		.withVariable("y", 0)
		.build();
		
		double limit = 10E1;
		
		double[][] res = new double[(int) limit][2];
		long start = System.currentTimeMillis();
//		for (double x = 0; x < limit; x++) {
//			calc.setVariable("x", x);
//			calc.setVariable("y", x);
//
//			double result1 = calc.calculate();
//		}
//		
//		System.out.println(System.currentTimeMillis() - start);
//		
		start = System.currentTimeMillis();
		JEP jep = new JEP();
		jep.addStandardConstants();
		jep.addStandardFunctions();
		jep.addVariable("x", 0);
		jep.addVariable("y", 0);
		jep.addVariable("z", 0);
		Node node = jep.parse(expr);
		
		int count = 0;
		for (double x = 0; x < limit; x++) {
			jep.setVarValue("x", x);
			jep.setVarValue("y", x);
			
			double result = (double) jep.evaluate(node);
			res[count++][0] = result;
		}
		
		System.out.println(System.currentTimeMillis() - start);
		
		Expression ex = ExpressionTree.parse("cos(a)");
		
		VarMap vm = new VarMap();
		vm.setValue("x", 0);
		vm.setValue("y", 0);
		
		FuncMap fm = new FuncMap();
		fm.loadDefaultFunctions();
		
		count = 0;
		start = System.currentTimeMillis();
		for (double x = 0; x < limit; x++) {
			vm.setValue("x", x);
			vm.setValue("y", x);
			
			double result = ex.eval(vm, fm);
			res[count++][1] = result;
		}
		
		System.out.println(System.currentTimeMillis() - start);
		
		count = 0;
		for (int i = 0; i < res.length; i++) {
//			System.out.println(res[i][0] + "  " + res[i][1]);
			
			if (res[i][0] != res[i][1])
				count++;
		}
		System.out.println(count);
	}
	

}