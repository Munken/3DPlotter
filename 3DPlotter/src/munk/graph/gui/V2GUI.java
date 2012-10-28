package munk.graph.gui;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.vecmath.Color3f;

import munk.emesp.exceptions.IllegalExpressionException;
import munk.graph.IO.*;
import munk.graph.appearance.Colors;
import munk.graph.function.*;
import munk.graph.function.implicit.ImplicitFunction;
import munk.graph.function.implicit.SphericalFunction;
import munk.graph.gui.labels.FunctionLabel;
import munk.graph.gui.panel.ColorOptionPanel;
import munk.graph.gui.panel.tab.*;


/**
 * A simple GUI for the 3DPlotter application.
 * 
 * @author xXx
 *
 */
public class V2GUI {

	// Constants
	private static final int CANVAS_INITIAL_WIDTH = 600;
	private static final int CANVAS_INITIAL_HEIGTH = 600;
	
	// GUI Variables.
	private static V2GUI window;
	private JFrame frame;
	private JTabbedPane tabbedPane;
	private FunctionTab stdFuncTab;
 	private FunctionTab paramFuncTab;
 	private FunctionTab sphFuncTab;
	private JPanel canvasPanel;
	private JDialog colorDialog;

	// Non-GUI variables.
	private Plotter3D plotter;
	private int controlsWidth;
	private int controlsHeight;
	private HashMap<Function, FunctionLabel> map = new HashMap<Function, FunctionLabel>();
	private javax.swing.Timer resizeTimer;
	private ColorList colorList;
	private String filePath;
	
	// Menu variables
	private JMenuBar menuBar;
	private JMenu mnFile;
	private JMenuItem mntmSaveProject, mntmLoadProject, mntmExit, mntmPrintCanvas;
	private JMenu mnColorOptions;
	private JMenu mnHelp;
	private JMenuItem mntmDocumentation, mntmAbout;
	private JMenuItem mntmImportColors, mntmExportColors, mntmAddCustomColor;
	private String	defaultImageExtension = "png";
 	
