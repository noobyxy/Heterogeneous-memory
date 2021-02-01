package edu.rice.cs.hpc.traceviewer.main;

import org.eclipse.swt.graphics.Image;

import edu.rice.cs.hpc.traceviewer.painter.ImagePosition;

public class DetailImagePosition extends ImagePosition {

	final public Image imageOriginal;
	
	public DetailImagePosition(int position, Image image, Image imageOriginal) {
		super(position, image);
		this.imageOriginal = imageOriginal;
	}

}
