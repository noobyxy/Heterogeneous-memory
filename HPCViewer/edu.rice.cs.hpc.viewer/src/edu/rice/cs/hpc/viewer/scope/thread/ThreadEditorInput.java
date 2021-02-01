package edu.rice.cs.hpc.viewer.scope.thread;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import edu.rice.cs.hpc.data.experiment.metric.MetricRaw;
import edu.rice.cs.hpc.viewer.window.Database;

public class ThreadEditorInput implements IEditorInput {

	private final Database database;
	private final MetricRaw metric;
	
	public ThreadEditorInput(Database database, MetricRaw metric)
	{
		this.metric   = metric;
		this.database = database;
	}
	
	public Database getDatabase()
	{
		return database;
	}
	
	public MetricRaw getMetric()
	{
		return metric;
	}
	
	public String getID() {
		return getID(database, metric);
	}
	
	static public String getID(Database database, MetricRaw metric) {
		String ID = database.getWindowIndex() + ":" + metric.getID();
		return ID;
	}
	
	@Override
	public Object getAdapter(Class adapter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean exists() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		return database.getExperiment().getDefaultDirectory().getAbsolutePath();
	}

	@Override
	public IPersistableElement getPersistable() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getToolTipText() {
		// TODO Auto-generated method stub
		return database.getExperiment().getDefaultDirectory().getAbsolutePath();
	}

}
