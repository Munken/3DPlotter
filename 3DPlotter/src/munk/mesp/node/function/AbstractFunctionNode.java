package munk.mesp.node.function;

import java.util.*;

import munk.mesp.AbstractExpression;
import munk.mesp.Expression;

public abstract class AbstractFunctionNode extends AbstractExpression implements FunctionNode {

	private String name;
	private boolean negate = false;
	private List<Expression> children = new ArrayList<Expression>(nParameters());
	
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
			node.children = new ArrayList<Expression>(node.nParameters());
			return node;
		} catch (CloneNotSupportedException e) {
			// Tada !
		}
		return null;
	}

}
