package com.graphbuilder.math;

/**
A node of an expression tree, represented by the symbol "/".
*/
public class DivNode extends OpNode {

	public DivNode(Expression leftChild, Expression rightChild) {
		super(leftChild, rightChild);
	}

	/**
	Divides the evaluation of the left side by the evaluation of the right side and returns the result.
	*/
	public double eval(VarMap v, FuncMap f) {
		double a = leftChild.eval(v, f);
		double b = rightChild.eval(v, f);
		return a / b;
	}

	public String getSymbol() {
		return "/";
	}

	@Override
	public DivNode getDerivative(FuncMap f, String varName) {
		MultNode topLeft = new MultNode(leftChild.getDerivative(f, varName), rightChild);
		MultNode topRight = new MultNode(leftChild, rightChild.getDerivative(f, varName));
		SubNode top = new SubNode(topLeft, topRight);
		
		PowNode bottom = new PowNode(rightChild, TWO);
		
		return new DivNode(top, bottom);
	}
}
