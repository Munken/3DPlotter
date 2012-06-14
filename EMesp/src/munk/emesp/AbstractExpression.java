package munk.emesp;

import munk.emesp.node.function.FunctionNode;
import munk.emesp.node.operator.OperatorNode;
import munk.emesp.node.values.VariableNode;
import munk.emesp.struc.Bag;

/**
The class from which all nodes of an expression tree are descendents.  Expressions can be evaluated
using the eval method.  Expressions that are or have VarNodes as descendents must provide
a VarMap.  Expressions that consist entirely of OperatorNodes and ValNodes do not
require a VarMap or FuncMap.  For Expressions that support children (OpNodes, FuncNodes), a child can
only be accepted provided it currently has no parent, a cyclic reference is not formed, and it is
non-null.
*/
public abstract class AbstractExpression implements Expression {
	
	/**
	Returns the result of evaluating the expression tree rooted at this node.
	*/
	public abstract double eval(VariableValues varVal);
	
	/**
	 * Return the derivative of the expression with regards to variable
	 */
	public abstract Expression getDerivative(String variable);
	
	/**
	Returns an array of exact length of the variable names contained in the expression tree rooted at this node.
	*/
	public String[] getVariableNames() {
		return getTermNames(true);
	}
	
	/**
	Returns an array of exact length of the function names contained in the expression tree rooted at this node.
	*/
	public String[] getFunctionNames() {
		return getTermNames(false);
	}

	private String[] getTermNames(boolean varNames) {
		Bag b = new Bag();
		getTermNames(this, b, varNames);
		String[] arr = new String[b.size()];
		for (int i = 0; i < arr.length; i++)
			arr[i] = (String) b.get(i);
		return arr;
	}
	
	private static void getTermNames(Expression x, Bag b, boolean varNames) {
		if (x instanceof OperatorNode) {
			OperatorNode o = (OperatorNode) x;
			getTermNames(o.getLeftChild(), b, varNames);
			getTermNames(o.getRightChild(), b, varNames);
			
		}
		else if (varNames && x instanceof VariableNode) {
				VariableNode v = (VariableNode) x;
				if (!b.contains(v.getName()))
					b.add(v.getName());
		}
		else if (!varNames &&x instanceof FunctionNode) {
			FunctionNode f = (FunctionNode) x;

				if (!b.contains(f.getName()))
					b.add(f.getName());

			for (Expression child : f.getChildren())
				getTermNames(child, b, varNames);
		}
	}
	
	/**
	Returns a string that represents the expression tree rooted at this node.
	*/
	public String toString() {
		StringBuffer sb = new StringBuffer();
		toString(sb);
		return sb.toString();
	}
	
	public void ensureVariablesDefined(VariableValues map) throws UndefinedVariableException {
		String[] variables = getVariableNames();
		for (String var : variables) {
			if (!map.isDefined(var))
				throw new UndefinedVariableException("Variable is undefined: " + var);
		}
	}

}
