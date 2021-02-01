package edu.rice.cs.hpc.viewer.actions;


import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import edu.rice.cs.hpc.viewer.experiment.ExperimentManager;

public class OpenDatabase extends AbstractHandler {

	public Object execute(ExecutionEvent event) throws ExecutionException 
	{
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		final ExperimentManager expFile = new ExperimentManager(window); 
				//ExperimentData.getInstance(window).getExperimentManager();
		expFile.openFileExperiment(ExperimentManager.FLAG_DEFAULT);

		
		return null;
	}


}
