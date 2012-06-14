package munk.graph.gui;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;

import javax.media.j3d.*;
import javax.swing.JPanel;
import javax.vecmath.*;

import munk.graph.function.Function;
import munk.graph.plot.Axes;
import munk.graph.rotaters.*;

import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.pickfast.PickCanvas;
import com.sun.j3d.utils.universe.SimpleUniverse;

@SuppressWarnings("serial")
public class Plotter3D extends JPanel{
	
	private static final int	OFFSCREEN_SCALE	= 3;
	private TransformGroup  root;
	private BranchGroup	plots;
	private BranchGroup axes;
	private SimpleUniverse universe;
	private Canvas3D canvas;
	private BranchGroup	currentAxis;
	private Canvas3D	offScreenCanvas;
	
	public Plotter3D() {
		this(600, 600);
	}
	public Plotter3D(int width, int heigth) {
	    GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
	    
	    canvas = new Canvas3D(config);
	    canvas.setSize(width, heigth);

	    universe = new SimpleUniverse(canvas);
	    BranchGroup group = new BranchGroup();
	    setupOffscreenCanvas();
	    
	    addLights(group);
    
	    root = new TransformGroup();
	    root.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
	    
	    
	    plots = new BranchGroup();
		plots.setCapability(Group.ALLOW_CHILDREN_EXTEND);
		plots.setCapability(Group.ALLOW_CHILDREN_WRITE);
	    initAxis();
	    
	    // Init rotaters
	    initMouseRotation();
	    canvas.addKeyListener(new KeyRotate(root));
	    canvas.addKeyListener(new ViewZoomer(universe.getViewingPlatform().getViewPlatformTransform()));
	    canvas.addMouseWheelListener(new WheelZoomer(universe.getViewingPlatform().getViewPlatformTransform()));
	    
	    root.addChild(plots);
	    group.addChild(root);
	    
	    group.compile();
	    
	    universe.addBranchGraph(group);
	     
	    initView();
	    add(canvas);
	    
	}
	
	private Canvas3D createCanvas3D(boolean offscreen) {
		GraphicsConfigTemplate3D gc3D = new GraphicsConfigTemplate3D();
		gc3D.setSceneAntialiasing(GraphicsConfigTemplate.PREFERRED);
		GraphicsDevice gd[] = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getScreenDevices();

		Canvas3D c3d = new Canvas3D(gd[0].getBestConfiguration(gc3D), offscreen);
		c3d.setSize(500, 500);
		
		return c3d;
	}

	private void setupOffscreenCanvas() {

		offScreenCanvas = createCanvas3D(true);
		int offScreenWidth = OFFSCREEN_SCALE*canvas.getWidth();
		int offScreenHeight = OFFSCREEN_SCALE*canvas.getHeight();
		offScreenCanvas.getScreen3D()
		.setSize(offScreenWidth, offScreenHeight);
		offScreenCanvas.getScreen3D().setPhysicalScreenHeight(
				0.0254 / 90 * offScreenHeight);
		offScreenCanvas.getScreen3D().setPhysicalScreenWidth(
				0.0254 / 90 * offScreenWidth);

		RenderedImage renderedImage = new BufferedImage(offScreenWidth,
				offScreenHeight, BufferedImage.TYPE_3BYTE_BGR);
		ImageComponent2D imageComponent = new ImageComponent2D(ImageComponent.FORMAT_RGB8,
				renderedImage);
		imageComponent.setCapability(ImageComponent2D.ALLOW_IMAGE_READ);
		offScreenCanvas.setOffScreenBuffer(imageComponent);

		universe.getViewer().getView().addCanvas3D(offScreenCanvas);

	}
	
	public RenderedImage takeScreenshot() {
		offScreenCanvas.renderOffScreenBuffer();
		offScreenCanvas.waitForOffScreenRendering();
		return offScreenCanvas.getOffScreenBuffer().getImage();
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
//		long start = System.currentTimeMillis();
		BranchGroup bg = function.getPlot();
//		System.out.println(System.currentTimeMillis() - start);
		
		if (bg != null) {
			plots.addChild(bg);
			updateAxes();
			adjustZoom();
		}
	}
	
	
	public void removePlot(Function function) {
		BranchGroup bg = function.getPlot();
		
		if (bg != null) {
			bg.detach();
		}
		adjustZoom();
	}
	
	public void showPlot(Function function) {
		BranchGroup bg = function.getPlot();
		if (bg != null) {
			boolean show = function.isVisible();
			if (show) {
				if (bg.getParent() == null) // Should be overkill. Lets keep for robustness
					plots.addChild(bg);	
			}
			else bg.detach();
		}
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
	    
	    BoundingSphere bs = new BoundingSphere(new Point3d(0,0,0), 6.0);    
	    mouseRotate.setSchedulingBounds(bs);
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
	
	public void updateSize(int width,int height){
		canvas.setSize(width, height);
	}
	
	public Function getPickedFunction(MouseEvent e) {
		PickCanvas pc = new PickCanvas(canvas, plots);
		pc.setMode(PickInfo.PICK_GEOMETRY);
//		pc.setTolerance(4);
		pc.setShapeLocation(e);
		pc.setFlags(PickInfo.NODE);

		PickInfo result = pc.pickClosest();
		
		if (result != null) {
			Node n = result.getNode();
			
			if (n.getUserData() instanceof Function)
				return (Function) n.getUserData();
			
		}
		
		return null;
	}
	
	
	@Override
	public synchronized void addMouseListener(MouseListener l) {
		canvas.addMouseListener(l);
	}
	
}
