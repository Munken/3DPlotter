package munk.emesp.test;


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

}
