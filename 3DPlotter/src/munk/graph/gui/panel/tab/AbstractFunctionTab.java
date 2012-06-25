package munk.graph.gui.panel.tab;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.*;
import javax.swing.Timer;
import javax.vecmath.Color3f;

import munk.emesp.exceptions.IllegalExpressionException;
import munk.graph.appearance.Colors;
import munk.graph.function.Function;
import munk.graph.function.TemplateFunction;
import munk.graph.gui.*;
import munk.graph.gui.labels.*;
import munk.graph.gui.listener.FunctionEvent;
import munk.graph.gui.listener.FunctionListener;
import munk.graph.gui.panel.AppearanceOptionPanel;
import munk.graph.gui.panel.gridoption.GridOptionPanel;

@SuppressWarnings("serial")
public abstract class AbstractFunctionTab extends JPanel implements FunctionTab{

	// Constants.
	private static final Color	NORMAL_COLOR	= Color.WHITE;
	private static final Color SELECTED_COLOR = new Color(189, 214, 224);
	private static final ExecutorService PLOTTING_QUEUE = Executors.newSingleThreadExecutor(); // Only plot one at a time


	private static Function ACTIVE_FUNCTION;

	// GUI variables.
	private JPanel outerFuncTab;
	protected JPanel innerFuncTab;
	private JScrollPane funcPanelWrapper;
	private JPanel mainOP;
	private GridOptionPanel gridOP;
	private AppearanceOptionPanel apperanceOP;
	private JTextField[] input;

	// Other variables.
	protected HashMap<Function, FunctionLabel> map;
	protected List<Function> funcList = new ArrayList<Function>();
	private Function templateFunc;
	private Function selectedFunction;
	private Plotter3D plotter;
	private ColorList colorList;
	private ArrayList<ActionListener> listeners = new ArrayList<ActionListener>();
	private int nInputs;


	public AbstractFunctionTab(ColorList colorList, HashMap<Function, FunctionLabel> map, 
			Function templateFunc, Plotter3D plotter, int nInputs) {
		this.templateFunc = templateFunc;
		this.map = map;
		this.plotter = plotter;
		this.colorList = colorList;
		this.nInputs = nInputs;

		addFunctionKeyboardShortcuts();
		init();
	}



	private void init() {
		input = new JTextField[nInputs];

		double[] rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		rowWeights[nInputs+2] = 1.0; 

		GridBagLayout gbl_functionPanel = new GridBagLayout();
		gbl_functionPanel.columnWidths = new int[]{5, 0, 50, 50, 30, 25, 5, 0};
		gbl_functionPanel.rowHeights = new int[]{10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 10};
		gbl_functionPanel.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_functionPanel.rowWeights = rowWeights;
		this.setLayout(gbl_functionPanel);


		// Function input field.
		for(int i = 0; i < nInputs; i++){
			// Creation.
			input[i] = new JTextField();
			GridBagConstraints gbc_stdFuncInput = new GridBagConstraints();
			gbc_stdFuncInput.gridwidth = 4;
			gbc_stdFuncInput.insets = new Insets(0, 0, 5, 5);
			gbc_stdFuncInput.fill = GridBagConstraints.HORIZONTAL;
			gbc_stdFuncInput.anchor = GridBagConstraints.NORTH;
			gbc_stdFuncInput.gridx = 2;
			gbc_stdFuncInput.gridy = i+1;

			// Setup.
			GuiUtil.setupUndoListener(input[i]);
			setupInputListeners(input[i]);
			this.add(input[i], gbc_stdFuncInput);

		}

		// Labels for the input fields
		String[] labelNames = labelNames();
		for (int i = 0; i < labelNames.length; i++) {
			GridBagConstraints gbc_FuncLabel = new GridBagConstraints();
			gbc_FuncLabel.gridwidth = 1;
			gbc_FuncLabel.insets = new Insets(0, 0, 5, 5);
			gbc_FuncLabel.fill = GridBagConstraints.HORIZONTAL;
			gbc_FuncLabel.anchor = GridBagConstraints.CENTER;
			gbc_FuncLabel.gridx = 1;
			gbc_FuncLabel.gridy = i+1;

			JLabel label = new JLabel(labelNames[i]);
			this.add(label, gbc_FuncLabel);
		}

		// OptionPanel.
		mainOP = new JPanel();
		mainOP.setBorder(BorderFactory.createEtchedBorder());
		GridBagConstraints gbc_mainOP = new GridBagConstraints();
		gbc_mainOP.fill = GridBagConstraints.BOTH;
		gbc_mainOP.gridwidth = 5;
		gbc_mainOP.insets = new Insets(0, 0, 5, 5);
		gbc_mainOP.gridx = 1;
		gbc_mainOP.gridy = nInputs+1;
		this.add(mainOP, gbc_mainOP);

		gridOP = getGridOptionPanel();
		gridOP.addFunctionListener(createGridOptionPanelListener());
		apperanceOP = new AppearanceOptionPanel(colorList, map);
		mainOP.setLayout(new BoxLayout(mainOP, BoxLayout.Y_AXIS));
		mainOP.add((Component) gridOP);
		mainOP.add(apperanceOP);

		// The standard function list
		outerFuncTab = new JPanel();
		funcPanelWrapper = new JScrollPane(outerFuncTab);
		funcPanelWrapper.setBorder(BorderFactory.createEtchedBorder());
		funcPanelWrapper.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		funcPanelWrapper.setMinimumSize(new Dimension(310,500));
		GridBagConstraints gbc_funcPanelWrapper = new GridBagConstraints();
		gbc_funcPanelWrapper.fill = GridBagConstraints.BOTH;
		gbc_funcPanelWrapper.gridwidth = 5;
		gbc_funcPanelWrapper.insets = new Insets(0, 0, 5, 5);
		gbc_funcPanelWrapper.gridx = 1;
		gbc_funcPanelWrapper.gridy = nInputs+2;;
		this.add(funcPanelWrapper, gbc_funcPanelWrapper);

		GridBagLayout gbl_stdFuncPanel = new GridBagLayout();
		gbl_stdFuncPanel.columnWidths = new int[]{0, 0};
		gbl_stdFuncPanel.rowHeights = new int[]{0, 0};
		gbl_stdFuncPanel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_stdFuncPanel.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		outerFuncTab.setLayout(gbl_stdFuncPanel);

		innerFuncTab = new JPanel();
		GridBagConstraints gbc_innerFuncPanel = new GridBagConstraints();
		gbc_innerFuncPanel.anchor = GridBagConstraints.NORTH;
		gbc_innerFuncPanel.fill = GridBagConstraints.HORIZONTAL;
		gbc_innerFuncPanel.gridx = 0;
		gbc_innerFuncPanel.gridy = 0;
		outerFuncTab.add(innerFuncTab, gbc_innerFuncPanel);
		innerFuncTab.setLayout(new BoxLayout(innerFuncTab, BoxLayout.Y_AXIS));
	}