	// Template functions
 	private Function stdTemplateFunc;
 	private Function paramTemplateFunc;
 	private Function sphTemplateFunc;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		UIManager.put("Slider.onlyLeftMouseButtonDrag", Boolean.FALSE);
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
		colorList = new ColorList();
		filePath = File.separator+"tmp";
		try {
			initialize();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/**
	 * Initialize;
	 * @throws Exception 
	 */
	private void initialize() throws Exception{
		// Set up template functions.
		sphTemplateFunc = new TemplateFunction(new String[]{"0","1","0","pi","0","2*pi"}, new float[]{0.1f,0.1f,0.1f},colorList);
		paramTemplateFunc = new TemplateFunction(new String[]{"0","2*pi","0","2*pi"}, new float[]{0.1f,0.1f},colorList);
		stdTemplateFunc = new TemplateFunction(new String[]{"-1","1","-1","1","-1","1"}, new float[]{0.1f,0.1f,0.1f},colorList);
		
		// Initialize GUI components.
		initFrame();
		initTabbedPane();
		init3Dplotter();
     	initFunctionTabs();
		initMenuBar();
		initIcon();
		initPicking();
     
     	// Update references.
     	paramFuncTab.updateReferences(paramTemplateFunc);
     	stdFuncTab.updateReferences(stdTemplateFunc);
     	sphFuncTab.updateReferences(sphTemplateFunc);
     	
     	// Finish up.
     	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
     	frame.setVisible(true);
     	frame.pack();
     	
     	// Test function
     	try {
			stdFuncTab.addPlot(new String[]{"y = sin(x*5)*cos(z*5)"}, Colors.RED, new String[]{"-1","1","-1","1","-1","1"}, new float[]{(float) 0.1,(float) 0.1,(float) 0.1});
		} catch (IllegalExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
     	autoResize();
	}
	
	private void initPicking() {
		plotter.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e)) {
					Function result = plotter.getPickedFunction(e);
					if (result != null) {
						switchToFunction(result);
					}
				}
			}
		});
		
	}
	
	private void switchToFunction(Function function) {
		
		FunctionTab wantedTab = null;
		if (function instanceof XYZFunction || function instanceof ImplicitFunction) {
			wantedTab = stdFuncTab;
		} else if (function instanceof ParametricFunction){
			wantedTab = paramFuncTab;
		} else {
			wantedTab = sphFuncTab;
		}
		
		// The function tabs are a components. They just dont know it...
		Component[] tabs = tabbedPane.getComponents();
		
		for (Component candidate : tabs) {
			if (candidate == wantedTab) {
				tabbedPane.setSelectedComponent(candidate);
				
				wantedTab.setSelected(function);
			}
		}
	}

	private void initIcon() {
		Image icon = new ImageIcon("Icons/128.png").getImage();
		icon = icon.getScaledInstance(16, 16, Image.SCALE_SMOOTH);
		frame.setIconImage(icon);
	}

	private void initFrame(){
		frame = new JFrame("Ultra Mega Epic Xtreme Plotter 3D");
		frame.setBounds(0, 0, 1000, 1000);
     	GridBagLayout gbl = new GridBagLayout();
     	gbl.columnWidths = new int[]{10, 300, 0, 0, 0};
     	gbl.rowHeights = new int[]{2, 0, 5, 0};
     	gbl.columnWeights = new double[]{0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
     	gbl.rowWeights = new double[]{0.0, 2.0, 0.0, Double.MIN_VALUE};
     	frame.getContentPane().setLayout(gbl);
	}

	private void initTabbedPane(){
     	tabbedPane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
     	GridBagConstraints gbc_tabbedPane = new GridBagConstraints();
     	gbc_tabbedPane.insets = new Insets(0, 0, 5, 5);
     	gbc_tabbedPane.fill = GridBagConstraints.BOTH;
     	gbc_tabbedPane.gridx = 1;
     	gbc_tabbedPane.gridy = 1;
     	frame.getContentPane().add(tabbedPane, gbc_tabbedPane);
     	


     	
	}
	
	private void init3Dplotter(){
		canvasPanel = new JPanel();
     	plotter = new Plotter3D();
    	GridBagConstraints gbc_plotter = new GridBagConstraints();
    	gbc_plotter.gridheight = 3;
     	gbc_plotter.insets = new Insets(0, 0, 5, 5);
     	gbc_plotter.gridx = 4;
     	gbc_plotter.gridy = 1;
     	canvasPanel.add(plotter, gbc_plotter);
     	GridBagConstraints gbc_list = new GridBagConstraints();
     	gbc_list.anchor = GridBagConstraints.NORTH;
     	gbc_list.insets = new Insets(0, 0, 5, 5);
     	gbc_list.fill = GridBagConstraints.HORIZONTAL;
     	gbc_list.gridx = 1;
     	gbc_list.gridy = 2;
     	GridBagConstraints gbc_canvasPanel = new GridBagConstraints();
     	gbc_canvasPanel.fill = GridBagConstraints.BOTH;
     	gbc_canvasPanel.gridheight = 2;
     	gbc_canvasPanel.gridy = 1;
     	gbc_canvasPanel.gridx = 3;
     	frame.getContentPane().add(canvasPanel, gbc_canvasPanel);
     	

	}

	private void initFunctionTabs() throws Exception{
		ActionListener a = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(e.getSource() instanceof Exception){
					showMessageDialogThreadSafe(((Exception) e.getSource()).getMessage());
				}
			}
		};
		
		stdFuncTab = new XYZFunctionTab(colorList, map, stdTemplateFunc, plotter);
		stdFuncTab.addActionListener(a);
		tabbedPane.addTab("Standard equations", (Component) stdFuncTab);
		paramFuncTab = new ParametricFunctionTab(colorList, map, paramTemplateFunc, plotter);
		paramFuncTab.addActionListener(a);
		tabbedPane.addTab("Parametric equations", (Component) paramFuncTab);   
		sphFuncTab = new SphericalFunctionTab(colorList, map, sphTemplateFunc, plotter);
		sphFuncTab.addActionListener(a);
		tabbedPane.addTab("Spherical equations", (Component) sphFuncTab);
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
	 * Spawn simple color chooser.
	 */
	private void spawnColorChooser(){
		if(colorDialog == null){
		colorDialog = new JDialog();
		colorDialog.setLocation(frame.getLocationOnScreen());
		ColorOptionPanel colorOptionPanel = new ColorOptionPanel(colorList);
		colorOptionPanel.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(e.getID() == 0){
					addColor((Color3f) e.getSource());
				}
				else if(e.getID() == 1){
					if((int) e.getSource() >= 0){
					removeColor((int) e.getSource());
					}
				}
				else{
					colorDialog.setVisible(false);
				}
			}

			
		});
		colorDialog.getContentPane().add(colorOptionPanel);
		colorDialog.pack();
		}
		colorDialog.setVisible(true);
	}
	
	
	
	
	private void addColor(Color3f color) {
		colorList.add(color);
		updateColorChooser();
	}
	
	private void removeColor(int index) {
		colorList.remove(index);
		updateColorChooser();
	}

	private void updateColorChooser() {
		stdFuncTab.updateColors();
		paramFuncTab.updateColors();
		sphFuncTab.updateColors();
	}
	
	private void savePlotToDisk(final File outputFile) {
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				RenderedImage outputImage = plotter.takeScreenshot();

				try {
					boolean canWrite = ImageIO.write(outputImage, GuiUtil.getFileExtension(outputFile), outputFile);
					if (!canWrite) {
						
						File newPath = new File(outputFile.getAbsolutePath() + "." + defaultImageExtension);
						ImageIO.write(outputImage, defaultImageExtension, newPath);
						showMessageDialogThreadSafe("Unknown image format. Defaulted to " + defaultImageExtension);
					}
				} catch (IOException e) {
					showMessageDialogThreadSafe("Unable to write to file");
				}
			}
		});
		t.start();
		
	}

	private void showMessageDialogThreadSafe(final String message) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JOptionPane.showMessageDialog(frame, message);
			}
		});
	}
	
	private void saveAsImage() {
		String[][] fileEndings = {{"png"}, {"jpg", "jpeg"}, {"gif"}, {"bmp"}};
		String[] description = {"PNG image", "JPEG image", "GIF image", "Bitmap graphic"};
		
		File outputFile = GuiUtil.spawnExportDialog(filePath, fileEndings, description, frame);
		if(outputFile != null){
			filePath=outputFile.getPath().replace(outputFile.getName(), "");

			savePlotToDisk(outputFile);
			
		}
	}
	
	private void exportColors() {
		File outputFile = GuiUtil.spawnExportDialog(filePath, frame);
		if(outputFile != null){
			filePath=outputFile.getPath().replace(outputFile.getName(), "");
			try {
				XMLWriter writer = new XMLWriter();
				
				writer.addColor(colorList);
				writer.output(outputFile.getAbsolutePath());
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(frame,new JLabel("Unable to write file.",JLabel.CENTER));
			}
		}
	}
	
	private void importColors() {
		File inputFile = GuiUtil.spawnImportDialog(filePath, frame);
		if(inputFile != null){
			filePath=inputFile.getPath().replace(inputFile.getName(), "");
			try{
				XMLReader reader = new XMLReader(inputFile.getAbsolutePath());
				
				java.util.List<Color3f> list = reader.processColors();
				for (Color3f color : list) {
					colorList.add(color);
				}
				updateColorChooser();
			}
			catch(Exception e){
				JOptionPane.showMessageDialog(frame,new JLabel("Unable to read color list from file.",JLabel.CENTER));
			}
		}
	}
	
	private void exportWorkspace() {
		File outputFile = GuiUtil.spawnExportDialog(filePath, frame);
		if(outputFile != null){		
			XMLWriter writer = new XMLWriter();
			
			writer.addFunctions(stdFuncTab.getFunctionList());
			writer.addFunctions(paramFuncTab.getFunctionList());
			writer.addColor(colorList);
			writer.output(outputFile.getAbsolutePath());
			
			filePath=outputFile.getPath().replace(outputFile.getName(), "");
		}
	}

	private void importWorkspace() {
		File inputFile = GuiUtil.spawnImportDialog(filePath, frame);
		if(inputFile != null){
			filePath=inputFile.getPath().replace(inputFile.getName(), "");
			try{
//				ZippedFunction[][] importLists = (ZippedFunction[][]) ObjectReader.ObjectFromFile(inputFile);
				// Determine if current workspace should be erased.
				eraseWorkspace();
				//Read new functions from xml
				
				XMLReader reader = new XMLReader(inputFile.getAbsolutePath());
				java.util.List<Function> importFunctions = reader.processFunctions();
				
				for (Function f : importFunctions) {
					if (f.getClass() == XYZFunction.class)
						stdFuncTab.addPlot(f);
					else if (f.getClass() == ParametricFunction.class)
						paramFuncTab.addPlot(f);
					else 
						sphFuncTab.addPlot(f);
					
					
					colorList.add(f.getColor());
				}
				
				java.util.List<Color3f> colors = reader.processColors();
				
				for (Color3f c : colors) {
					System.out.println(c);
					colorList.add(c);
				}
				
				updateColorChooser();
			}
			catch(Exception ex){
				JOptionPane.showMessageDialog(frame,new JLabel("Unable to import workspace from file.",JLabel.CENTER));
				ex.printStackTrace();
			} 
		}
	}

	private void eraseWorkspace() {
		boolean eraseWorkspace = (map.size() == 0) ||
				(0 == JOptionPane.showOptionDialog(frame, 
						"Would you like to erase current workspace during import?",
						"Import Dialog", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null));

		if(eraseWorkspace){
			java.util.List<Function> functions = new ArrayList<Function>(map.keySet());
			
			for (int i = functions.size() - 1; i >= 0; i--) {
				Function f = functions.get(i);
				
				if(f.getClass() == ParametricFunction.class){
					paramFuncTab.deletePlot(f);
				}
				else if(f.getClass() == SphericalFunction.class){
					sphFuncTab.deletePlot(f);
				}
				else{
					stdFuncTab.deletePlot(f);
				}
			}
		}
	}
	
	private void initMenuBar(){
		menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		mnFile = new JMenu("File");
		menuBar.add(mnFile);

		mntmSaveProject = new JMenuItem("Export workspace", new ImageIcon("Icons/save.png"));
		mntmSaveProject.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_MASK));
		mntmSaveProject.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				exportWorkspace();
			}
		});
		mnFile.add(mntmSaveProject);

		mntmLoadProject = new JMenuItem("Import workspace", new ImageIcon("Icons/file.png"));
		mntmLoadProject.setMnemonic('I');
		mntmLoadProject.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, KeyEvent.CTRL_MASK));
		mntmLoadProject.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				importWorkspace();
			}
		});
		mnFile.add(mntmLoadProject);
		
		mntmPrintCanvas = new JMenuItem("Save as image", new ImageIcon("Icons/png.png"));
		mntmPrintCanvas.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, KeyEvent.CTRL_MASK));
		mntmPrintCanvas.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				saveAsImage();
			}
		});
		mnFile.add(mntmPrintCanvas);
		
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
				exportColors();
			}
		});
		mnColorOptions.add(mntmExportColors);
		
		mntmImportColors = new JMenuItem("Import colors", new ImageIcon("Icons/file.png"));
		mntmImportColors.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				importColors();
			}
		});
		mnColorOptions.add(mntmImportColors);
		
		mntmAddCustomColor = new JMenuItem("Customize colors", new ImageIcon("Icons/settings.png"));
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
				String message = "<html> <center> 3DPlotter is a free simple 3D graphing tool. It is currently being developed <br> as a spare time project by Michael Munch & Emil Haldrup Eriksen. </center> </html>";
				JLabel label = new JLabel(message);
				JOptionPane.showMessageDialog(frame,label,"About",JOptionPane.PLAIN_MESSAGE,null);
			}
		});
		mnHelp.add(mntmAbout);
	}
}