package munk.emesp.node.function;


import munk.emesp.Expression;

public interface FunctionNode extends Expression, Cloneable{
	
	public String getName();
	public Iterable<Expression> getChildren();
	public void setNegate(boolean negate);
	public boolean isNegate();
	public int getNumberChildren();
	public void addChild(Expression child);
	public FunctionNode clone();
}
