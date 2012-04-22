package munk.graph.gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.vecmath.Color3f;

import munk.graph.function.*;

import com.graphbuilder.math.*;

@SuppressWarnings("serial")
public class EditOptionPanel extends JPanel {
	
	private Function oldFunc;
	private JComboBox comboBox;
	private JSlider slider;
	private ColorList colorList;
	private JRadioButton rdbtnGrid;
	private JRadioButton rdbtnSolid;
	private JRadioButton rdbtnPoint;
	private ButtonGroup viewGroup;
	private JCheckBox chckbxFasterImplicits;
	private JLabel resolution;
	
	public EditOptionPanel(final ColorList colorList, Function f, final ActionListener a){
		//setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{5, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{5, 0, 0, 0, 5, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		resolution = new JLabel("Resolution");
		GridBagConstraints gbc_lblStepSize = new GridBagConstraints();
		gbc_lblStepSize.insets = new Insets(0, 0, 5, 5);
		gbc_lblStepSize.anchor = GridBagConstraints.WEST;
		gbc_lblStepSize.gridx = 1;
		gbc_lblStepSize.gridy = 1;
		add(resolution, gbc_lblStepSize);
		
		slider = new JSlider();
		slider.setPreferredSize(new Dimension(150,20));
		GridBagConstraints gbc_slider = new GridBagConstraints();
		gbc_slider.gridwidth = 8;
		gbc_slider.insets = new Insets(0, 0, 5, 5);
		gbc_slider.gridx = 2;
		gbc_slider.gridy = 1;
		add(slider, gbc_slider);
		slider.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseReleased(MouseEvent arg0) {
				Function wrapFunction = null;
				if(slider.isEnabled() && GuiUtil.getStepsize(slider.getValue(), oldFunc.getBounds())[0] != oldFunc.getStepsize()){
					try {
						wrapFunction = new XYZFunction("y=x", (Color3f) colorList.get(comboBox.getSelectedIndex()), oldFunc.getBounds(), GuiUtil.getStepsize(slider.getValue(), oldFunc.getBounds())[0]);
					} catch (ExpressionParseException | UndefinedVariableException e) {
						e.printStackTrace();
					}
					a.actionPerformed(new ActionEvent(new Function[]{oldFunc, wrapFunction},1,""));
				}
			}
		});
		
		comboBox = new JComboBox(colorList.getIconList());
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.insets = new Insets(0, 0, 5, 5);
		gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox.gridx = 10;
		gbc_comboBox.gridy = 1;
		add(comboBox, gbc_comboBox);
		
		rdbtnPoint = new JRadioButton("Point");
		GridBagConstraints gbc_rdbtnPoint = new GridBagConstraints();
		gbc_rdbtnPoint.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnPoint.gridx = 1;
		gbc_rdbtnPoint.gridy = 2;
		add(rdbtnPoint, gbc_rdbtnPoint);
		
		rdbtnGrid = new JRadioButton("Grid");
		GridBagConstraints gbc_rdbtnGrid = new GridBagConstraints();
		gbc_rdbtnGrid.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnGrid.gridx = 2;
		gbc_rdbtnGrid.gridy = 2;
		add(rdbtnGrid, gbc_rdbtnGrid);
		
		rdbtnSolid = new JRadioButton("Solid");
		GridBagConstraints gbc_rdbtnSolid = new GridBagConstraints();
		gbc_rdbtnSolid.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnSolid.gridx = 3;
		gbc_rdbtnSolid.gridy = 2;
		add(rdbtnSolid, gbc_rdbtnSolid);
		
		viewGroup = new ButtonGroup();
		viewGroup.add(rdbtnGrid);
		viewGroup.add(rdbtnPoint);
		viewGroup.add(rdbtnSolid);
		
		chckbxFasterImplicits = new JCheckBox("Faster Implicit");
		GridBagConstraints gbc_chckbxFasterImplicits = new GridBagConstraints();
		gbc_chckbxFasterImplicits.gridwidth = 7;
		gbc_chckbxFasterImplicits.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxFasterImplicits.gridx = 4;
		gbc_chckbxFasterImplicits.gridy = 2;
		add(chckbxFasterImplicits, gbc_chckbxFasterImplicits);
		comboBox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Function wrapFunction = null;
				Color3f selectedColor = (Color3f) colorList.get(comboBox.getSelectedIndex());
				if(comboBox.isEnabled() && !selectedColor.equals(oldFunc.getColor())){
				try {
					wrapFunction = new XYZFunction("y=x", (Color3f) colorList.get(comboBox.getSelectedIndex()), new float[]{-1,1,-1,1,-1,1}, (float) (0.505 - Math.log10(slider.getValue()+1)/4));
				} catch (ExpressionParseException | UndefinedVariableException ex) {
					ex.printStackTrace();
				}
				a.actionPerformed(new ActionEvent(new Function[]{oldFunc, wrapFunction},0,""));
				}
			}
		});
		
		this.colorList = colorList;
		chckbxFasterImplicits.setEnabled(false);
		chckbxFasterImplicits.setSelected(true);
		enableOptions(false);
	}
	
	public void updateFuncReference(Function f){
		enableOptions(true);
		oldFunc = f;
		comboBox.setSelectedIndex(colorList.indexOf(f.getColor()));
		slider.setValue(GuiUtil.getSliderValue(f.getStepsize(),f.getBounds()));
	}
	
	public void updateColors(){
		comboBox.setModel(new JComboBox(colorList.getIconList()).getModel());
	}
	
	public void enableOptions(Boolean b){
		resolution.setEnabled(b);
		slider.setEnabled(b);
		comboBox.setEnabled(b);
		rdbtnGrid.setEnabled(b);
		rdbtnPoint.setEnabled(b);
		rdbtnSolid.setEnabled(b);
		// chckbxFasterImplicits.setEnabled(b);
	}
	
}
