package com.tang.zk.wizard.page;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.ResourceManager;

import com.tang.zk.CacheManager;

/**
 * 向导页，grid布局
 * 
 * @author Administrator
 *
 */
public class NewZookeeperConnectionWizardPage extends WizardPage{
	
	private Text serverName;
	private Text ip;
	private Text port;
	private Text userName;
	private Text password;
	private Button btnAuth;

	public NewZookeeperConnectionWizardPage(Wizard wizard) {
		super("Connect Zookeeper Parameters");
		setImageDescriptor(ResourceManager.getImageDescriptor(NewZookeeperConnectionWizardPage.class, "/resources/images/png/wizard-banners/new-zookeeper-connection.png"));
		this.setDescription("Connetcion Parameters");
		this.setTitle("Connect Zookeeper");
	}

	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		setControl(container);
		
		container.setLayout(new GridLayout(2,false));
		new Label(container, SWT.NONE);
		
		Group grpIsauth = new Group(container, SWT.NONE);
		grpIsauth.setText("Address");
		GridData gd_grpIsauth = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_grpIsauth.heightHint = 120;
		gd_grpIsauth.widthHint = 532;
		grpIsauth.setLayoutData(gd_grpIsauth);
		
		Label lblName = new Label(grpIsauth, SWT.NONE);
		lblName.setBounds(10, 28, 61, 17);
		lblName.setText("Name:");
		
		//lblName.
		
		serverName = new Text(grpIsauth, SWT.BORDER);
		serverName.setBounds(106, 23, 422, 23);
		
		serverName.addModifyListener(new ModifyListener() {
			
			public void modifyText(ModifyEvent e) {
				String value = ((Text)e.getSource()).getText();
				System.out.println("name is modifyed!!!" + value);
				if (null != CacheManager.getServerNode(value))
				{
					updateErrorStatus(true);
				}
				else
				{
					updateErrorStatus(false);
				}
			}
		});
		
		
		
		Label lblIp = new Label(grpIsauth, SWT.NONE);
		lblIp.setBounds(10, 65, 61, 17);
		lblIp.setText("Ip:");
		
		ip = new Text(grpIsauth, SWT.BORDER);
		ip.setBounds(106, 59, 422, 23);
		
		Label lblPort = new Label(grpIsauth, SWT.NONE);
		lblPort.setBounds(10, 95, 61, 17);
		lblPort.setText("Port:");
		
		port = new Text(grpIsauth, SWT.BORDER);
		port.setBounds(106, 95, 422, 23);
		new Label(container, SWT.NONE);
		
		Group grpIsAuth = new Group(container, SWT.NONE);
		grpIsAuth.setText("Is Auth");
		GridData gd_grpIsAuth = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_grpIsAuth.heightHint = 94;
		gd_grpIsAuth.widthHint = 536;
		grpIsAuth.setLayoutData(gd_grpIsAuth);
		
		btnAuth = new Button(grpIsAuth, SWT.CHECK);
		btnAuth.setBounds(10, 28, 98, 17);
		btnAuth.setText("Auth Switch");
		btnAuth.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				//如果选中则高亮password
				if(btnAuth.getSelection())
				{
					userName.setEnabled(true);
					password.setEnabled(true);
				}
				else
				{
					userName.setEnabled(false);
					password.setEnabled(false);
				}
			}
			
		});
		
		Label lblPasssword = new Label(grpIsAuth, SWT.NONE);
		lblPasssword.setBounds(10, 51, 72, 17);
		lblPasssword.setText("UserName:");
		
		userName = new Text(grpIsAuth, SWT.BORDER);
		userName.setBounds(106, 48, 422, 23);
		userName.setEnabled(false);
		
		Label lblNewLabel = new Label(grpIsAuth, SWT.NONE);
		lblNewLabel.setBounds(10, 87, 61, 17);
		lblNewLabel.setText("Password:");
		
		password = new Text(grpIsAuth, SWT.BORDER);
		password.setEnabled(false);
		password.setBounds(106, 81, 422, 23);
		
	}

	public void updateErrorStatus(boolean isError)
	{
		if(isError)
		{
			String errorMsg = "Name Must Be Unique.";
			this.setErrorMessage(errorMsg);
			this.setPageComplete(false);
		}
		else
		{
			this.setErrorMessage(null);
			this.setPageComplete(true);
		}
	}
	
	public Text getPassword() {
		return password;
	}

	public Text getIp() {
		return ip;
	}

	public Text getPort() {
		return port;
	}

	public Button getBtnAuth() {
		return btnAuth;
	}

	public Text getServerName() {
		return serverName;
	}

	public Text getUserName() {
		return userName;
	}
}
