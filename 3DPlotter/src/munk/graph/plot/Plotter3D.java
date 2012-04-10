package munk.graph.plot;

import java.awt.GraphicsConfiguration;
import java.util.HashMap;
import java.util.Map;

import javax.media.j3d.*;
import javax.swing.JPanel;
import javax.vecmath.*;

import munk.graph.appearance.ColorAppearance;
import munk.graph.function.Function;
import munk.graph.rotaters.KeyRotate;
import munk.graph.rotaters.ViewZoomer;

import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.universe.SimpleUniverse;

@SuppressWarnings("serial")
public class Plotter3D extends JPanel{
	
	private TransformGroup  root;
	private TransformGroup	plots;
	private BranchGroup axes;
	private SimpleUniverse universe;
	private Canvas3D canvas;
	private BranchGroup	currentAxis;
	private Map<String, BranchGroup> functions = new HashMap<String, BranchGroup>();
	private Map<String, Shape3D> shapes = new HashMap<String, Shape3D>();
	
	public Plotter3D() {
		this(600, 600);
	}
	public Plotter3D(int width, int heigth) {
	    GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
	    
	    canvas = new Canvas3D(config);
	    canvas.setSize(width, heigth);

	    universe = new SimpleUniverse(canvas);
	    BranchGroup group = new BranchGroup();
	    
	    addLights(group);
    
	    root = new TransformGroup();
	    root.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
	    
	    plots = new TransformGroup();
		plots.setCapability(Group.ALLOW_CHILDREN_EXTEND);
		plots.setCapability(Group.ALLOW_CHILDREN_WRITE);
	    initAxis();
	    
	    // Init rotaters
	    initMouseRotation();
	    canvas.addKeyListener(new KeyRotate(root));
	    canvas.addKeyListener(new ViewZoomer(universe.getViewingPlatform().getViewPlatformTransform()));
	    
	    root.addChild(plots);
	    group.addChild(root);
	    
	    group.compile();
	    
	    universe.addBranchGraph(group);
	     
	    initView();
	    add(canvas);
	}


	private void initView() {
		universe.getViewingPlatform().setNominalViewingTransform();
		adjustZoom();
	}
	
	private void adjustZoom() {
        // point the view at the center of the object
        BoundingSphere sceneBounds = (BoundingSphere)root.getBounds();
        double radius = sceneBounds.getRadius();

        // Getting the transformation matrix for the root node
        Transform3D scaling = new Transform3D();
        root.getTransform(scaling);
        
        // Scaling to fit in view
        scaling.setScale(1.5/radius);
        
        root.setTransform(scaling);
	}
	
	public void plotFunction(Function function) {
		BranchGroup bg = function.getPlot();
		
		if (bg != null) {
			bg.compile();
			plots.addChild(bg);
			updateAxes();
			adjustZoom();
		}
	}
	
	public void changeColor(String expr, Color3f color) {
		Shape3D shape = shapes.get(expr);
		shape.setAppearance(new ColorAppearance(color));
	}
	
	
	public void removePlot(Function function) {
		BranchGroup bg = function.getPlot();
		
		if (bg != null) {
			bg.detach();
			functions.remove(bg);
		}
		adjustZoom();
	}
	
	public void showPlot(Function function) {
		BranchGroup bg = function.getPlot();
		
		boolean show = function.isVisible();
		if (show) {
			if (bg.getParent() == null) // Should be overkill. Lets keep for robustness
				plots.addChild(bg);	
		}
		else bg.detach();
	}
	
	


	protected void addLights(BranchGroup b) {
		// Create a bounds for the lights
		BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0),
				100.0);
		// Set up the global lights
		Color3f lightColour1 = new Color3f(1.0f, 1.0f, 1.0f);
		Vector3f lightDir1 = new Vector3f(-1.0f, -1.0f, -1.0f);
		Vector3f lightDir2 = new Vector3f(-1.0f, -1.0f, 1.0f);
		Color3f ambientColour = new Color3f(0.5f, 0.5f, 0.5f);
		AmbientLight ambientLight1 = new AmbientLight(ambientColour);
		ambientLight1.setInfluencingBounds(bounds);
		DirectionalLight light1 = new DirectionalLight(lightColour1, lightDir1);
		light1.setInfluencingBounds(bounds);
		DirectionalLight light2 = new DirectionalLight(lightColour1, lightDir2);
		light2.setInfluencingBounds(bounds);
		b.addChild(ambientLight1);
		b.addChild(light1);
		b.addChild(light2);

	}
	

	private void initMouseRotation() {
	    MouseRotate mouseRotate = new MouseRotate();
	    mouseRotate.setTransformGroup(root);
	    root.addChild(mouseRotate);
	    
//	    BoundingSphere bs = new BoundingSphere(new Point3d(0,0,0), 6.0);
	    mouseRotate.setSchedulingBounds(root.getBounds());
	}
	
	private void initAxis() {
		axes = new BranchGroup();
		currentAxis = new BranchGroup();
		currentAxis.addChild(new Axes().getSquareAxis(-10, 10));
		axes.addChild(currentAxis);
		
		axes.setCapability(Group.ALLOW_CHILDREN_WRITE);
		axes.setCapability(Group.ALLOW_CHILDREN_EXTEND);
		currentAxis.setCapability(BranchGroup.ALLOW_DETACH);
		root.addChild(axes);
	}
	
	private void updateAxes() {
		currentAxis.detach();
		
		currentAxis = new BranchGroup();
		currentAxis.setCapability(BranchGroup.ALLOW_DETACH);
		currentAxis.addChild(new Axes().getBoundingAxes(root));
		axes.addChild(currentAxis);
	}
	
	/*
	 * ADDED BY EMHER
	 */
	
	public void reset(){
		plots.removeAllChildren();
	}
	
	public void updateSize(int width,int height){
		canvas.setSize(width, height);
	}
	
}
