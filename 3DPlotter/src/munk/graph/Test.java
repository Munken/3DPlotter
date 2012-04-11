package munk.graph;

import munk.graph.marching.ImplicitRecursive;

public class Test  {

  public static void main(String[] args) {
	ImplicitRecursive im = new ImplicitRecursive(0, 3, 0, 3, 0, 3, 0.1f);
	
	long current = System.currentTimeMillis();
	System.out.println(im.findStartCube());
	System.out.println(System.currentTimeMillis() - current);
  }
}