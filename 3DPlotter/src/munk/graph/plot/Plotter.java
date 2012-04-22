package munk.graph.plot;

import javax.media.j3d.Node;
import javax.media.j3d.Shape3D;

public interface Plotter {

	Node getPlot();
	Shape3D getShape();
	void cancel();
	boolean isCancelled();
}
