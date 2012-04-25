package munk.graph.gui;

import java.awt.*;

import javax.swing.*;

import com.graphbuilder.math.ExpressionParseException;

public class ParamGridOptionPanel extends JPanel{
	private JTextField tMin;
	private JTextField tMax;
	private JTextField uMin;
	private JTextField uMax;
	private JSlider uSlider;
	private JSlider tSlider;

	public ParamGridOptionPanel(float[] bounds) {

		this.setPreferredSize(new Dimension(300,70));

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 40, 50, 40, 0, 40, 50, 40, 0, 0};
		gridBagLayout.rowHeights = new int[]{10, 0, 0, 10, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);

		tMin = new JTextField();
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.insets = new Insets(0, 0, 5, 5);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 1;
		gbc_textField.gridy = 1;
		add(tMin, gbc_textField);

		JLabel tLabel = new JLabel("< t <");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 2;
		gbc_lblNewLabel.gridy = 1;
		add(tLabel, gbc_lblNewLabel);

		tMax = new JTextField();
		GridBagConstraints gbc_textField_1 = new GridBagConstraints();
		gbc_textField_1.insets = new Insets(0, 0, 5, 5);
		gbc_textField_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_1.gridx = 3;
		gbc_textField_1.gridy = 1;
		add(tMax, gbc_textField_1);

		tSlider = new JSlider();
		GridBagConstraints gbc_slider_1 = new GridBagConstraints();
		gbc_slider_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_slider_1.gridwidth = 3;
		gbc_slider_1.insets = new Insets(0, 0, 5, 5);
		gbc_slider_1.gridx = 5;
		gbc_slider_1.gridy = 1;
		add(tSlider, gbc_slider_1);

		uMin = new JTextField();
		GridBagConstraints gbc_textField_2 = new GridBagConstraints();
		gbc_textField_2.insets = new Insets(0, 0, 5, 5);
		gbc_textField_2.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_2.gridx = 1;
		gbc_textField_2.gridy = 2;
		add(uMin, gbc_textField_2);

		JLabel uLabel = new JLabel("< u <");
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.insets = new Insets(0, 0, 5, 5);
		gbc_label.gridx = 2;
		gbc_label.gridy = 2;
		add(uLabel, gbc_label);

		uMax = new JTextField();
		GridBagConstraints gbc_textField_3 = new GridBagConstraints();
		gbc_textField_3.insets = new Insets(0, 0, 5, 5);
		gbc_textField_3.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_3.gridx = 3;
		gbc_textField_3.gridy = 2;
		add(uMax, gbc_textField_3);

		uSlider = new JSlider();
		GridBagConstraints gbc_slider = new GridBagConstraints();
		gbc_slider.fill = GridBagConstraints.HORIZONTAL;
		gbc_slider.gridwidth = 3;
		gbc_slider.insets = new Insets(0, 0, 5, 5);
		gbc_slider.gridx = 5;
		gbc_slider.gridy = 2;
		add(uSlider, gbc_slider);
		
     	GuiUtil.setupUndoListener(tMin);
     	GuiUtil.setupUndoListener(tMax);
     	GuiUtil.setupUndoListener(uMin);
     	GuiUtil.setupUndoListener(uMax);
     	
		setGridBounds(bounds);
	}

	public float[] getGridBounds() throws ExpressionParseException{
		float[] bounds = new float[4];
		bounds[0] = GuiUtil.evalString(tMin.getText());
		bounds[1] = GuiUtil.evalString(tMax.getText());
		bounds[2] = GuiUtil.evalString(uMin.getText());
		bounds[3] = GuiUtil.evalString(uMax.getText());
		return bounds;
	}
	
	public float[] getGridStepsize() throws ExpressionParseException{
		float[] bounds = getGridBounds();
		float[] stepSize = new float[2];
		stepSize[0] = Math.abs(bounds[1]-bounds[0])/tSlider.getValue();
		stepSize[1] = Math.abs(bounds[3]-bounds[2])/uSlider.getValue();
		return stepSize;
	}
	
	public void setGridBounds(float[] bounds){
		tMin.setText(bounds[0] + "");
		tMax.setText(bounds[1] + "");
		uMin.setText(bounds[2] + "");
		uMax.setText(bounds[3] + "");
	}
	
	public void setSliders(float[] stepSize) throws ExpressionParseException{
		float[] bounds = getGridBounds();
		tSlider.setValue((int) (Math.abs(bounds[1]-bounds[0])/stepSize[0]));
		uSlider.setValue((int) (Math.abs(bounds[1]-bounds[0])/stepSize[1]));
	}
}