package com.starterkit.rcpapp;

import java.net.ConnectException;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import com.starterkit.rcpapp.dialogs.ConnectionDialog;

public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

	public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
		super(configurer);
	}

	@Override
	public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
		return new ApplicationActionBarAdvisor(configurer);
	}

	@Override
	public void preWindowOpen() {
		IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
		configurer.setInitialSize(new Point(1400, 900));
		configurer.setShowCoolBar(false);
		configurer.setShowStatusLine(false);
		configurer.setTitle("StrarterKit");
		configurer.setShowPerspectiveBar(true);
	}

	@Override
	public void postWindowOpen() {
		super.postWindowOpen();
		setUpConnection();
	}

	private ConnectionDialog createConnectionDialog() {
		IWorkbench workbench = PlatformUI.getWorkbench();
		Shell shell = workbench.getActiveWorkbenchWindow().getShell();
		ConnectionDialog connectionDialog = new ConnectionDialog(shell);
		connectionDialog.create();
		return connectionDialog;
	}

	private void setUpConnection() {
		ConnectionDialog connectionDialog = createConnectionDialog();
		IWorkbench workbench = PlatformUI.getWorkbench();
		Shell shell = workbench.getActiveWorkbenchWindow().getShell();
		while (!(connectionDialog.isConnectionFlag())) {
			if (connectionDialog.open() == Dialog.CANCEL) {
				try {
					shell.open();
				} finally {
					if (!shell.isDisposed()) {
						shell.dispose();
					}
				}
				System.exit(0);
				break;
			}
			
		}

	}
}
