package edu.rice.cs.hpc.filter.service;

import java.util.HashMap;
import java.util.Map;
import org.eclipse.ui.AbstractSourceProvider;
import org.eclipse.ui.ISources;

/**************************************************************
 * 
 * Class to manage filter state, whether it needs to be refreshed
 * or there's a filter selection in the view
 *
 **************************************************************/
public class FilterStateProvider extends AbstractSourceProvider 
{
	final static public String FILTER_REFRESH_PROVIDER = "edu.rice.cs.hpc.filter.update";
	final static public String FILTER_ENABLE_PROVIDER = "edu.rice.cs.hpc.filter.enable";
	
	final static public String TOGGLE_COMMAND = "org.eclipse.ui.commands.toggleState";
	final static public String SELECTED_STATE = "SELECTED";
	
	public FilterStateProvider() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public Map<String, Object> getCurrentState() {
		Map<String, Object> map = new HashMap<String, Object>(1);
		map.put(FILTER_REFRESH_PROVIDER, FilterMap.getInstance());
		
		final String filterVal = System.getProperty("FILTER");
		map.put(FILTER_ENABLE_PROVIDER, filterVal);
		
		return map;
	}

	@Override
	public String[] getProvidedSourceNames() {
		return new String[] {FILTER_REFRESH_PROVIDER, FILTER_ENABLE_PROVIDER};
	}
	
	
	/*****
	 * refresh the table as the filter pattern may change
	 * Usually called by FilterAdd and FilterDelete 
	 */
	public void refresh()
	{
		fireSourceChanged(ISources.WORKBENCH, FILTER_REFRESH_PROVIDER, true);
	}
}
