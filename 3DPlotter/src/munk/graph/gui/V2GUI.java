package munk.graph.gui;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.vecmath.Color3f;

import munk.graph.appearance.Colors;
import munk.graph.function.Function;
import munk.graph.function.FunctionList;
import munk.graph.function.FunctionUtil;
import munk.graph.function.XYZFunction;
import munk.graph.plot.Plotter3D;

import com.graphbuilder.math.ExpressionParseException;
import javax.swing.JScrollPane;


/**
 * A simple GUI for the 3DPlotter application.
 * @author xXx
 *
 */
public class V2GUI {
	
	private static final int CANVAS_INITIAL_WIDTH = 600;
	private static final int CANVAS_INITIAL_HEIGTH = 600;
	private static final float DEFAULT_STEPSIZE = (float) 0.1;
	private static final float[] DEFAULT_BOUNDS = {-1,1,-1,1,-1,1};
	
	// GUI Variables.
	private static V2GUI window;
	private JFrame frame;
	private JPanel stdFuncTab;
	private JPanel stdFuncPanel;
	private JPanel paramFuncTab; 
	private JPanel paramFuncPanel;
	JTabbedPane tabbedPane;

	// Non-GUI variables.
	private Plotter3D plotter;
	private int controlsWidth;
	private int controlsHeight;
	private FunctionList<Function> functionList; 
	private FunctionList<Function> paramFunctionList; 
	private int noOfFunctions;
	private boolean maximized;
	
