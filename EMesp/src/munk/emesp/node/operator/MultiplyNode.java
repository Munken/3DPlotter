package munk.emesp.node.operator;

import munk.emesp.Expression;
import munk.emesp.VariableValues;

public class MultiplyNode extends AbstractOperatorNode {

	public MultiplyNode(Expression leftChild, Expression rightChild) {
		super(leftChild, rightChild);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getSymbol() {
		return "*";
	}

	@Override
	public double eval(VariableValues varVal) {
		double left = getLeftChild().eval(varVal);
		double right = getRightChild().eval(varVal);
		return left * right;
	}

	@Override
	public Expression getDerivative(String variable) {
		MultiplyNode left = new MultiplyNode(getLeftChild().getDerivative(variable), getRightChild());
		MultiplyNode right = new MultiplyNode(getLeftChild(), getRightChild().getDerivative(variable));
		return new PlusNode(left, right);
	}

}
