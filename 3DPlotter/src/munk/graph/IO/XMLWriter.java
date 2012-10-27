package munk.graph.IO;

import java.io.*;

import javax.vecmath.Color3f;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import munk.graph.function.*;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XMLWriter {
	
	private Document doc;
	private Element root;
	private static final String[] AXES = {"x", "y", "z"};
	private static final String[] PARAMETERS = {"t", "u"};
	
	public XMLWriter() {
		try {
	        DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
	        DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
			doc = docBuilder.newDocument();
			
            //create the root element and add it to the document
            root = doc.createElement("functions");
            doc.appendChild(root);
					
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void addStdFunction(XYZFunction output) {
		Element function = doc.createElement("function");
		function.setAttribute("type", "std");

		// Append formular
		Element formular = doc.createElement("equation");
		function.appendChild(formular);
		
		// Append equations
		Element f = createExpression("f", output.toString());
		formular.appendChild(f);
		
		
		// Append color
		function.appendChild(createColor(output));
		
		// Add stepsizes and bounds
		Element bounds = doc.createElement("bounds");
		function.appendChild(bounds);
		
		Element stepsize = doc.createElement("stepsize");
		function.appendChild(stepsize);		
		
		String[] functionBounds = output.getBoundsString();
		float[] stepsizes = output.getStepsize();
		for (int i = 0; i < stepsizes.length; i++) {
			String variable = AXES[i];
			
			bounds.appendChild(createBound(variable, functionBounds[2*i], functionBounds[2*i+1]));
			stepsize.appendChild(createStepsize(variable, stepsizes[i]));
		}
		
		
		
		
		root.appendChild(function);
	}

	
	public void output(String path) {
		try {
        /////////////////
        //Output the XML

        //set up a transformer
        TransformerFactory transfac = TransformerFactory.newInstance();
        transfac.setAttribute("indent-number", 2);
        Transformer trans = transfac.newTransformer();
        trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        trans.setOutputProperty(OutputKeys.INDENT, "yes");

        //create string from xml tree
        FileWriter writer = new FileWriter(path);
        StreamResult result = new StreamResult(writer);
        DOMSource source = new DOMSource(doc);
        trans.transform(source, result);
		}
		catch (Exception e) {
			
		}
	}

	
	public void addParametricFunction(ParametricFunction output) {
		Element function = doc.createElement("function");
		function.setAttribute("type", "parametric");
		
		// Append expressions
		Element formular = doc.createElement("equation");
		function.appendChild(formular);
		
		String[] expressions = output.getExpression();
		for (int i = 0; i < expressions.length; i++) {
			Element expression = createExpression(AXES[i], expressions[i]);
			formular.appendChild(expression);
		}
		
		// Append color
		function.appendChild(createColor(output));
		
		
		// Add stepsizes and bounds
		Element bounds = doc.createElement("bounds");
		function.appendChild(bounds);
		
		Element stepsize = doc.createElement("stepsize");
		function.appendChild(stepsize);		
		
		String[] functionBounds = output.getBoundsString();
		float[] stepsizes = output.getStepsize();
		for (int i = 0; i < stepsizes.length; i++) {
			String variable = PARAMETERS[i];
			
			bounds.appendChild(createBound(variable, functionBounds[2*i], functionBounds[2*i+1]));
			stepsize.appendChild(createStepsize(variable, stepsizes[i]));
		}
		
		
		root.appendChild(function);		
	}
	
	private Element createExpression(String name, String equation) {

		Element f = doc.createElement(name);
		f.setAttribute("equation", equation);
		return f;
	}

	private Element createColor(Function function) {
		Element color = doc.createElement("color");
		Color3f functionColor = function.getColor();
		color.setAttribute("rgb", functionColor.x + "," + functionColor.y + "," + functionColor.z);
		return color;
	}
	
	private Element createBound(String variable, String min, String max) {
		Element bound = doc.createElement(variable);
		bound.setAttribute("min", min);
		bound.setAttribute("max", max);
		return bound;
	}
	
	private Element createStepsize(String variable, float size) {
		Element stepsize = doc.createElement(variable);
		stepsize.setAttribute("size", Float.toString(size));
		return stepsize;
	}

}
