package munk.graph.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;
import javax.vecmath.Color3f;

import com.graphbuilder.math.ExpressionParseException;
import com.graphbuilder.math.UndefinedVariableException;

import munk.graph.function.*;

@SuppressWarnings("serial")
public class EditOptionPanel extends JPanel {
	
	private JTextField stepSize;
	private Function oldFunc;
	private ArrayList<ActionListener> listeners;
	private JComboBox<ColorIcon> comboBox;
	private ColorList colorList;
	
	public EditOptionPanel(ColorList colorList, Function f){
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{5, 0, 0, 0, 5, 0};
		gridBagLayout.rowHeights = new int[]{5, 0, 0, 5, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 1.0, 1.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JLabel lblStepSize = new JLabel("Step size =");
		GridBagConstraints gbc_lblStepSize = new GridBagConstraints();
		gbc_lblStepSize.insets = new Insets(0, 0, 5, 5);
		gbc_lblStepSize.anchor = GridBagConstraints.EAST;
		gbc_lblStepSize.gridx = 1;
		gbc_lblStepSize.gridy = 1;
		add(lblStepSize, gbc_lblStepSize);
		
		stepSize = new JTextField(f.getStepsize() + "");
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.insets = new Insets(0, 0, 5, 5);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 2;
		gbc_textField.gridy = 1;
		add(stepSize, gbc_textField);
		stepSize.setColumns(10);
		
		comboBox = new JComboBox<ColorIcon>(colorList.getIconList());
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.insets = new Insets(0, 0, 5, 5);
		gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox.gridx = 3;
		gbc_comboBox.gridy = 1;
		add(comboBox, gbc_comboBox);
		comboBox.setSelectedItem(new ColorIcon(f.getColor()));
		
		JButton btnOK = new JButton("OK");
		GridBagConstraints gbc_btnApply = new GridBagConstraints();
		gbc_btnApply.insets = new Insets(0, 0, 5, 5);
		gbc_btnApply.gridx = 2;
		gbc_btnApply.gridy = 2;
		add(btnOK, gbc_btnApply);
		btnOK.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				signalAll();
			}
		});
		
		this.colorList = colorList;
		this.oldFunc = f;
		listeners = new ArrayList<ActionListener>();
	}
	
	public void addActionListener(ActionListener a){
		listeners.add(a);
	}
	
	/*
	 * All information is wrapped in a function object, which is passed to GUI.
	 */
	private void signalAll(){
		for(ActionListener a : listeners){
			Function f;
			try {
				f = FunctionUtil.createFunction(oldFunc.getExpression(), 
						(Color3f) colorList.get(comboBox.getSelectedIndex()),
						oldFunc.getBounds(), Float.parseFloat(stepSize.getText()));
				a.actionPerformed(new ActionEvent(f, FunctionLabel.UPDATE, ""));
			} catch (ExpressionParseException | IllegalEquationException | UndefinedVariableException e) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						JOptionPane.showMessageDialog(null, e.getMessage());
					}
				});
			} catch (final NumberFormatException e) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						JOptionPane.showMessageDialog(null, e.getMessage());
					}
				});
			}
		}
	}
	
}
