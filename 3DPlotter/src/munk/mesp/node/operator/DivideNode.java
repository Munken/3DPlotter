package munk.mesp.node.operator;

import munk.mesp.Expression;
import munk.mesp.VariableValues;
import munk.mesp.node.values.ValueNode;

public class DivideNode extends AbstractOperatorNode{

	public DivideNode(Expression leftChild, Expression rightChild) {
		super(leftChild, rightChild);
	}

	@Override
	public String getSymbol() {
		return "/";
	}

	@Override
	public double eval(VariableValues varVal) {
		double left = getLeftChild().eval(varVal);
		double right = getRightChild().eval(varVal);
		return left / right;
	}

	@Override
	public Expression getDerivative(String variable) {
		MultiplyNode topLeft = new MultiplyNode(getLeftChild().getDerivative(variable), getRightChild());
		MultiplyNode topRight = new MultiplyNode(getLeftChild(), getRightChild().getDerivative(variable));
		MinusNode nominator = new MinusNode(topLeft, topRight);
		
		PowerNode denominator = new PowerNode(getRightChild(), ValueNode.TWO);
		
		return new DivideNode(nominator, denominator);
	}

}
