package edu.rice.cs.hpc.traceviewer.painter;

import org.eclipse.swt.graphics.Image;

public class ImagePosition {
	final public Image image;
	final public int position;
	
	public ImagePosition(int position, Image image) 
	{
		this.image = image;
		this.position = position;
	}
}
