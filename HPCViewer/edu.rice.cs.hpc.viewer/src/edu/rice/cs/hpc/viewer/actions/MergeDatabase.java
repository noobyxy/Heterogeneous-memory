package edu.rice.cs.hpc.viewer.actions;

import java.io.File;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.dialogs.ListSelectionDialog;
import org.eclipse.ui.handlers.HandlerUtil;

import edu.rice.cs.hpc.data.experiment.Experiment;
import edu.rice.cs.hpc.data.experiment.merge.ExperimentMerger;
import edu.rice.cs.hpc.data.experiment.scope.RootScopeType;
import edu.rice.cs.hpc.viewer.experiment.ExperimentView;
import edu.rice.cs.hpc.viewer.window.ViewerWindow;
import edu.rice.cs.hpc.viewer.window.ViewerWindowManager;


/*******************************************************************
 * 
 * command action to merge two databases (at the moment)
 * 
 * Databases have to be loaded first before merged, and the user
 * needs to decide which databases to be combined
 * 
 *******************************************************************/
public abstract class MergeDatabase extends AbstractHandler 
{

	/***
	 * execute merging operation of two databases (or more)
	 * 	in case of more than 2 database, users have to select two db only
	 * 
	 * @param event
	 * @param type
	 * @return
	 * @throws ExecutionException
	 */
	public Object execute(ExecutionEvent event, final RootScopeType type) 
			throws ExecutionException {

		final IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		final ViewerWindow vWin = ViewerWindowManager.getViewerWindow(window);
		final Experiment[] dbArray = vWin.getExperiments();
		
		// need to get the display here before Eclipse removes the pointer on the current display widget
		final Display display = HandlerUtil.getActiveShell(event).getDisplay();

		// merge is enabled if the number of open databases is more than 1
		if (dbArray.length > 1) 
		{
			final Experiment db1;
			final Experiment db2;

			// if we have 2 open database, we can go directly merging them
			// otherwise we should ask user to select which database to be merged
			if (dbArray.length == 2)
			{
				db1 = dbArray[0];
				db2 = dbArray[1];
				
			} else
			{
				// selecting database
				ListSelectionDialog dlg = new ListSelectionDialog(window.getShell(), dbArray, 
						new ArrayContentProvider(), new ExperimentLabelProvider(), "Select two databases to merge:");
				dlg.setTitle("Merging database");
				dlg.open();
				Object[] selectedDatabases = dlg.getResult();

				if ((selectedDatabases != null) && (selectedDatabases.length == 2)) {

					db1 = (Experiment) selectedDatabases[0];
					db2 = (Experiment) selectedDatabases[1];
				} else if (selectedDatabases.length>2) {
					MessageDialog.openError(window.getShell(), "Error", "Please just select two databases.\nMerging more than two databases is not supported yet.");
					return null;
				} else
				{
					// either only select one or none of cancel
					return null;
				}
			}
			// try to asynchronously merge the experiments. it may take some time to finish
			display.asyncExec(new Runnable(){

				@Override
				public void run() {
					try {
						Experiment expMerged = ExperimentMerger.merge(db1, db2, type);

						ExperimentView ev = new ExperimentView(window.getActivePage());
						ev.generateView(expMerged);
					} catch (Exception e) {
						MessageDialog.openError(window.getShell(), "Error merging database",
								e.getMessage());
					}
				}				
			});
		}
		else
		{
			MessageDialog.openError( window.getShell(), "Error merging database", 
					"The number of open database has to be at least 2 to enable to merge");
		}

		return null;
	}


	/*****
	 * 
	 * label for the list of databases
	 *
	 */
	private class ExperimentLabelProvider extends LabelProvider {

		public String getText(Object element) 
		{
			final Experiment exp = (Experiment) element;
			final File file = exp.getXMLExperimentFile();

			final String path = file.getAbsolutePath();
			return path;
		}
	}
}
