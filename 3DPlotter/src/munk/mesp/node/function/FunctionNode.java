package munk.mesp.node.function;


import munk.mesp.Expression;

public interface FunctionNode extends Expression {
	
	public String getName();
	public Iterable<Expression> getChildren();
}
