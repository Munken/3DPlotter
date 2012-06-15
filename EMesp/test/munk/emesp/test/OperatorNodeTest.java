package munk.emesp.test;

import junit.framework.Assert;
import munk.emesp.node.operator.*;
import munk.emesp.node.values.ValueNode;

import org.junit.Test;

public class OperatorNodeTest {
	
	double left = 200;
	double right = 24.5;
	
	@Test
	public void divideNodeTest() {
		DivideNode div = new DivideNode(new ValueNode(left), new ValueNode(right));
		
		double value = div.eval(null);
		
		Assert.assertEquals(value, left/right, 10E-5);
	}
	
	@Test
	public void multiplyNodeTest() {
		MultiplyNode div = new MultiplyNode(new ValueNode(left), new ValueNode(right));
		
		double value = div.eval(null);
		
		Assert.assertEquals(value, left*right, 10E-5);
	}
	
	@Test
	public void plusNodeTest() {
		PlusNode div = new PlusNode(new ValueNode(left), new ValueNode(right));
		
		double value = div.eval(null);
		
		Assert.assertEquals(value, left+right, 10E-5);
	}
	
	@Test
	public void minusNodeTest() {
		MinusNode div = new MinusNode(new ValueNode(left), new ValueNode(right));
		
		double value = div.eval(null);
		
		Assert.assertEquals(value, left-right, 10E-5);
	}
	
	@Test
	public void powerNodeTest() {
		PowerNode div = new PowerNode(new ValueNode(left), new ValueNode(right));
		
		double value = div.eval(null);
		
		Assert.assertEquals(value, Math.pow(left, right), 10E-5);
	}

}
