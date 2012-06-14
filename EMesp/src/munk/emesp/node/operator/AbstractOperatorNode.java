package munk.emesp.node.operator;

import munk.emesp.*;

public abstract class AbstractOperatorNode extends AbstractExpression implements OperatorNode{
	
	private Expression leftChild;
	private Expression rightChild;
	
	public AbstractOperatorNode(Expression leftChild, Expression rightChild) {
		this.leftChild = leftChild;
		this.rightChild = rightChild;
	}

	@Override
	public Expression getLeftChild() {
		return leftChild;
	}

	@Override
	public Expression getRightChild() {
		return rightChild;
	}
	
	/**
	Returns the text symbol that represents the operation.
	*/
	public abstract String getSymbol();
	
	@Override
	public void toString(StringBuffer buffer) {
		buffer.append("(");
		getLeftChild().toString(buffer);
		buffer.append(getSymbol());
		getRightChild().toString(buffer);
		buffer.append(")");
	}

}
