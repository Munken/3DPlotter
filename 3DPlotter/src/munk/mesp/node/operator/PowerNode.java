package munk.mesp.node.operator;

import munk.mesp.Expression;
import munk.mesp.VariableValues;
import munk.mesp.node.values.ValueNode;

public class PowerNode extends AbstractOperatorNode{

	public PowerNode(Expression leftChild, Expression rightChild) {
		super(leftChild, rightChild);
	}

	@Override
	public String getSymbol() {
		return "^";
	}

	@Override
	public double eval(VariableValues varVal) {
		double inner = getLeftChild().eval(varVal);
		double exponent = getRightChild().eval(varVal);
		return Math.pow(inner, exponent);
	}

	@Override
	public Expression getDerivative(String variable) {
		MinusNode newPower = new MinusNode(getRightChild(), ValueNode.ONE); 
		PowerNode lowerPower = new PowerNode(getLeftChild(), newPower);
		
		MultiplyNode front = new MultiplyNode(getRightChild(), getLeftChild().getDerivative(variable));
		
		return new MultiplyNode(front, lowerPower);
	}

}
