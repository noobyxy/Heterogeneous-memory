package edu.rice.cs.hpc.traceviewer.actions;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.services.ISourceProviderService;

import edu.rice.cs.hpc.traceviewer.depth.HPCDepthView;
import edu.rice.cs.hpc.traceviewer.services.DataService;

public class Refresh extends AbstractHandler 
{

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		final IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		if (window != null) {
			final IWorkbenchPage page = window.getActivePage();
			IViewPart view = page.findView(HPCDepthView.ID);
			if (view != null) {
				ISourceProviderService sourceProviderService = (ISourceProviderService) window.getService(
						ISourceProviderService.class);
				
				DataService dataService = (DataService) sourceProviderService.getSourceProvider(DataService.DATA_PROVIDER);
				// broadcast to all views if the data is available
				if (dataService.isDataAvailable())
					dataService.broadcastUpdate(null);

			}
		}
		return null;
	}

}
