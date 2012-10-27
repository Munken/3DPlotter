package munk.emesp.visitor;

import munk.emesp.node.function.FunctionNode;
import munk.emesp.node.operator.*;
import munk.emesp.node.values.ValueNode;
import munk.emesp.node.values.VariableNode;

public interface ExpressionVisitor<T> {

	public T visit(PlusNode node);
	public T visit(MinusNode node);
	public T visit(DivideNode node);
	public T visit(MultiplyNode node);
	public T visit(PowerNode node);
	public T visit(ValueNode node);
	public T visit(VariableNode node);
	public T visit(FunctionNode node);
	
}
