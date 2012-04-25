package munk.graph.gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.EtchedBorder;

import java.util.ArrayList;
import java.util.List;

import munk.graph.function.Function;
import munk.graph.gui.listener.FunctionEvent;
import munk.graph.gui.listener.FunctionListener;

@SuppressWarnings("serial")
public class ParametricFunctionLabel extends JPanel implements FunctionLabel{
	

	ToggleButton toggleButton;
	JTextField exprFieldX;
	Function mother;
	private JButton btnDelete;
	private JTextField exprFieldY;
	private JTextField exprFieldZ;
	private JLabel lblX;
	private JLabel lblY;
	private JLabel lblZ;
	private boolean selected;
	
	private List<FunctionListener> listeners = new ArrayList<FunctionListener>();

	public ParametricFunctionLabel (Function function){
		this.mother = function;
		
		// GUI representation
		GridBagLayout gbl_fLabel = new GridBagLayout();
		gbl_fLabel.columnWidths = new int[]{0, 0, 150, 30, 0, 0};
		gbl_fLabel.rowHeights = new int[]{5, 0, 0, 0, 0, 0, 0};
		gbl_fLabel.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_fLabel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		this.setLayout(gbl_fLabel);

		lblX = new JLabel("x =");
		GridBagConstraints gbc_lblX = new GridBagConstraints();
		gbc_lblX.insets = new Insets(0, 0, 5, 5);
		gbc_lblX.anchor = GridBagConstraints.EAST;
		gbc_lblX.gridx = 1;
		gbc_lblX.gridy = 1;
		add(lblX, gbc_lblX);

		exprFieldX = new JTextField(mother.getExpression()[0]);
		GridBagConstraints gbc_list = new GridBagConstraints();
		gbc_list.insets = new Insets(0, 0, 5, 5);
		gbc_list.fill = GridBagConstraints.HORIZONTAL;
		gbc_list.gridx = 2;
		gbc_list.gridy = 1;
		this.add(exprFieldX, gbc_list);

		toggleButton = new ToggleButton(new ImageIcon("Icons/selected.png"),new ImageIcon("Icons/notSelected.png"));
		GridBagConstraints gbc_chckbxTest = new GridBagConstraints();
		gbc_chckbxTest.gridheight = 2;
		gbc_chckbxTest.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxTest.gridx = 3;
		gbc_chckbxTest.gridy = 1;
		this.add(toggleButton, gbc_chckbxTest);

		lblY = new JLabel("y =");
		GridBagConstraints gbc_lblY = new GridBagConstraints();
		gbc_lblY.gridheight = 2;
		gbc_lblY.insets = new Insets(0, 0, 5, 5);
		gbc_lblY.anchor = GridBagConstraints.EAST;
		gbc_lblY.gridx = 1;
		gbc_lblY.gridy = 2;
		add(lblY, gbc_lblY);

		exprFieldY = new JTextField(mother.getExpression()[1]);
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.gridheight = 2;
		gbc_textField.insets = new Insets(0, 0, 5, 5);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 2;
		gbc_textField.gridy = 2;
		add(exprFieldY, gbc_textField);
		exprFieldY.setColumns(10);

		lblZ = new JLabel("z =");
		GridBagConstraints gbc_lblZ = new GridBagConstraints();
		gbc_lblZ.insets = new Insets(0, 0, 5, 5);
		gbc_lblZ.anchor = GridBagConstraints.EAST;
		gbc_lblZ.gridx = 1;
		gbc_lblZ.gridy = 4;
		add(lblZ, gbc_lblZ);

		exprFieldZ = new JTextField(mother.getExpression()[2]);
		GridBagConstraints gbc_textField_1 = new GridBagConstraints();
		gbc_textField_1.insets = new Insets(0, 0, 5, 5);
		gbc_textField_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_1.gridx = 2;
		gbc_textField_1.gridy = 4;
		add(exprFieldZ, gbc_textField_1);
		exprFieldZ.setColumns(10);

		// Don't fuck up layout, when text string becomes too long.
		exprFieldX.setPreferredSize(new Dimension(100,20));
		exprFieldY.setPreferredSize(new Dimension(100,20));
		exprFieldZ.setPreferredSize(new Dimension(100,20));

		btnDelete = new JButton(new ImageIcon("Icons/delete.png"));
		GridBagConstraints gbc_btnDelete = new GridBagConstraints();
		gbc_btnDelete.gridheight = 2;
		gbc_btnDelete.insets = new Insets(0, 0, 5, 5);
		gbc_btnDelete.gridx = 3;
		gbc_btnDelete.gridy = 3;
		add(btnDelete, gbc_btnDelete);

		// Add listeners;
		addTextChangeListener();
		addToggleButtonListener();	
		addDeleteListener();
		
		this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));

		GuiUtil.setupUndoListener(exprFieldX);
		GuiUtil.setupUndoListener(exprFieldY);
		GuiUtil.setupUndoListener(exprFieldZ);
	}

	private void addDeleteListener() {
		// Delete function on click.
		btnDelete.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				FunctionEvent ev = new FunctionEvent(mother, FunctionEvent.ACTION.DELETE);
				notifyListeners(ev);
			}
		});
	}

	private void addToggleButtonListener() {
		// Update visibility.
		toggleButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mother.setVisible(toggleButton.isSelected());
				FunctionEvent ev = new FunctionEvent(mother, FunctionEvent.ACTION.VISIBILITY);
				
				notifyListeners(ev);
			}
		});
	}

	private void addTextChangeListener() {
		// Edit panel.
		FocusListener editListener = new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				FocusListener[] listeners = getFocusListeners();
				
				for (FocusListener l : listeners) {
					l.focusGained(e);
				}
			}
			
		};
		
		exprFieldX.addFocusListener(editListener);
		exprFieldY.addFocusListener(editListener);
		exprFieldZ.addFocusListener(editListener);

		KeyAdapter keyListener = new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent e) {
				if(!(exprFieldX.getBackground() == FAILED_COLOR) || !(e.getKeyCode() == KeyEvent.VK_ENTER)){
					
					if (!exprFieldX.getText().equals(mother.getExpression()[0]) 
							|| !exprFieldY.getText().equals(mother.getExpression()[1]) 
							|| !exprFieldZ.getText().equals(mother.getExpression()[2])) {
						
						// They want to plot
						if(e.getKeyCode() == KeyEvent.VK_ENTER){
							// Inform the listeners					
							notifyPlotUpdate(exprFieldX.getText(), exprFieldY.getText(), exprFieldZ.getText());
							
							// Update the colors
							if(exprFieldX.getText().equals(mother.getExpression()[0]) 
									&& exprFieldY.getText().equals(mother.getExpression()[1]) 
									&& exprFieldZ.getText().equals(mother.getExpression()[2])){
								if(selected){
									setExpressionFieldBackground(SELECTED_COLOR);
								}
								else{
									setExpressionFieldBackground(NORMAL_COLOR);
								}
							}
							else{
								setExpressionFieldBackground(FAILED_COLOR);
							}
						} 
						else { // Still working on the expression
							setExpressionFieldBackground(WARNING_COLOR);
						}
					}
					// Nothing changed just keey calm and carry on
					else {
						if(selected){
							setExpressionFieldBackground(SELECTED_COLOR);
						}
						else{
							setExpressionFieldBackground(NORMAL_COLOR);
						}
					}
				}
			}


		};
		exprFieldX.addKeyListener(keyListener);
		exprFieldY.addKeyListener(keyListener);
		exprFieldZ.addKeyListener(keyListener);
	}
	
	private void setExpressionFieldBackground(Color color) {
		exprFieldX.setBackground(color);
		exprFieldY.setBackground(color);
		exprFieldZ.setBackground(color);
	}

	public void setMother(Function f){
		mother = f;
		exprFieldX.setText(mother.getExpression()[0]);
		exprFieldY.setText(mother.getExpression()[1]);
		exprFieldZ.setText(mother.getExpression()[2]);
	}

	public void setIndeterminate(boolean b) {
		toggleButton.setIndeterminate(b);
	}
	
	
	private void notifyListeners(FunctionEvent e) {
		for (FunctionListener l : listeners) {
			l.functionChanged(e);
		}
	}
	
	public void addFunctionListener(FunctionListener l) {
		listeners.add(l);
	}
	
	private void notifyPlotUpdate(String... text) {
		FunctionEvent ev = new FunctionEvent(mother, text, 
												mother.getColor(), mother.getBoundsString(), 
												mother.getStepsize(), FunctionEvent.ACTION.UPDATE);
		notifyListeners(ev);
	}
	
	@Override
	public void addFocusListener(FocusListener l) {
		super.addFocusListener(l);
	}
	
	public void setSelected(boolean b){
		selected = b;
		if(selected){
			setExpressionFieldBackground(SELECTED_COLOR);
		}
		else{
			setExpressionFieldBackground(NORMAL_COLOR);
		}
	}
	
	public Function getMother(){
		return mother;
	}
}
