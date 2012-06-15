package munk.emesp.node.operator;

import munk.emesp.Expression;
import munk.emesp.VariableValues;
import munk.emesp.node.values.ValueNode;
import munk.emesp.visitor.ExpressionVisitor;

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
	
	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visit(this);
	}

}
