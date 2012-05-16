package munk.graph.function;

import munk.graph.appearance.Colors;
import munk.graph.gui.ColorList;

import com.graphbuilder.math.ExpressionParseException;

public class TemplateFunction extends AbstractFunction {
	
	public TemplateFunction(String[] bounds, float[] stepSize, ColorList colorList) throws ExpressionParseException {
		super(new String[]{""}, Colors.RED, bounds, stepSize, null);
	}

	public void setView(FILL state){
		super.state = state;
	}

}
