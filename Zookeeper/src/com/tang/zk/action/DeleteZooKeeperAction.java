/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tang.zk.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.wb.swt.ResourceManager;

import com.tang.zk.CacheManager;
import com.tang.zk.ZKConnectionManager;
import com.tang.zk.data.ZNode;
import com.tang.zk.model.RootElement;
import com.tang.zk.model.ZKElement;

/**
 * 删除节点或者删除整颗zk树
 * @author Administrator
 *
 */
public class DeleteZooKeeperAction extends Action {

	private static final String ACTION_TEXT = "Delete";
	
	private static final String ACTION_TOOL_TIP_TEXT = ACTION_TEXT;
	
	private ISelectionProvider selectionProvider;
	
	private TreeViewer treeViewer;
	
    public DeleteZooKeeperAction(TreeViewer treeViewer) {
    	setImageDescriptor(ResourceManager.getImageDescriptor(DeleteZooKeeperAction.class, "/resources/images/png/actions/delete.png"));
        setText(ACTION_TEXT);
        setToolTipText(ACTION_TOOL_TIP_TEXT);
        this.treeViewer = treeViewer;
    }

	@Override
	public void runWithEvent(Event event){
		ISelection selection = selectionProvider.getSelection();
		String typeName = "ZNode";
		String name = null;
		if(null != selection)
		{
			Object node = ((IStructuredSelection)selection).getFirstElement();
			if(node instanceof ZKElement<?>)
			{
				ZKElement<?> zkNode = (ZKElement<?>)node;
				ZNode data = (ZNode)zkNode.getNode();
				name = data.getPath();
				MessageBox messageBox = null;
				if(zkNode.isLeaf())
				{
					String msg = "是否要删除 " + typeName + " '" + name + "'?";
					//创建确认对话框
					messageBox = createMessageBox(msg);
				}
				else
				{
					String msg = "是否要删除 " + typeName + " '" + name + "' 及其子节点?";
					//创建确认对话框
					 messageBox = createMessageBox(msg);
				}
				int response = messageBox.open();
				if (response == SWT.YES) {
					delete(zkNode);
				}
				
			}
			else if (node instanceof RootElement<?>)
			{
				RootElement<?> rootNode = (RootElement<?>)node;
				String serverName = rootNode.getServerName();
				String msg = "是否要删除 Zookeeper '" + serverName + "'?";
				MessageBox messageBox = createMessageBox(msg);
				int response = messageBox.open();
				if (response == SWT.YES) {
					delete(serverName);
				}
			}
		}
	}

	private MessageBox createMessageBox(String msg) {
		MessageBox messageBox = new MessageBox(Display.getCurrent().getActiveShell(), SWT.ICON_QUESTION | SWT.YES | SWT.NO);
		messageBox.setMessage(msg);
		messageBox.setText("确认删除");
		return messageBox;
	}

	private void delete(ZKElement<?> zkNode) {
		if(zkNode.isLeaf())
		{
			String path = ((ZNode)zkNode.getNode()).getPath();
			String serverName = zkNode.getServerName();
			ZKConnectionManager.getInstance().deleteZKData(path, serverName);
		}
		else
		{
			String path = ((ZNode)zkNode.getNode()).getPath();
			String serverName = zkNode.getServerName();
			ZKConnectionManager.getInstance().deleteZKDataAndChild(path, serverName);
		}
	}

	private void delete(String serverName) {
		CacheManager.deleteServerNode(serverName);
		//关键代码，此处一定要异步执行，否则UI不会刷新
		Display.getDefault().asyncExec(new Runnable(){
			public void run() {
				treeViewer.refresh(CacheManager.getVirtualRoot(),true);
			}
		});
	}

	
	public ISelectionProvider getSelectionProvider() {
		return selectionProvider;
	}

	public void setSelectionProvider(ISelectionProvider selectionProvider) {
		this.selectionProvider = selectionProvider;
	}

	public TreeViewer getTreeViewer() {
		return treeViewer;
	}

	public void setTreeViewer(TreeViewer treeViewer) {
		this.treeViewer = treeViewer;
	}
    
    
}
