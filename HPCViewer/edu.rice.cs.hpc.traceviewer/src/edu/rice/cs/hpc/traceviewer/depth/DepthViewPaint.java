package edu.rice.cs.hpc.traceviewer.depth;

import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.GC;
import org.eclipse.ui.IWorkbenchWindow;

import edu.rice.cs.hpc.traceviewer.data.controller.SpaceTimeDataController;
import edu.rice.cs.hpc.traceviewer.data.db.ImageTraceAttributes;
import edu.rice.cs.hpc.traceviewer.data.db.TimelineDataSet;
import edu.rice.cs.hpc.traceviewer.painter.BasePaintThread;
import edu.rice.cs.hpc.traceviewer.painter.BaseViewPaint;
import edu.rice.cs.hpc.traceviewer.painter.ISpaceTimeCanvas;
import edu.rice.cs.hpc.traceviewer.painter.ImagePosition;

import edu.rice.cs.hpc.traceviewer.timeline.BaseTimelineThread;

/******************************************************
 * 
 * Painting class for depth view
 *
 ******************************************************/
public class DepthViewPaint extends BaseViewPaint {

	private final GC masterGC;
	private final AtomicInteger timelineDone, numDataCollected;
	private float numPixels;

	public DepthViewPaint(IWorkbenchWindow window, final GC masterGC, SpaceTimeDataController data,
			ImageTraceAttributes attributes, boolean changeBound, ISpaceTimeCanvas canvas, 
			ExecutorService threadExecutor) {
		
		super("Depth view", data, attributes, changeBound,  window, canvas, threadExecutor);
		this.masterGC = masterGC;
		timelineDone  = new AtomicInteger(0);
		numDataCollected  = new AtomicInteger(0);
	}

	@Override
	protected boolean startPainting(int linesToPaint, int numThreads, boolean changedBounds) 
	{
		int process = attributes.getPosition().process;
		
		// we need to check if the data is ready.
		// data is ready iff 
		//  - a process has been selected for the depth view (within the range)
		//  - and the main view has finished generated the timelines
		
		if (process >= attributes.getProcessBegin() && process <= attributes.getProcessEnd()) {
			// TODO warning: data races for accessing the current process timeline 
			if ( controller.getCurrentDepthTrace() != null) {
				numPixels = attributes.numPixelsDepthV/(float)controller.getMaxDepth();
				return changedBounds;
			}
		}
		return false;
	}


	@Override
	protected int getNumberOfLines() {
		return Math.min(attributes.numPixelsDepthV, controller.getMaxDepth());
	}

	@Override
	protected BaseTimelineThread getTimelineThread(ISpaceTimeCanvas canvas, double xscale, double yscale,
			Queue<TimelineDataSet> queue, IProgressMonitor monitor) {
		return new TimelineDepthThread( controller, attributes, yscale, queue, numDataCollected,
				controller.isEnableMidpoint(), monitor);
	}

	@Override
	protected void launchDataGettingThreads(boolean changedBounds,
			int numThreads) {
		//We don't want to get data here.
	}

	@Override
	protected BasePaintThread getPaintThread(
			Queue<TimelineDataSet> queue, int linesToPaint, 
			Device device, int width, IProgressMonitor monitor) {

		return new DepthPaintThread(controller, queue, linesToPaint, 
				numDataCollected, timelineDone, 
				device, width, monitor);
	}

	@Override
	protected void drawPainting(ISpaceTimeCanvas canvas, ImagePosition img) {
		if (masterGC != null && !masterGC.isDisposed() && img != null && img.image != null)
		{
			try {
				masterGC.drawImage(img.image, 0, 0, img.image.getBounds().width, 
					img.image.getBounds().height, 0, 
					Math.round(img.position*numPixels), 
					img.image.getBounds().width, img.image.getBounds().height);
			} catch (Exception e) {
				e.printStackTrace();
			}
			img.image.dispose();
		}
	}

	@Override
	protected void endPainting(boolean isCanceled) {
		if (masterGC != null && !masterGC.isDisposed())
			masterGC.dispose();
	}
}
