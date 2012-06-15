package munk.emesp.visitor;

import munk.emesp.Expression;
import munk.emesp.node.function.FunctionNode;
import munk.emesp.node.operator.*;
import munk.emesp.node.values.ValueNode;
import munk.emesp.node.values.VariableNode;

public class GetConstantVisitor implements ExpressionVisitor<Double>{

	public static GetConstantVisitor getInstance() {
		return INSTANCE;
	}
	
	private static final GetConstantVisitor INSTANCE = new GetConstantVisitor();
	private GetConstantVisitor() {}
	
	@Override
	public Double visit(PlusNode node) {
		Double left = node.getLeftChild().accept(this);
		Double right = node.getRightChild().accept(this);
		
		return (left != null && right != null) ? left + right : null;
	}

	@Override
	public Double visit(MinusNode node) {
		Double left = node.getLeftChild().accept(this);
		Double right = node.getRightChild().accept(this);
		
		return (left != null && right != null) ? left - right : null;
	}

	@Override
	public Double visit(DivideNode node) {
		Double left = node.getLeftChild().accept(this);
		Double right = node.getRightChild().accept(this);
		
		return (left != null && right != null) ? left / right : null;
	}

	@Override
	public Double visit(MultiplyNode node) {
		Double left = node.getLeftChild().accept(this);
		Double right = node.getRightChild().accept(this);
		
		return (left != null && right != null) ? left * right : null;
	}

	@Override
	public Double visit(PowerNode node) {
		Double left = node.getLeftChild().accept(this);
		Double right = node.getRightChild().accept(this);
		
		return (left != null && right != null) ? left * right : null;
	}

	@Override
	public Double visit(ValueNode node) {
		return node.getValue();
	}

	@Override
	public Double visit(VariableNode node) {
		return null;
	}

	@Override
	public Double visit(FunctionNode node) {
		for (Expression child : node.getChildren()) {
			if (child.accept(this) == null)
				return null;
		}
		
		return node.eval(null);
	}

	
	
}
