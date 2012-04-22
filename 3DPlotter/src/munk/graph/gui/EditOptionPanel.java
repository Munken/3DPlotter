package munk.graph.gui;

import static munk.graph.gui.GuiUtil.getStepsize;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

import javax.swing.*;
import javax.vecmath.Color3f;

import munk.graph.function.Function;
import munk.graph.function.XYZFunction;
import munk.graph.gui.listener.FunctionEvent;
import munk.graph.gui.listener.FunctionListener;

import com.graphbuilder.math.ExpressionParseException;
import com.graphbuilder.math.UndefinedVariableException;

@SuppressWarnings("serial")
public class EditOptionPanel extends JPanel {
	
	private Function oldFunc;
	private JComboBox comboBox;
	private JSlider slider;
	private ColorList colorList;
	private List<FunctionListener> listeners = new ArrayList<FunctionListener>();
	
	public EditOptionPanel(final ColorList colorList, Function f){
		//setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{5, 0, 0, 0, 5, 0};
		gridBagLayout.rowHeights = new int[]{5, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 0.0, 1.0, 0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, 0.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JLabel resolution = new JLabel("Resolution");
		GridBagConstraints gbc_lblStepSize = new GridBagConstraints();
		gbc_lblStepSize.insets = new Insets(0, 0, 5, 5);
		gbc_lblStepSize.anchor = GridBagConstraints.EAST;
		gbc_lblStepSize.gridx = 1;
		gbc_lblStepSize.gridy = 1;
		add(resolution, gbc_lblStepSize);
		
		comboBox = new JComboBox(colorList.getIconList());
		comboBox.setEnabled(false);
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.insets = new Insets(0, 0, 5, 5);
		gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox.gridx = 3;
		gbc_comboBox.gridy = 1;
		add(comboBox, gbc_comboBox);
		comboBox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Color3f selectedColor = (Color3f) colorList.get(comboBox.getSelectedIndex());
				if(!selectedColor.equals(oldFunc.getColor()) && comboBox.isEnabled()){
					notifyColorUpdated(selectedColor);
				}
			}
		});
		
		slider = new JSlider();
		slider.setEnabled(false);
		slider.setPreferredSize(new Dimension(150,20));
		GridBagConstraints gbc_slider = new GridBagConstraints();
		gbc_slider.insets = new Insets(0, 0, 5, 5);
		gbc_slider.gridx = 2;
		gbc_slider.gridy = 1;
		add(slider, gbc_slider);
		slider.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseReleased(MouseEvent arg0) {
				float[] newStepsizes = getStepsize(slider.getValue(), oldFunc.getBounds());
				if(slider.isEnabled() && !Arrays.equals(newStepsizes, oldFunc.getStepsizes())){
					notifyStepsizeChanged(newStepsizes);
				}
			}
		});

		this.colorList = colorList;
	}
	


	public void updateFuncReference(Function f){
		slider.setEnabled(true);
		comboBox.setEnabled(true);
		oldFunc = f;
		comboBox.setSelectedIndex(colorList.indexOf(f.getColor()));
		slider.setValue(GuiUtil.getSliderValue(f.getStepsize(),f.getBounds()));
	}
	
	public void updateColors(){
		comboBox.setModel(new JComboBox(colorList.getIconList()).getModel());
	}
	
	public void initMode(){
		slider.setEnabled(false);
		comboBox.setEnabled(false);
	}
	
	public void notifyColorUpdated(Color3f newColor) {
		FunctionEvent e = new FunctionEvent(oldFunc, oldFunc.getExpression(), 
												newColor, oldFunc.getBounds(), 
												oldFunc.getStepsizes(), FunctionEvent.ACTION.COLOR_CHANGE);
		notifyListeners(e);
	}
	
	private void notifyStepsizeChanged(float[] newStepsizes) {
		FunctionEvent e = new FunctionEvent(oldFunc, oldFunc.getExpression(), 
				oldFunc.getColor(), oldFunc.getBounds(), 
				newStepsizes, FunctionEvent.ACTION.STEPSIZE_CHANGED);
		notifyListeners(e);
	}
	
	private void notifyListeners(FunctionEvent e) {
		for (FunctionListener l : listeners) {
			l.functionChanged(e);
		}
	}
	
	public void addFunctionListener(FunctionListener l) {
		listeners.add(l);
	}
	
	
