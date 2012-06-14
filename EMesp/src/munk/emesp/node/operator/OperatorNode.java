package munk.emesp.node.operator;

import munk.emesp.Expression;

public interface OperatorNode extends Expression {

	public Expression getLeftChild();
	public Expression getRightChild();
	public String getSymbol();
}
