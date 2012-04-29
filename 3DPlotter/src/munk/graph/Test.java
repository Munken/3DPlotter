package munk.graph;

import java.util.Arrays;

import munk.graph.appearance.Colors;
import munk.graph.function.*;
import munk.graph.plot.intersection.ImplicitIntersection;

import com.graphbuilder.math.ExpressionParseException;
import com.graphbuilder.math.UndefinedVariableException;



public class Test {
	
	public static void main(String[] args) throws ExpressionParseException, IllegalEquationException, UndefinedVariableException {
		
		Function f1 = FunctionUtil.createFunction(new String[] {"y = cos(x)"}, Colors.BLUE, 
				new String[] {"-1", "1", "-1", "1", "-1", "1"}, new float[] {0.2f, 0.2f, 0.2f});
		
		Function f2 = FunctionUtil.createFunction(new String[] {"y = x"}, Colors.BLUE, 
				new String[] {"-2", "1", "-2", "1", "-2", "1"}, new float[] {0.2f, 0.2f, 0.2f});
		
		
		ImplicitIntersection it = new ImplicitIntersection(f1, f2);
		
		it.plot();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}