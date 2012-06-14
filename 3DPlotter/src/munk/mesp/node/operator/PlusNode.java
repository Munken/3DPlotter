package munk.mesp.node.operator;

import munk.mesp.Expression;
import munk.mesp.VariableValues;

public class PlusNode extends AbstractOperatorNode {

	public PlusNode(Expression leftChild, Expression rightChild) {
		super(leftChild, rightChild);
	}

	@Override
	public double eval(VariableValues varVal) {
		return getLeftChild().eval(varVal) + getRightChild().eval(varVal);
	}

	@Override
	public Expression getDerivative(String variable) {
		Expression left = getLeftChild().getDerivative(variable);
		Expression right = getRightChild().getDerivative(variable);
		
		return new PlusNode(left, right);
	}

	@Override
	public String getSymbol() {
		return "+";
	}

}
