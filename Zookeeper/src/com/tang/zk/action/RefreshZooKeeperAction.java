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
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.wb.swt.ResourceManager;

/**
 * TODO: Comment.
 * 
 * @author Mark Masse
 */
public class RefreshZooKeeperAction extends Action {

	private static final String ACTION_TEXT = "Refresh";
	
	private static final String ACTION_TOOL_TIP_TEXT = ACTION_TEXT;
	
	private ISelectionProvider selectionProvider;
	
	private TreeViewer treeViewer;
	
    public RefreshZooKeeperAction() {
    	setImageDescriptor(ResourceManager.getImageDescriptor(RefreshZooKeeperAction.class, "/resources/images/png/actions/refresh.png"));
        setText(ACTION_TEXT);
        setToolTipText(ACTION_TOOL_TIP_TEXT);
    }

	@Override
	public void runWithEvent(Event event){
		//关键代码，此处一定要异步执行，否则UI不会刷新
		Display.getDefault().asyncExec(new Runnable(){
			public void run() {
				treeViewer.refresh();
			}
		});
		/*ISelection selection = selectionProvider.getSelection();
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
				
				
			}
		}*/
		//刷新对应的节点
		/*System.out.println("refresh zk node value");
		Object path = zkValueText.getData("path");
		String text = zkValueText.getText();
		Object server = zkValueText.getData("server");
		Node node = CacheManager.getElement((String)server, (String)path).getNode();
		if(node instanceof ZNode)
		{
			String newDataValue = ((ZNode) node).getData().toString();
			if(!text.equals(newDataValue))
			{
				zkValueText.setText(newDataValue);
			}
		}*/
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
