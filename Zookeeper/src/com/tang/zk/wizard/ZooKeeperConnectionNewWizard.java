package com.tang.zk.wizard;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.wb.swt.ResourceManager;

import com.tang.zk.CacheManager;
import com.tang.zk.ZKClientApplication;
import com.tang.zk.ZKConnectionManager;
import com.tang.zk.data.ServerNode;
import com.tang.zk.wizard.page.NewZookeeperConnectionWizardPage;

/**
 * 新建zk连接向导，该向导只有一个向导页。 向导负责用户完成数据填写后的动作。
 * 
 * @author Administrator
 * 
 */
public class ZooKeeperConnectionNewWizard extends Wizard {

	private NewZookeeperConnectionWizardPage page1;

	private ZKClientApplication zkManager;

	public ZooKeeperConnectionNewWizard() {
		this.setDefaultPageImageDescriptor(ResourceManager.getImageDescriptor(
				ZooKeeperConnectionNewWizard.class,
				"/resources/images/png/wizards/new-zookeeper-connection.png"));
		this.setWindowTitle("New Zookeeper Connection Wizard...");
	}

	// 向导配置完后执行该方法
	public boolean performFinish() {
		System.out.println("wizard finish!");
		System.out.println("ip:" + page1.getIp().getText());
		String serverName = page1.getServerName().getText();

		boolean isExists = checkExists(serverName);
		if (isExists) {
			System.out.println(serverName + " has exists.");
			return false;
		}

		// 创建ZKServer对象
		ServerNode serverNode = CacheManager.createServerNode(page1.getIp()
				.getText(), page1.getPort().getText(), serverName, page1
				.getUserName().getText(), page1.getPassword().getText());

		ZKConnectionManager.getInstance().initClient(serverNode,
				zkManager.getTreeViewer());
		// TreeVirtualRootElement root = new TreeVirtualRootElement();
		zkManager.getTreeViewer().setInput(CacheManager.getVirtualRoot());
		// 创建TreeViewer
		// zkManager.getTreeViewer().refresh();
		return true;
	}

	private boolean checkExists(String serverName) {
		return !(null == CacheManager.getServerNode(serverName));
	}

	public void addPages() {
		page1 = new NewZookeeperConnectionWizardPage(this);
		addPage(page1);
	}

	public NewZookeeperConnectionWizardPage getPage1() {
		return page1;
	}

	public ZKClientApplication getZkManager() {
		return zkManager;
	}

	public void setZkManager(ZKClientApplication zkManager) {
		this.zkManager = zkManager;
	}

}
