package munk.graph.gui.panel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.vecmath.Color3f;

import munk.graph.function.Function;
import munk.graph.function.IllegalEquationException;
import munk.graph.function.TemplateFunction;
import munk.graph.gui.ColorList;
import munk.graph.gui.FunctionLabel;
import munk.graph.gui.GuiUtil;
import munk.graph.gui.ParametricFunctionLabel;
import munk.graph.gui.Plotter3D;
import munk.graph.gui.StdFunctionLabel;
import munk.graph.gui.listener.FunctionEvent;
import munk.graph.gui.listener.FunctionListener;

import com.graphbuilder.math.ExpressionParseException;
import com.graphbuilder.math.UndefinedVariableException;

@SuppressWarnings("serial")
public abstract class AbstractFunctionTab extends JPanel implements FunctionTab{
	
	// Constants.
	static final Color	NORMAL_COLOR	= Color.WHITE;
	static final Color SELECTED_COLOR = new Color(189, 214, 224);
	
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
	private ExecutorService plottingQueue = Executors.newSingleThreadExecutor(); // Only plot one at a time
	private ColorList colorList;
	private ArrayList<ActionListener> listeners = new ArrayList<ActionListener>();;

	public AbstractFunctionTab(ColorList colorList, HashMap<Function, FunctionLabel> map, Function templateFunc, Plotter3D plotter) throws Exception{
		this.templateFunc = templateFunc;
		this.map = map;
		this.plotter = plotter;
		this.colorList = colorList;
	}
	
	protected void init() throws ExpressionParseException{
		input = new JTextField[getNoOfInputs()];
		
		double[] rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		rowWeights[getNoOfInputs()+2] = 1.0; 
		
		GridBagLayout gbl_functionPanel = new GridBagLayout();
		gbl_functionPanel.columnWidths = new int[]{5, 25, 50, 50, 30, 25, 5, 0};
		gbl_functionPanel.rowHeights = new int[]{10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 10};
		gbl_functionPanel.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_functionPanel.rowWeights = rowWeights;
		this.setLayout(gbl_functionPanel);

		// Function input field.
		for(int i = 0; i < getNoOfInputs(); i++){
			// Creation.
			input[i] = new JTextField();
			GridBagConstraints gbc_stdFuncInput = new GridBagConstraints();
			gbc_stdFuncInput.gridwidth = 5;
			gbc_stdFuncInput.insets = new Insets(0, 0, 5, 5);
			gbc_stdFuncInput.fill = GridBagConstraints.HORIZONTAL;
			gbc_stdFuncInput.anchor = GridBagConstraints.NORTH;
			gbc_stdFuncInput.gridx = 1;
			gbc_stdFuncInput.gridy = i+1;
			// Setup.
			GuiUtil.setupUndoListener(input[i]);
			setupInputListeners(input[i]);
			this.add(input[i], gbc_stdFuncInput);
		}

		// OptionPanel.
		mainOP = new JPanel();
		mainOP.setBorder(BorderFactory.createEtchedBorder());
		GridBagConstraints gbc_mainOP = new GridBagConstraints();
		gbc_mainOP.fill = GridBagConstraints.BOTH;
		gbc_mainOP.gridwidth = 5;
		gbc_mainOP.insets = new Insets(0, 0, 5, 5);
		gbc_mainOP.gridx = 1;
		gbc_mainOP.gridy = getNoOfInputs()+1;
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
		gbc_funcPanelWrapper.gridy = getNoOfInputs()+2;;
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
	public abstract Function createNewFunction(String[] expressions, Color3f color, String[] bounds, float[] stepSize) throws ExpressionParseException, UndefinedVariableException, IllegalEquationException;

	private void setupInputListeners(final JTextField currentInput){
		currentInput.addKeyListener(new KeyAdapter() {
			// Plot the graph.

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER && currentInput.isFocusOwner()) {
					try{
						String[] equations = new String[getNoOfInputs()];
						for(int i = 0; i < getNoOfInputs(); i++){
							equations[i] = input[i].getText();
						}
						addPlot(equations,templateFunc.getColor(), gridOP.getGridBounds(), gridOP.getGridStepSize());
					} catch (ExpressionParseException | IllegalEquationException | UndefinedVariableException e1) {
						signalAll(new ActionEvent(e1, -1, ""));
						e1.printStackTrace();
					}
				}

			}
		});
		currentInput.addFocusListener(new FocusAdapter() {

			@Override
			public void focusGained(FocusEvent arg0) {
				try {
					setSelected(templateFunc);
				} catch (ExpressionParseException e) {
					signalAll(new ActionEvent(e, -1, ""));
					e.printStackTrace();
				}
			}
		});
	}

	public void setSelected(Function f) throws ExpressionParseException{
		if(f != selectedFunction){
			// Deselection.
			if(selectedFunction != null && selectedFunction == templateFunc){
				for(int i = 0; i < input.length; i++){
					input[i].setBackground(NORMAL_COLOR);
				}
			}
			else if (selectedFunction != null){
				try{
					map.get(selectedFunction).setSelected(false);
				}
				catch(NullPointerException e){
					// Do nothing.
				}
			}
			selectedFunction = f;

			// Selection.
			if(selectedFunction != null && selectedFunction == templateFunc){
				for(int i = 0; i < input.length; i++){
					input[i].setBackground(SELECTED_COLOR);
				}
				updateReferences(selectedFunction);
			}
			else if (selectedFunction != null){
				FunctionLabel selectedLabel = map.get(selectedFunction);
				updateReferences(selectedFunction);
				selectedLabel.setSelected(true);
			}
		}
	}
	
	protected FunctionLabel addXYZPlot(Function newFunction) {
		funcList.add(newFunction);
		
		final StdFunctionLabel label = new StdFunctionLabel(newFunction);
		
		label.addFocusListener(new FocusAdapter() {
			
			@Override
			public void focusGained(FocusEvent e) {
				try {
					setSelected(label.getMother());
				} catch (ExpressionParseException e1) {
					e1.printStackTrace();
				}
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
				try {
					setSelected(label.getMother());
				} catch (ExpressionParseException e1) {
					e1.printStackTrace();
				}
			}
		});
		innerFuncTab.add(label);
		return label;
	}

	/*
	 * Add new plot.
	 */
	public void addPlot(String[] expr, Color3f color, String[] bounds, float[] stepSize) throws ExpressionParseException, IllegalEquationException, UndefinedVariableException{
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
					} catch (ExpressionParseException
							| IllegalEquationException
							| UndefinedVariableException e1) {
						signalAll(new ActionEvent(e1, -1, ""));
						e1.printStackTrace();
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
					} catch (ExpressionParseException
							| IllegalEquationException
							| UndefinedVariableException e1) {
						signalAll(new ActionEvent(e1, -1, ""));
						e1.printStackTrace();
					}
				} 

			}
		};
	}

	/*
	 * Update a function.
	 */
	private void updatePlot(Function oldFunc, String newExpr[], Color3f newColor, String[] bounds, float[] stepSize) throws ExpressionParseException, IllegalEquationException, UndefinedVariableException {
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
		plottingQueue.execute(worker);
	}
	
	@Override
	public void updateColors() {
		apperanceOP.updateColors();
	}
	
	public void updateReferences(Function f) throws ExpressionParseException{
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
}

