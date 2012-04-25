package munk.graph.gui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

import javax.swing.*;

import munk.graph.function.AbstractFunction.FILL;
import munk.graph.function.*;
import munk.graph.gui.listener.*;

@SuppressWarnings("serial")
public class AppearanceOptionPanel extends JPanel {
	
	private Function selectedFunc;
	private JComboBox comboBox;
	private ColorList colorList;
	private JRadioButton rdbtnGrid;
	private JRadioButton rdbtnSolid;
	private JRadioButton rdbtnPoint;
	private ButtonGroup viewGroup;
	
	public AppearanceOptionPanel(final ColorList colorList, HashMap<Function, FunctionLabel> map) {
		
		this.setPreferredSize(new Dimension(300,35));
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 10, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		viewGroup = new ButtonGroup();
		
		this.colorList = colorList;
		
		rdbtnPoint = new JRadioButton("Point");
		GridBagConstraints gbc_rdbtnPoint = new GridBagConstraints();
		gbc_rdbtnPoint.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnPoint.gridx = 1;
		gbc_rdbtnPoint.gridy = 0;
		add(rdbtnPoint, gbc_rdbtnPoint);
		rdbtnPoint.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				selectedFunc.setView(FILL.POINT);
			}
		});
		viewGroup.add(rdbtnPoint);
		
		rdbtnGrid = new JRadioButton("Grid");
		GridBagConstraints gbc_rdbtnGrid = new GridBagConstraints();
		gbc_rdbtnGrid.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnGrid.gridx = 3;
		gbc_rdbtnGrid.gridy = 0;
		add(rdbtnGrid, gbc_rdbtnGrid);
		rdbtnGrid.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				selectedFunc.setView(FILL.GRID);
			}
		});
		viewGroup.add(rdbtnGrid);
		
		rdbtnSolid = new JRadioButton("Solid");
		GridBagConstraints gbc_rdbtnSolid = new GridBagConstraints();
		gbc_rdbtnSolid.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnSolid.gridx = 5;
		gbc_rdbtnSolid.gridy = 0;
		add(rdbtnSolid, gbc_rdbtnSolid);
		rdbtnSolid.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				selectedFunc.setView(FILL.FILL);
			}
		});
		viewGroup.add(rdbtnSolid);
		
		comboBox = new JComboBox(colorList.getIconList());
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.insets = new Insets(0, 0, 5, 5);
		gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox.gridx = 7;
		gbc_comboBox.gridy = 0;
		add(comboBox, gbc_comboBox);
		enableOptions(false);
	}

	public void updateFuncReference(Function f){
		enableOptions(true);
		selectedFunc = f;
		comboBox.setSelectedIndex(colorList.indexOf(f.getColor()));
		if(f.getView() == FILL.POINT){
			rdbtnPoint.setSelected(true);
		}
		else if(f.getView() == FILL.GRID){
			rdbtnGrid.setSelected(true);
		}
		else if(f.getView() == FILL.FILL){
			rdbtnSolid.setSelected(true);
		}
	}

	public void updateColors(){
		comboBox.setModel(new JComboBox(colorList.getIconList()).getModel());
	}

	public void enableOptions(Boolean b){
		comboBox.setEnabled(b);
		rdbtnGrid.setEnabled(b);
		rdbtnPoint.setEnabled(b);
		rdbtnSolid.setEnabled(b);
	}		
}

