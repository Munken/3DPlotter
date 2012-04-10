package munk.graph.gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

import function.AbstractFunction;

@SuppressWarnings("serial")
public class FunctionLabel extends JPanel{
	
	JCheckBox chckbx;
	JTextField textLabel;
	AbstractFunction mother;

	public FunctionLabel (AbstractFunction f){
		this.mother = f;
		// GUI representation
		GridBagLayout gbl_fLabel = new GridBagLayout();
		gbl_fLabel.columnWidths = new int[]{80, 20, 0};
		gbl_fLabel.rowHeights = new int[]{0, 0};
		gbl_fLabel.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		gbl_fLabel.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		this.setLayout(gbl_fLabel);

		textLabel = new JTextField(mother.getEquation());
		GridBagConstraints gbc_list = new GridBagConstraints();
		gbc_list.insets = new Insets(0, 0, 0, 5);
		gbc_list.fill = GridBagConstraints.HORIZONTAL;
		gbc_list.gridx = 0;
		gbc_list.gridy = 0;
		this.add(textLabel, gbc_list);
		textLabel.setEditable(false);
		textLabel.setBackground(Color.WHITE);
		// Update selection.
		textLabel.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				mother.setSelected(!mother.isSelected());
				if(mother.isSelected()){
					textLabel.setBackground(Color.BLUE);
				}
				else{
					textLabel.setBackground(Color.WHITE);
				}
			}
			public void mouseEntered(MouseEvent e) {
			}
			public void mouseExited(MouseEvent e) {	
			}
			public void mousePressed(MouseEvent e) {	
			}
			public void mouseReleased(MouseEvent e) {
			}
		});

		chckbx = new JCheckBox("");
		GridBagConstraints gbc_chckbxTest = new GridBagConstraints();
		gbc_chckbxTest.gridx = 1;
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
}
