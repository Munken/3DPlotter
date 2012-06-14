package munk.mesp.node.function;

import munk.mesp.Expression;
import munk.mesp.VariableValues;
import munk.mesp.node.values.ValueNode;

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
