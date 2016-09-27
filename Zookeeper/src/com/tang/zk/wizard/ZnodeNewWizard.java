package com.tang.zk.wizard;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.wb.swt.ResourceManager;

import com.tang.zk.ZKConnectionManager;
import com.tang.zk.data.Node;
import com.tang.zk.model.ZKElement;
import com.tang.zk.wizard.page.NewZnodeWizardPage;

public class ZnodeNewWizard extends Wizard implements INewWizard{

	private NewZnodeWizardPage page;
	
	private ZKElement<Node> zkElement;
	
	private TreeViewer treeViewer;
	
	public ZnodeNewWizard(TreeViewer treeViewer)
	{
		this.setDefaultPageImageDescriptor(ResourceManager.getImageDescriptor(ZnodeNewWizard.class, "/resources/images/png/wizards/new-znode.png"));
		this.setWindowTitle("New Znode Wizard...");
		this.treeViewer = treeViewer;
	}
	
	@SuppressWarnings("unchecked")
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		Object firstElement = selection.getFirstElement();
        zkElement = (ZKElement<Node>) firstElement;
	}

	@Override
	public boolean performFinish() {
		String data = page.getZkText().getText();
		String parentPath = page.getParentValueLabel().getText();
		String path = page.getPathText().getText();
		String fullPath = getFullPath(parentPath, path);
		String serverName = zkElement.getServerName();
		
		//获取创建模式
		boolean isEphemeral = page.getEphemeralButton().getSelection();
		boolean isSequential = page.getSequentialButton().getSelection();
		//调用zk新增节点接口
		ZKConnectionManager.getInstance().addZKData(fullPath, data, serverName,isEphemeral,isSequential);
		
		return true;
	}

	private String getFullPath(String parentPath, String path) {
		if("/".equals(parentPath))
		{
			return parentPath + path;
		}
		String fullPath = parentPath + "/" + path;
		return fullPath;
	}

	public void addPages() {
		page = new NewZnodeWizardPage(this,zkElement);
		addPage(page);
	}
}
