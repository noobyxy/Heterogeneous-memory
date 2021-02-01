/**
 * 
 */
package edu.rice.cs.hpc.viewer.help;

import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.resources.IFile;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.swt.browser.*;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.SWT;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.ui.part.FileEditorInput;
import org.osgi.framework.Bundle;

import edu.rice.cs.hpc.viewer.framework.Activator;


/**
 * @author laksonoadhianto
 *
 */
public class HTMLEditor extends EditorPart {
	public static String ID = "edu.rice.cs.hpc.viewer.util.HTMLEditor";

	
	/**
	 * SWT Browser 
	 */
	protected Browser browser;
	/**
	 * The address of URL document ot display
	 */
	protected String sURI;
	protected String sFilePath;
	
	/**
	 * Default constructor
	 */
	public HTMLEditor() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#doSave(org.eclipse.core.runtime.IProgressMonitor)
	 */
	//@Override
	public void doSave(IProgressMonitor monitor) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#doSaveAs()
	 */
	//@Override
	public void doSaveAs() {
		// TODO Auto-generated method stub

	}

	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#init(org.eclipse.ui.IEditorSite, org.eclipse.ui.IEditorInput)
	 */
	//@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {

		this.setSite(site);
		this.setInput(input);

		FileEditorInput objInput = (FileEditorInput) input;
		IFile file = objInput.getFile();
		if(file != null) {
			IPath objPath = file.getFullPath();
			Bundle plugin = Activator.getDefault().getBundle();
			URL fileInPlugin = FileLocator.find(plugin, objPath, null);

			try {
				URL pageUrl = FileLocator.toFileURL(fileInPlugin);
				this.sFilePath = file.getName();
				sURI = pageUrl.toString();
			} catch (IOException e) {

				e.printStackTrace();
			}
			
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#isDirty()
	 */
	//@Override
	public boolean isDirty() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#isSaveAsAllowed()
	 */
	//@Override
	public boolean isSaveAsAllowed() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.ISaveablePart#isSaveOnCloseNeeded()
	 */
	public boolean isSaveOnCloseNeeded() {
		return false;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	//@Override
	public void createPartControl(Composite parent) {
		try {
			// attempt to instantiate browser widget
			browser = new Browser(parent, SWT.BORDER);
			// set the property
			browser.setData(HTMLEditor.ID, this);
			// display the URL
			GridDataFactory.fillDefaults().grab(true, true).applyTo(browser);

			browser.setUrl(sURI);

		} catch (SWTError e) {
			// if the platform doesn't support pluggable browser,
			//   then we show error message
			MessageDialog.openError(getSite().getShell(), "Unable to display HTML page", 
					"HPCViewer is unable to open HTML page to display the help file\n"
					+ "Please refer to the manual in HPCToolkit website (http://hpctoolkit.org)");
		}
		// display the original source of the HTML file (debugging purpose)
		this.setContentDescription(sURI);
		// set the title of the editor
		this.setPartName(this.getEditorInput().getName());
		
		// set the layout: the browser has to be expanded as much as possible
		GridLayoutFactory.fillDefaults().numColumns(1).generateLayout(parent);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	//@Override
	public void setFocus() {
		if (browser != null)
			this.browser.setFocus();
	}
	
}
