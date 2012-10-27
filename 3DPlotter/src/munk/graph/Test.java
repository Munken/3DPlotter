package munk.graph;

import java.io.IOException;
import java.util.List;

import javax.vecmath.Color3f;

import munk.emesp.exceptions.IllegalExpressionException;
import munk.graph.IO.XMLReader;
import munk.graph.IO.XMLWriter;
import munk.graph.function.ParametricFunction;
import munk.graph.function.XYZFunction;
import munk.graph.function.Function;

public class Test {
	/**
     * Our goal is to create a DOM XML tree and then print the XML.
	 * @throws IllegalExpressionException 
	 * @throws IOException 
     */
    public static void main (String args[]) throws IllegalExpressionException, IOException {
//        new Test();
    	
    	XMLReader reader = new XMLReader("C:/Users/Munk/Desktop/file.xml");
    	List<Function> f = reader.processFunctions();
    	
    	for (Function func : f) {
    		System.out.println(func);
    	}
    }

    public Test() {
    	
    	
        try {
            String[] formular = {"x=y"};
            Color3f colors = new Color3f(1,1,1);
            String[] bounds = {"0", "1*pi", "2", "3", "4", "5"};
            float[] stepsize = {0.1f, 0.1f, 0.1f};
            
            XYZFunction function = new XYZFunction(formular, colors, bounds, stepsize);

            
            String[] formular2 = {"2", "3*t", "4"};
            String[] bounds2 = {"0", "1*pi", "2", "3"};
            float[] stepsize2 = {0.1f, 0.1f};
            
            ParametricFunction p = new ParametricFunction(formular2, colors, bounds2, stepsize2);
            
        
            
            XMLWriter xml = new XMLWriter();
            
            xml.addStdFunction(function);
            xml.addParametricFunction(p);

            xml.output("C:/file.xml");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
