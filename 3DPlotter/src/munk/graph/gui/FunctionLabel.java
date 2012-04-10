package munk.graph.gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

import munk.graph.function.Function;
import javax.swing.JButton;

@SuppressWarnings("serial")
public class FunctionLabel extends JPanel{
	
	JCheckBox chckbx;
	JTextField exprField;
	Function mother;
	ActionListener listener;

	public FunctionLabel (Function function, ActionListener a){
		this.mother = function;
		this.listener = a;
		
		// GUI representation
		GridBagLayout gbl_fLabel = new GridBagLayout();
		gbl_fLabel.columnWidths = new int[]{0, 80, 20, 0};
		gbl_fLabel.rowHeights = new int[]{0, 0};
		gbl_fLabel.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_fLabel.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		this.setLayout(gbl_fLabel);
		
		JButton btnEdit = new JButton("Edit");
		GridBagConstraints gbc_btnEdit = new GridBagConstraints();
		gbc_btnEdit.insets = new Insets(0, 0, 0, 5);
		gbc_btnEdit.gridx = 0;
		gbc_btnEdit.gridy = 0;
		add(btnEdit, gbc_btnEdit);

		exprField = new JTextField(mother.getExpression()[0]);
		GridBagConstraints gbc_list = new GridBagConstraints();
		gbc_list.insets = new Insets(0, 0, 0, 5);
		gbc_list.fill = GridBagConstraints.HORIZONTAL;
		gbc_list.gridx = 1;
		gbc_list.gridy = 0;
		this.add(exprField, gbc_list);
		exprField.setEditable(true);

		/*
		 * If text is updated, color the cell RED. When "enter" is pressed, process the new function, and recolor the cell WHITE.
		 */
		exprField.addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyReleased(KeyEvent e) {
				if (!exprField.getText().equals(mother.getExpression()[0])) exprField.setBackground(Color.RED);
				if(e.getKeyCode() == KeyEvent.VK_ENTER){
					exprField.setBackground(Color.WHITE);
					listener.actionPerformed(new ActionEvent(mother, 0, exprField.getText()));
				}
			}
		});

		chckbx = new JCheckBox("");
		GridBagConstraints gbc_chckbxTest = new GridBagConstraints();
		gbc_chckbxTest.gridx = 2;
		gbc_chckbxTest.gridy = 0;
		chckbx.setSelected(true);
		this.add(chckbx, gbc_chckbxTest);
		
		// Update visibility.
		chckbx.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mother.setVisible(chckbx.isSelected());
			}
		});
	}
	
	public void setMother(Function f){
		mother = f;
		exprField.setText(mother.getExpression()[0]);
	}
}
