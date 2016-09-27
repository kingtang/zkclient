package com.tang.zk.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.ResourceManager;

import com.tang.zk.ZKClientApplication;
import com.tang.zk.wizard.ZooKeeperConnectionNewWizard;

public class NewZookeeperConnectionAction extends Action{

	private ZooKeeperConnectionNewWizard newWizard;
	
	private ApplicationWindow zkManager;
	
	public NewZookeeperConnectionAction(ApplicationWindow zkManager)
	{
		this.setText("New Connection");
		this.zkManager = zkManager;
	}
	
	public void run() {
		if(!isEnabled())
		{
			return;
		}
		System.out.println("NewZookeeperConnectionAction");
		newWizard = new ZooKeeperConnectionNewWizard();
		newWizard.setZkManager((ZKClientApplication)zkManager);
		WizardDialog wizardDialog = new WizardDialog(getActiveShell(), newWizard)
		{
			//只显示关闭按钮
			protected int getShellStyle() {
				return SWT.DIALOG_TRIM;
			}

			//覆写父类方法，将finish改写为connect
			protected Button createButton(Composite parent, int id,
					String label, boolean defaultButton) {
				Button button = super.createButton(parent, id, label, defaultButton);
				if(null !=button.getData() && 16 == ((Integer)button.getData()).intValue())
				{
					button.setText("Connect");
				}
				return button;
			}
		};
		
		WizardDialog.setDefaultImage(ResourceManager.getImage(NewZookeeperConnectionAction.class, "/resources/images/png/wizards/new-zookeeper-connection.png"));
        wizardDialog.setBlockOnOpen(true);
        wizardDialog.open();
	}

	private Shell getActiveShell() {
		return Display.getCurrent().getActiveShell();
	}

	public ApplicationWindow getZkManager() {
		return zkManager;
	}

	public void setZkManager(ApplicationWindow zkManager) {
		this.zkManager = zkManager;
	}

}
