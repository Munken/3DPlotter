package munk.graph.function;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/*
 * An ArrayList with the possibility to add action listeners.
 */
@SuppressWarnings({ "serial", "hiding" })
public class FunctionList<Function> extends ArrayList<Function> {
	
	ArrayList<ActionListener> listeners;
	
	public FunctionList(){
		listeners = new ArrayList<ActionListener>();
	}
	
	public boolean add(Function f){
		signalAll("ADD",0,f);
		return super.add(f);
	}
	
	public Function remove(int i){
		Function source = super.remove(i);
		signalAll("REMOVE",i,source);
		return source;
	}
	
	public boolean remove(Object f){
		@SuppressWarnings("unchecked")
		Function source = (Function) f;
		int i = indexOf(f);
		signalAll("REMOVE",i,source);
		return super.remove(f);
	}
	
	public Function set(int i, Function f){
		Function source = f;
		signalAll("SET",i,source);
		return super.set(i, source);
	}
	
	public void addActionListener(ActionListener a){
		listeners.add(a);
	}
	
	private void signalAll(String command, int index, Function f){
		for(ActionListener a : listeners){
			a.actionPerformed(new ActionEvent(f, index, command));
		}
	}
	
}
