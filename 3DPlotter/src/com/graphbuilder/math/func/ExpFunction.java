package com.graphbuilder.math.func;

import com.graphbuilder.math.*;

/**
The exp function.

@see java.lang.Math#exp(double)
*/
public class ExpFunction extends AbstractOneVariableFunction {

	public ExpFunction() {}

	/**
	Returns Euler's number, <i>e</i>, raised to the exponent of the value at index location 0.
	*/
	public double of(double[] d, int numParam) {
		return Math.exp(d[0]);
	}

	/**
	Returns true only for 1 parameter, false otherwise.
	*/
	public boolean acceptNumParam(int numParam) {
		return numParam == 1;
	}

	public String toString() {
		return "exp(x)";
	}

	@Override
	public Expression getOuterDerivative(Expression inner, FuncMap f, String varName) {
		FuncNode exp = new FuncNode("exp", false);
		exp.add(inner);
		
		return exp;
	}
}