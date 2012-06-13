package com.graphbuilder.math.func;

import com.graphbuilder.math.*;

/**
The natural logarithm function.

@see java.lang.Math#log(double)
*/
public class LnFunction extends AbstractOneVariableFunction {

	public LnFunction() {}

	/**
	Returns the natural logarithm of the value at index location 0.
	*/
	public double of(double[] d, int numParam) {
		return java.lang.Math.log(d[0]);
	}

	/**
	Returns true only for 1 parameter, false otherwise.
	*/
	public boolean acceptNumParam(int numParam) {
		return numParam == 1;
	}

	public String toString() {
		return "ln(x)";
	}

	@Override
	public Expression getOuterDerivative(Expression inner, FuncMap f, String varName) {
		return new DivNode(Expression.ONE, inner);
	}
}