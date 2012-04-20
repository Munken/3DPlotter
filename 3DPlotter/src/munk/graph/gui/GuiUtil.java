package munk.graph.gui;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.Document;
import javax.swing.undo.*;

import com.graphbuilder.math.*;

public class GuiUtil {
	
	public static void setupUndoListener(JTextField textfield) {
		final UndoManager manager = new UndoManager();
     	Document document = textfield.getDocument();
     	document.addUndoableEditListener(manager);
     	
     	textfield.addKeyListener(new KeyAdapter() {
     		
     		@Override
     		public void keyPressed(KeyEvent e) {
     			if (e.getKeyCode() == KeyEvent.VK_Z && e.getModifiers() == KeyEvent.CTRL_MASK) {
     				try {
     					manager.undo();
     				} catch (CannotUndoException excep) {
     					// Ignore
     				}
     			} else if (e.getKeyCode() == KeyEvent.VK_Z && e.getModifiers() == KeyEvent.ALT_MASK) {
     				try {
     					manager.redo();
     				} catch (CannotRedoException excep) {
     					// Ignore
     				}
     			}
     		}
		});
	}
	
	/*
	 * Spawn simple export dialog.
	 */
	public static File spawnExportDialog(String filePath, JFrame frame){
		File outputFile = null;
		JFileChooser fc = new JFileChooser(new File(filePath));
		fc.showSaveDialog(frame);
		outputFile = fc.getSelectedFile();
		return outputFile;
	}
	
	/**
	 * Spawn not so simple export dialog.
	 */
	public static File spawnExportDialog(String filePath, String[][] allowedFileTypes, String[] description, JFrame frame){
		File outputFile = null;
		JFileChooser fc = new JFileChooser(new File(filePath));
		
		for (int i = 0; i < allowedFileTypes.length; i++) {
			String[] next = allowedFileTypes[i];
			
			FileNameExtensionFilter filter = new FileNameExtensionFilter(description[i], next);
			fc.addChoosableFileFilter(filter);
			
			if (i == 0)
				fc.setFileFilter(filter);
		}

		
		fc.showSaveDialog(frame);
		outputFile = fc.getSelectedFile();
		
		// Fix file extension
		if (outputFile != null) {
			if (fc.getFileFilter().getClass() == FileNameExtensionFilter.class) {
				String fileEnding = getFileExtension(outputFile);
				
				FileNameExtensionFilter filter = (FileNameExtensionFilter) fc.getFileFilter();
				
				String[] allowedEndings = filter.getExtensions();
				boolean match = false;
				
				for (String allowed : allowedEndings) {
					match = allowed.equals(fileEnding);
				}
				
				if(!match){
					outputFile = new File(outputFile.getAbsolutePath() + "." + allowedEndings[0]);
				}
			}
			return outputFile;
		} else 
			return null;
		
	}
	
	/*
	 * Spawn simple import dialog.
	 */
	public static File spawnImportDialog(String filePath, JFrame frame){
		File inputFile = null;
		JFileChooser fc = new JFileChooser(new File(filePath));
		fc.showOpenDialog(frame);
		inputFile = fc.getSelectedFile();
		return inputFile;
	}

	/*
	 * Get screen shot of the plotter.
	 */
	public static BufferedImage getSSH(GraphicsDevice device, Point p, int w, int h){
		Rectangle bounds = new Rectangle((int) p.getX(), (int) p.getY(), w, h);		
		Robot robot = null;
		try {
			robot = new Robot(device);
		} catch (AWTException e) {
			e.printStackTrace();
		}
		return robot.createScreenCapture(bounds);
	}
	
	/*
	 * Evaluate expression, accounting only for constants.
	 */
	public static float evalString(String expr) throws ExpressionParseException{
			VarMap varMap = new VarMap();
			// Add more constants here.
			varMap.setValue("pi", 3.14159265);
			varMap.setValue("e", 2.71828183);
			return (float) ExpressionTree.parse(expr).eval(varMap, new FuncMap());
	}
	
		
	public static String getFileExtension(File file) {
		return getFileExtension(file.getAbsolutePath());
	}
	
	public static String getFileExtension(String path) {
		int index = path.lastIndexOf('.');
		if(index > 0 && index < path.length() - 1) {
			return path.substring(index + 1).toLowerCase();
		} else 
			return null;
	}
	
	public static String sphericalToCartesian(String expr){
		if(expr.contains("r") || expr.contains("theta") || expr.contains("phi")){
			expr = stringReplace(expr,"theta", "acos(z/r)");
			expr = stringReplace(expr,"r", "(x^2+y^2+z^2)^0.5");
			expr = stringReplace(expr,"phi", "atan(y/x)");
			return expr;
		}
		throw new IllegalArgumentException("Expression must contain r, theta or phi.");
	}
	
	private static String stringReplace(String str, String pattern, String replace) {
	    int s = 0;
	    int e = 0;
	    StringBuffer result = new StringBuffer();

	    while ((e = str.indexOf(pattern, s)) >= 0) {
	        result.append(str.substring(s, e));
	        result.append(replace);
	        s = e+pattern.length();
	    }
	    result.append(str.substring(s));
	    return result.toString();
	}
	
	/*
	 * Return step size. Should maybe account for world size? 
	 */
	public static float[] getStepsize(int sliderValue, float[] bounds){
		float stepX = Math.abs(bounds[1]-bounds[0])/sliderValue;
		float stepY = Math.abs(bounds[3]-bounds[2])/sliderValue;
		float stepZ = Math.abs(bounds[5]-bounds[4])/sliderValue;
		return new float[]{stepX,stepY,stepZ};
	}
	
	/*
	 * Return step size. Should maybe account for world size? 
	 */
	public static int getSliderValue(float stepX, float[] bounds){
		int sliderValue = (int) (Math.abs(bounds[1]-bounds[0])/stepX);
		
		return sliderValue;
	}
}
