package munk.graph.gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.vecmath.Color3f;

import munk.graph.function.*;

import com.graphbuilder.math.*;
import javax.swing.border.EtchedBorder;

@SuppressWarnings("serial")
public class EditOptionPanel extends JPanel {
	
	private Function oldFunc;
	private JComboBox comboBox;
	private JSlider slider;
	
	
	
	public EditOptionPanel(final ColorList colorList, Function f, final ActionListener a){
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
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.insets = new Insets(0, 0, 5, 5);
		gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox.gridx = 3;
		gbc_comboBox.gridy = 1;
		add(comboBox, gbc_comboBox);
		comboBox.setSelectedItem(new ColorIcon(f.getColor()));
		comboBox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Function wrapFunction = null;
				Color3f selectedColor = (Color3f) colorList.get(comboBox.getSelectedIndex());
				if(!selectedColor.equals(oldFunc.getColor())){
				try {
					wrapFunction = new XYZFunction("y=x", (Color3f) colorList.get(comboBox.getSelectedIndex()), new float[]{-1,1,-1,1,-1,1}, (float) (0.505 - Math.log10(slider.getValue()+1)/4));
				} catch (ExpressionParseException | UndefinedVariableException ex) {
					ex.printStackTrace();
				}
				a.actionPerformed(new ActionEvent(new Function[]{oldFunc, wrapFunction},0,""));
				}
			}
		});
//		comboBox.addFocusListener(new FocusListener() {
//			
//			@Override
//			public void focusLost(FocusEvent arg0) {
//				a.actionPerformed(new ActionEvent("",FunctionLabel.HIDEEDIT,""));
//			}
//			
//			@Override
//			public void focusGained(FocusEvent arg0) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
		
		slider = new JSlider();
		slider.setValue(GuiUtil.getSliderValue(f.getStepsize(),f.getBounds()));
		slider.setPreferredSize(new Dimension(150,20));
		GridBagConstraints gbc_slider = new GridBagConstraints();
		gbc_slider.insets = new Insets(0, 0, 5, 5);
		gbc_slider.gridx = 2;
		gbc_slider.gridy = 1;
		add(slider, gbc_slider);
		slider.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseReleased(MouseEvent arg0) {
				Function wrapFunction = null;
				if(GuiUtil.getStepsize(slider.getValue(), oldFunc.getBounds())[0] != oldFunc.getStepsize()){
					try {
						wrapFunction = new XYZFunction("y=x", (Color3f) colorList.get(comboBox.getSelectedIndex()), new float[]{-1,1,-1,1,-1,1}, GuiUtil.getStepsize(slider.getValue(), oldFunc.getBounds())[0]);
					} catch (ExpressionParseException | UndefinedVariableException e) {
						e.printStackTrace();
					}
					a.actionPerformed(new ActionEvent(new Function[]{oldFunc, wrapFunction},1,""));
				}
			}
		});
//		slider.addFocusListener(new FocusListener() {
//
//			@Override
//			public void focusLost(FocusEvent arg0) {
//				a.actionPerformed(new ActionEvent("",FunctionLabel.HIDEEDIT,""));
//			}
//			
//			@Override
//			public void focusGained(FocusEvent arg0) {
//				// TODO Auto-generated method stub
//				
//			}
//		});

		this.oldFunc = f;
	}
	
	public void updateFuncReference(Function f){
		oldFunc = f;
	}
	
}
