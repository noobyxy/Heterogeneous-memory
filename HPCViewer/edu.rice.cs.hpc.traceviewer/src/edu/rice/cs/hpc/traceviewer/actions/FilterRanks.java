package edu.rice.cs.hpc.traceviewer.actions;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.services.ISourceProviderService;

import edu.rice.cs.hpc.data.experiment.extdata.IFilteredData;
import edu.rice.cs.hpc.traceviewer.data.controller.SpaceTimeDataController;
import edu.rice.cs.hpc.traceviewer.filter.FilterDialog;
import edu.rice.cs.hpc.traceviewer.services.DataService;


/*****
 * 
 * Action class to filter ranks
 *
 */
public class FilterRanks extends AbstractHandler {
	


	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		final Shell shell = HandlerUtil.getActiveShell(event);
		IWorkbenchWindow winObj = HandlerUtil.getActiveWorkbenchWindow(event);
		ISourceProviderService sourceProviderService = (ISourceProviderService) winObj.getService(
				ISourceProviderService.class);
		
		DataService dataService = (DataService) sourceProviderService.getSourceProvider(DataService.DATA_PROVIDER);
        SpaceTimeDataController absData = dataService.getData();
        
		/*
		 * This isn't the prettiest, but when we are local, we don't want to set
		 * it to filtered unless we have to (ie. unless the user actually
		 * applies a filter). If the data is already filtered, we don't care and
		 * we just return the filtered data we have been using (which makes the
		 * call to set it redundant). If it's not, we wait to replace the
		 * current filter with the new filter until we know we have to.
		 */
        IFilteredData filteredBaseData = absData.getFilteredBaseData();
        if (filteredBaseData == null){
        	filteredBaseData = absData.createFilteredBaseData();
        }
        
        FilterDialog dlgFilter = new FilterDialog(shell, filteredBaseData);
		
		if (dlgFilter.open() == Dialog.OK){

			absData.setBaseData(filteredBaseData);

			dataService.broadcastUpdate(true);
		}
		
		return null;
	}

}
