package munk.graph.function;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.media.j3d.*;
import javax.vecmath.Color3f;

import munk.graph.appearance.ColorAppearance;
import munk.graph.gui.Plotter3D;
import munk.graph.plot.*;

import com.graphbuilder.math.*;

public class FunctionUtil {

	private static Pattern PATTERN = Pattern.compile("( *([xyz]) *=([^=]+)$)|(([^=]+)= *([xyz]) *)");
	
	public static BranchGroup setApperancePackInBranchGroup(Color3f color, Shape3D shape, Node handle) {
		shape.setAppearance(new ColorAppearance(color));
		shape.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);

		BranchGroup bg = new BranchGroup();
		bg.setCapability(BranchGroup.ALLOW_DETACH);
		bg.addChild(handle);

		return bg;
	}

	public static String[] expressionArray(String expr) {
		String[] result = {expr};
		return result;
	}

	/**
	 * Determine function type and return the correct function.
	 * @param expressions
	 * @param color
	 * @param bounds
	 * @param stepsize
	 * @return
	 * @throws ExpressionParseException 
	 */
	public static Function createFunction(String[] expressions, Color3f color,
										float[] bounds, float stepsize) throws ExpressionParseException{
		if (expressions.length == 3) {
			return new ParametricFunction(expressions, color, bounds, stepsize);
		} 
		
		String expr = expressions[0];
		Matcher m = PATTERN.matcher(expr);
		
		Function result = null;
		if (m.matches()) {
			result = new XYZFunction(expressions, color, bounds, stepsize);
		} else {
			result = new ImplicitFunction(expr, color, bounds, stepsize);
		}
		
		
		for (int i = 0; i < bounds.length; i+=2) {
			if (bounds[i] > bounds[i+1]) {
				float tmp = bounds[i+1];
				bounds[i+1] = bounds[i];
				bounds[i] = tmp;
			}
		}
		
		return result;
	}
	
	public static Function createFunction(String expression, Color3f color, 
										float[] bounds, float stepsize) throws ExpressionParseException {
		String[] expressions = {expression};
		return createFunction(expressions, color, bounds, stepsize);
	}

	public static String[] variableNames (String[] expressions) {
		Set<String> variables = new HashSet<String>();
		
		for (String ex : expressions) {
			Expression n = ExpressionTree.parse(ex);
			
			String[] variableNames = n.getVariableNames();
			for (String var : variableNames) {
				variables.add(var);
			}
		}
		
		String[] varNames = new String[variables.size()];
		variables.toArray(varNames);
		return varNames;
	}
	
	public static ActionListener createActionListener(final Plotter3D plotter){
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Function source = (Function) e.getSource();
				plotter.showPlot(source);
			}
		};
	}
}
