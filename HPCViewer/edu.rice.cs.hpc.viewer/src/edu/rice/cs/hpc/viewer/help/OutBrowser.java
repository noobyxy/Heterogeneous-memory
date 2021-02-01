package edu.rice.cs.hpc.viewer.help;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

public class OutBrowser {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Shell shell = new Shell(SWT.SHELL_TRIM);
		shell.setLayout(new GridLayout(1, true));

		final Browser browser = new Browser(shell, SWT.BORDER );
		GridData gridData1 = new GridData(GridData.FILL_BOTH);
		gridData1.horizontalSpan = 1;
		gridData1.minimumWidth = 200;
		gridData1.minimumHeight = 400;
		browser.setLayoutData(gridData1);				

		FileDialog objFileDlg = new FileDialog(shell);
		String sFile = objFileDlg.open();
		browser.setUrl(sFile);
		System.out.println("File: "+sFile);
		
		Display display = shell.getDisplay();
		shell.pack();
		shell.open();
		while (!shell.isDisposed())
			if (!display.readAndDispatch())
				display.sleep();		
	}

}
