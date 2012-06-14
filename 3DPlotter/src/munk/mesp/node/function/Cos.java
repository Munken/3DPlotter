package munk.mesp.node.function;

import munk.mesp.Expression;
import munk.mesp.VariableValues;

public class Cos extends DifferentialFunctionNode {

	public Cos() {
		this(false);
	}
	
	public Cos(boolean negate) {
		super("cos", negate);
	}
	
	public Cos(Expression inner, boolean negate) {
		this(negate);
		addChild(inner);
	}

	@Override
	protected int nParameters() {
		return 1;
	}

	@Override
	public double eval(VariableValues varVal) {
		Expression inner = getChild(0);
		double innerValue = inner.eval(varVal);
		
		return Math.cos(innerValue);
	}

	@Override
	protected Expression getOuterDerivative(String variable) {
		return new Sin(getChild(0), !isNegate());
	}



}
