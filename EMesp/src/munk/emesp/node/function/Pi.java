package munk.emesp.node.function;

import munk.emesp.Expression;
import munk.emesp.VariableValues;
import munk.emesp.node.values.ValueNode;

public class Pi extends AbstractFunctionNode{

	
	public Pi() {
		this(false);
	}
	
	public Pi(boolean negate) {
		super("pi", negate);
	}

	@Override
	protected int nParameters() {
		return 0;
	}

	@Override
	public double eval(VariableValues varVal) {
		return Math.PI;
	}

	@Override
	public Expression getDerivative(String variable) {
		return ValueNode.ZERO;
	}

}
