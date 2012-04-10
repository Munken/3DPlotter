package munk.graph.function;

import static munk.graph.function.FunctionUtil.*;

import java.awt.event.ActionListener;

import javax.media.j3d.*;
import javax.vecmath.Color3f;

import munk.graph.plot.XYZPlotter;

import com.graphbuilder.math.ExpressionParseException;

/*
 * XYZ functions; to be evaluated directly by Mesp.
 */
public class XYZFunction extends AbstractFunction {

	private XYZPlotter fp;
	
	public XYZFunction(String expr, Color3f color, float[] bounds, float stepsize) throws ExpressionParseException{
		this(expressionArray(expr),color,bounds, stepsize);
	}
	
	public XYZFunction(String[] expr, Color3f color, float[] bounds, float stepsize) throws ExpressionParseException{
		super(expr,color,bounds, stepsize);
		String expression = expr[0];
		fp = new XYZPlotter(expression, bounds[0], bounds[1], bounds[2], bounds[3], stepsize);
	}
	
	@Override
	protected BranchGroup plot() {
		TransformGroup tg = fp.getPlot();
		Shape3D shape = fp.getShape();
		
		if (shape != null) {
			BranchGroup bg = setApperancePackInBranchGroup(getColor(), shape, tg);
			return bg;
		} else 
			return null;
	}
}
