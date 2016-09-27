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
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWizard;
import org.eclipse.wb.swt.ResourceManager;

import com.tang.zk.wizard.ZnodeNewWizard;
import com.tang.zk.wizard.ZooKeeperConnectionNewWizard;

/**
 * Starts the {@link ZnodeNewWizard}.
 * 
 * @author Mark Masse
 */
public class NewZnodeAction extends Action {

    private static final ImageDescriptor ACTION_IMAGE_DESCRIPTOR = ResourceManager.getImageDescriptor(ZooKeeperConnectionNewWizard.class, "/resources/images/png/actions/new-znode.png");
    private static final String ACTION_TEXT = "New Znode...";
    private static final String ACTION_TOOL_TIP_TEXT = ACTION_TEXT;

    private ISelectionProvider selectionProvider;
    
    private TreeViewer treeViewer;
    
    /**
     * Constructor.
     * 
     * @param inputType The action's {@link InputType}
     */
    public NewZnodeAction(TreeViewer treeViewer) {
        setText(ACTION_TEXT);
        setToolTipText(ACTION_TOOL_TIP_TEXT);
        setImageDescriptor(ACTION_IMAGE_DESCRIPTOR);
        this.treeViewer = treeViewer;
    }

    @Override
	public void runWithEvent(Event event){
    	
    	ISelection selection = selectionProvider.getSelection();
    	/*String parentPath = null;
		if(null != selection)
		{
			Object node = ((IStructuredSelection)selection).getFirstElement();
			if(node instanceof ZKElement<?>)
			{
				ZKElement<?> zkNode = (ZKElement<?>)node;
				ZNode data = (ZNode)zkNode.getNode();
				parentPath = data.getPath();
			}
		}*/
    	
    	//开启一个wizard
    	IWorkbenchWizard wizard = new ZnodeNewWizard(treeViewer);
    	wizard.init(null, (IStructuredSelection)selection);
        WizardDialog wizardDialog = new WizardDialog(getActiveShell(), wizard);

        WizardDialog.setDefaultImage(ResourceManager.getImage(NewZnodeAction.class, "/resources/images/png/wizards/new-znode.png"));
        wizardDialog.setBlockOnOpen(true);
        wizardDialog.open();
		
	}
    
    private Shell getActiveShell() {
		return Display.getCurrent().getActiveShell();
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
