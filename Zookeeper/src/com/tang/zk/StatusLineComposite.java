package com.tang.zk;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class StatusLineComposite extends Composite{

	private Label server;
	private Label status;
	private Label label_1;
	
	public StatusLineComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(5, false));
		
		label_1 = new Label(this, SWT.SEPARATOR | SWT.VERTICAL);
		GridData gd_label_1 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_label_1.heightHint = 20;
		label_1.setLayoutData(gd_label_1);
		
		server = new Label(this, SWT.NONE);
		server.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		server.setText("Server");
		
		Label label = new Label(this, SWT.SEPARATOR);
		GridData gd_label = new GridData(SWT.CENTER, SWT.CENTER, false, false, 2, 1);
		gd_label.heightHint = 20;
		label.setLayoutData(gd_label);
		
		status = new Label(this, SWT.NONE);
		GridData gd_path = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_path.widthHint = 80;
		status.setLayoutData(gd_path);
		status.setText("Status");
	}

	public Label getServer() {
		return server;
	}

	public void setServer(Label server) {
		this.server = server;
	}

	public Label getStatus() {
		return status;
	}

	public void setStatus(Label status) {
		this.status = status;
	}

}
