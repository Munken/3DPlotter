package munk.graph.gui;
import java.awt.Desktop;
import java.awt.Dimension;
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
import java.awt.event.KeyListener;
import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.vecmath.Color3f;

import munk.graph.IO.ObjectReader;
import munk.graph.IO.ObjectWriter;
import munk.graph.function.Function;
import munk.graph.function.FunctionList;
import munk.graph.function.FunctionUtil;
import munk.graph.function.IllegalEquationException;
import munk.graph.function.ParametricFunction;
import munk.graph.function.ZippedFunction;

import com.graphbuilder.math.ExpressionParseException;
import com.graphbuilder.math.UndefinedVariableException;


/**
 * A simple GUI for the 3DPlotter application.
 * @author xXx
 *
 */
public class V2GUI {

	private static final int CANVAS_INITIAL_WIDTH = 600;
	private static final int CANVAS_INITIAL_HEIGTH = 600;
	private static final float DEFAULT_STEPSIZE = (float) 0.05;
	private static final float[] DEFAULT_BOUNDS = {-1,1,-1,1,-1,1};
	
	// GUI Variables.
	private static V2GUI window;
	private JFrame frame;
	private JPanel stdFuncTab, stdFuncOuterPanel, stdFuncInnerPanel;
	private JScrollPane stdFuncPanelWrapper;
	private JPanel paramFuncTab, paramFuncOuterPanel, paramFuncInnerPanel;
	private JScrollPane paramFuncPanelWrapper;
	private JPanel optionPanel;
	private JScrollPane optionPanelWrapper;
	private JTabbedPane tabbedPane;
	private JDialog colorDialog;
	private JDialog editDialog;

	// Non-GUI variables.
	private Plotter3D plotter;
	private int controlsWidth;
	private int controlsHeight;
	private FunctionList<Function> stdFunctionList; 
	private FunctionList<Function> paramFunctionList; 
	private javax.swing.Timer resizeTimer;
	private ColorList colorList;
	private String filePath;
	private File inputFile;
	private File outputFile;
	
	// Option variables
	private JTextField stdFuncInput;
	private JLabel label_1, label_2, label_3;
	private JTextField txtXmin, txtYmin, txtZmin, txtXmax, txtYmax,  txtZmax;
	private JMenuBar menuBar;
	private JMenu mnFile;
	private JMenuItem mntmSaveProject, mntmLoadProject, mntmExit;
	private JMenu mnColorOptions;
	private JMenu mnHelp;
	private JMenuItem mntmDocumentation, mntmAbout;
	private JMenuItem mntmImportColors, mntmExportColors, mntmAddCustomColor;
	private JTextField inputX, inputY, inputZ;
	private JLabel lblX, lblY, lblZ;
	
	
	// Plotter renderes
	private ExecutorService plottingQueue = Executors.newSingleThreadExecutor(); // Only plot one at a time
	
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
		// Initialize variables.
		stdFunctionList = new FunctionList<Function>();
		paramFunctionList = new FunctionList<Function>();
		colorList = new ColorList();
		
