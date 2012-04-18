package munk.graph;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.print.*;
import java.io.FileNotFoundException;

import javax.media.j3d.*;
import javax.swing.*;
import javax.vecmath.*;

import com.sun.j3d.loaders.*;
import com.sun.j3d.loaders.objectfile.ObjectFile;
import com.sun.j3d.utils.behaviors.mouse.*;
import com.sun.j3d.utils.universe.SimpleUniverse;
public class Test extends JFrame implements ActionListener {
  private static final boolean spin = false;
  private static final boolean noTriangulate = false;
  private static final boolean noStripify = false;
  private static final double creaseAngle = 60.0;
  private JMenuItem snapshotItem;
  private JMenuItem printItem;
  private JMenuItem quitItem;
  private SimpleUniverse u;
  private Canvas3D canvas3D;
  private OffScreenCanvas3D offScreenCanvas3D;
  private static final int OFF_SCREEN_SCALE = 3;
  private class AppPanel extends JPanel {
    private String filename = null;
    public BranchGroup createSceneGraph(String args[]) {
      // Create the root of the branch graph
      BranchGroup objRoot = new BranchGroup();
      // Create a Transformgroup to scale all objects so they
      // appear in the scene.
      TransformGroup objScale = new TransformGroup();
      Transform3D t3d = new Transform3D();
      t3d.setScale(0.7);
      objScale.setTransform(t3d);
      objRoot.addChild(objScale);
      // Create the transform group node and initialize it to the
      // identity. Enable the TRANSFORM_WRITE capability so that
      // our behavior code can modify it at runtime. Add it to the
      // root of the subgraph.
      TransformGroup objTrans = new TransformGroup();
      objTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
      objTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
      objScale.addChild(objTrans);
      int flags = ObjectFile.RESIZE;
      if (!noTriangulate)
        flags |= ObjectFile.TRIANGULATE;
      if (!noStripify)
        flags |= ObjectFile.STRIPIFY;
      ObjectFile f = new ObjectFile(flags,
          (float) (creaseAngle * Math.PI / 180.0));
      Scene s = null;
      try {
        s = f.load(filename);
      } catch (FileNotFoundException e) {
        System.err.println(e);
        System.exit(1);
      } catch (ParsingErrorException e) {
        e.printStackTrace();
        System.exit(1);
      } catch (IncorrectFormatException e) {
        System.err.println(e);
        System.exit(1);
      }
      objTrans.addChild(s.getSceneGroup());
      BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0,
          0.0), 100.0);
      if (spin) {
        Transform3D yAxis = new Transform3D();
        Alpha rotationAlpha = new Alpha(-1, Alpha.INCREASING_ENABLE, 0,
            0, 4000, 0, 0, 0, 0, 0);
        RotationInterpolator rotator = new RotationInterpolator(
            rotationAlpha, objTrans, yAxis, 0.0f,
            (float) Math.PI * 2.0f);
        rotator.setSchedulingBounds(bounds);
        objTrans.addChild(rotator);
      } else {
        // Create the rotate behavior node
        MouseRotate behavior = new MouseRotate();
        behavior.setTransformGroup(objTrans);
        objTrans.addChild(behavior);
        behavior.setSchedulingBounds(bounds);
        // Create the zoom behavior node
        MouseZoom behavior2 = new MouseZoom();
        behavior2.setTransformGroup(objTrans);
        objTrans.addChild(behavior2);
        behavior2.setSchedulingBounds(bounds);
        // Create the translate behavior node
        MouseTranslate behavior3 = new MouseTranslate();
        behavior3.setTransformGroup(objTrans);
        objTrans.addChild(behavior3);
        behavior3.setSchedulingBounds(bounds);
      }
      // Set up the background
      Color3f bgColor = new Color3f(0.05f, 0.05f, 0.5f);
      Background bgNode = new Background(bgColor);
      bgNode.setApplicationBounds(bounds);
      objRoot.addChild(bgNode);
      // Set up the ambient light
      Color3f ambientColor = new Color3f(0.1f, 0.1f, 0.1f);
      AmbientLight ambientLightNode = new AmbientLight(ambientColor);
      ambientLightNode.setInfluencingBounds(bounds);
      objRoot.addChild(ambientLightNode);
      // Set up the directional lights
      Color3f light1Color = new Color3f(1.0f, 1.0f, 0.9f);
      Vector3f light1Direction = new Vector3f(4.0f, -7.0f, -12.0f);
      Color3f light2Color = new Color3f(0.3f, 0.3f, 0.4f);
      Vector3f light2Direction = new Vector3f(-6.0f, -2.0f, -1.0f);
      DirectionalLight light1 = new DirectionalLight(light1Color,
          light1Direction);
      light1.setInfluencingBounds(bounds);
      objRoot.addChild(light1);
      DirectionalLight light2 = new DirectionalLight(light2Color,
          light2Direction);
      light2.setInfluencingBounds(bounds);
      objRoot.addChild(light2);
      return objRoot;
    }
    private void usage() {
      System.out.println("Usage: java PrintCanvas3D <.obj file>");
      System.exit(0);
    } // End of usage
    // Create the Canvas3D (both on-screen and off-screen)
    private void createCanvas3D(String[] args) {
      if (args.length == 0) {
        usage();
      } else {
        for (int i = 0; i < args.length; i++) {
          if (args[i].startsWith("-")) {
            System.err.println("Argument " + args[i]
                + " ignored.");
          } else {
            filename = args[i];
          }
        }
      }
      if (filename == null) {
        usage();
      }
      // Create Canvas3D
      GraphicsConfiguration config = SimpleUniverse
          .getPreferredConfiguration();
      canvas3D = new Canvas3D(config);
      canvas3D.setSize(600, 450);
      // Create a simple scene and attach it to the virtual universe
      BranchGroup scene = createSceneGraph(args);
      u = new SimpleUniverse(canvas3D);
      // This will move the ViewPlatform back a bit so the
      // objects in the scene can be viewed.
      u.getViewingPlatform().setNominalViewingTransform();
      u.addBranchGraph(scene);
      // Create the off-screen Canvas3D object
      offScreenCanvas3D = new OffScreenCanvas3D(config, true);
      // Set the off-screen size based on a scale factor times the
      // on-screen size
      Screen3D sOn = canvas3D.getScreen3D();
      Screen3D sOff = offScreenCanvas3D.getScreen3D();
      Dimension dim = sOn.getSize();
      dim.width *= OFF_SCREEN_SCALE;
      dim.height *= OFF_SCREEN_SCALE;
      sOff.setSize(dim);
      sOff.setPhysicalScreenWidth(sOn.getPhysicalScreenWidth()
          * OFF_SCREEN_SCALE);
      sOff.setPhysicalScreenHeight(sOn.getPhysicalScreenHeight()
          * OFF_SCREEN_SCALE);
      // attach the offscreen canvas to the view
      u.getViewer().getView().addCanvas3D(offScreenCanvas3D);
    }
    private AppPanel(String args[]) {
      setLayout(new BorderLayout());
      // Create Canvas3D and scene graph
      createCanvas3D(args);
      add("Center", canvas3D);
    }
  }
  public void actionPerformed(ActionEvent event) {
    Object target = event.getSource();
    if ((target == snapshotItem) || (target == printItem)) {
      Point loc = canvas3D.getLocationOnScreen();
      offScreenCanvas3D.setOffScreenLocation(loc);
      Dimension dim = canvas3D.getSize();
      dim.width *= OFF_SCREEN_SCALE;
      dim.height *= OFF_SCREEN_SCALE;
      BufferedImage bImage = offScreenCanvas3D.doRender(dim.width,
          dim.height);
      if (target == snapshotItem) {
        new ImageDisplayer(bImage);
      } else { // (target == printItem)
        new ImagePrinter(bImage).print();
      }
    } else if (target == quitItem) {
      u.removeAllLocales();
      System.exit(0);
    }
  }
  private JMenuBar createMenuBar() {
    JMenuBar menuBar = new JMenuBar();
    JMenu fileMenu = new JMenu("File");
    snapshotItem = new JMenuItem("Snapshot");
    snapshotItem.addActionListener(this);
    printItem = new JMenuItem("Print...");
    printItem.addActionListener(this);
    quitItem = new JMenuItem("Quit");
    quitItem.addActionListener(this);
    fileMenu.add(snapshotItem);
    fileMenu.add(printItem);
    fileMenu.add(new JSeparator());
    fileMenu.add(quitItem);
    menuBar.add(fileMenu);
    return menuBar;
  }
  private Test(String args[]) {
    this.setTitle("Canvas3D Print Test");
    // Create and initialize menu bar
    JPopupMenu.setDefaultLightWeightPopupEnabled(false);
    this.setJMenuBar(createMenuBar());
    // Handle the close event
    this.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent winEvent) {
        System.exit(0);
      }
    });
    // Add main panel to top-level frame and make it visible
    this.getContentPane().add(new AppPanel(args));
    this.pack();
    this.setVisible(true);
  }
  public static void main(String[] args) {
    new Test(args);
  }
}
class OffScreenCanvas3D extends Canvas3D {
  OffScreenCanvas3D(GraphicsConfiguration graphicsConfiguration,
      boolean offScreen) {
    super(graphicsConfiguration, offScreen);
  }
  BufferedImage doRender(int width, int height) {
    BufferedImage bImage = new BufferedImage(width, height,
        BufferedImage.TYPE_INT_ARGB);
    ImageComponent2D buffer = new ImageComponent2D(
        ImageComponent.FORMAT_RGBA, bImage);
    setOffScreenBuffer(buffer);
    renderOffScreenBuffer();
    waitForOffScreenRendering();
    bImage = getOffScreenBuffer().getImage();
    return bImage;
  }
  public void postSwap() {
    // No-op since we always wait for off-screen rendering to complete
  }
}
class ImageDisplayer extends JFrame implements ActionListener {
  BufferedImage bImage;
  private class ImagePanel extends JPanel {
    public void paint(Graphics g) {
      g.setColor(Color.black);
      g.fillRect(0, 0, getSize().width, getSize().height);
      g.drawImage(bImage, 0, 0, this);
    }
    private ImagePanel() {
      setPreferredSize(new Dimension(bImage.getWidth(), bImage
          .getHeight()));
    }
  }
  private JMenuItem printItem;
  private JMenuItem closeItem;
  public void actionPerformed(ActionEvent event) {
    Object target = event.getSource();
    if (target == printItem) {
      new ImagePrinter(bImage).print();
    } else if (target == closeItem) {
      this.removeAll();
      this.setVisible(false);
      bImage = null;
    }
  }
  private JMenuBar createMenuBar() {
    JMenuBar menuBar = new JMenuBar();
    JMenu fileMenu = new JMenu("File");
    printItem = new JMenuItem("Print...");
    printItem.addActionListener(this);
    closeItem = new JMenuItem("Close");
    closeItem.addActionListener(this);
    fileMenu.add(printItem);
    fileMenu.add(new JSeparator());
    fileMenu.add(closeItem);
    menuBar.add(fileMenu);
    return menuBar;
  }
  ImageDisplayer(BufferedImage bImage) {
    this.bImage = bImage;
    this.setTitle("Off-screen Canvas3D Snapshot");
    // Create and initialize menu bar
    this.setJMenuBar(createMenuBar());
    // Create scroll pane, and embedded image panel
    ImagePanel imagePanel = new ImagePanel();
    JScrollPane scrollPane = new JScrollPane(imagePanel);
    scrollPane.getViewport().setPreferredSize(new Dimension(700, 700));
    // Add scroll pane to the frame and make it visible
    this.getContentPane().add(scrollPane);
    this.pack();
    this.setVisible(true);
  }
}
class ImagePrinter implements Printable, ImageObserver {
  BufferedImage bImage;
  public int print(Graphics g, PageFormat pf, int pi) throws PrinterException {
    if (pi >= 1) {
      return Printable.NO_SUCH_PAGE;
    }
    Graphics2D g2d = (Graphics2D) g;
    //g2d.translate(pf.getImageableX(), pf.getImageableY());
    AffineTransform t2d = new AffineTransform();
    t2d.translate(pf.getImageableX(), pf.getImageableY());
    double xscale = pf.getImageableWidth() / (double) bImage.getWidth();
    double yscale = pf.getImageableHeight() / (double) bImage.getHeight();
    double scale = Math.min(xscale, yscale);
    t2d.scale(scale, scale);
    try {
      g2d.drawImage(bImage, t2d, this);
    } catch (Exception ex) {
      ex.printStackTrace();
      return Printable.NO_SUCH_PAGE;
    }
    return Printable.PAGE_EXISTS;
  }
  void print() {
    PrinterJob printJob = PrinterJob.getPrinterJob();
    PageFormat pageFormat = printJob.defaultPage();
    pageFormat.setOrientation(PageFormat.LANDSCAPE);
    pageFormat = printJob.validatePage(pageFormat);
    printJob.setPrintable(this, pageFormat);
    if (printJob.printDialog()) {
      try {
        printJob.print();
      } catch (PrinterException ex) {
        ex.printStackTrace();
      }
    }
  }
  public boolean imageUpdate(Image img, int infoflags, int x, int y,
      int width, int height) {
    return false;
  }
  ImagePrinter(BufferedImage bImage) {
    this.bImage = bImage;
  }
}