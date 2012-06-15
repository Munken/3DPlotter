package munk.emesp.visitor;

import munk.emesp.Expression;
import munk.emesp.node.function.FunctionNode;
import munk.emesp.node.operator.*;
import munk.emesp.node.values.ValueNode;
import munk.emesp.node.values.VariableNode;

public class CollapseConstantsVisitor implements ExpressionVisitor<Expression>{
	
	public static CollapseConstantsVisitor getInstance() {
		return INSTANCE;
	}
	
	private static final CollapseConstantsVisitor INSTANCE = new CollapseConstantsVisitor();
	private CollapseConstantsVisitor() {}
	private GetConstantVisitor constant = GetConstantVisitor.getInstance();
	
	@Override
	public Expression visit(PlusNode node) {
		Expression left = node.getLeftChild();
		Expression right = node.getRightChild();
		Expression collapsedLeft = left.accept(this);
		Expression collapsedRight = right.accept(this);

		Double leftValue  = collapsedLeft.accept(constant);
		Double rightValue = collapsedRight.accept(constant);
		
		if (leftValue != null) {
			if (rightValue != null) {
				return new ValueNode(leftValue + rightValue);
			} else if (leftValue == 0)
				return collapsedRight;
		} else if (rightValue == 0)
			return collapsedLeft;
		
		return new PlusNode(collapsedLeft, collapsedRight);
	}

	@Override
	public Expression visit(MinusNode node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Expression visit(DivideNode node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Expression visit(MultiplyNode node) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Expression visit(PowerNode node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Expression visit(ValueNode node) {
		return node;
	}

	@Override
	public Expression visit(VariableNode node) {
		return node;
	}

	@Override
	public Expression visit(FunctionNode node) {
		// TODO Auto-generated method stub
		return null;
	}

}
