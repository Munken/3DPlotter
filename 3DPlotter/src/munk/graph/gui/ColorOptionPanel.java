package munk.graph.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.vecmath.Color3f;

@SuppressWarnings("serial")
public class ColorOptionPanel extends JPanel {

	JColorChooser colorChooser;
	ArrayList<ChangeListener> listeners;
	
	public ColorOptionPanel(){
		listeners = new ArrayList<ChangeListener>();
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 317, 0, 5, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		colorChooser = new JColorChooser();
		GridBagConstraints gbc_colorChooser = new GridBagConstraints();
		gbc_colorChooser.gridwidth = 7;
		gbc_colorChooser.insets = new Insets(0, 0, 5, 5);
		gbc_colorChooser.anchor = GridBagConstraints.NORTHWEST;
		gbc_colorChooser.gridx = 1;
		gbc_colorChooser.gridy = 1;
		
		this.add(colorChooser, gbc_colorChooser);
		JButton btnADD = new JButton("Add color");
		GridBagConstraints gbc_btnOk = new GridBagConstraints();
		gbc_btnOk.insets = new Insets(0, 0, 5, 5);
		gbc_btnOk.gridx = 4;
		gbc_btnOk.gridy = 2;
		add(btnADD, gbc_btnOk);
		btnADD.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				signalAll("ADD");
			}
		});
		
		JButton btnClose = new JButton("Close");
		GridBagConstraints gbc_btnClose = new GridBagConstraints();
		gbc_btnClose.insets = new Insets(0, 0, 5, 5);
		gbc_btnClose.gridx = 5;
		gbc_btnClose.gridy = 2;
		add(btnClose, gbc_btnClose);
		btnClose.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				signalAll("CLOSE");
			}
		});
		
	}
	
	public void addChangeListener(ChangeListener c){
		listeners.add(c);
	}
	
	public void signalAll(String command){
		ChangeEvent event = new ChangeEvent(command);
		if(command.equals("ADD")){
			event = new ChangeEvent(new Color3f(colorChooser.getColor()));
		}
		for(ChangeListener c : listeners){
			c.stateChanged(event);
		}
	}
}
