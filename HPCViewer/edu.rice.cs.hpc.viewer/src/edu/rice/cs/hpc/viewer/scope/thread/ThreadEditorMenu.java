package edu.rice.cs.hpc.viewer.scope.thread;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;

import edu.rice.cs.hpc.data.experiment.Experiment;
import edu.rice.cs.hpc.data.experiment.metric.BaseMetric;
import edu.rice.cs.hpc.data.experiment.metric.MetricRaw;
import edu.rice.cs.hpc.viewer.window.Database;

/***********************************************************************************
 * 
 * Class to create thread map menu
 *
 ***********************************************************************************/
public class ThreadEditorMenu 
{
	/*****
	 * create a list of menus for thread map for each thread-level metric
	 * 
	 * @param window : current active window
	 * @param mgr : the menu manager
	 * @param database : the current database (should have thread-level metric)
	 */
	static public void createAdditionalMenu(IWorkbenchWindow window, IMenuManager mgr,
			Database database) {
		if (database != null) {
			Experiment experiment = database.getExperiment();
			BaseMetric []metrics = experiment.getMetricRaw();
			
			if (metrics != null)
				for(BaseMetric m : metrics) {
					ThreadEditorAction action = new ThreadEditorAction(
							window.getActivePage(), database, (MetricRaw) m);
					mgr.add(action);
				}
		}
	}

	/***************
	 * 
	 * class to generate the menu for a specific metric
	 *
	 ***************/
	static private class ThreadEditorAction extends Action
	{
		final private Database database;
		final private MetricRaw metric;
		final private IWorkbenchPage page;
		
		ThreadEditorAction(IWorkbenchPage page, Database database, MetricRaw metric) {
			super("Show the map of " + metric.getDisplayName());
			this.page     = page;
			this.database = database;
			this.metric   = metric;
		}
		
		public void run() {
	    	String ID = ThreadEditorInput.getID(database, metric);
	    	try {
				showEditor(ID);
			} catch (PartInitException e) {
				MessageDialog.openError( page.getWorkbenchWindow().getShell(), 
						"Unable to open the editor", e.getMessage());
				e.printStackTrace();
			}
		}
		
		private void showEditor(String ID) throws PartInitException {
			ThreadEditorInput objInput = null;
			
			// first, try to find from the existing opened editor 
			IEditorReference editors[] = page.getEditorReferences();
			if (editors != null) {
				//-------------------------------------------------------------------
				// look at all active editors if our editor has been there or not
				//-------------------------------------------------------------------
				for (IEditorReference editor : editors) {
					IEditorInput input;
					input = editor.getEditorInput();
					if (input instanceof ThreadEditorInput) {
						ThreadEditorInput tei = (ThreadEditorInput) input;
						if (ID.equals(tei.getID())) {
							objInput = tei;
						}
					}
				}
			}
			// if there is no opened editor, we create a new one
			if (objInput == null) {
				objInput = new ThreadEditorInput(database, metric);
			}
			page.openEditor(objInput, ThreadEditor.ID);
		}
	}
}
