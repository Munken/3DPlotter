package munk.emesp.test;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import junit.framework.Assert;
import munk.emesp.VariableValues;
import munk.emesp.node.values.ValueNode;
import munk.emesp.node.values.VariableNode;

import org.junit.Test;

public class ValueNodeTest {

	double[] values = {200, 192, 3423.212, 12312, 23131.2312};
	
	@Test
	public void valueNodeTest() {
		for (double val : values) {
			ValueNode value = new ValueNode(val);
			double eval = value.eval(null);
			
			Assert.assertEquals(eval, val, 10E-5);
		}
		
	}
	
	@Test 
	public void valueNodeEquality() {
		for (double val : values) {
			ValueNode value = new ValueNode(val);
			ValueNode value2 = new ValueNode(val);
			
			Assert.assertEquals(value, value2);
			Assert.assertFalse(value.equals(null));
		}
	}
	
	@Test
	public void variableNodeTest() {
		VariableValues vm = new VariableValues();
		
		VariableNode variable = new VariableNode("y");
		for (double val : values) {
			vm.setValue("y", val);
			
			double eval = variable.eval(vm);
			
			Assert.assertEquals(eval, val, 10E-5);
		}
		
		variable = new VariableNode("x", true);
		
		for (double val : values) {
			vm.setValue("x", val);
			
			double eval = variable.eval(vm);
			
			Assert.assertEquals(eval, -val, 10E-5);
		}
	}
	
	@Test 
	public void variableNodeEquality() {
		VariableNode var1 = new VariableNode("x");
		VariableNode var2 = new VariableNode("y");
		VariableNode var3 = new VariableNode("x");
		
		assertEquals(var1, var3);
		assertFalse(var2.equals(var3));
	}

}
