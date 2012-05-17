package munk.graph.plot.implicit;

import munk.graph.function.IllegalEquationException;

import com.graphbuilder.math.ExpressionParseException;
import com.graphbuilder.math.UndefinedVariableException;

public class XYZMulti extends ImplicitMulti {

	public XYZMulti(String expression, float xMin, float xMax, float yMin,
			float yMax, float zMin, float zMax, float[] stepsizes)
			throws ExpressionParseException, IllegalEquationException,
			UndefinedVariableException {
		super(expression, xMin, xMax, yMin, yMax, zMin, zMax, stepsizes);
		// TODO Auto-generated constructor stub
	}

	protected boolean validPosition(int x, int y, int z) {
		return (x >= 0 && x < xLength) && (y >= 0 && y < yLength) && (z >= 0 && z < zLength);
	}
	
}
