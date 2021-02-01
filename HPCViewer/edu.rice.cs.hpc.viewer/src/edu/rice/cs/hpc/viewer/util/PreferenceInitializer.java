package edu.rice.cs.hpc.viewer.util;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import edu.rice.cs.hpc.viewer.framework.Activator;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
	 */
	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		//store.setDefault(PreferenceConstants.P_FONT_VIEW, Utilities.fontMetric.toString());
		store.setDefault(PreferenceConstants.P_PATH,"");
		store.setDefault(PreferenceConstants.P_THRESHOLD, 
				String.valueOf(PreferenceConstants.P_THRESHOLD_DEFAULT));
		store.setDefault(PreferenceConstants.P_CALLER_VIEW, true);
	}

}
