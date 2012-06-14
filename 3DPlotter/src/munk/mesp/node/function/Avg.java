package munk.mesp.node.function;

import munk.mesp.Expression;
import munk.mesp.VariableValues;

public class Avg extends AbstractFunctionNode {

	
	public Avg() {
		this(false);
	}
	
	public Avg(boolean negate) {
		super("avg", negate);
	}

	@Override
	protected int nParameters() {
		return Integer.MAX_VALUE;
	}

	@Override
	public double eval(VariableValues varVal) {
		double sum = 0;
		for (Expression child : getChildren()) {
			sum += child.eval(varVal);
		}
		
		return sum / getNumberChildren();
	}

	@Override
	public Expression getDerivative(String variable) {
		throw new UnsupportedOperationException("The derivative of avg is not defined");
	}

}