	// Option variables
	private static final float STEP_SIZE = (float) 1; 
	private JScrollPane scrollPane;
	private JTextField stdFuncInput;
	
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
				window = new V2GUI();
				window.frame.setVisible(true);
			}
		});
	}
	
	/**
	 * Create the application.
	 */
	public V2GUI() {
		frame = new JFrame("Ultra Mega Epic Xtreme Plotter 3D");
		functionList = new FunctionList<Function>();
		paramFunctionList = new FunctionList<Function>();
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
     	gbl.columnWidths = new int[]{10, 250, 0, 2, 0};
     	gbl.rowHeights = new int[]{2, 0, 0, 0};
     	gbl.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
     	gbl.rowWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
     	frame.getContentPane().setLayout(gbl);
     	
     	// The tab pane containing the functions.
     	tabbedPane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
     	GridBagConstraints gbc_tabbedPane = new GridBagConstraints();
     	gbc_tabbedPane.insets = new Insets(0, 0, 5, 5);
     	gbc_tabbedPane.fill = GridBagConstraints.BOTH;
     	gbc_tabbedPane.gridx = 1;
     	gbc_tabbedPane.gridy = 1;
     	frame.getContentPane().add(tabbedPane, gbc_tabbedPane);
     	
		// The 3D plotter.
     	plotter = new Plotter3D();
    	GridBagConstraints gbc_plotter = new GridBagConstraints();
     	gbc_plotter.insets = new Insets(0, 0, 5, 5);
     	gbc_plotter.gridx = 2;
     	gbc_plotter.gridy = 1;
     	frame.getContentPane().add(plotter, gbc_plotter);
     	GridBagConstraints gbc_list = new GridBagConstraints();
     	gbc_list.anchor = GridBagConstraints.NORTH;
     	gbc_list.insets = new Insets(0, 0, 5, 5);
     	gbc_list.fill = GridBagConstraints.HORIZONTAL;
     	gbc_list.gridx = 1;
     	gbc_list.gridy = 2;
     	
     	// The standard function tab.
     	stdFuncTab = new JPanel();
     	tabbedPane.addTab("Standard equations", stdFuncTab);
     	GridBagLayout gbl_functionPanel = new GridBagLayout();
     	gbl_functionPanel.columnWidths = new int[]{5, 225, 5, 0};
     	gbl_functionPanel.rowHeights = new int[]{5, 0, 0, 5, 5, 0};
     	gbl_functionPanel.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
     	gbl_functionPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
     	stdFuncTab.setLayout(gbl_functionPanel);
     	
     	// Function input field.
     	stdFuncInput = new JTextField();
     	GridBagConstraints gbc_textField = new GridBagConstraints();
     	gbc_textField.insets = new Insets(0, 0, 5, 5);
     	gbc_textField.fill = GridBagConstraints.HORIZONTAL;
     	gbc_textField.anchor = GridBagConstraints.NORTH;
     	gbc_textField.gridx = 1;
     	gbc_textField.gridy = 1;
     	stdFuncTab.add(stdFuncInput, gbc_textField);
     	stdFuncInput.setColumns(10);
     	stdFuncInput.addKeyListener(new KeyAdapter() {
     		// Plot the graph.
     		@Override
     		public void keyReleased(KeyEvent e) {
     			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
     				addPlot(stdFuncInput.getText(),getNextColor());
     			}
     		}
     	});
     	
     	// The standard function list
     	stdFuncPanel = new JPanel();
     	stdFuncPanel.setLayout(new BoxLayout(stdFuncPanel, BoxLayout.Y_AXIS));
     	GridBagConstraints gbc_stdFuncPanel = new GridBagConstraints();
     	gbc_stdFuncPanel.anchor = GridBagConstraints.NORTH;
     	gbc_stdFuncPanel.insets = new Insets(0, 0, 5, 5);
     	gbc_stdFuncPanel.gridx = 1;
     	gbc_stdFuncPanel.gridy = 3;
     	stdFuncTab.add(stdFuncPanel, gbc_stdFuncPanel);

     	// Auto update List according to the function list.
     	functionList.addActionListener(new ActionListener() {

     		@Override
     		public void actionPerformed(ActionEvent e) {
     			if(e.getActionCommand().equals("ADD")){
     				stdFuncPanel.add(new FunctionLabel((Function) e.getSource(), new ActionListener() {
     					public void actionPerformed(ActionEvent e) {
     						Function source = (Function) e.getSource();
     						if(e.getID() == 0){
     							String newExpr = e.getActionCommand();
     							updatePlot(source, newExpr, source.getColor(), source.getBounds(), source.getStepsize());
     						}
     						if(e.getID() == 1){
     							spawnEditDialog(source);
     						}
     					}
     				}));
     			}
     			else if(e.getActionCommand().equals("REMOVE")){
     				stdFuncPanel.remove(e.getID());
     			}
     			else if(e.getActionCommand().equals("SET")){
     				FunctionLabel label = (FunctionLabel) stdFuncPanel.getComponent(e.getID());
     				label.setMother((Function) e.getSource());
     			}
     		}
     	});

     	// The parametric function tab.
     	paramFuncTab = new JPanel();
     	tabbedPane.addTab("Parametric equations", new JLabel("To be implemented"));

     	// Finish up.
     	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
     	frame.setVisible(true);
     	frame.pack();
     	controlsWidth = frame.getWidth() - CANVAS_INITIAL_WIDTH;
     	controlsHeight = frame.getHeight() - CANVAS_INITIAL_HEIGTH;

     	// Auto resize.
     	canvasResize();

		// TEST
		addPlot("y=x", Colors.BLUE);
	}

	/*
	 * Resize the plot canvas according to window resize.
	 */
	private void canvasResize() {
		frame.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				//plotter.updateSize(frame.getWidth()- controlsWidth,frame.getHeight()- controlsHeight);
				
				// Er nedenst�ende n�dvendigt ?
				// XXX Ja, med mindre du har en bedre l�sning. 
				// Problemet er, at frame.pack() resizer vinduet. 
				// Dvs. uden dette check opn�s et uendeligt loop.
				if(!e.getSource().equals(frame) || maximized){
					//frame.pack();
					maximized = false;
				}
				if(e.getSource().toString().contains("maximized")){
					//frame.pack();
					maximized = true;
				}
			}
		});
	}
	
	/*
	 * Add new plot.
	 */
	private void addPlot(String expr, Color3f color) {
		// Create the function.
		try{
		Function newFunc = FunctionUtil.createFunction(expr,color,DEFAULT_BOUNDS,DEFAULT_STEPSIZE);
		newFunc.addActionListener(FunctionUtil.createActionListener(plotter));
		functionList.add(newFunc);
		plotter.plotFunction(newFunc);
		noOfFunctions++;
		frame.pack();
		}
		catch(ExpressionParseException e){
			String message = e.getMessage();
			JLabel label = new JLabel(message,JLabel.CENTER);
			JOptionPane.showMessageDialog(frame,label);
		}
	}
	
	/*
	 * Remove a function from the plot.
	 */
	private void removePlot(Function function) {
		plotter.removePlot(function);
	}

	/*
	 * Update a function.
	 */
	private void updatePlot(Function oldFunc, String newExpr, Color3f newColor, float[] bounds, float stepsize) {
		// Try evaluating the function.
		try {
			Function newFunc = FunctionUtil.createFunction(newExpr, newColor, bounds, stepsize);
			functionList.set(functionList.indexOf(oldFunc),newFunc);
			plotter.removePlot(oldFunc);
			plotter.plotFunction(newFunc);
			frame.pack();
		} 
		// Catch error.
		catch (ExpressionParseException e) {
			// TODO Hvis der trykkes enter fanges den ogs� af plotfeltet.
			String message = e.getMessage();
			JLabel label = new JLabel(message,JLabel.CENTER);
			JOptionPane.showMessageDialog(frame,label);
		}
	}

