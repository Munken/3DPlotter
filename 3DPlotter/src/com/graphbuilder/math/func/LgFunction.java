package com.graphbuilder.math.func;

import com.graphbuilder.math.*;

/**
The log base 2 function.
*/
public class LgFunction extends AbstractOneVariableFunction {

	public LgFunction() {}

	/**
	Returns the log base 2 of the value at index location 0.
	*/
	public double of(double[] d, int numParam) {
		return java.lang.Math.log(d[0]) / java.lang.Math.log(2);
	}

	/**
	Returns true only for 1 parameter, false otherwise.
	*/
	public boolean acceptNumParam(int numParam) {
		return numParam == 1;
	}

	public String toString() {
		return "lg(x)";
	}

	@Override
	public Expression getOuterDerivative(Expression inner, FuncMap f, String varName) {
		ValNode nominator = new ValNode(1 / Math.log(2));
		
		
		return new DivNode(nominator, inner);
	}
	
	
}