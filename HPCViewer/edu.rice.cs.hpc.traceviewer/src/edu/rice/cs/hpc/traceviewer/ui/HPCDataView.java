package edu.rice.cs.hpc.traceviewer.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISizeProvider;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import edu.rice.cs.hpc.traceviewer.data.controller.SpaceTimeDataController;
import edu.rice.cs.hpc.traceviewer.data.db.Position;
import edu.rice.cs.hpc.traceviewer.main.HPCTraceView;


/// A view for displaying application data objects
public class HPCDataView extends ViewPart implements ISizeProvider//, ITraceData, ITracePosition
{
	public static final String ID = "hpcdataview.view";
	
	Composite master;

	SpaceTimeDataController stData;
	
	DataViewer dataViewer;
		
	public HPCTraceView traceview;
	
	public void createPartControl(Composite _master) 
	{
		master = _master;
		try 
		{
			traceview = (HPCTraceView)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(HPCTraceView.ID);
		}
		catch (PartInitException e) 
		{
			traceview = null;
			e.printStackTrace();
			System.exit(0);
		}
				
		setupEverything();
	}
	
	private void setupEverything()
	{
		/*************************************************************************
		 * Master Composite
		 ************************************************************************/
		master.setLayout(new GridLayout());
		master.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, true));
				
		/*************************************************************************
		 * DataViewer
		 ************************************************************************/
		dataViewer = new DataViewer(master, this);		
	}
	
	
	public void updateView(SpaceTimeDataController _stData) 
	{
		this.stData = _stData;	
		this.dataViewer.updateView(_stData);
		
//		stData.addDataListener(this);
//		stData.addPositionListener(this);
	}

	public void setFocus()
	{
	}

	public int computePreferredSize(boolean width, int availableParallel, int availablePerpendicular, int preferredSize) 
	{
		return preferredSize;
	}

	public int getSizeFlags(boolean width) 
	{
		return width ? SWT.MAX : 0;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.rice.cs.hpc.traceviewer.events.ITraceData#setData(int)
	 */
	public void setData(int dataIdx) {
		this.dataViewer.setData(dataIdx);
	}

	/*
	 * (non-Javadoc)
	 * @see edu.rice.cs.hpc.traceviewer.events.ITracePosition#setPosition(edu.rice.cs.hpc.traceviewer.painter.Position)
	 */
	public void setPosition(Position position) {
		this.dataViewer.setSample(position, stData.getMaxDepth(), stData.getDataIndex());
	}
}
