package munk.graph.function;

import munk.graph.appearance.Colors;

import com.graphbuilder.math.ExpressionParseException;

public class TemplateFunction extends AbstractFunction {
	
	public TemplateFunction(String[] bounds, float[] stepSize) throws ExpressionParseException {
		super(new String[]{""}, Colors.RED, bounds, stepSize, null);
	}

	public void setView(FILL state){
		super.state = state;
	}

}
