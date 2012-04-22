package munk.graph.gui;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import munk.graph.function.Function;
import munk.graph.gui.listener.FunctionEvent;
import munk.graph.gui.listener.FunctionListener;

@SuppressWarnings("serial")
public class StdFunctionLabel extends JPanel implements FunctionLabel{
	
	private ToggleButton toggleButton;
	private JTextField exprField;
	private Function mother;
	private JButton btnDelete;
	private List<FunctionListener> listeners = new ArrayList<FunctionListener>();

	public StdFunctionLabel (Function function){
		this.mother = function;
		
		// GUI representation
		GridBagLayout gbl_fLabel = new GridBagLayout();
		gbl_fLabel.columnWidths = new int[]{20, 150, 20, 0};
		gbl_fLabel.rowHeights = new int[]{0, 0};
		gbl_fLabel.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_fLabel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		this.setLayout(gbl_fLabel);

		toggleButton = new ToggleButton(new ImageIcon("Icons/selected.png"),new ImageIcon("Icons/notSelected.png"));
		GridBagConstraints gbc_chckbxTest = new GridBagConstraints();
		gbc_chckbxTest.insets = new Insets(0, 0, 0, 5);
		gbc_chckbxTest.gridx = 0;
		gbc_chckbxTest.gridy = 0;
		this.add(toggleButton, gbc_chckbxTest);

		exprField = new JTextField(mother.getExpression()[0]);
		GridBagConstraints gbc_list = new GridBagConstraints();
		gbc_list.insets = new Insets(0, 0, 0, 5);
		gbc_list.fill = GridBagConstraints.HORIZONTAL;
		gbc_list.gridx = 1;
		gbc_list.gridy = 0;
		this.add(exprField, gbc_list);
		exprField.setEditable(true);
		// Don't fuck up layout, when text string becomes too long.
		exprField.setPreferredSize(new Dimension(100,20));
		
		btnDelete = new JButton(new ImageIcon("Icons/delete.png"));
		GridBagConstraints gbc_btnDelete = new GridBagConstraints();
		gbc_btnDelete.gridx = 2;
		gbc_btnDelete.gridy = 0;
		add(btnDelete, gbc_btnDelete);
		addTextChangeListener();
		addToggleButtonListener();	
		addDeleteListener();
		GuiUtil.setupUndoListener(exprField);
	}

	private void addDeleteListener() {
		// Delete function on click.
		btnDelete.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
//				listener.actionPerformed(new ActionEvent(mother, FunctionLabel.DELETE, exprField.getText()));
				
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

	
	/*
	 * WHITE: Equation evaluated.
	 * YELLOW: Equation not evaluated.
	 * RED: Evaluation failed.
	 */
	private void addTextChangeListener() {
		
		exprField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				FocusListener[] listeners = getFocusListeners();
				
				for (FocusListener l : listeners) {
					l.focusGained(e);
				}
			}
			
		});
		
		// Evaluation.
		exprField.addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent e) {
				if(!(exprField.getBackground() == FAILED_COLOR) || !(e.getKeyCode() == KeyEvent.VK_ENTER)){
					
					if (!exprField.getText().equals(mother.getExpression()[0])) {
						
						if(e.getKeyCode() == KeyEvent.VK_ENTER){
							notifyPlotUpdate(exprField.getText());
							
							if(exprField.getText().equals(mother.getExpression()[0])){
								exprField.setBackground(NORMAL_COLOR);
							}
							else {
								exprField.setBackground(FAILED_COLOR);
							}
							
						} else {
							exprField.setBackground(WARNING_COLOR);
						}
					} else {
						exprField.setBackground(NORMAL_COLOR);
					}

				}
			}
		});
	}

	protected void notifyPlotUpdate(String text) {
		FunctionEvent ev = new FunctionEvent(mother, new String[] {text}, 
												mother.getColor(), mother.getBounds(), 
												mother.getStepsizes(), FunctionEvent.ACTION.UPDATE);
		notifyListeners(ev);
	}

	public void setMother(Function f){
		mother = f;
		exprField.setText(mother.getExpression()[0]);
	}
	
	public void setIndeterminate(boolean b){
		toggleButton.setIndeterminate(b);
	}
	
	@Override
	public String toString() {
		return exprField.getText();
	}
	
	private void notifyListeners(FunctionEvent e) {
		for (FunctionListener l : listeners) {
			l.functionChanged(e);
		}
	}
	
	public void addFunctionListener(FunctionListener l) {
		listeners.add(l);
	}
	
}
