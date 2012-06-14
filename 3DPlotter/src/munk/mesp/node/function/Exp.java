package munk.mesp.node.function;

import munk.mesp.Expression;
import munk.mesp.VariableValues;

public class Exp extends DifferentialOneVariableFunctionNode {

	public Exp() {
		this(false);
	}
	
	public Exp(boolean negate) {
		super("exp", negate);
	}
	
	public Exp(Expression inner, boolean negate) {
		this(negate);
		addChild(inner);
	}

	@Override
	public double eval(VariableValues varVal) {
		double innerValue = getInner().eval(varVal);
		
		return Math.exp(innerValue);
	}

	@Override
	protected Expression getOuterDerivative(String variable) {
		return new Exp(getInner(), !isNegate());
	}



}
