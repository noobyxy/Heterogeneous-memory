package edu.rice.cs.hpc.viewer.provider;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.ui.AbstractSourceProvider;
import org.eclipse.ui.ISources;
import org.eclipse.ui.IWorkbenchWindow;
import edu.rice.cs.hpc.viewer.window.ViewerWindow;
import edu.rice.cs.hpc.viewer.window.ViewerWindowManager;

/**********************************************************
 * 
 * Database state provider.
 * The class provides services for different states of 
 * databases, whether the database is opened, needs to be
 * refreshed or has thread metric data.
 *
 **********************************************************/
public class DatabaseState extends AbstractSourceProvider 
{
	public final static String DATABASE_ACTIVE_STATE = "edu.rice.cs.hpc.viewer.provider.data.active";
	public final static String DATABASE_MERGE_STATE  = "edu.rice.cs.hpc.viewer.provider.data.merge";
	static final public String DATABASE_NEED_REFRESH = "edu.rice.cs.hpc.viewer.provider.data.refresh";
	static final public String DATABASE_THREAD_STATE = "edu.rice.cs.hpc.viewer.provider.data.threads";

	public final static String ENABLED = "ENABLED";
	public final static String DISABLED = "DISABLED";
	
	private int num_opened_database = 0;
	private boolean need_to_refresh = false;
	private boolean has_thread_data = false;
	
	public void dispose() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.ISourceProvider#getCurrentState()
	 */
	public Map getCurrentState() 
	{
		Map<String, Object> map = new HashMap<String, Object>(2);
		String value = num_opened_database>0 ? ENABLED : DISABLED;
		map.put(DATABASE_ACTIVE_STATE, value);
		
		value = num_opened_database>1 ? ENABLED : DISABLED;
		map.put(DATABASE_MERGE_STATE, value);
		
		map.put(DATABASE_NEED_REFRESH, Boolean.valueOf(need_to_refresh));
	    map.put(DATABASE_THREAD_STATE, Boolean.valueOf(has_thread_data));
		
		return map;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.ISourceProvider#getProvidedSourceNames()
	 */
	public String[] getProvidedSourceNames() 
	{
		return new String[] { DATABASE_ACTIVE_STATE, DATABASE_MERGE_STATE, 
							  DATABASE_NEED_REFRESH, DATABASE_THREAD_STATE };
	}

	/*****
	 * enable/disable menus
	 * @param window
	 */
	public void toogleEnabled(IWorkbenchWindow window) 
	{
		ViewerWindow vw = ViewerWindowManager.getViewerWindow(window);
		
		num_opened_database = vw.getOpenDatabases();
		String value = num_opened_database>0 ? ENABLED : DISABLED;
		setState(DATABASE_ACTIVE_STATE, value);
		
		value = num_opened_database>1 ? ENABLED : DISABLED;
		setState(DATABASE_MERGE_STATE, value);
	}

	/****
	 * refresh views
	 * 
	 * @param filter : needs to be filtered or not
	 */
	public void refreshDatabase(boolean filter)
	{
		need_to_refresh = filter;
		setState(DATABASE_NEED_REFRESH, Boolean.valueOf(filter));
	}
	
	/****
	 * change the state of the provider
	 * 
	 * @param provider_id : provider id
	 * @param state : the state of the provider. has to be matched with
	 * 		the state in plugin.xml
	 */
	public void setState(String provider_id, Object state)
	{
		fireSourceChanged(ISources.WORKBENCH, provider_id, state);
	}
}
