package edu.rice.cs.hpc.viewer.window;

import java.io.IOException;

import edu.rice.cs.hpc.data.experiment.Experiment;
import edu.rice.cs.hpc.data.experiment.extdata.IThreadDataCollection;
import edu.rice.cs.hpc.data.experiment.metric.BaseMetric;
import edu.rice.cs.hpc.data.experiment.metric.MetricRaw;
import edu.rice.cs.hpc.viewer.experiment.ExperimentView;
import edu.rice.cs.hpc.viewer.metric.ThreadDataCollectionFactory;

public class Database 
{
	private int winIndex;
	private Experiment experiment;
	private ExperimentView view;
	private IThreadDataCollection dataThread;
	
	/**
	 *  get the index of the viewer window in which this database is displayed.
	 * @return
	 */
	public int getWindowIndex () {
		return winIndex;
	}


	/**
	 *  get the Experiment class used for this database
	 * @return
	 */
	public Experiment getExperiment () {
		return experiment; //this.view.getExperimentData().getExperiment(); // 
	}

	
	/**
	 *  get the ExperimentView class used for this database
	 * @param path
	 */
	public ExperimentView getExperimentView () {
		return view;
	}

	/**
	 *  set the viewer window index to record the window in which this database is displayed.
	 * @param index
	 */
	public void setWindowIndex (int index) {
		winIndex = index;
	}


	/**
	 *  set the Experiment class used for this database
	 * @param path
	 * @throws IOException 
	 */
	public void setExperiment (Experiment experiment) throws IOException {
		this.experiment = experiment;
		// TODO hack: since we just created the manager, we need to inform
		// MetricRaw to set the new manager
		BaseMetric[]metrics = experiment.getMetricRaw();
		if (metrics != null) {
			dataThread = ThreadDataCollectionFactory.build(experiment);
			for (BaseMetric metric: metrics)
			{
				if (metric instanceof MetricRaw)
					((MetricRaw)metric).setThreadData(dataThread);
			}
		}
	}
	
	public IThreadDataCollection getThreadDataCollection()
	{
		return dataThread;
	}

	/**
	 *  set the ExperimentView class used for this database
	 * @param path
	 */
	public void setExperimentView (ExperimentView experView) {
		view = experView;
	}
	
	public void dispose() {
		experiment.dispose();
		if (dataThread != null)
			dataThread.dispose();
	}
}
