package com.graphbuilder.math.func;

import com.graphbuilder.math.Expression;
import com.graphbuilder.math.FuncMap;

/**
The random function.

@see java.lang.Math#random()
*/
public class RandFunction implements Function {

	public RandFunction() {}

	/**
	Returns a random value in the range [0, 1) that does not depend on the input.
	*/
	public double of(double[] d, int numParam) {
		return java.lang.Math.random();
	}

	/**
	Returns true only for 0 parameters, false otherwise.
	*/
	public boolean acceptNumParam(int numParam) {
		return numParam == 0;
	}

	public String toString() {
		return "rand()";
	}

	@Override
	public Expression getDerivative(Expression inner, FuncMap f, String varName) {
		return Expression.ZERO;
	}
}