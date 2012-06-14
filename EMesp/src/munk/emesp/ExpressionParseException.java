package munk.emesp;

/**
Exception thrown if expression cannot be parsed correctly.
[Munk] Changed to a checked exception
[Munk] Parsing description to super constructor

@see com.graphbuilder.math.ExpressionTree
*/
@SuppressWarnings("serial")
public class ExpressionParseException extends RuntimeException {

	private String descrip;
	private int index = 0;

	public ExpressionParseException(String descrip, int index) {
		super(descrip);
		this.descrip = descrip;
		this.index = index;
	}

	public String getDescription() {
		return descrip;
	}

	public int getIndex() {
		return index;
	}

	public String toString() {
		return "(" + index + ") " + descrip;
	}
}
