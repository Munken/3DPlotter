package munk.mesp.node.operator;

import munk.mesp.Expression;
import munk.mesp.VariableValues;

public class MinusNode extends AbstractOperatorNode{

	public MinusNode(Expression leftChild, Expression rightChild) {
		super(leftChild, rightChild);
	}

	@Override
	public String getSymbol() {
		return "-";
	}

	@Override
	public double eval(VariableValues varVal) {
		double left = getLeftChild().eval(varVal);
		double right = getRightChild().eval(varVal);
		return left - right;
	}

	@Override
	public Expression getDerivative(String variable) {
		Expression left = getLeftChild().getDerivative(variable);
		Expression right = getRightChild().getDerivative(variable);
		
		return new MinusNode(left, right);
	}

}
