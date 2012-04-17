package munk.graph.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

import munk.graph.function.Function;

@SuppressWarnings("serial")
public class ParametricFunctionLabel extends JPanel implements FunctionLabel{
	
	ToggleButton toggleButton;
	JTextField exprFieldX;
	Function mother;
	ActionListener listener;
	private JButton btnDelete;
	private static final Color WARNING_COLOR = new Color(255, 215, 0); 
	private JTextField exprFieldY;
	private JTextField exprFieldZ;
	private JLabel lblX;
	private JLabel lblY;
	private JLabel lblZ;

	public ParametricFunctionLabel (Function function, ActionListener a){
		this.mother = function;
		this.listener = a;
		
		// GUI representation
		GridBagLayout gbl_fLabel = new GridBagLayout();
		gbl_fLabel.columnWidths = new int[]{0, 0, 150, 30, 0, 0};
		gbl_fLabel.rowHeights = new int[]{5, 0, 0, 0, 0, 0};
		gbl_fLabel.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_fLabel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
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
		gbc_chckbxTest.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxTest.gridx = 3;
		gbc_chckbxTest.gridy = 1;
		this.add(toggleButton, gbc_chckbxTest);

		lblY = new JLabel("y =");
		GridBagConstraints gbc_lblY = new GridBagConstraints();
		gbc_lblY.insets = new Insets(0, 0, 5, 5);
		gbc_lblY.anchor = GridBagConstraints.EAST;
		gbc_lblY.gridx = 1;
		gbc_lblY.gridy = 2;
		add(lblY, gbc_lblY);

		exprFieldY = new JTextField(mother.getExpression()[1]);
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.insets = new Insets(0, 0, 5, 5);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 2;
		gbc_textField.gridy = 2;
		add(exprFieldY, gbc_textField);
		exprFieldY.setColumns(10);

		JButton btnEdit = new JButton(new ImageIcon("Icons/edit.png"));
		GridBagConstraints gbc_btnEdit = new GridBagConstraints();
		gbc_btnEdit.insets = new Insets(0, 0, 5, 5);
		gbc_btnEdit.gridx = 3;
		gbc_btnEdit.gridy = 2;
		add(btnEdit, gbc_btnEdit);
		addEditListener(btnEdit);

		lblZ = new JLabel("z =");
		GridBagConstraints gbc_lblZ = new GridBagConstraints();
		gbc_lblZ.insets = new Insets(0, 0, 5, 5);
		gbc_lblZ.anchor = GridBagConstraints.EAST;
		gbc_lblZ.gridx = 1;
		gbc_lblZ.gridy = 3;
		add(lblZ, gbc_lblZ);

		exprFieldZ = new JTextField(mother.getExpression()[2]);
		GridBagConstraints gbc_textField_1 = new GridBagConstraints();
		gbc_textField_1.insets = new Insets(0, 0, 5, 5);
		gbc_textField_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_1.gridx = 2;
		gbc_textField_1.gridy = 3;
		add(exprFieldZ, gbc_textField_1);
		exprFieldZ.setColumns(10);

		// Don't fuck up layout, when text string becomes too long.
		exprFieldX.setPreferredSize(new Dimension(100,20));
		exprFieldY.setPreferredSize(new Dimension(100,20));
		exprFieldZ.setPreferredSize(new Dimension(100,20));

		btnDelete = new JButton(new ImageIcon("Icons/delete.png"));
		GridBagConstraints gbc_btnDelete = new GridBagConstraints();
		gbc_btnDelete.insets = new Insets(0, 0, 5, 5);
		gbc_btnDelete.gridx = 3;
		gbc_btnDelete.gridy = 3;
		add(btnDelete, gbc_btnDelete);

		// Add listeners;
		addTextChangeListener();
		addToggleButtonListener();	
		addDeleteListener();
		
		this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));

	}

	private void addDeleteListener() {
		// Delete function on click.
		btnDelete.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				listener.actionPerformed(new ActionEvent(mother, 2, ""));
			}
		});
	}

	private void addEditListener(JButton btnEdit) {
		// Spawn edit dialog on click.
		btnEdit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				listener.actionPerformed(new ActionEvent(mother, 1, ""));
			}
		});
	}

	private void addToggleButtonListener() {
		// Update visibility.
		toggleButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mother.setVisible(toggleButton.isSelected());
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
		KeyAdapter keyListener = new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent e) {
				if(!exprFieldX.getBackground().equals(Color.RED) || !(e.getKeyCode() == KeyEvent.VK_ENTER)){
					if (!exprFieldX.getText().equals(mother.getExpression()[0]) || !exprFieldY.getText().equals(mother.getExpression()[1]) || !exprFieldZ.getText().equals(mother.getExpression()[2])) {
						exprFieldX.setBackground(WARNING_COLOR);
						exprFieldY.setBackground(WARNING_COLOR);
						exprFieldZ.setBackground(WARNING_COLOR);
						if(e.getKeyCode() == KeyEvent.VK_ENTER){
							listener.actionPerformed(new ActionEvent(mother, 0, exprFieldX.getText() + "," + exprFieldY.getText() + "," + exprFieldZ.getText()));
							if(exprFieldX.getText().equals(mother.getExpression()[0]) && exprFieldY.getText().equals(mother.getExpression()[1]) && exprFieldZ.getText().equals(mother.getExpression()[2])){
								exprFieldX.setBackground(Color.WHITE);
								exprFieldY.setBackground(Color.WHITE);
								exprFieldZ.setBackground(Color.WHITE);
							}
							else{
								exprFieldX.setBackground(Color.RED);
								exprFieldY.setBackground(Color.RED);
								exprFieldZ.setBackground(Color.RED);
							}
						}
					}
					else {
						exprFieldX.setBackground(Color.WHITE);
						exprFieldY.setBackground(Color.WHITE);
						exprFieldZ.setBackground(Color.WHITE);
					}
				}
			}
		};
		exprFieldX.addKeyListener(keyListener);
		exprFieldY.addKeyListener(keyListener);
		exprFieldZ.addKeyListener(keyListener);
	}

	public void setMother(Function f){
		mother = f;
		exprFieldX.setText(mother.getExpression()[0]);
		exprFieldY.setText(mother.getExpression()[1]);
		exprFieldZ.setText(mother.getExpression()[2]);
	}

	@Override
	public void setIndeterminate(boolean b) {
		toggleButton.setIndeterminate(b);
	}
}
