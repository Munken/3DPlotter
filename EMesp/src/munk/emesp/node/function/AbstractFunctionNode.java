package munk.emesp.node.function;

import java.util.*;

import munk.emesp.AbstractExpression;
import munk.emesp.Expression;
import munk.emesp.visitor.ExpressionVisitor;

public abstract class AbstractFunctionNode extends AbstractExpression implements FunctionNode {

	private String name;
	private boolean negate = false;
	private List<Expression> children = new ArrayList<Expression>(1);
	
	public AbstractFunctionNode(String name, boolean negate) {
		this.name = name;
		this.negate = negate;
	}
	
	@Override
	public void toString(StringBuffer buffer) {

		if (isNegate()) {
			buffer.append("(");
			buffer.append("-");
		}

		buffer.append(getName());
		buffer.append("(");
		if (getNumberChildren() > 0)
			children.get(0).toString(buffer);

		for (int i = 1; i < getNumberChildren(); i++) {
			buffer.append(", ");
			children.get(i).toString(buffer);
		}

		buffer.append(")");

		if (isNegate())
			buffer.append(")");
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Iterable<Expression> getChildren() {
		return new Iterable<Expression>() {

			@Override
			public Iterator<Expression> iterator() {
				return children.iterator();
			}
		};
	}
	
	protected Expression getChild(int i) {
		return children.get(i);
	}

	@Override
	public void setNegate(boolean negate) {
		this.negate = negate;
	}
	
	public boolean isNegate() {
		return negate;
	}

	@Override
	public int getNumberChildren() {
		return children.size();
	}

	@Override
	public void addChild(Expression child) {
		if (children.size() + 1 <= nParameters()) {
			children.add(child);
		} else {
			throw new IllegalArgumentException(
					getName() + " only accepts " + nParameters() + " parameters.");
		}
	}
	
	protected abstract int nParameters();
	
	public FunctionNode clone() {
		try {
			AbstractFunctionNode node = (AbstractFunctionNode) super.clone();
			node.children = new ArrayList<Expression>(1);
			return node;
		} catch (CloneNotSupportedException e) {
			// Tada !
		}
		return null;
	}
	
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visit(this);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		
		if (this.getClass() == obj.getClass()) {
			AbstractFunctionNode node = (AbstractFunctionNode) obj;
			
			if (getNumberChildren() != node.getNumberChildren())
				return false;
			
			List<Expression> otherChildren = new ArrayList<Expression>(node.children);
			
			for (Expression child : children) {				
				int index = otherChildren.indexOf(child);
				
				if (index >= 0) {
					otherChildren.remove(index);
				} else {
					return false;
				}
			}
			return true;
		}
		return false;
	}

}
