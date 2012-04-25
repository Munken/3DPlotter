package munk.graph.function;

import javax.vecmath.Color3f;

import munk.graph.appearance.Colors;

import com.graphbuilder.math.ExpressionParseException;

public class TemplateFunction extends AbstractFunction {

	public TemplateFunction(String[] bounds, float[] stepSize) throws ExpressionParseException {
		super(new String[]{""}, new Color3f(0,0,0), bounds, stepSize, null);
	}
	
	@Override
	public Color3f getColor() {
		return Colors.RED;
	}

}
