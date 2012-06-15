package munk.emesp.test;

import static munk.emesp.ExpressionParser.parse;
import static org.junit.Assert.fail;
import munk.emesp.Expression;
import munk.emesp.visitor.CollapseConstantsVisitor;

import org.junit.Test;

public class CollapseConstantsVisitorTest {

	private CollapseConstantsVisitor collapser = CollapseConstantsVisitor.getInstance();
	@Test
	public void testVisitPlusNode() {
		Expression collapsable = parse("0 + 2", null);
		Expression unCollapsable = parse("0 + x + 0", null);
		
		Expression collapsed = collapsable.accept(collapser);
		Expression nonCollapsed = unCollapsable.accept(collapser);
		
		System.out.println(collapsable);
		System.out.println(collapsed);
		System.out.println(unCollapsable);
		System.out.println(nonCollapsed);
	}

	@Test
	public void testVisitMinusNode() {
		fail("Not yet implemented");
	}

	@Test
	public void testVisitDivideNode() {
		fail("Not yet implemented");
	}

	@Test
	public void testVisitMultiplyNode() {
		fail("Not yet implemented");
	}

	@Test
	public void testVisitPowerNode() {
		fail("Not yet implemented");
	}

	@Test
	public void testVisitValueNode() {
		fail("Not yet implemented");
	}

	@Test
	public void testVisitVariableNode() {
		fail("Not yet implemented");
	}

	@Test
	public void testVisitFunctionNode() {
		fail("Not yet implemented");
	}

}
