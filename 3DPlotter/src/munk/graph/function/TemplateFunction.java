package munk.graph.function;

import munk.graph.gui.ColorList;

import com.graphbuilder.math.ExpressionParseException;

public class TemplateFunction extends AbstractFunction {
	
	public TemplateFunction(String[] bounds, float[] stepSize, ColorList colorList) throws ExpressionParseException {
		super(new String[]{""}, colorList.getNextAvailableColor(), bounds, stepSize, null);
	}

	public void setView(FILL state){
		super.state = state;
	}

}
