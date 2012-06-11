package munk.graph;

import munk.graph.appearance.Colors;
import munk.graph.function.IllegalEquationException;
import munk.graph.function.implicit.ImplicitMultiFunction;
import munk.graph.function.implicit.ImplicitSlowFunction;

import com.graphbuilder.math.ExpressionParseException;
import com.graphbuilder.math.UndefinedVariableException;


public class Test {
	
	
	public static void main(String[] args) throws ExpressionParseException, InterruptedException, IllegalEquationException, UndefinedVariableException {
		
		ImplicitMultiFunction m = new ImplicitMultiFunction(new String[] {"x^2 + y^2 + z^3= 1.5"}, 
				Colors.BLUE, new String[] {"-2","2","-2","2","-2","2"}, new float[] {0.1f, 0.1f, 0.1f});
		
		
		ImplicitSlowFunction isf = new ImplicitSlowFunction(new String[] {"x^2 + y^2 + z^2 = 1"}, 
				Colors.BLUE, new String[] {"-1","1","-1","1","-1","1"}, new float[] {0.05f, 0.05f, 0.05f});
		
		
		
		long start = System.currentTimeMillis();
//		p.plotFunction(isf);
		isf.getPlot();
		System.out.println(System.currentTimeMillis() - start);
		

	}


}