package munk.emesp.exceptions;

@SuppressWarnings("serial")
public class UndefinedVariableException extends IllegalExpressionException {

	public UndefinedVariableException() {
	}
	
	public UndefinedVariableException(String message) {
		super(message);
	}
}
