package com.graphbuilder.math.func;

import com.graphbuilder.math.*;

/**
The absolute function.

@see java.lang.Math#abs(double)
*/
public class AbsFunction extends AbstractOneVariableFunction {

	public AbsFunction() {}

	/**
	Returns the positive value of the value stored at index location 0.
	*/
	public double of(double[] d, int numParam) {
		return Math.abs(d[0]);
	}

	/**
	Returns true only for 1 parameter, false otherwise.
	*/
	public boolean acceptNumParam(int numParam) {
		return numParam == 1;
	}

	public String toString() {
		return "abs(x)";
	}

	@Override
	public Expression getOuterDerivative(Expression inner, FuncMap f, String varName) {
		FuncNode sign = new FuncNode("sign", false);
		sign.add(inner);
		
		return sign;
	}
}