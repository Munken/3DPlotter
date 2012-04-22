package munk.graph.plot;

import java.util.concurrent.atomic.AtomicBoolean;

import javax.media.j3d.Node;
import javax.media.j3d.Shape3D;

public abstract class AbstractPlotter implements Plotter{

	private Node plot;
	private AtomicBoolean isCancelled = new AtomicBoolean(false);
	private Shape3D shape;
	
	@Override
	public Node getPlot() {
		if (plot == null) {
			plot = plot();
		}
		
		return plot; 
	}
	
	protected abstract Node plot();
	
	@Override
	public void cancel() {
		isCancelled.set(true);
	}

	@Override
	public boolean isCancelled() {
		return isCancelled.get();
	}
	
	@Override
	public Shape3D getShape() {
		if (shape == null) {
			getPlot();
		}
		return shape;
	}
	
	protected void setShape(Shape3D newShape) {
		shape = newShape;
	}

}
