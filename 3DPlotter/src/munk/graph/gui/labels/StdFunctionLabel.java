package munk.graph.gui.labels;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import munk.graph.function.Function;
import munk.graph.gui.GuiUtil;
import munk.graph.gui.ToggleButton;

@SuppressWarnings("serial")
public class StdFunctionLabel extends AbstractFunctionLabel{
	
	private JTextField exprField;

	public StdFunctionLabel (Function function){
		super(function);
		
		// GUI representation
		GridBagLayout gbl_fLabel = new GridBagLayout();
		gbl_fLabel.columnWidths = new int[]{20, 150, 20, 0};
		gbl_fLabel.rowHeights = new int[]{0, 0, 0};
		gbl_fLabel.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_fLabel.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		this.setLayout(gbl_fLabel);
		
		btnDelete = new JButton(new ImageIcon("Icons/delete.png"));
		GridBagConstraints gbc_btnDelete = new GridBagConstraints();
		gbc_btnDelete.insets = new Insets(0, 0, 5, 5);
		gbc_btnDelete.gridx = 0;
		gbc_btnDelete.gridy = 0;
		add(btnDelete, gbc_btnDelete);

		exprField = new JTextField(function.getExpression()[0]);
		GridBagConstraints gbc_list = new GridBagConstraints();
		gbc_list.insets = new Insets(0, 0, 5, 5);
		gbc_list.fill = GridBagConstraints.BOTH;
		gbc_list.gridx = 1;
		gbc_list.gridy = 0;
		this.add(exprField, gbc_list);
		exprField.setEditable(true);

		toggleButton = new ToggleButton(new ImageIcon("Icons/selected.png"),new ImageIcon("Icons/notSelected.png"));
		GridBagConstraints gbc_chckbxTest = new GridBagConstraints();
		gbc_chckbxTest.insets = new Insets(0, 0, 5, 0);
		gbc_chckbxTest.gridx = 2;
		gbc_chckbxTest.gridy = 0;
		this.add(toggleButton, gbc_chckbxTest);
		
		// Don't fuck up layout, when text string becomes too long.
		exprField.setPreferredSize(new Dimension(100,20));
		addTextChangeListener();
		addToggleButtonListener();	
		addDeleteListener();
		GuiUtil.setupUndoListener(exprField);
	}

	private void addDeleteListener() {
		// Delete function on click.
		btnDelete.addActionListener(getDeleteListener());
	}

	private void addToggleButtonListener() {
		// Update visibility.
		toggleButton.addActionListener(getToogleListener());
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
				
				updateColor();
			}

		});

		// Evaluation.
		exprField.addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent e) {
				
				Function currentFunction = getMother();
				boolean hasChanged = !exprField.getText().equals(currentFunction.getExpression()[0]);
				
				// They want to update the plot
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					
					if (hasChanged) {
						
						// Update the plot
						notifyPlotUpdate(exprField.getText());
						Function newFunction = getMother();
						
						if (currentFunction.equals(newFunction)) {
							setState(STATE.FAILED);
						} else {
							setState(STATE.SELECTED);
						}
					} 
				}	
				// Doing something else than plotting
				else if (hasChanged) {
					setState(STATE.CHANGED);

				} else {
					setState(STATE.SELECTED);
				}
				
				updateColor();
			}
		});
	}



	public void setMother(Function f){
		super.setMother(f);
		exprField.setText(f.getExpression()[0]);
	}
	
	@Override
	public String toString() {
		return exprField.getText();
	}
	

	
	public void setSelected(boolean b){
		selected = b;
		if(selected){
			setExpressionFieldBackground(SELECTED_COLOR);
			exprField.requestFocusInWindow();
			
			fireCaretUpdate(exprField);
			
		}
		else{
			setExpressionFieldBackground(NORMAL_COLOR);
		}
	}

	@Override
	protected void setExpressionFieldBackground(Color color) {
		exprField.setBackground(color);
	}
	
}
