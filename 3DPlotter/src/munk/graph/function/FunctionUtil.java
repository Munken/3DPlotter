package munk.graph.function;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.media.j3d.*;
import javax.vecmath.Color3f;

import munk.emesp.exceptions.IllegalExpressionException;
import munk.graph.function.implicit.ImplicitSlowFunction;

import com.graphbuilder.math.ExpressionParseException;
import com.graphbuilder.math.UndefinedVariableException;

public class FunctionUtil {

	private static Pattern LHS_RHS = Pattern.compile("(^.*)=(.*$)");
	private static Pattern SEPARATION = Pattern.compile("^ *([xyz]) *=(?:(?!(?:(\\1|=))).)*$");
	
	public static BranchGroup setApperancePackInBranchGroup(Color3f color, Shape3D shape, Node handle) {
//		shape.setAppearance(new ColorAppearance(color));
		shape.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);

		BranchGroup bg = new BranchGroup();
		bg.setCapability(BranchGroup.ALLOW_DETACH);
		bg.addChild(handle);
		bg.compile();
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
	 * @throws IllegalEquationException The non-parametric expressions must be of the form \<Expression\> = \<Expression\>
	 * @throws UndefinedVariableException 
	 */
	public static Function createFunction(String[] expressions, Color3f color,
										String[] bounds, float[] stepSizes) 
												throws IllegalExpressionException {
		
		Function result = null;
		
		if (expressions.length == 1) {
			String expr = expressions[0];

			if (isXYZExpression(expr)) {
				result = new XYZFunction(expressions, color, bounds, stepSizes);
			} 
			else{
				//			result = new ImplicitMultiFunction(expressions, color, bounds, stepSize);
				result = new ImplicitSlowFunction(expressions, color, bounds, stepSizes);
			}
		} else {
			result = new ParametricFunction(expressions, color, bounds, stepSizes);
		}
		
		return result;
	}

	public static Function loadFunction(ZippedFunction zip) throws IllegalExpressionException {
		Function result = createFunction(zip.getExpression(), zip.getColor(), zip.getBounds(), zip.getStepsize());
		result.setSelected(zip.isSelected());
		result.setVisible(zip.isVisible());
		result.setFastImplicit(zip.getFastImplicit());
		result.setView(zip.getState());
		return result;
	}
	
	private static boolean isXYZExpression(String expr) {
		Matcher m = SEPARATION.matcher(expr);
		
		for (int i = 0; i < 2; i++) {
			
			if (m.matches()) {
				return true;
			} else if (i == 0){
				m = LHS_RHS.matcher(expr);
				if (m.matches()) {
					String input = m.group(2) + "=" + m.group(1);
					m = SEPARATION.matcher(input);
				}
			}
		}
		
		return false;
	}
	

	
	public static ZippedFunction[] zipFunctionList(List<Function> list){
		ZippedFunction[] zippedList = new ZippedFunction[list.size()];
		for(int i = 0; i < list.size(); i++){
			Function f = list.get(i);
			zippedList[i] = new ZippedFunction(f.getExpression(), f.getColor(), f.getBoundsString(), f.getStepsize(), f.isSelected(), f.isVisible(), f.getView(), f.getFastImplicit());
		}
		return zippedList;
	}
}
