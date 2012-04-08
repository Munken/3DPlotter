package munk.graph.gui;

import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.vecmath.Color3f;

import org.nfunk.jep.ParseException;

import munk.graph.appearance.Colors;
import munk.graph.plot.Plotter3D;

/*
 * DETTE ER EN TEST
 */

public class TestGUI {
	
	private static final int CANVAS_INITIAL_WIDTH = 600;
	private static final int CANVAS_INITIAL_HEIGTH = 600;
	
	// GUI Variables.
	private static TestGUI window;
	private JFrame frame;
	private JTextField function;
	private JButton btnEdit;
	private JButton btnDelete;
	private JPanel functionPanel;
	private boolean maximized;

	
	// Non-GUI variables.
	private Plotter3D plotter;
	private int controlsWidth;
	private int controlsHeight;
	private ArrayList<Function> functionList; 
	private JLabel lblFunctions;
	private JButton btnPlot;
	private int noOfFunctions;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				// Set look-and-feel to OS default.
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} 
				catch (Exception e) {
					System.out.println("Unable to load native look and feel: " + e.toString());
				}
				window = new TestGUI();
				window.frame.setVisible(true);
			}
		});
	}
	
	/**
	 * Create the application.
	 */
	public TestGUI() {
		frame = new JFrame("Ultra Mega Epic Xtreme Plotter 3D");
		functionList = new ArrayList<Function>();
		noOfFunctions = 0;
		maximized = false;
		initialize();
	}

	/**
	 * Initialize;
	 */
	private void initialize(){
		// Layout definition.
		frame.setBounds(100, 100, 1000, 1000);
     	GridBagLayout gbl = new GridBagLayout();
     	gbl.columnWidths = new int[]{5, 0, 50, 50, 5, 0};
     	gbl.rowHeights = new int[]{5, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
     	gbl.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
     	gbl.rowWeights = new double[]{0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
     	frame.getContentPane().setLayout(gbl);
     	
		// The 3D plotter.
     	plotter = new Plotter3D();
    	GridBagConstraints gbc_plotter = new GridBagConstraints();
    	gbc_plotter.gridheight = 11;
     	gbc_plotter.insets = new Insets(0, 0, 5, 5);
     	gbc_plotter.gridx = 1;
     	gbc_plotter.gridy = 1;
     	frame.getContentPane().add(plotter, gbc_plotter);
     	
     	// The list of plotted functions.
     	functionPanel = new JPanel();
     	functionPanel.setLayout(new BoxLayout(functionPanel,BoxLayout.Y_AXIS));
     	GridBagConstraints gbc_list = new GridBagConstraints();
     	gbc_list.anchor = GridBagConstraints.NORTH;
     	gbc_list.gridwidth = 2;
     	gbc_list.insets = new Insets(0, 0, 5, 5);
     	gbc_list.fill = GridBagConstraints.HORIZONTAL;
     	gbc_list.gridx = 2;
     	gbc_list.gridy = 7;
     	frame.getContentPane().add(functionPanel, gbc_list);
     	
     	lblFunctions = new JLabel("Equations");
     	GridBagConstraints gbc_lblFunctions = new GridBagConstraints();
     	gbc_lblFunctions.gridwidth = 2;
     	gbc_lblFunctions.insets = new Insets(0, 0, 5, 5);
     	gbc_lblFunctions.gridx = 2;
     	gbc_lblFunctions.gridy = 6;
     	frame.getContentPane().add(lblFunctions, gbc_lblFunctions);
     	
     	// Delete button.
     	btnDelete = new JButton("Delete");
     	GridBagConstraints gbc_btnDelete = new GridBagConstraints();
     	gbc_btnDelete.insets = new Insets(0, 0, 5, 5);
     	gbc_btnDelete.gridx = 2;
     	gbc_btnDelete.gridy = 8;
     	frame.getContentPane().add(btnDelete, gbc_btnDelete);
     	btnDelete.addActionListener(new ActionListener() {
     		// Remove the graph.
     		@Override
     		public void actionPerformed(ActionEvent arg0) {   			
     			deleteFunction(); 
     			frame.pack();
     		}
     	});
     	
     	// Edit button.
     	btnEdit = new JButton("Edit");
     	GridBagConstraints gbc_btnEdit = new GridBagConstraints();
     	gbc_btnEdit.insets = new Insets(0, 0, 5, 5);
     	gbc_btnEdit.gridx = 3;
     	gbc_btnEdit.gridy = 8;
     	frame.getContentPane().add(btnEdit, gbc_btnEdit);
     	btnEdit.addActionListener(new ActionListener() {
     		// Edit the graph.
     		@Override
     		public void actionPerformed(ActionEvent arg0) {
//     			Function currentFunction = null;
     			for(Function f : functionList){
//     				if(f.isSelected()) currentFunction = f;
     				// XXX Giver det ikke mere mening at spawne for alle der er selected?
     				if(f.isSelected()) spawnEditDialog(f);
     			}
     		}
     	});
     	
     	// Function input.
     	function = new JTextField();
     	function.setText("Input equation here.");
     	GridBagConstraints gbc_txtInputFunctionExpression = new GridBagConstraints();
     	gbc_txtInputFunctionExpression.gridwidth = 2;
     	gbc_txtInputFunctionExpression.insets = new Insets(0, 0, 5, 5);
     	gbc_txtInputFunctionExpression.fill = GridBagConstraints.HORIZONTAL;
     	gbc_txtInputFunctionExpression.gridx = 2;
     	gbc_txtInputFunctionExpression.gridy = 9;
     	frame.getContentPane().add(function, gbc_txtInputFunctionExpression);
     	function.setColumns(10);
     	function.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				function.selectAll();
			}
		});
     	
     	function.addKeyListener(new KeyAdapter() {
     		// Plot the graph.
     		@Override
     		public void keyReleased(KeyEvent e) {
     			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
     				addPlot(function.getText());
//    				if(noOfFunctions < 10){
//    				}
//    				else{
//    					// TODO: Nogen grund til at sætte max her ?
//    					String message = ("Too many functions. Please delete some.");
//    					JLabel label = new JLabel(message,JLabel.CENTER);
//    					JOptionPane.showMessageDialog(frame,label);
//    				}
     			}
     		}
     	});
     	
     	// Plot button.
     	btnPlot = new JButton("Plot");
     	GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
     	gbc_btnNewButton.gridwidth = 2;
     	gbc_btnNewButton.insets = new Insets(0, 0, 5, 5);
     	gbc_btnNewButton.gridx = 2;
     	gbc_btnNewButton.gridy = 10;
     	frame.getContentPane().add(btnPlot, gbc_btnNewButton);
     	btnPlot.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				addPlot(function.getText());
