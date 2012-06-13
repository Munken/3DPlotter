package com.graphbuilder.math.func;

import com.graphbuilder.math.Expression;
import com.graphbuilder.math.FuncMap;

/**
The mod function.
*/
public class ModFunction implements Function {

	public ModFunction() {}

	/**
	Returns the value of x % y, where x = d[0] and y = d[1].  More precisely, the value returned is
	x minus the value of x / y, where x / y is rounded to the closest integer value towards 0.
	*/
	public double of(double[] d, int numParam) {
		return d[0] % d[1];
	}

	/**
	Returns true only for 2 parameters, false otherwise.
	*/
	public boolean acceptNumParam(int numParam) {
		return numParam == 2;
	}

	public String toString() {
		return "mod(x, y)";
	}

	@Override
	public Expression getDerivative(Expression inner, FuncMap f, String varName) {
		return Expression.ZERO;
	}
}