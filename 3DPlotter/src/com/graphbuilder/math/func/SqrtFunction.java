package com.graphbuilder.math.func;

import com.graphbuilder.math.*;

/**
The square root function.

@see java.lang.Math#sqrt(double)
*/
public class SqrtFunction extends AbstractOneVariableFunction {

	public SqrtFunction() {}

	/**
	Returns the square root of the value at index location 0.
	*/
	public double of(double[] d, int numParam) {
		return java.lang.Math.sqrt(d[0]);
	}

	/**
	Returns true only for 1 parameter, false otherwise.
	*/
	public boolean acceptNumParam(int numParam) {
		return numParam == 1;
	}

	public String toString() {
		return "sqrt(x)";
	}

	@Override
	public Expression getOuterDerivative(Expression inner, FuncMap f, String varName) {
		ValNode nominator = new ValNode(0.5);
		
		FuncNode sqrt = new FuncNode("sqrt", false);
		sqrt.add(inner);
		
		return new DivNode(nominator, sqrt);
	}
}