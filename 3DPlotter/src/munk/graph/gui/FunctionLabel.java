package munk.graph.gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import munk.graph.function.Function;

@SuppressWarnings("serial")
public class FunctionLabel extends JPanel{
	
	JCheckBox chckbx;
	JTextField exprField;
	Function mother;
	ActionListener listener;
	private JButton btnDelete;
	private static final Color WARNING_COLOR = new Color(255, 215, 0); 

	public FunctionLabel (Function function, ActionListener a){
		this.mother = function;
		this.listener = a;
		
		// GUI representation
		GridBagLayout gbl_fLabel = new GridBagLayout();
		gbl_fLabel.columnWidths = new int[]{20, 150, 30, 20, 0};
		gbl_fLabel.rowHeights = new int[]{0, 0};
		gbl_fLabel.columnWeights = new double[]{0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_fLabel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		this.setLayout(gbl_fLabel);

		chckbx = new JCheckBox("");
		GridBagConstraints gbc_chckbxTest = new GridBagConstraints();
		gbc_chckbxTest.insets = new Insets(0, 0, 0, 5);
		gbc_chckbxTest.gridx = 0;
		gbc_chckbxTest.gridy = 0;
		chckbx.setSelected(true);
		this.add(chckbx, gbc_chckbxTest);

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

		JButton btnEdit = new JButton(new ImageIcon("Icons/edit.png"));
		GridBagConstraints gbc_btnEdit = new GridBagConstraints();
		gbc_btnEdit.insets = new Insets(0, 0, 0, 5);
		gbc_btnEdit.gridx = 2;
		gbc_btnEdit.gridy = 0;
		add(btnEdit, gbc_btnEdit);
		
		addEditListener(btnEdit);
		
		btnDelete = new JButton(new ImageIcon("Icons/delete.png"));
		GridBagConstraints gbc_btnDelete = new GridBagConstraints();
		gbc_btnDelete.gridx = 3;
		gbc_btnDelete.gridy = 0;
		add(btnDelete, gbc_btnDelete);
		addTextChangeListener();
		addCheckboxListener();	
		addDeleteListener();
	}

	private void addDeleteListener() {
		// Delete function on click.
		btnDelete.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				listener.actionPerformed(new ActionEvent(mother, 2, exprField.getText()));
			}
		});
	}

	private void addEditListener(JButton btnEdit) {
		// Spawn edit dialog on click.
		btnEdit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				listener.actionPerformed(new ActionEvent(mother, 1, exprField.getText()));
			}
		});
	}

	private void addCheckboxListener() {
		// Update visibility.
		chckbx.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mother.setVisible(chckbx.isSelected());
				listener.actionPerformed(new ActionEvent(mother, 3, ""));
			}
		});
	}

	/*
	 * WHITE: Equation evaluated.
	 * YELLOW: Equation not evaluated.
	 * RED: Evaluation failed.
	 */
	private void addTextChangeListener() {
		exprField.addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent e) {
				if(!exprField.getBackground().equals(Color.RED) || !(e.getKeyCode() == KeyEvent.VK_ENTER)){
					if (!exprField.getText().equals(mother.getExpression()[0])) {
						exprField.setBackground(WARNING_COLOR);
						if(e.getKeyCode() == KeyEvent.VK_ENTER){
							listener.actionPerformed(new ActionEvent(mother, 0, exprField.getText()));
							if(exprField.getText().equals(mother.getExpression()[0])){
								exprField.setBackground(Color.WHITE);
							}
							else{
								exprField.setBackground(Color.RED);
							}
						}
					} else {
						exprField.setBackground(Color.WHITE);
					}

				}
			}
		});
	}

	public void setMother(Function f){
		mother = f;
		exprField.setText(mother.getExpression()[0]);
	}
}