//	/*
//	 * Delete all selected functions.
//	 */
//	private void deleteSelectedFunctions() {
//		for (int i = 0; i < functionList.size(); i++) {
//			Function f = functionList.get(i);
//			if (f.isSelected()) {
//				noOfFunctions--;
//				removePlot(f);
//				functionList.remove(i);
//				i--;
//			}
//		}
//		frame.pack();
//	}
	
	private Color3f getNextColor(){
		// TODO: To be implemented.
		return Colors.BLUE;
	}

	/*
	 * Spawn an edit dialog and process the input.
	 */
	private void spawnEditDialog(Function f) {
		String curExpr = f.getExpression()[0];
		
		// Set up dialog.
		JPanel inputPanel = new JPanel();
		GridBagLayout gbl_inputPanel = new GridBagLayout();
		gbl_inputPanel.columnWidths = new int[]{5, 193, 20, 5, 0};
		gbl_inputPanel.rowHeights = new int[]{5, 20, 0};
		gbl_inputPanel.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_inputPanel.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		inputPanel.setLayout(gbl_inputPanel);
		
		JTextField equation = new JTextField(curExpr);
		GridBagConstraints gbc_equation = new GridBagConstraints();
		gbc_equation.fill = GridBagConstraints.HORIZONTAL;
		gbc_equation.anchor = GridBagConstraints.NORTH;
		gbc_equation.insets = new Insets(0, 0, 0, 5);
		gbc_equation.gridx = 1;
		gbc_equation.gridy = 1;
		inputPanel.add(equation, gbc_equation);
		
		JComboBox<Icon> colors = new JComboBox<Icon>(Colors.getAllColors());
		GridBagConstraints gbc_colors = new GridBagConstraints();
		gbc_colors.insets = new Insets(0, 0, 0, 5);
		gbc_colors.anchor = GridBagConstraints.NORTHWEST;
		gbc_colors.gridx = 2;
		gbc_colors.gridy = 1;
		inputPanel.add(colors, gbc_colors);
		
		JOptionPane.showMessageDialog(frame, inputPanel, "Edit Function", JOptionPane.PLAIN_MESSAGE, null);
		
		// Update function in case of changes.
		ColorIcon selectedIcon = (ColorIcon) colors.getSelectedItem();
		String newExpr = equation.getText();
		Color3f newColor = selectedIcon.getColor();
		// TODO: Implement the option to change bounds and stepsize.
		if (!curExpr.equals(newExpr)) {
			updatePlot(f, newExpr, newColor, DEFAULT_BOUNDS, DEFAULT_STEPSIZE);
		} else if (newColor != null && !f.getColor().equals(newColor)) {
			functionList.get(functionList.indexOf(f)).setColor(newColor);
		}
	}
}