//				if(noOfFunctions < 10){
//				}
//				else{
//					String message = ("Too many functions. Please delete some.");
//					JLabel label = new JLabel(message,JLabel.CENTER);
//					JOptionPane.showMessageDialog(frame,label);
//				}
			}
		});
     	setupRepresentation();

     	// Finish up.
     	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
     	frame.setVisible(true);
		frame.pack();
		controlsWidth = frame.getWidth() - CANVAS_INITIAL_WIDTH;
		controlsHeight = frame.getHeight() - CANVAS_INITIAL_HEIGTH;
		
		// Auto resize.
		canvasResize();
	}

	private void canvasResize() {
		frame.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				plotter.updateSize(frame.getWidth()- controlsWidth,frame.getHeight()- controlsHeight);
				
				// XXX Er nedenstående nødvendigt ?
				if(!e.getSource().equals(frame) || maximized){
					frame.pack();
					maximized = false;
				}
				if(e.getSource().toString().contains("maximized")){
					frame.pack();
					maximized = true;
				}
				frame.pack();
			}
		});
	}
	


	private void addPlot(String expr) {
		addPlot(expr, Colors.RED);
	}
	
	private void addPlot(String expr, Color3f color) {
		functionList.add(null);
		boolean succes = setPlot(noOfFunctions, expr, color);
		if (succes)
			noOfFunctions++;
		else 
			functionList.remove(noOfFunctions);
	}
	
	// Ved den ligner addPlot til forveksling, men noOfFunctions skal kun opdateres hvis success
	private boolean setPlot(int index, String newExpr, Color3f color) {
		// Fixed zoom level for now
		int i = 3;
//		int j = 0;
		
		try {
			plotter.plotFunction(newExpr, -i, i, -i, i, color);
			Function newFunction = new Function(newExpr,color);
			newFunction.addVisibilityListener(getVisibilityListener(newFunction));
			functionList.set(index, newFunction);
			setupRepresentation();
			frame.pack();
			return true;
			
		} catch (ParseException e) {
			// TODO Hvis der trykkes enter fanges den også af plotfeltet.
			String message = ("Unable to parse equation. Please try again.");
			JLabel label = new JLabel(message,JLabel.CENTER);
			JOptionPane.showMessageDialog(frame,label);
			return false;
		}
	}
	
	private void changeColor(Function function, Color3f color) {
		plotter.changeColor(function.getEquation(), color);
		functionList.get(functionList.indexOf(function)).setColor(color);
	}
	
	private void removePlot(String expr) {
		plotter.removePlot(expr);
	}
	
	private void updateFunction(Function function, String newExpr, Color3f color) {
		removePlot(function.getEquation());
		setPlot(functionList.indexOf(function), newExpr, color);
	}
	
	
	private void setupRepresentation() {
		// TODO: Lav mig smartere :)
		functionPanel.removeAll();
		
		int count = 0;
		for (Function f : functionList) {
			functionPanel.add(f.getRepresentation());
			count++;
		}
		
		for (int i = 0; i < 10 - count; i++) {
			functionPanel.add(new Function("", null).getRepresentation());
		}
	}

	private void deleteFunction() {
		for (int i = 0; i < functionList.size(); i++) {
			Function f = functionList.get(i);
			if (f.isSelected()) {
				noOfFunctions--;
				removePlot(f.getEquation());
				functionList.remove(i);
				i--;
			}
		}
		setupRepresentation();
	}

	private void spawnEditDialog(Function currentFunction) {
		String curExpr = currentFunction.getEquation();
		JTextField equation = new JTextField(curExpr);
		String currentColor = Colors.getColorName(currentFunction.getColor());
		System.out.println(currentColor);
		String color = (String) JOptionPane.showInputDialog(
		                    frame,
		                    equation,
		                    "Edit function",
		                    JOptionPane.PLAIN_MESSAGE,
		                    null, Colors.getAllColorNames(),
		                    currentColor);
		
		
		String newExpr = equation.getText();
		if (!curExpr.equals(newExpr)) {
			System.out.println(color);
			updateFunction(currentFunction, newExpr, Colors.getColor(color));
		} else if (color != null && !currentColor.equals(color)) {
			changeColor(currentFunction, Colors.getColor(color));
		}
	}
	
	private ActionListener getVisibilityListener(final Function function) {
		return new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				plotter.showPlot(function.getEquation(), function.isVisible());
			}
		};
	}
	
//	// Update graphs in GUI.
//	/**
//	 * @deprecated
//	 */
//	private void updatePlot(){
//		// Fixed zoom level for now
//		int i = 3;
//		int j = 0;
//		// Reset all
//		functionPanel.removeAll();
//		plotter.reset();
//		// Plot again
//		try{
//		for(int l = 0; l < 10; l++){
//			if(functionList.get(l).isVisible()){
//			plotter.plotFunction(-i, i, -i, i, functionList.get(l).getEquation(), functionList.get(l).getColor());
//			}
//			j++;
//			functionPanel.add(functionList.get(l).getRepresentation());
//		}
//		}
//		// Error handling
//		catch(org.nfunk.jep.ParseException e){
//			functionList.set(j, new Function("",null,null));
//			noOfFunctions--;
//			for(int k = j-1; k<10; k++){
//				functionPanel.add(functionList.get(k).getRepresentation());
//			}
//			e.printStackTrace();
//			String message = ("Unable to parse equation. Please try again.");
//			JLabel label = new JLabel(message,JLabel.CENTER);
//			JOptionPane.showMessageDialog(frame,label);
//		}
//		finally{
//			frame.pack();
//		}
//	}
}
