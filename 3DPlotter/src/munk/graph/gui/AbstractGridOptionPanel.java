package munk.graph.gui;

import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.JPanel;

import munk.graph.function.*;
import munk.graph.gui.listener.*;

import com.graphbuilder.math.ExpressionParseException;

public abstract class AbstractGridOptionPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private ArrayList<FunctionListener> listeners = new ArrayList<FunctionListener>();
	protected Function selectedFunction;
	
	protected abstract void setSliders(float[] f) throws ExpressionParseException;
	protected abstract void setGridBounds(String[] s);
	protected abstract String[] getGridBounds() throws ExpressionParseException;
	protected abstract float[] getGridStepSize() throws ExpressionParseException;
	
	public void updateFuncReference(Function f) throws ExpressionParseException{
		selectedFunction = f;
		setSliders(f.getStepsize());
		setGridBounds(f.getBoundsString());
	}
	
	public void addFunctionListener(FunctionListener f){
		listeners.add(f);
	}
	
	protected void signallAll() throws ExpressionParseException{
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
					} catch (ExpressionParseException e1) {
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
					} catch (ExpressionParseException e) {
						e.printStackTrace();
					}
				}
			}
		};
		return f;
	}
}
