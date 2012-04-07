package munk.graph.gui;

import java.awt.*;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.vecmath.Color3f;


public class Function {
	
	private String equation;
	private Color3f color;
	private JPanel representation;
	private JCheckBox chckbx;
	private SelectableJLabel textArea;

	public Function(String equation, Color3f color){
		this.equation = equation;
		
		// GUI representation
		representation = new JPanel();
		GridBagLayout gbl_representation = new GridBagLayout();
		gbl_representation.columnWidths = new int[]{80, 20, 0};
		gbl_representation.rowHeights = new int[]{0, 0};
		gbl_representation.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		gbl_representation.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		representation.setLayout(gbl_representation);
		
		textArea = new SelectableJLabel(equation);
		GridBagConstraints gbc_list = new GridBagConstraints();
		gbc_list.insets = new Insets(0, 0, 0, 5);
		gbc_list.fill = GridBagConstraints.HORIZONTAL;
		gbc_list.gridx = 0;
		gbc_list.gridy = 0;
		representation.add(textArea, gbc_list);
		
		chckbx = new JCheckBox("");
		GridBagConstraints gbc_chckbxTest = new GridBagConstraints();
		gbc_chckbxTest.gridx = 1;
		gbc_chckbxTest.gridy = 0;
		representation.add(chckbx, gbc_chckbxTest);

		// If the function is present.
		if(color != null){
			chckbx.setSelected(true);
			chckbx.setEnabled(true);
			this.color = color;
//			chckbx.addActionListener(new ActionListener() {
//
//				@Override
//				public void actionPerformed(ActionEvent e) {
//					aL.actionPerformed(new ActionEvent("", 0, ""));
//				}
//			});
//			addVisibilityListener(a);
		}
		// If the function is not present.
		else{
			chckbx.setEnabled(false);
			textArea.setBackground(representation.getBackground());
		}
	}

	public String getEquation() {
		return equation;
	}

	public void setEquation(String equation) {
		this.equation = equation;
	}

	public Color3f getColor() {
		return color;
	}
	
	public void setColor(Color3f color) {
		this.color = color;
	}
	
	public void setVisible(boolean visible){
		chckbx.setSelected(visible); 
	}
	
	public boolean isVisible(){
		return chckbx.isSelected(); 
	}
	
	public boolean isSelected(){
		return textArea.isSelected();
	}
	
	public JPanel getRepresentation(){
		return representation;
	}
	
	public void addVisibilityListener(ActionListener listener) {
		chckbx.addActionListener(listener);
	}
	
	public void removeVisibilityListener(ActionListener listener) {
		chckbx.removeActionListener(listener);
	}
}