	public abstract void addPlot(Function newFunction);

	public abstract Function createNewFunction(String[] expressions, Color3f color, 
			String[] bounds, float[] stepSize) 
					throws IllegalExpressionException;

	protected String[] labelNames() {
		return new String[0];
	}


	private void setupInputListeners(final JTextField currentInput){
		currentInput.addKeyListener(new KeyAdapter() {
			// Plot the graph.

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER && currentInput.isFocusOwner()) {
					try{
						String[] equations = new String[nInputs];
						for(int i = 0; i < nInputs; i++){
							equations[i] = input[i].getText();
						}
						addPlot(equations,templateFunc.getColor(), gridOP.getGridBounds(), gridOP.getGridStepSize());
					} catch (IllegalExpressionException e1) {
						signalAll(new ActionEvent(e1, -1, ""));
					}
				}

			}
		});
		currentInput.addFocusListener(new FocusAdapter() {

			@Override
			public void focusGained(FocusEvent arg0) {
				setSelected(templateFunc);
			}
		});
	}

	public void setSelected(Function f) {
		try {

			ACTIVE_FUNCTION = f;
			if(f != selectedFunction){
				// Deselection.
				if(selectedFunction != null && selectedFunction == templateFunc){
					for(int i = 0; i < input.length; i++){
						input[i].setBackground(NORMAL_COLOR);
					}
				}
				else if (selectedFunction != null){
					FunctionLabel label = map.get(selectedFunction);
					if (label != null)
						label.setSelected(false);
				}
				selectedFunction = f;

				// Selection of input fields.
				if(selectedFunction != null && selectedFunction == templateFunc){
					for(int i = 0; i < input.length; i++){
						input[i].setBackground(SELECTED_COLOR);
					}

					updateReferences(selectedFunction);
				}
			}

			// Needed for picking. The caret might not be active, 
			if (selectedFunction != null && selectedFunction != templateFunc){
				FunctionLabel selectedLabel = map.get(selectedFunction);
				updateReferences(selectedFunction);
				selectedLabel.setSelected(true);
			}
		} catch (IllegalExpressionException e ) {

		}
	}

	protected FunctionLabel addXYZPlot(Function newFunction) {
		funcList.add(newFunction);

		final StdFunctionLabel label = new StdFunctionLabel(newFunction);

		label.addFocusListener(new FocusAdapter() {

			@Override
			public void focusGained(FocusEvent e) {
				setSelected(label.getMother());
			}
		});
		innerFuncTab.add(label);
		return label;
	}

	protected FunctionLabel addParametricPlot(final Function newFunction) {
		funcList.add(newFunction);

		final ParametricFunctionLabel label = new ParametricFunctionLabel(newFunction);

		label.addFocusListener(new FocusAdapter() {

			@Override
			public void focusGained(FocusEvent e) {
				setSelected(label.getMother());
			}
		});
		innerFuncTab.add(label);
		return label;
	}

	/*
	 * Add new plot.
	 */
	public void addPlot(String[] expr, Color3f color, String[] bounds, float[] stepSize) throws IllegalExpressionException {
		// Create the function.
		Function newFunction = createNewFunction(expr,color,bounds,stepSize);
		addPlot(newFunction);
	}

	private FunctionListener createGridOptionPanelListener() {
		return new FunctionListener() {

			@Override
			public void functionChanged(FunctionEvent e) {
				Function func = e.getOldFunction();
				if(func.getClass() != TemplateFunction.class){

					try {
						updatePlot(func, func.getExpression(), func.getColor(), e.getStringBounds(), e.getStepsize());
					} catch (IllegalExpressionException e1) {
						signalAll(new ActionEvent(e1, -1, ""));
					}
				}
				else{
					func.setBoundsString(e.getStringBounds());
					func.setStepsize(e.getStepsize());
				}
			}
		};
	}

	protected FunctionListener createFunctionListener() {
		return new FunctionListener() {

			@Override
			public void functionChanged(FunctionEvent e) {
				FunctionEvent.ACTION action = e.getAction();
				Function func = e.getOldFunction();

				if (action == FunctionEvent.ACTION.VISIBILITY) {
					plotter.showPlot(func);

				} else if (action == FunctionEvent.ACTION.DELETE){
					deletePlot(func);

				} else if (action == FunctionEvent.ACTION.UPDATE) {
					try {
						updatePlot(func, e.getNewExpr(), e.getColor(), e.getStringBounds(), e.getStepsize());
					} catch (IllegalExpressionException e1) {
						signalAll(new ActionEvent(e1, -1, ""));
					}
				} 

			}
		};
	}

	/*
	 * Update a function.
	 */
	private void updatePlot(Function oldFunc, String newExpr[], Color3f newColor, String[] bounds, float[] stepSize) throws IllegalExpressionException {
		// Try evaluating the function.
		Function newFunc =  createNewFunction(newExpr, newColor, bounds, stepSize);
		newFunc.setView(oldFunc.getView());
		funcList.set(funcList.indexOf(oldFunc), newFunc);
		updateReferences(newFunc);
		FunctionLabel label = map.get(oldFunc);
		label.setMother(newFunc);
		map.remove(oldFunc);
		map.put(newFunc, label);
		setSelected(newFunc);
		plotter.removePlot(oldFunc);
		spawnNewPlotterThread(newFunc);
	}

	/*
	 * Delete a function.
	 */
	public void deletePlot(Function f) {
		f.cancel();
		plotter.removePlot(f);
		int index = funcList.indexOf(f);
		innerFuncTab.remove(index);
		funcList.remove(index);
		map.remove(f);
	}

	/*
	 * Spawn new plotter thread.
	 */
	protected void spawnNewPlotterThread(final Function function) {
		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

			@Override
			protected Void doInBackground() throws Exception {

				map.get(function).setIndeterminate(true);
				// Test of spinner.
				// Thread.currentThread().sleep(5000);
				plotter.plotFunction(function);
				return null;
			}

			@Override
			protected void done() {
				FunctionLabel label = map.get(function);
				if (label != null)
					label.setIndeterminate(false);
			}

		};
		PLOTTING_QUEUE.execute(worker);
	}

	@Override
	public void updateColors() {
		apperanceOP.updateColors();
	}

	public void updateReferences(Function f) throws IllegalExpressionException {
		gridOP.updateFuncReference(f);
		apperanceOP.updateFuncReference(f);
	}

	public List<Function> getFunctionList(){
		return funcList;
	}

	public void addActionListener(ActionListener a){
		listeners.add(a);
	}

	private void signalAll(ActionEvent e){
		for(ActionListener a : listeners){
			a.actionPerformed(e);
		}
	}

	private void addFunctionKeyboardShortcuts() {
		addShortcutBlinker();
		addShortcutDelete();
	}

	private void addShortcutDelete() {
		getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ctrl D"), "Delete Function");
		getActionMap().put("Delete Function", new AbstractAction() {


			public void actionPerformed(ActionEvent e) {
				if (ACTIVE_FUNCTION == null)
					return;

				deletePlot(ACTIVE_FUNCTION);

			};
		});
	}



	private void addShortcutBlinker() {
		getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ctrl B"), "Blink Function");
		getActionMap().put("Blink Function", new AbstractAction() {


			@Override
			public void actionPerformed(ActionEvent e) {
				if (ACTIVE_FUNCTION == null)
					return;


				setEnabled(false);
				final Color3f oldColor = ACTIVE_FUNCTION.getColor();
				ACTIVE_FUNCTION.setColor(Colors.WHITE);

				Timer timer = new Timer(300, new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {	
						ACTIVE_FUNCTION.setColor(oldColor);
						setEnabled(true);
					}
				});

				timer.setRepeats(false);
				timer.start();
			}
		});
	}
	
	
}