		initialize();
	}

	/**
	 * Initialize;
	 */
	private void initialize(){
		// Initialize GUI components.
		initFrame();
		initMenuBar();
		initTabbedPane();
		init3Dplotter();
     	initStdFunctionTab();
     	initParamFunctionTab();
		initOptionPanel();
		
     	// Finish up.
     	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
     	frame.setVisible(true);
     	frame.pack();
     	
     	// Test Function
     	addPlot(new String[]{"y = sin(x*5)*cos(z*5)"}, colorList.getNextAvailableColor(stdFunctionList), getBounds(), DEFAULT_STEPSIZE);
     	
     	autoResize();
	}
	
	private void initFrame(){
		frame = new JFrame("Ultra Mega Epic Xtreme Plotter 3D");
		frame.setBounds(100, 100, 1000, 1000);
     	GridBagLayout gbl = new GridBagLayout();
     	gbl.columnWidths = new int[]{10, 0, 330, 0, 0, 2, 0, 0};
     	gbl.rowHeights = new int[]{2, 0, 0, 0, 0, 0};
     	gbl.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
     	gbl.rowWeights = new double[]{0.0, 2.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
     	frame.getContentPane().setLayout(gbl);
	}
	
	private void initMenuBar(){
		menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		mntmSaveProject = new JMenuItem("Save Project", new ImageIcon("Icons/save.png"));
		mntmSaveProject.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				spawnExportDialog(new ZippedFunction[][]{FunctionUtil.zipFunctionList(stdFunctionList),FunctionUtil.zipFunctionList(paramFunctionList)});
			}
		});
		mnFile.add(mntmSaveProject);
		
		mntmLoadProject = new JMenuItem("Load project", new ImageIcon("Icons/file.png"));
		mntmLoadProject.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				spawnImportDialog("FunctionList");
			}
		});
		mnFile.add(mntmLoadProject);
		
		// Close application on click.
		mntmExit = new JMenuItem("Exit", new ImageIcon("Icons/exit.png"));
		mntmExit.addActionListener(new ActionListener(
				) {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		mnFile.add(mntmExit);
		
		mnColorOptions = new JMenu("Color Options");
		menuBar.add(mnColorOptions);
		
		mntmExportColors = new JMenuItem("Export colors", new ImageIcon("Icons/save.png"));
		mntmExportColors.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				spawnExportDialog(colorList);
			}
		});
		mnColorOptions.add(mntmExportColors);
		
		mntmImportColors = new JMenuItem("Import colors", new ImageIcon("Icons/file.png"));
		mntmImportColors.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				spawnImportDialog("ColorList");
			}
		});
		mnColorOptions.add(mntmImportColors);
		
		mntmAddCustomColor = new JMenuItem("Add custom color", new ImageIcon("Icons/settings.png"));
		mntmAddCustomColor.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				spawnColorChooser();
			}
		});
		mnColorOptions.add(mntmAddCustomColor);
		
		mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		
		// Open the documentation PDF.
		mntmDocumentation = new JMenuItem("Documentation", new ImageIcon("Icons/pdf.png"));
		mntmDocumentation.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					Desktop.getDesktop().open(new File("Files/3DPlotter.pdf"));
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		mnHelp.add(mntmDocumentation);

		// Open about pop up on click.
		mntmAbout = new JMenuItem("About", new ImageIcon("Icons/info.png"));
		mntmAbout.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String message = "<html> 3DPlotter is a free simple 3D graphing tool. It is currently being developed as a spare time <br> project by Michael Munch, Emil Haldrup Eriksen and Kristoffer Theis Skalmstang. Please email <br> bugs, suggestions and generel feedback to <br> <br> <center> emil.h.eriksen@gmail.com </center> </html>";
				JLabel label = new JLabel(message);
				JOptionPane.showMessageDialog(frame,label,"About",JOptionPane.PLAIN_MESSAGE,null);
			}
		});
		mnHelp.add(mntmAbout);
	}
	
	private void initTabbedPane(){
     	tabbedPane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
     	GridBagConstraints gbc_tabbedPane = new GridBagConstraints();
     	gbc_tabbedPane.insets = new Insets(0, 0, 5, 5);
     	gbc_tabbedPane.fill = GridBagConstraints.BOTH;
     	gbc_tabbedPane.gridx = 2;
     	gbc_tabbedPane.gridy = 1;
     	frame.getContentPane().add(tabbedPane, gbc_tabbedPane);
	}
	
	private void init3Dplotter(){
     	plotter = new Plotter3D();
    	GridBagConstraints gbc_plotter = new GridBagConstraints();
    	gbc_plotter.gridheight = 3;
     	gbc_plotter.insets = new Insets(0, 0, 5, 5);
     	gbc_plotter.gridx = 4;
     	gbc_plotter.gridy = 1;
     	frame.getContentPane().add(plotter, gbc_plotter);
     	GridBagConstraints gbc_list = new GridBagConstraints();
     	gbc_list.anchor = GridBagConstraints.NORTH;
     	gbc_list.insets = new Insets(0, 0, 5, 5);
     	gbc_list.fill = GridBagConstraints.HORIZONTAL;
     	gbc_list.gridx = 1;
     	gbc_list.gridy = 2;
	}
	
	private void initStdFunctionTab(){
		// The standard function tab.
     	stdFuncTab = new JPanel();
     	tabbedPane.addTab("Standard equations", stdFuncTab);
     	GridBagLayout gbl_functionPanel = new GridBagLayout();
     	gbl_functionPanel.columnWidths = new int[]{5, 25, 50, 50, 50, 25, 5, 0};
     	gbl_functionPanel.rowHeights = new int[]{5, 0, 0, 0, 0, 10, 5, 5, 0};
     	gbl_functionPanel.columnWeights = new double[]{0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
     	gbl_functionPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
     	stdFuncTab.setLayout(gbl_functionPanel);
     	
     	// Function input field.
     	stdFuncInput = new JTextField();
     	GridBagConstraints gbc_stdFuncInput = new GridBagConstraints();
     	gbc_stdFuncInput.gridwidth = 5;
     	gbc_stdFuncInput.insets = new Insets(0, 0, 5, 5);
     	gbc_stdFuncInput.fill = GridBagConstraints.HORIZONTAL;
     	gbc_stdFuncInput.anchor = GridBagConstraints.NORTH;
     	gbc_stdFuncInput.gridx = 1;
     	gbc_stdFuncInput.gridy = 1;
     	stdFuncTab.add(stdFuncInput, gbc_stdFuncInput);
     	stdFuncInput.setColumns(10);
     	stdFuncInput.addKeyListener(new KeyAdapter() {
     		// Plot the graph.
     		
     		@Override
     		public void keyPressed(KeyEvent e) {
     			if (e.getKeyCode() == KeyEvent.VK_ENTER && stdFuncInput.isFocusOwner()) {
     				addPlot(new String[]{stdFuncInput.getText()},colorList.getNextAvailableColor(stdFunctionList), getBounds(), DEFAULT_STEPSIZE);
     			}
     		}
     	});
     	
     	// The standard function list
     	stdFuncOuterPanel = new JPanel();
     	stdFuncPanelWrapper = new JScrollPane(stdFuncOuterPanel);
     	stdFuncPanelWrapper.setBorder(null);
     	stdFuncPanelWrapper.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
     	GridBagConstraints gbc_stdFuncPanel = new GridBagConstraints();
     	gbc_stdFuncPanel.fill = GridBagConstraints.BOTH;
     	gbc_stdFuncPanel.gridwidth = 5;
     	gbc_stdFuncPanel.insets = new Insets(0, 0, 5, 5);
     	gbc_stdFuncPanel.gridx = 1;
     	gbc_stdFuncPanel.gridy = 6;
     	stdFuncTab.add(stdFuncPanelWrapper, gbc_stdFuncPanel);
     	GridBagLayout gbl_stdFuncPanel = new GridBagLayout();
     	gbl_stdFuncPanel.columnWidths = new int[]{0, 0};
     	gbl_stdFuncPanel.rowHeights = new int[]{0, 0};
     	gbl_stdFuncPanel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
     	gbl_stdFuncPanel.rowWeights = new double[]{1.0, Double.MIN_VALUE};
     	stdFuncOuterPanel.setLayout(gbl_stdFuncPanel);
     	
     	stdFuncInnerPanel = new JPanel();
     	GridBagConstraints gbc_panel = new GridBagConstraints();
     	gbc_panel.anchor = GridBagConstraints.NORTH;
     	gbc_panel.fill = GridBagConstraints.HORIZONTAL;
     	gbc_panel.gridx = 0;
     	gbc_panel.gridy = 0;
     	stdFuncOuterPanel.add(stdFuncInnerPanel, gbc_panel);
     	stdFuncInnerPanel.setLayout(new BoxLayout(stdFuncInnerPanel, BoxLayout.Y_AXIS));


     	// Auto update List according to the function list.
     	stdFunctionList.addActionListener(new ActionListener() {

     		@Override
     		public void actionPerformed(ActionEvent e) {
     			if(e.getActionCommand().equals("ADD")){
     				stdFuncInnerPanel.add(new FunctionLabel((Function) e.getSource(), new ActionListener() {
     					public void actionPerformed(ActionEvent e) {
     						Function source = (Function) e.getSource();
     						if(e.getID() == 0){
     							String newExpr = e.getActionCommand();
     							updatePlot(source, new String[]{newExpr}, source.getColor(), source.getBounds(), source.getStepsize());
     						}
     						if(e.getID() == 1){
     							spawnEditDialog(source);
     						}
     						if(e.getID() == 2){
     							deletePlot(source);
     						}
     						if(e.getID() == 3){
     							plotter.showPlot(source);
     						}
     					}
     				}));
     			}
     			else if(e.getActionCommand().equals("REMOVE")){
     				stdFuncInnerPanel.remove(e.getID());
     			}
     			else if(e.getActionCommand().equals("SET")){
     				FunctionLabel label = (FunctionLabel) stdFuncInnerPanel.getComponent(e.getID());
     				label.setMother((Function) e.getSource());
     			}
     		}
     	});
	}
	
	private void initParamFunctionTab(){
		// The parametric function tab.
     	paramFuncTab = new JPanel();
     	tabbedPane.addTab("Parametric equations", paramFuncTab);
     	GridBagLayout gbl_paramFunctionPanel = new GridBagLayout();
     	gbl_paramFunctionPanel.columnWidths = new int[]{5, 25, 50, 50, 50, 25, 5, 0};
     	gbl_paramFunctionPanel.rowHeights = new int[]{5, 0, 0, 0, 0, 10, 5, 5, 0};
     	gbl_paramFunctionPanel.columnWeights = new double[]{0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
     	gbl_paramFunctionPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
     	paramFuncTab.setLayout(gbl_paramFunctionPanel);
     	
     	lblX = new JLabel("x =");
     	GridBagConstraints gbc_lblX = new GridBagConstraints();
     	gbc_lblX.insets = new Insets(0, 0, 5, 5);
     	gbc_lblX.anchor = GridBagConstraints.EAST;
     	gbc_lblX.gridx = 1;
     	gbc_lblX.gridy = 1;
     	paramFuncTab.add(lblX, gbc_lblX);
     	
     	// Parametric function input fields.
     	inputX = new JTextField();
     	GridBagConstraints gbc_paramFuncInput = new GridBagConstraints();
     	gbc_paramFuncInput.gridwidth = 4;
     	gbc_paramFuncInput.insets = new Insets(0, 0, 5, 5);
     	gbc_paramFuncInput.fill = GridBagConstraints.HORIZONTAL;
     	gbc_paramFuncInput.anchor = GridBagConstraints.NORTH;
     	gbc_paramFuncInput.gridx = 2;
     	gbc_paramFuncInput.gridy = 1;
     	paramFuncTab.add(inputX, gbc_paramFuncInput);
     	inputX.setColumns(10);
     	
     	lblY = new JLabel("y =");
     	GridBagConstraints gbc_lblY = new GridBagConstraints();
     	gbc_lblY.insets = new Insets(0, 0, 5, 5);
     	gbc_lblY.anchor = GridBagConstraints.EAST;
     	gbc_lblY.gridx = 1;
     	gbc_lblY.gridy = 2;
     	paramFuncTab.add(lblY, gbc_lblY);
     	
     	inputY = new JTextField();
     	inputY.setColumns(10);
     	GridBagConstraints gbc_textField = new GridBagConstraints();
     	gbc_textField.gridwidth = 4;
     	gbc_textField.insets = new Insets(0, 0, 5, 5);
     	gbc_textField.fill = GridBagConstraints.HORIZONTAL;
     	gbc_textField.gridx = 2;
     	gbc_textField.gridy = 2;
     	paramFuncTab.add(inputY, gbc_textField);
     	
     	lblZ = new JLabel("z =");
     	GridBagConstraints gbc_lblZ = new GridBagConstraints();
     	gbc_lblZ.insets = new Insets(0, 0, 5, 5);
     	gbc_lblZ.gridx = 1;
     	gbc_lblZ.gridy = 3;
     	paramFuncTab.add(lblZ, gbc_lblZ);
     	
     	inputZ = new JTextField();
     	inputZ.setColumns(10);
     	GridBagConstraints gbc_textField_1 = new GridBagConstraints();
     	gbc_textField_1.gridwidth = 4;
     	gbc_textField_1.insets = new Insets(0, 0, 5, 5);
     	gbc_textField_1.fill = GridBagConstraints.HORIZONTAL;
     	gbc_textField_1.gridx = 2;
     	gbc_textField_1.gridy = 3;
     	paramFuncTab.add(inputZ, gbc_textField_1);
     	
     	// Input detection
     	KeyListener inputListener = new KeyAdapter() {
     		// Plot the graph.
     		
     		@Override
     		public void keyPressed(KeyEvent e) {
     			String[] paramExpr = new String[]{inputX.getText(),inputY.getText(),inputZ.getText()};
     			if (e.getKeyCode() == KeyEvent.VK_ENTER && (inputX.isFocusOwner() || inputY.isFocusOwner() || inputZ.isFocusOwner())) {
     				addPlot(paramExpr,colorList.getNextAvailableColor(stdFunctionList), getBounds(), DEFAULT_STEPSIZE);
     			}
     		}
     	};
     	inputX.addKeyListener(inputListener);
     	inputY.addKeyListener(inputListener);
     	inputZ.addKeyListener(inputListener);
     	
     	// The parametric function list
     	paramFuncOuterPanel = new JPanel();
     	paramFuncPanelWrapper = new JScrollPane(paramFuncOuterPanel);
     	paramFuncPanelWrapper.setBorder(null);
     	paramFuncPanelWrapper.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
     	GridBagConstraints gbc_paramFuncPanel = new GridBagConstraints();
     	gbc_paramFuncPanel.fill = GridBagConstraints.BOTH;
     	gbc_paramFuncPanel.gridwidth = 5;
     	gbc_paramFuncPanel.insets = new Insets(0, 0, 5, 5);
     	gbc_paramFuncPanel.gridx = 1;
     	gbc_paramFuncPanel.gridy = 6;
     	paramFuncTab.add(paramFuncPanelWrapper, gbc_paramFuncPanel);
     	GridBagLayout gbl_paramFuncPanel = new GridBagLayout();
     	gbl_paramFuncPanel.columnWidths = new int[]{0, 0};
     	gbl_paramFuncPanel.rowHeights = new int[]{0, 0};
     	gbl_paramFuncPanel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
     	gbl_paramFuncPanel.rowWeights = new double[]{1.0, Double.MIN_VALUE};
     	paramFuncOuterPanel.setLayout(gbl_paramFuncPanel);
     	
     	paramFuncInnerPanel = new JPanel();
     	GridBagConstraints gbc_panel = new GridBagConstraints();
     	gbc_panel.anchor = GridBagConstraints.NORTH;
     	gbc_panel.fill = GridBagConstraints.HORIZONTAL;
     	gbc_panel.gridx = 0;
     	gbc_panel.gridy = 0;
     	paramFuncOuterPanel.add(paramFuncInnerPanel, gbc_panel);
     	paramFuncInnerPanel.setLayout(new BoxLayout(paramFuncInnerPanel, BoxLayout.Y_AXIS));

     	// Auto update List according to the function list.
     	paramFunctionList.addActionListener(new ActionListener() {

     		@Override
     		public void actionPerformed(ActionEvent e) {
     			if(e.getActionCommand().equals("ADD")){
     				paramFuncInnerPanel.add(new ParametricFunctionLabel((Function) e.getSource(), new ActionListener() {
     					public void actionPerformed(ActionEvent e) {
     						Function source = (Function) e.getSource();
     						if(e.getID() == 0){
     							String newExpr = e.getActionCommand();
     							System.out.println(newExpr);
     							updatePlot(source, newExpr.split(","), source.getColor(), source.getBounds(), source.getStepsize());
     						}
     						if(e.getID() == 1){
     							spawnEditDialog(source);
     						}
     						if(e.getID() == 2){
     							deletePlot(source);
     						}
     						if(e.getID() == 3){
     							plotter.showPlot(source);
     						}
     					}
     				}));
     			}
     			else if(e.getActionCommand().equals("REMOVE")){
     				paramFuncInnerPanel.remove(e.getID());
     			}
     			else if(e.getActionCommand().equals("SET")){
     				ParametricFunctionLabel label = (ParametricFunctionLabel) paramFuncInnerPanel.getComponent(e.getID());
     				label.setMother((Function) e.getSource());
     			}
     		}
     	});
	}
	
	// TODO: Think the design through! Current thoughts:
		//			- Step size should be a bar from "min" to "max" ensure no errors (e.g. OutOfMemoryError), where min/max depends on the x, y, z limits.
		//			- I guess faster implicit should be an option here - or should it be controlled by a menu bar item?
		//			- How should variable limits be handled in the parametric case?
	private void initOptionPanel(){
		optionPanelWrapper = new JScrollPane();
     	GridBagConstraints gbc_scrollPane = new GridBagConstraints();
     	gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
     	gbc_scrollPane.fill = GridBagConstraints.BOTH;
     	gbc_scrollPane.gridx = 2;
     	gbc_scrollPane.gridy = 3;
     	frame.getContentPane().add(optionPanelWrapper, gbc_scrollPane);
     	
     	optionPanel = new JPanel();
     	optionPanelWrapper.setViewportView(optionPanel);
     	GridBagLayout gbl_panel = new GridBagLayout();
     	gbl_panel.columnWidths = new int[]{5, 0, 0, 0, 5, 0};
     	gbl_panel.rowHeights = new int[]{10, 0, 0, 0, 5, 0};
     	gbl_panel.columnWeights = new double[]{1.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
     	gbl_panel.rowWeights = new double[]{1.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
     	optionPanel.setLayout(gbl_panel);
     	
     	// The limit data.
     	txtXmin = new JTextField();
     	GridBagConstraints gbc_txtXmin = new GridBagConstraints();
     	gbc_txtXmin.insets = new Insets(0, 0, 5, 5);
     	gbc_txtXmin.gridx = 1;
     	gbc_txtXmin.gridy = 1;
     	optionPanel.add(txtXmin, gbc_txtXmin);
     	txtXmin.setText("" + DEFAULT_BOUNDS[0]);
     	txtXmin.setColumns(10);

     	label_1 = new JLabel("< X <");
     	GridBagConstraints gbc_label_1 = new GridBagConstraints();
     	gbc_label_1.insets = new Insets(0, 0, 5, 5);
     	gbc_label_1.gridx = 2;
     	gbc_label_1.gridy = 1;
     	optionPanel.add(label_1, gbc_label_1);
     	
     	txtXmax = new JTextField();
     	GridBagConstraints gbc_txtXmax = new GridBagConstraints();
     	gbc_txtXmax.insets = new Insets(0, 0, 5, 5);
     	gbc_txtXmax.gridx = 3;
     	gbc_txtXmax.gridy = 1;
     	optionPanel.add(txtXmax, gbc_txtXmax);
     	txtXmax.setText("" + DEFAULT_BOUNDS[1]);
     	txtXmax.setColumns(10);
     	
     	txtYmin = new JTextField();
     	GridBagConstraints gbc_txtYmin = new GridBagConstraints();
     	gbc_txtYmin.insets = new Insets(0, 0, 5, 5);
     	gbc_txtYmin.gridx = 1;
     	gbc_txtYmin.gridy = 2;
     	optionPanel.add(txtYmin, gbc_txtYmin);
     	txtYmin.setText("" + DEFAULT_BOUNDS[2]);
     	txtYmin.setColumns(10);
     	
     	label_2 = new JLabel("< Y <");
     	GridBagConstraints gbc_label_2 = new GridBagConstraints();
     	gbc_label_2.insets = new Insets(0, 0, 5, 5);
     	gbc_label_2.gridx = 2;
     	gbc_label_2.gridy = 2;
     	optionPanel.add(label_2, gbc_label_2);
     	
     	txtYmax = new JTextField();
     	GridBagConstraints gbc_txtYmax = new GridBagConstraints();
     	gbc_txtYmax.insets = new Insets(0, 0, 5, 5);
     	gbc_txtYmax.gridx = 3;
     	gbc_txtYmax.gridy = 2;
     	optionPanel.add(txtYmax, gbc_txtYmax);
     	txtYmax.setText("" + DEFAULT_BOUNDS[3]);
     	txtYmax.setColumns(10);
     	
     	txtZmin = new JTextField();
     	GridBagConstraints gbc_txtZmin = new GridBagConstraints();
     	gbc_txtZmin.insets = new Insets(0, 0, 5, 5);
     	gbc_txtZmin.gridx = 1;
     	gbc_txtZmin.gridy = 3;
     	optionPanel.add(txtZmin, gbc_txtZmin);
     	txtZmin.setText("" + DEFAULT_BOUNDS[4]);
     	txtZmin.setColumns(10);
     	
     	label_3 = new JLabel("< Z <");
     	GridBagConstraints gbc_label_3 = new GridBagConstraints();
     	gbc_label_3.insets = new Insets(0, 0, 5, 5);
     	gbc_label_3.gridx = 2;
     	gbc_label_3.gridy = 3;
     	optionPanel.add(label_3, gbc_label_3);
     	
     	txtZmax = new JTextField();
     	GridBagConstraints gbc_txtZmax = new GridBagConstraints();
     	gbc_txtZmax.insets = new Insets(0, 0, 5, 5);
     	gbc_txtZmax.gridx = 3;
     	gbc_txtZmax.gridy = 3;
     	optionPanel.add(txtZmax, gbc_txtZmax);
     	txtZmax.setText("" + DEFAULT_BOUNDS[5]);
     	txtZmax.setColumns(10);
	}

	private void autoResize(){
		controlsWidth = frame.getWidth() - CANVAS_INITIAL_WIDTH;
		controlsHeight = frame.getHeight() - CANVAS_INITIAL_HEIGTH;
		tabbedPane.setPreferredSize(new Dimension(tabbedPane.getWidth(),tabbedPane.getHeight()));
		frame.setMinimumSize(new Dimension(600, 400));
		// Auto resize frame.
		resizeTimer = new javax.swing.Timer(100, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				plotter.updateSize(frame.getWidth()- controlsWidth,frame.getHeight()- controlsHeight);
				frame.pack();
			}
		});
		frame.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				resizeTimer.restart();
			}
		});
	}

	/*
	 * Add new plot.
	 */
	private void addPlot(String[] expr, Color3f color, float[] bounds, float stepSize) {
		// Create the function.
		try{
		Function newFunc = FunctionUtil.createFunction(expr,color,bounds,stepSize);
		if(newFunc.getClass().equals(ParametricFunction.class)){
			paramFunctionList.add(newFunc);
		}
		else{
			stdFunctionList.add(newFunc);
		}
		spawnNewPlotterThread(newFunc);
		frame.pack();
		} catch (ExpressionParseException e) {
			String message = e.getMessage();
			JLabel label = new JLabel(message,JLabel.CENTER);
			JOptionPane.showMessageDialog(frame,label);
		} catch (IllegalEquationException e) {
			String message = e.getMessage();
			JLabel label = new JLabel(message,JLabel.CENTER);
			JOptionPane.showMessageDialog(frame,label);
		} catch (UndefinedVariableException e) {
			String message = e.getMessage();
			JLabel label = new JLabel(message,JLabel.CENTER);
			JOptionPane.showMessageDialog(frame,label);
		}
	}

	/*
	 * Update a function.
	 */
	private void updatePlot(Function oldFunc, String newExpr[], Color3f newColor, float[] bounds, float stepsize) {
		// Try evaluating the function.
		try {
			Function newFunc = FunctionUtil.createFunction(newExpr, newColor, bounds, stepsize);
			if(newFunc.getClass().equals(ParametricFunction.class)){
				paramFunctionList.set(paramFunctionList.indexOf(oldFunc),newFunc);
			}
			else{
				stdFunctionList.set(stdFunctionList.indexOf(oldFunc),newFunc);
			}
			plotter.removePlot(oldFunc);
			spawnNewPlotterThread(newFunc);
			frame.pack();
		} 
		// Catch error.
		catch (ExpressionParseException e) {
			String message = e.getMessage();
			JLabel label = new JLabel(message,JLabel.CENTER);
			JOptionPane.showMessageDialog(frame,label);
		} catch (IllegalEquationException e) {
			String message = e.getMessage();
			JLabel label = new JLabel(message,JLabel.CENTER);
			JOptionPane.showMessageDialog(frame,label);
		} catch (UndefinedVariableException e) {
			String message = e.getMessage();
			JLabel label = new JLabel(message,JLabel.CENTER);
			JOptionPane.showMessageDialog(frame,label);
		}
	}

	/*
	 * Delete a function.
	 */
	private void deletePlot(Function f) {
		plotter.removePlot(f);
		if(f.getClass().equals(ParametricFunction.class)){
			paramFunctionList.remove(f);
		}
		else{
			stdFunctionList.remove(f);
		}
		frame.pack();
	}

	/*
	 * Return current bounds (set in GUI).
	 */
	private float[] getBounds(){
		float[] bounds = new float[6];
		bounds[0] = Float.parseFloat(txtXmin.getText());
		bounds[1] = Float.parseFloat(txtXmax.getText());
		bounds[2] = Float.parseFloat(txtYmin.getText());
		bounds[3] = Float.parseFloat(txtYmax.getText());
		bounds[4] = Float.parseFloat(txtZmin.getText());
		bounds[5] = Float.parseFloat(txtZmax.getText());
		return bounds;
	}

	/*
	 * Spawn an edit dialog and process the input.
	 */
	// TODO: Think the design through! Current thoughts:
	//			- Step size should be a bar from "min" to "max" ensure no errors (e.g. OutOfMemoryError), where min/max depends on the x, y, z limits.
	//			- Maybe faster implicit should be a function dependent property?
	//			- XMin, XMax and ZMax should be added; but how should this be handled in the parametric case?
	private void spawnEditDialog(final Function f) {
		editDialog = new JDialog();
		editDialog.setLocation(frame.getLocationOnScreen());
		EditOptionPanel editOptionPanel = new EditOptionPanel(colorList, f);
		editOptionPanel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Function wrapFunction = (Function) e.getSource();
				if(wrapFunction.getStepsize() != f.getStepsize()){
					updatePlot(f, f.getExpression(), wrapFunction.getColor(), wrapFunction.getBounds(), wrapFunction.getStepsize());
				}
				else if(!wrapFunction.getColor().equals(f.getColor())){
					if(!f.getClass().equals(ParametricFunction.class)){
						stdFunctionList.get(stdFunctionList.indexOf(f)).setColor(wrapFunction.getColor());
					}
					else{
						paramFunctionList.get(paramFunctionList.indexOf(f)).setColor(wrapFunction.getColor());
					}
				}
				editDialog.setVisible(false);
			}
		});
		editDialog.getContentPane().add(editOptionPanel);
		editDialog.pack();
		editDialog.setVisible(true);
	}

	/*
	 * Spawn simple color chooser.
	 */
	private void spawnColorChooser(){
		if(colorDialog == null){
		colorDialog = new JDialog();
		colorDialog.setLocation(frame.getLocationOnScreen());
		ColorOptionPanel colorOptionPanel = new ColorOptionPanel();
		colorOptionPanel.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				if(e.getSource().equals("CLOSE")){
					colorDialog.setVisible(false);
				}
				else if(!colorList.contains(e.getSource())){
					colorList.add((Color3f) e.getSource());
				}
			}
		});
		colorDialog.getContentPane().add(colorOptionPanel);
		colorDialog.pack();
		}
		colorDialog.setVisible(true);
	}
	
	/*
	 * Spawn simple import dialog.
	 */
	private void spawnImportDialog(String type){
		JFrame frame = new JFrame();
		if(filePath == null){
			filePath = File.separator+"tmp";
		}
		JFileChooser fc = new JFileChooser(new File(filePath));
		fc.showOpenDialog(frame);
		inputFile = fc.getSelectedFile();
		
		if (inputFile != null) {
			try{
				filePath=inputFile.getPath().replace(inputFile.getName(), "");
				if(type.equals("ColorList")){
					colorList = (ColorList) ObjectReader.ObjectFromFile(inputFile);
				}
				if(type.equals("FunctionList")){
					ZippedFunction[][] importLists = (ZippedFunction[][]) ObjectReader.ObjectFromFile(inputFile);
					//Erase current work space.
					for(int i = stdFunctionList.size()-1; i >= 0; i--){
						deletePlot(stdFunctionList.get(i));
					}
					for(int i = paramFunctionList.size()-1; i >= 0; i--){
						deletePlot(paramFunctionList.get(i));
					}
					//Read new functions from zipped object.
					for(int i = 0; i < importLists[0].length; i++){
						addPlot(importLists[0][i].getExpression(), importLists[0][i].getColor(), importLists[0][i].getBounds(), importLists[0][i].getStepsize());
						stdFunctionList.get(i).setSelected(importLists[0][i].isSelected());
						stdFunctionList.get(i).setVisible(importLists[0][i].isVisible());
					}
					for(int i = 0; i < importLists[1].length; i++){
						addPlot(importLists[1][i].getExpression(), importLists[1][i].getColor(), importLists[1][i].getBounds(), importLists[1][i].getStepsize());
						stdFunctionList.get(i).setSelected(importLists[1][i].isSelected());
						stdFunctionList.get(i).setVisible(importLists[1][i].isVisible());
					}
				}
			}
			catch(Exception e){
				String message = e.getMessage();
				JLabel label = new JLabel(message,JLabel.CENTER);
				JOptionPane.showMessageDialog(frame,label);
			}
		}
	}

	/*
	 * Spawn simple export dialog.
	 */
	private void spawnExportDialog(Object o){
		outputFile = null;
		JFrame frame = new JFrame();
		// Remember dir.
		if(filePath == null){
			filePath = File.separator+"tmp";
		}
		JFileChooser fc = new JFileChooser(new File(filePath));
		fc.showSaveDialog(frame);
		outputFile = fc.getSelectedFile();
		// Write file.
		if (outputFile != null){
			filePath=outputFile.getPath().replace(outputFile.getName(), "");
			ObjectWriter.ObjectToFile(outputFile, o);
		}
	}
	
	private void spawnNewPlotterThread(final Function function) {
		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

			@Override
			protected Void doInBackground() throws Exception {
				System.out.println("Starter");
				plotter.plotFunction(function);
				return null;
			}
			
			@Override
			protected void done() {
				System.out.println("Færdig");
			}
			
		};
		plottingQueue.execute(worker);
	}
}
