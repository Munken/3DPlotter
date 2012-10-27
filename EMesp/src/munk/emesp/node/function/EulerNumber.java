package munk.emesp.node.function;

import munk.emesp.Expression;
import munk.emesp.VariableValues;
import munk.emesp.node.values.ValueNode;

public class EulerNumber extends AbstractFunctionNode{

	
	public EulerNumber() {
		this(false);
	}
	
	public EulerNumber(boolean negate) {
		super("e", negate);
	}

	@Override
	protected int nParameters() {
		return 0;
	}

	@Override
	public double eval(VariableValues varVal) {
		return Math.E;
	}

	@Override
	public Expression getDerivative(String variable) {
		return ValueNode.ZERO;
	}

}
