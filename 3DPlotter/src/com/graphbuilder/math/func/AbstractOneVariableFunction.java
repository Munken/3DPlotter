package com.graphbuilder.math.func;

import com.graphbuilder.math.*;

public abstract class AbstractOneVariableFunction implements Function {
	
	public Expression getDerivative(Expression inner, FuncMap fm, String varName) {
		return new MultNode(inner.getDerivative(fm, varName), getOuterDerivative(inner, fm, varName));
	}
	
	protected abstract Expression getOuterDerivative(Expression inner, FuncMap fm, String varName);

}
