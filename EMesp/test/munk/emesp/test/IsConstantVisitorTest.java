package munk.emesp.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static munk.emesp.ExpressionParser.parse;
import munk.emesp.*;
import munk.emesp.exceptions.ExpressionParseException;
import munk.emesp.node.values.ValueNode;
import munk.emesp.node.values.VariableNode;
import munk.emesp.visitor.IsConstantVisitor;

import org.junit.Before;
import org.junit.Test;

public class IsConstantVisitorTest {

	IsConstantVisitor visitor = IsConstantVisitor.getInstance();
	FunctionMap fm;
	
	@Before
	public void init() {
		fm = new FunctionMap();
		fm.loadDefaultFunctions();
	}
	
	@Test
	public void testVisitPlusNode() throws ExpressionParseException {
		Expression constant = parse("2 + 2", null);
		Expression notConstant = parse("2 + x", null);
		
		assertTrue(constant.accept(visitor));
		assertFalse(notConstant.accept(visitor));
	}

	@Test
	public void testVisitMinusNode() throws ExpressionParseException {
		Expression constant = parse("2 - 2", null);
		Expression notConstant = parse("2 - x", null);
		
		assertTrue(constant.accept(visitor));
		assertFalse(notConstant.accept(visitor));
	}

	@Test
	public void testVisitDivideNode() throws ExpressionParseException {
		Expression constant = parse("2 / 2", null);
		Expression notConstant = parse("2 / x", null);
		
		assertTrue(constant.accept(visitor));
		assertFalse(notConstant.accept(visitor));
	}

	@Test
	public void testVisitMultiplyNode() throws ExpressionParseException {
		Expression constant = parse("2 * 2", null);
		Expression notConstantRight = parse("2 * x", null);
		Expression notConstantLeft = parse("x * 2", null);
		
		assertTrue(constant.accept(visitor));
		assertFalse(notConstantRight.accept(visitor));
		assertFalse(notConstantLeft.accept(visitor));
	}

	@Test
	public void testVisitPowerNode() throws ExpressionParseException {
		Expression constant = parse("2 ^ 2", null);
		Expression notConstant = parse("2 ^ x", null);
		
		assertTrue(constant.accept(visitor));
		assertFalse(notConstant.accept(visitor));
	}
	
	@Test
	public void testFunctionNode() throws ExpressionParseException {
		Expression constant = parse("sin(2 + 2 * (7 * 9 + 1))", fm);
		Expression notConstant = parse("sin(2 * x + 1)", fm);
		
		assertTrue(constant.accept(visitor));
		assertFalse(notConstant.accept(visitor));
		
		Expression max = parse("max(2, 5, 8)", fm);
		Expression maxNotConstant = parse("max(2, x, 8)", fm);
		
		assertTrue(max.accept(visitor));
		assertFalse(maxNotConstant.accept(visitor));
	}

	@Test
	public void testVisitValueNode() {
		ValueNode node = new ValueNode(2);
		
		assertTrue(node.accept(visitor));
	}

	@Test
	public void testVisitVariableNode() {
		VariableNode node = new VariableNode("x");
		
		assertFalse(node.accept(visitor));
	}
	
	@Test
	public void testCompoundExpression() throws ExpressionParseException {
		Expression constant = parse("2 * (2 + 3) / (2 + 3/2 * 89)", null);
		Expression notConstant = parse("2 * (2 + x) / (y + 3/2 * y)", null);
		
		assertTrue(constant.accept(visitor));
		assertFalse(notConstant.accept(visitor));
	}

}
