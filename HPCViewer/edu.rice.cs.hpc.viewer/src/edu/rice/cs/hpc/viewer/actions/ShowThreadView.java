package edu.rice.cs.hpc.viewer.actions;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;
import edu.rice.cs.hpc.data.experiment.Experiment;
import edu.rice.cs.hpc.data.experiment.scope.RootScope;
import edu.rice.cs.hpc.data.experiment.scope.RootScopeType;
import edu.rice.cs.hpc.viewer.scope.thread.ThreadView;
import edu.rice.cs.hpc.viewer.util.Utilities;
import edu.rice.cs.hpc.viewer.window.Database;
import edu.rice.cs.hpc.viewer.window.ViewerWindow;
import edu.rice.cs.hpc.viewer.window.ViewerWindowManager;

public class ShowThreadView extends AbstractHandler 
{
	static final public String ID="edu.rice.cs.hpc.viewer.actions.ShowThreadView";
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException 
	{
		final IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		final IWorkbenchPage page = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage();
		
		if (page != null) {
			final Experiment experiment = Utilities.getActiveExperiment(window);
			try {
				IViewPart view = page.showView(ThreadView.ID, experiment.getDefaultDirectory().getAbsolutePath(),
						IWorkbenchPage.VIEW_ACTIVATE);
				if (view != null && (view instanceof ThreadView))
				{
					ViewerWindow vWin = ViewerWindowManager.getViewerWindow(window);
					final Database db = vWin.getDb(experiment.getDefaultDirectory().getAbsolutePath());
					RootScope scope   = experiment.getRootScope(RootScopeType.CallingContextTree);
					((ThreadView)view).setInput(db, scope, false);
				}
				
			} catch (PartInitException e) {
				e.printStackTrace();
				MessageDialog.openError(window.getShell(), "Error", e.getMessage());
			}
		}
		return null;
	}

}
