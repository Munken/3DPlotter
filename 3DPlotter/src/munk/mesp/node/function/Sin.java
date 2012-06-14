package munk.mesp.node.function;

import munk.mesp.Expression;
import munk.mesp.VariableValues;

public class Sin extends AbstractFunctionNode {

	public Sin() {
		this(false);
	}
	
	public Sin(boolean negate) {
		super("sin", negate);
	}
	
	public Sin(Expression inner, boolean negate) {
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
		
		return Math.sin(innerValue);
	}

	@Override
	public Expression getDerivative(String variable) {
		return new Cos(getChild(0), isNegate());
	}

}