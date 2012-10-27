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
		} else if (rightValue != null && rightValue == 0)
			return collapsedLeft;
		
		return new PlusNode(collapsedLeft, collapsedRight);
	}

	@Override
	public Expression visit(MinusNode node) {
		Expression left = node.getLeftChild();
		Expression right = node.getRightChild();
		Expression collapsedLeft = left.accept(this);
		Expression collapsedRight = right.accept(this);

		Double leftValue  = collapsedLeft.accept(constant);
		Double rightValue = collapsedRight.accept(constant);
		
		if (leftValue != null) {
			if (rightValue != null) {
				return new ValueNode(leftValue - rightValue);
			} else if (leftValue == 0)
				return collapsedRight;
		} else if (rightValue != null && rightValue == 0)
			return collapsedLeft;
		
		return new MinusNode(collapsedLeft, collapsedRight);
	}

	@Override
	public Expression visit(DivideNode node) {
		Expression left = node.getLeftChild();
		Expression right = node.getRightChild();
		
		Expression collapsedLeft = left.accept(this);
		Double leftValue  = collapsedLeft.accept(constant);
		if (leftValue != null && leftValue == 0)
			return ValueNode.ZERO;
		
		Expression collapsedRight = right.accept(this);
		Double rightValue = collapsedRight.accept(constant);
		
		if (leftValue != null && rightValue != null) {
			return new ValueNode(leftValue / rightValue);
		} else if (rightValue != null && rightValue == 1)
			return collapsedLeft;
		
		return new DivideNode(collapsedLeft, collapsedRight);
	}

	@Override
	public Expression visit(MultiplyNode node) {
		Expression left = node.getLeftChild();
		Expression right = node.getRightChild();
		
		Expression collapsedLeft = left.accept(this);
		Double leftValue  = collapsedLeft.accept(constant);
		if (leftValue != null && leftValue == 0)
			return ValueNode.ZERO;
		
		Expression collapsedRight = right.accept(this);
		Double rightValue = collapsedRight.accept(constant);
		if (rightValue != null && rightValue == 0)
			return ValueNode.ZERO;
		
		if (leftValue != null) {
			if (rightValue != null) {
				return new ValueNode(leftValue * rightValue);
			} else if (leftValue == 1)
				return collapsedRight;
		} else if (rightValue != null && rightValue == 1)
			return collapsedLeft;
		
		return new MultiplyNode(collapsedLeft, collapsedRight);
	}
	
	@Override
	public Expression visit(PowerNode node) {
		Expression left = node.getLeftChild();
		Expression right = node.getRightChild();
		
		Expression base = left.accept(this);
		Double baseValue  = base.accept(constant);
		if (baseValue != null) {
			if (baseValue == 0)
				return ValueNode.ZERO;
			else if (baseValue == 1)
				return ValueNode.ONE;
		}
		
		Expression exponent = right.accept(this);
		Double exponentValue = exponent.accept(constant);
		if (exponentValue != null) {
			if (exponentValue == 0) 
				return ValueNode.ONE;
			else if (exponentValue == 1)
				return base;
		}
		
		if (baseValue != null && exponentValue != null) 
				return new ValueNode(Math.pow(baseValue, exponentValue));
		
		return new PowerNode(base, exponent);
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
		
		boolean childAreConstant = true;
		for (Expression child : node.getChildren()) {
			
			childAreConstant = childAreConstant && child.accept(constant) != null;
		}
		// All children are constant. Just return the value
		if (childAreConstant)
			return new ValueNode(node.eval(null));
		
		
		// Collapse the children
		int i = 0;
		Expression[] collapsedChildren = new Expression[node.getNumberChildren()];	
		
		for (Expression child : node.getChildren()) {
			collapsedChildren[i] = child.accept(this);
		}
		
		FunctionNode collapsedFunction = node.clone();
		for (Expression newChildr : collapsedChildren)
			collapsedFunction.addChild(newChildr);
		
		return collapsedFunction;
	}

}
