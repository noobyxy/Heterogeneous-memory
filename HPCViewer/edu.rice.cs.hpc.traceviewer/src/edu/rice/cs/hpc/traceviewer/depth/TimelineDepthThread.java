package edu.rice.cs.hpc.traceviewer.depth;

import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

import org.eclipse.core.runtime.IProgressMonitor;

import edu.rice.cs.hpc.traceviewer.data.controller.SpaceTimeDataController;
import edu.rice.cs.hpc.traceviewer.data.db.DataLinePainting;
import edu.rice.cs.hpc.traceviewer.data.db.DataPreparation;
import edu.rice.cs.hpc.traceviewer.data.db.ImageTraceAttributes;
import edu.rice.cs.hpc.traceviewer.data.db.TimelineDataSet;
import edu.rice.cs.hpc.traceviewer.timeline.BaseTimelineThread;

import edu.rice.cs.hpc.traceviewer.data.timeline.ProcessTimeline;


/*************************************************
 * 
 * Timeline thread for depth view
 *
 *************************************************/
public class TimelineDepthThread 
	extends BaseTimelineThread
{

	/*****
	 * Thread initialization
	 *  
	 * @param data : global data
	 * @param canvas : depth view canvas
	 * @param scaleX : The scale in the x-direction of pixels to time 
	 * @param scaleY : The scale in the y-direction of max depth
	 * @param width  : the width
	 */
	public TimelineDepthThread(SpaceTimeDataController data, 
			ImageTraceAttributes attributes,
			double scaleY, Queue<TimelineDataSet> queue, 
			AtomicInteger timelineDone, 
			boolean usingMidpoint, IProgressMonitor monitor)
	{
		super(data, attributes, scaleY, queue, timelineDone,  usingMidpoint, monitor);
	}


	@Override
	protected ProcessTimeline getNextTrace(AtomicInteger currentLine) {
		ProcessTimeline depthTrace = stData.getCurrentDepthTrace();
		if (depthTrace == null) {
			monitor.setCanceled(true);
			monitor.done(); // forcing to reset the title bar
			return null;
		}
		
		int currentDepthLineNum = currentLine.getAndIncrement();
		if (currentDepthLineNum < Math.min(attributes.numPixelsDepthV, stData.getMaxDepth())) {
			
			// I can't get the data from the ProcessTimeline directly, so create
			// a ProcessTimeline with data=null and then copy the actual data to
			// it.
			ProcessTimeline toDonate = new ProcessTimeline(currentDepthLineNum,
					stData.getScopeMap(), stData.getBaseData(), 
					stData.computeScaledProcess(), attributes.numPixelsH,
					attributes.getTimeInterval(), 
					stData.getMinBegTime() + attributes.getTimeBegin());

			toDonate.copyDataFrom(depthTrace);

			return toDonate;
		} else
			return null;

	}

	@Override
	protected boolean init(ProcessTimeline trace) {

		return true;
	}

	@Override
	protected void finalize() {
	}

	@Override
	protected DataPreparation getData( DataLinePainting data ) {
		
		// the current depth is the current line to be painted
		
		data.depth = data.ptl.line();
		
		return new DepthDataPreparation(data);
	}	
}