//	
//	public EditOptionPanel(final ColorList colorList, Function f, final ActionListener a){
//		//setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
//		GridBagLayout gridBagLayout = new GridBagLayout();
//		gridBagLayout.columnWidths = new int[]{5, 0, 0, 0, 5, 0};
//		gridBagLayout.rowHeights = new int[]{5, 0, 0, 0};
//		gridBagLayout.columnWeights = new double[]{1.0, 0.0, 1.0, 0.0, 1.0, Double.MIN_VALUE};
//		gridBagLayout.rowWeights = new double[]{1.0, 0.0, 1.0, Double.MIN_VALUE};
//		setLayout(gridBagLayout);
//		
//		JLabel resolution = new JLabel("Resolution");
//		GridBagConstraints gbc_lblStepSize = new GridBagConstraints();
//		gbc_lblStepSize.insets = new Insets(0, 0, 5, 5);
//		gbc_lblStepSize.anchor = GridBagConstraints.EAST;
//		gbc_lblStepSize.gridx = 1;
//		gbc_lblStepSize.gridy = 1;
//		add(resolution, gbc_lblStepSize);
//		
//		comboBox = new JComboBox(colorList.getIconList());
//		comboBox.setEnabled(false);
//		GridBagConstraints gbc_comboBox = new GridBagConstraints();
//		gbc_comboBox.insets = new Insets(0, 0, 5, 5);
//		gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
//		gbc_comboBox.gridx = 3;
//		gbc_comboBox.gridy = 1;
//		add(comboBox, gbc_comboBox);
//		comboBox.addActionListener(new ActionListener() {
//			
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				Function wrapFunction = null;
//				Color3f selectedColor = (Color3f) colorList.get(comboBox.getSelectedIndex());
//				if(comboBox.isEnabled() && !selectedColor.equals(oldFunc.getColor())){
//				try {
//					wrapFunction = new XYZFunction(
//							new String[] {"y=x"}, (Color3f) colorList.get(comboBox.getSelectedIndex()), 
//							oldFunc.getBounds(), oldFunc.getStepsizes());
//					
//				} catch (ExpressionParseException | UndefinedVariableException ex) {
//					ex.printStackTrace();
//				}
//				a.actionPerformed(new ActionEvent(new Function[]{oldFunc, wrapFunction},0,""));
//				}
//			}
//		});
//		
//		slider = new JSlider();
//		slider.setEnabled(false);
//		slider.setPreferredSize(new Dimension(150,20));
//		GridBagConstraints gbc_slider = new GridBagConstraints();
//		gbc_slider.insets = new Insets(0, 0, 5, 5);
//		gbc_slider.gridx = 2;
//		gbc_slider.gridy = 1;
//		add(slider, gbc_slider);
//		slider.addMouseListener(new MouseAdapter() {
//
//			@Override
//			public void mouseReleased(MouseEvent arg0) {
//				Function wrapFunction = null;
//				if(slider.isEnabled() && !Arrays.equals(getStepsize(slider.getValue(), oldFunc.getBounds()),oldFunc.getStepsizes())){
//					try {
//						wrapFunction = new XYZFunction(new String[] {"y=x"}, (Color3f) colorList.get(comboBox.getSelectedIndex()), oldFunc.getBounds(), getStepsize(slider.getValue(), oldFunc.getBounds()));
//					} catch (ExpressionParseException | UndefinedVariableException e) {
//						e.printStackTrace();
//					}
//					a.actionPerformed(new ActionEvent(new Function[]{oldFunc, wrapFunction},1,""));
//				}
//			}
//		});
//
//		this.colorList = colorList;
//	}
	
}
