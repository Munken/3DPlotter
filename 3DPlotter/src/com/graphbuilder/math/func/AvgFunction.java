package com.graphbuilder.math.func;

import com.graphbuilder.math.Expression;
import com.graphbuilder.math.FuncMap;

/**
The average function.
*/
public class AvgFunction implements Function {

	public AvgFunction() {}

	/**
	Returns the average of the values in the array from [0, numParam).
	*/
	public double of(double[] d, int numParam) {
		double sum = 0;

		for (int i = 0; i < numParam; i++)
			sum += d[i];

		return sum / numParam;
	}

	/**
	Returns true for 1 or more parameters, false otherwise.
	*/
	public boolean acceptNumParam(int numParam) {
		return numParam > 0;
	}

	public String toString() {
		return "avg(x1, x2, ..., xn)";
	}

	@Override
	public Expression getDerivative(Expression inner, FuncMap f, String varName) {
		return Expression.ZERO;
	}
}