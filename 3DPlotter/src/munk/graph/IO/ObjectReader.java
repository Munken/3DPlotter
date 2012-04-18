package munk.graph.IO;

import java.io.*;

public class ObjectReader {

	public static Object ObjectFromFile(File f) throws ClassNotFoundException, IOException{
		Object returnObject = null;
		ObjectInputStream inputStream = null;
		try {
			inputStream = new ObjectInputStream(new FileInputStream(f));
			returnObject = inputStream.readObject();
		} finally {
			//Close the ObjectOutputStream
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return returnObject;
	}
}
