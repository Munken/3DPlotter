package munk.graph;

import munk.mesp.*;
import munk.mesp.node.function.Sin;

import com.graphbuilder.math.*;
import com.graphbuilder.math.ExpressionParseException;





public class Test {


	public static void main(String[] args) throws ExpressionParseException {
		String expr = "sin(x) + cos(x)*tan(x)";
		double max = 10E5;

		long start = System.currentTimeMillis();
		com.graphbuilder.math.Expression old = ExpressionTree.parse(expr);
		VarMap vm = new VarMap();
		FuncMap fm = new FuncMap();
		fm.loadDefaultFunctions();
		
		for (double i = 0; i < max; i++) {
			vm.setValue("x", i);
			
			old.eval(vm, fm);
		}
		
		
		System.out.println(System.currentTimeMillis() - start);
		start = System.currentTimeMillis();
		FunctionMap fm2 = new FunctionMap();
		fm2.loadDefaultFunctions();
		
		munk.mesp.Expression newEx = ExpressionParser.parse(expr, fm2);
		
		VariableValues vm2 = new VariableValues();
		
		for (double i = 0; i < max; i++) {
			vm2.setValue("x", i);
			
			newEx.eval(vm2);
		}
		
		System.out.println(System.currentTimeMillis() - start);
		System.out.println(newEx.getDerivative("x"));

	}


}