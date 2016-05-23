package com.starterkit.rcpapp.dialogs;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class ConnectionDialog extends TitleAreaDialog {
	private static final String serverURL = "http://localhost:8080/webstore";
	private Text text;
	private Label lblStatusLabel;
	private boolean connectionFlag;

	public ConnectionDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	public void create() {
		super.create();
		setTitle("Connection verification");
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		GridLayout layout = new GridLayout(2, false);
		container.setLayout(layout);
		new Label(container, SWT.NONE);

		lblStatusLabel = new Label(container, SWT.NONE);
		GridData gd_lblStatusLabel = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_lblStatusLabel.widthHint = 401;
		lblStatusLabel.setLayoutData(gd_lblStatusLabel);
		lblStatusLabel.setText("Please check your connection status by clicking Reconnect");
		new Label(container, SWT.NONE);

		text = new Text(container, SWT.BORDER);
		text.setEditable(false);
		GridData gd_text = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_text.heightHint = 44;
		text.setLayoutData(gd_text);
		new Label(container, SWT.NONE);

		Button btnReconnectButton = new Button(container, SWT.NONE);
		GridData gd_btnReconnectButton = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		gd_btnReconnectButton.widthHint = 136;
		btnReconnectButton.setLayoutData(gd_btnReconnectButton);
		btnReconnectButton.setText("Reconnect");
		btnReconnectButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setConnectionFlag(reconnectToServer());
				updateDialogData(connectionFlag);
			}
		});
		
		
		return area;
	}

	private void updateDialogData(boolean connectionFlag) {
		if (connectionFlag) {
			lblStatusLabel.setText("Congratulations! Now you can work with application!");
			setMessage("Warning", IMessageProvider.INFORMATION);
			text.setText("Everything all right");
			
		} else {
			lblStatusLabel.setText("Warning! Can not connect to server!");
			setMessage("Warning", IMessageProvider.ERROR);
			text.setText("There is a problem with connection to " + serverURL + "\n Check your web connection");

		}

	}

	private boolean reconnectToServer() {
		try {
			HttpURLConnection connection = (HttpURLConnection) new URL(serverURL + "/books/searchAll").openConnection();
			connection.setConnectTimeout(3000);
			connection.setReadTimeout(3000);
			connection.setRequestMethod("GET");

			int responseCode = connection.getResponseCode();
			return responseCode <= 201;
		} catch (IOException e) {
			return false;

		}

	}
	
	@Override
	protected void okPressed() {
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		if(connectionFlag == false){
			MessageDialog.openError(shell, "Error", "You can not work without connection!");
		} else {
			super.okPressed();
		}
		

		
	}

	public boolean isConnectionFlag() {
		return connectionFlag;
	}

	public void setConnectionFlag(boolean connectionFlag) {
		this.connectionFlag = connectionFlag;
	}

}
