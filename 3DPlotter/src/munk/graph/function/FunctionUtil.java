package munk.graph.function;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.media.j3d.*;
import javax.vecmath.Color3f;

import munk.graph.function.implicit.ImplicitMultiFunction;
import munk.graph.gui.GuiUtil;

import com.graphbuilder.math.*;

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
	
	public static float[] determineIntersection(Function f1, Function f2) {
				
		float[] bound1 = f1.getBounds();
		float[] bound2 = f2.getBounds();
		
		float[] xBounds = determineIntersection(bound1[0], bound1[1], bound2[0], bound1[1]);
		float[] yBounds = determineIntersection(bound1[2], bound1[3], bound2[2], bound1[3]);
		float[] zBounds = determineIntersection(bound1[4], bound1[5], bound2[4], bound1[5]);
		
		if (xBounds != null && yBounds != null && zBounds != null) {
			
			return new float[] 
					{xBounds[0], xBounds[1], yBounds[0], yBounds[1], zBounds[0], zBounds[1]};
		} else 
			return null;
	}
	
	private static float[] determineIntersection(float f1Min, float f1Max, float f2Min, float f2Max) {
//		if (f1Max >= f2Min)
//			return null;
//		
		return new float[] {Math.max(f1Min, f2Min), Math.min(f1Max, f2Max)};
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
										String[] bounds, float stepSize[]) 
												throws ExpressionParseException, IllegalEquationException, UndefinedVariableException{
		if (expressions.length == 3) {
			return new ParametricFunction(expressions, color, bounds, stepSize);
		} 
		
		String expr = expressions[0];
		
		Function result = null;
		if (isXYZExpression(expr)) {
			result = new XYZFunction(expressions, color, bounds, stepSize);
		} else {
			result = new ImplicitMultiFunction(expressions, color, bounds, stepSize);
		}
		
		for (int i = 0; i < bounds.length; i+=2) {
			if (GuiUtil.evalString(bounds[i]) > GuiUtil.evalString(bounds[i+1])) {
				String tmp = bounds[i+1];
				bounds[i+1] = bounds[i];
				bounds[i] = tmp;
			}
		}
		
		return result;
	}

	public static Function loadFunction(ZippedFunction zip) throws ExpressionParseException, IllegalEquationException, UndefinedVariableException{
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
	
	public static String[] variableNames (String[] expressions) throws ExpressionParseException {
		Set<String> variables = new HashSet<String>();
		
		for (String ex : expressions) {
			
			if (!ex.equals("")) {
				Expression n = ExpressionTree.parse(ex);
				String[] variableNames = n.getVariableNames();
				for (String var : variableNames) {
					variables.add(var);
				}
			} else {
				throw new ExpressionParseException("You must specify all coordinates", -1);
			}
		}
		
		String[] varNames = new String[variables.size()];
		variables.toArray(varNames);
		return varNames;
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
