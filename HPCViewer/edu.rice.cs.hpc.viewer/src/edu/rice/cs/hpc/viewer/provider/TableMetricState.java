package edu.rice.cs.hpc.viewer.provider;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.ui.AbstractSourceProvider;
import org.eclipse.ui.ISources;

import edu.rice.cs.hpc.data.experiment.BaseExperiment;
import edu.rice.cs.hpc.data.experiment.metric.DerivedMetric;


/***********************
 * 
 * Source provider class to manage table metric state
 * <p>
 * A table cam be modified (added, deleted, hidden, shown, renamed, ..)<br/>
 * A caller needs to call appropriate API to notify other views that
 *  a property of the table has changed
 *
 ***********************/
public class TableMetricState extends AbstractSourceProvider 
{
	static final public String METRIC_COLUMNS_VISIBLE = "edu.rice.cs.hpc.viewer.provider.metric.visible";
	static final public String METRIC_COLUMN_ADD      = "edu.rice.cs.hpc.viewer.provider.metric.add";

	@Override
	public void dispose() {	}

	@Override
	public Map<String, Object> getCurrentState() {
		Map<String, Object> map = new HashMap<String, Object>(2);
		Boolean allVisible = true;
		
		map.put(METRIC_COLUMNS_VISIBLE, allVisible);
		
		return map;
	}

	@Override
	public String[] getProvidedSourceNames() {
		return new String [] { METRIC_COLUMNS_VISIBLE };
	}

	public void notifyColumnChange(BaseExperiment experiment, Object status) {
		TableMetricData data = new TableMetricData(experiment, status);
		notifyStatusChange(METRIC_COLUMNS_VISIBLE, data);
	}
	
	public void notifyMetricAdd(BaseExperiment experiment, DerivedMetric objMetric) {
		TableMetricData data = new TableMetricData(experiment, objMetric);
		notifyStatusChange(METRIC_COLUMN_ADD, data);
	}
	
	private void notifyStatusChange(String id, Object value) {
		// 
		fireSourceChanged(ISources.ACTIVE_WORKBENCH_WINDOW, id, value);
	}
	
	
	/*******************************
	 * 
	 * Exchange Data between a notifier and listeners
	 * <p>The key of exchanging is the experiment object 
	 * (retrieved using {@link getExperiment}. <br/>
	 * It is listener responsibility to compare the experiment value with its experiment.
	 * If the experiment object is not the same, it has to be ignored.  
	 *
	 *******************************/
	static public class TableMetricData
	{
		private BaseExperiment experiment;
		private Object sourceValue;
		
		public TableMetricData(BaseExperiment experiment, Object sourceValue) {
			this.experiment  = experiment;
			this.sourceValue = sourceValue;
		}
		
		public BaseExperiment getExperiment() {
			return experiment;
		}
		
		
		public Object getValue() {
			return sourceValue;
		}
	}
}
