package munk.graph.gui.panel.gridoption;

import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;

import munk.emesp.exceptions.IllegalExpressionException;
import munk.graph.function.Function;
import munk.graph.function.TemplateFunction;
import munk.graph.gui.listener.FunctionEvent;
import munk.graph.gui.listener.FunctionListener;

public abstract class AbstractGridOptionPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private ArrayList<FunctionListener> listeners = new ArrayList<FunctionListener>();
	protected Function selectedFunction;
	
	protected abstract void setSliders(float[] f) throws IllegalExpressionException;
	protected abstract void setGridBounds(String[] s);
	protected abstract String[] getGridBounds() throws IllegalExpressionException;
	protected abstract float[] getGridStepSize() throws IllegalExpressionException;
	
	public void updateFuncReference(Function f) throws IllegalExpressionException{
		selectedFunction = f;
		setSliders(f.getStepsize());
		setGridBounds(f.getBoundsString());
	}
	
	public void addFunctionListener(FunctionListener f){
		listeners.add(f);
	}
	
	protected void signallAll() throws IllegalExpressionException{
		for(FunctionListener f : listeners){
			f.functionChanged(new FunctionEvent(selectedFunction, getGridBounds(), getGridStepSize()));
		}
	}
	
	protected KeyListener getBoundsListener(){
		KeyListener k = new KeyAdapter() {
     		// Plot the graph.
			
     		@Override
     		public void keyPressed(KeyEvent e) {
     			if (e.getKeyCode() == KeyEvent.VK_ENTER && selectedFunction.getClass() != TemplateFunction.class) {
     				try {
						signallAll();
					} catch (IllegalExpressionException e1) {
						e1.printStackTrace();
					}
     			}
     		}
     	};
     	return k;
	}
	
	protected FocusListener getUpdateBoundsListener(final AbstractGridOptionPanel currentPanel){
		FocusListener f = new FocusAdapter() {
			
			@Override
			public void focusLost(FocusEvent arg0) {
				if(selectedFunction.getClass() == TemplateFunction.class){
					try {
						selectedFunction.setBoundsString(currentPanel.getGridBounds());
					} catch (IllegalExpressionException e) {
						e.printStackTrace();
					}
				}
			}
		};
		return f;
	}
	
	protected MouseListener getSliderListener(final JSlider... sliders) {
		return new MouseAdapter() {
			BoundedRangeModel sharedModel;
			BoundedRangeModel[] models;
			
			{
				models = new BoundedRangeModel[sliders.length];
				
				for (int i = 0; i < models.length; i++) {
					models[i] = sliders[i].getModel();
				}
			}
			
			@Override
			public void mouseReleased(MouseEvent arg0) {
					try {
						if (stepsizeHasChanged()) {
							signallAll();
						}
						
						if (sharedModel != null) {
							int value = sharedModel.getValue();
							for (int i = 0; i < models.length; i++) {
								BoundedRangeModel m = models[i];
								m.setValue(value);
								sliders[i].setModel(m);
							}
						}
						sharedModel = null;
					} catch (IllegalExpressionException e) {
						e.printStackTrace();
					}
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e) || e.isShiftDown()) {
					JSlider slider = (JSlider) e.getSource();
					sharedModel = slider.getModel();
					
					for (int i = 0; i < models.length; i++) {
						sliders[i].setModel(sharedModel);
					}
				}
			}
			
			private boolean stepsizeHasChanged() throws IllegalExpressionException {
				float[] oldStepsize = selectedFunction.getStepsize();
				float[] newStepsize = getGridStepSize();
				
				for (int i = 0; i < oldStepsize.length; i++) {
					if (oldStepsize[i] != newStepsize[i])
						return true;
				}
				
				return false;
			}
			
		};
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
