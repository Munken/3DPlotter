package munk.emesp.visitor;

import munk.emesp.Expression;
import munk.emesp.node.function.FunctionNode;
import munk.emesp.node.operator.*;
import munk.emesp.node.values.ValueNode;
import munk.emesp.node.values.VariableNode;

public class IsConstantVisitor implements ExpressionVisitor<Boolean>{

	public static IsConstantVisitor getInstance() {
		return INSTANCE;
	}
	
	private static final IsConstantVisitor INSTANCE = new IsConstantVisitor();
	private IsConstantVisitor() {}
	
	@Override
	public Boolean visit(PlusNode node) {
		return visitOperator(node);
	}

	@Override
	public Boolean visit(MinusNode node) {
		return visitOperator(node);
	}

	@Override
	public Boolean visit(DivideNode node) {
		return visitOperator(node);
	}

	@Override
	public Boolean visit(MultiplyNode node) {
		return visitOperator(node);
	}

	@Override
	public Boolean visit(PowerNode node) {
		return visitOperator(node);
	}
	
	private Boolean visitOperator(OperatorNode node) {
		return node.getLeftChild().accept(this) && node.getRightChild().accept(this);
	}

	@Override
	public Boolean visit(ValueNode node) {
		return Boolean.TRUE;
	}

	@Override
	public Boolean visit(VariableNode node) {
		return Boolean.FALSE;
	}

	@Override
	public Boolean visit(FunctionNode node) {
		for (Expression child : node.getChildren()) {
			if (!child.accept(this))
				return Boolean.FALSE;
		}
		
		return Boolean.TRUE;
	}

	
	
}
