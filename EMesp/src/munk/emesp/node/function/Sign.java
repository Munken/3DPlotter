package munk.emesp.node.function;

import munk.emesp.Expression;
import munk.emesp.VariableValues;
import munk.emesp.node.values.ValueNode;

public class Sign extends AbstractFunctionNode {

	public Sign() {
		this(false);
	}
	
	public Sign(boolean negate) {
		super("sign", negate);
	}
	
	public Sign(Expression inner, boolean negate) {
		this(negate);
		addChild(inner);
	}

	@Override
	public double eval(VariableValues varVal) {
		double val = getChild(0).eval(varVal);
		if (val > 0) return 1;
		if (val < 0) return -1;
		return 0;
	}

	@Override
	protected int nParameters() {
		return 1;
	}

	@Override
	public Expression getDerivative(String variable) {
		return ValueNode.ZERO;
	}

}
