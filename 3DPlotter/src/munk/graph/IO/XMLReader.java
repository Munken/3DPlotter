package munk.graph.IO;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Color3f;
import javax.xml.parsers.*;

import munk.emesp.exceptions.IllegalExpressionException;
import munk.graph.function.ParametricFunction;
import munk.graph.function.XYZFunction;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import munk.graph.function.Function;

public class XMLReader {
	
	private Document	dom;

	public XMLReader(String path) {
		//get the factory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		try {

			//Using factory get an instance of document builder
			DocumentBuilder db = dbf.newDocumentBuilder();

			dom = db.parse(new File(path));
			dom.normalize();


		}catch(ParserConfigurationException pce) {
			pce.printStackTrace();
		}catch(SAXException se) {
			se.printStackTrace();
		}catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	public List<Function> processFunctions() throws IllegalExpressionException {
		NodeList list = dom.getElementsByTagName("function");
		
		List<Function> functions = new ArrayList<Function>(list.getLength());
		if (list != null && list.getLength() > 0) {
			
			for (int i = 0; i < list.getLength(); i++) {
				Element el = (Element) list.item(i);
				String type = el.getAttribute("type").toLowerCase();
				
				Function function = null;
				switch (type) {
					case "std": 		function = addStdFunction(el); break;
					case "parametric": 	function = addParametricFunction(el); break;
					default: System.out.println(type); 
				}
				
				functions.add(function);
			}
		}
		return functions;
	}
	
	public List<Color3f> processColors() {
		NodeList list = dom.getElementsByTagName("color");
		
		if (list != null && list.getLength() > 0) {
			return getColor(list);
		}
		return new ArrayList<Color3f>();
	}

	private XYZFunction addStdFunction(Element el) throws IllegalExpressionException {
		Color3f color = getColor(el);
		String[] expressions = getStdEquations(el);
		String[] bounds = getBounds(el);
		float[] stepsizes = getStepsize(el);
		
		return new XYZFunction(expressions, color, bounds, stepsizes);
	}
	
	private ParametricFunction addParametricFunction(Element el) throws IllegalExpressionException {
		Color3f color = getColor(el);
		String[] expressions = getParametricEquations(el);
		String[] bounds = getBounds(el);
		float[] stepsizes = getStepsize(el);
		
		return new ParametricFunction(expressions, color, bounds, stepsizes);
	}
	
	private Color3f getColor(Element function) {
		Element el = (Element) function.getElementsByTagName("color").item(0);
		
		String rgbString = el.getAttribute("rgb");
		float[] values = getColor(rgbString);
		return new Color3f(values);
	}
	
	private List<Color3f> getColor(NodeList nl) {
		List<Color3f> list = new ArrayList<Color3f>(nl.getLength());
		
		for (int i = 0; i < nl.getLength(); i++) {
			Node n = nl.item(i);
			if (n.getNodeType() == Node.ELEMENT_NODE) {
				Element e = (Element) n;
				String rgb = e.getAttribute("rgb");
				list.add(new Color3f(getColor(rgb)));
			}
		}
		return list;
	}

	private float[] getColor(String rgbString) {
		String[] stringValues = rgbString.split(",");
		float[] values = new float[3];
		
		for (int i = 0; i < values.length; i++) {
			values[i] = Float.valueOf(stringValues[i]);
		}
		return values;
	}
	
	private String[] getStdEquations(Element el) {
		
		Node n = el.getElementsByTagName("equations").item(0);
		NodeList list = n.getChildNodes();

		for (int i = 0; i < list.getLength(); i++) {
			Node next = list.item(i);
			if (next.getNodeType() == Node.ELEMENT_NODE) {
				Element e = (Element) next;
				return new String[] {e.getAttribute("equation")};
			}
		}
		return null;
	}
	
	private String[] getParametricEquations(Element el) {
		String[] attributes = {"equation"};
		return getAllFromElement(el, "equations", attributes);
	}
	
	private String[] getBounds(Element el) {
		String[] attributes = {"min", "max"};
		return getAllFromElement(el, "bounds", attributes);
	}
	
	private float[] getStepsize(Element el) {
		String[] attributes = {"size"};
		String[] stepsizes = getAllFromElement(el, "stepsize", attributes);
		float[] result = new float[stepsizes.length];
		
		for (int i = 0; i < result.length; i++) {
			result[i] = Float.parseFloat(stepsizes[i]);
		}
		return result;
	}
	
	private String[] getAllFromElement(Element el, String name, String[] attributes) {
		Node n = el.getElementsByTagName(name).item(0);
		NodeList list = n.getChildNodes();

		int count = 0;
		for (int i = 0; i < list.getLength(); i++) {
			if (list.item(i).getNodeType() == Node.ELEMENT_NODE)
				count++;	
		}
		
		int nAttributes = attributes.length;
		String[] result = new String[nAttributes * count];
		for (int i = 0; i < list.getLength(); i++) {
			Node next = list.item(i);
			if (next.getNodeType() == Node.ELEMENT_NODE) {
				Element e = (Element) next;
				
				int position = getXYZPosition(e.getNodeName());
				for (int j = 0; j < nAttributes; j++) {
					String attribute = e.getAttribute(attributes[j]);
					result[nAttributes*position + j] = attribute;
				}
			}
		}
		
		return result;
	}
	
	private int getXYZPosition(String var) {
		switch (var) {
		case "x": return 0;
		case "y": return 1;
		case "z": return 2;
		case "t": return 0;
		case "u": return 1;
		}
		
		return 0;
	}

}
