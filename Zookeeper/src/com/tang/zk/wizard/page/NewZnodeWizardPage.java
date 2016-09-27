package com.tang.zk.wizard.page;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.ResourceManager;

import com.tang.zk.data.Node;
import com.tang.zk.data.ZNode;
import com.tang.zk.model.ZKElement;

/**
 * 新建ZNode向导grid布局
 * 
 * @author Administrator
 *
 */
public class NewZnodeWizardPage extends WizardPage{
	
    public static final Image DEC_ERROR = FieldDecorationRegistry.getDefault().getFieldDecoration(
            FieldDecorationRegistry.DEC_ERROR).getImage();

    public static final Image DEC_REQUIRED = FieldDecorationRegistry.getDefault().getFieldDecoration(
            FieldDecorationRegistry.DEC_REQUIRED).getImage();
    
    public static final String DESC = "A wizard create zk node.";
    
    protected static final String CONTROL_NAME_CONNECTION_LABEL = "ZooKeeper Connection";
    protected static final String CONTROL_NAME_PARENT_IMAGE_LABEL = "Parent Znode Image";
    protected static final String CONTROL_NAME_PARENT_PATH_LABEL = "Parent Znode";
    
	private ZKElement<Node> zkElement;
	private Text pathText;
	private Text zkText;
	private Label parentValueLabel;
	private Button persistentButton;
	private Button ephemeralButton;
	private Button sequentialButton;
	
	private final Map<String, ControlDecoration> controlDecorations = new HashMap<String, ControlDecoration>();
	
	public NewZnodeWizardPage(Wizard wizard,ZKElement<Node> zkElement) {
		super("Connect Zookeeper Parameters");
		setImageDescriptor(ResourceManager.getImageDescriptor(NewZnodeWizardPage.class, "/resources/images/png/wizard-banners/new-znode.png"));
		this.setDescription(DESC);
		this.setTitle("New Znode");
		this.zkElement = zkElement;
	}

	public void createControl(Composite parent) {
		//创建Composite用于承载其他控件
		Composite container = new Composite(parent, SWT.NULL);
		setControl(container);
		GridLayout gl_container = getGridLayout();
		container.setLayout(gl_container);
		
		Label lblConnection = new Label(container, SWT.LEAD);
		lblConnection.setText("Connection:");
		lblConnection.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		
        Label connectionImageLabel = new Label(container, SWT.LEAD);
        connectionImageLabel.setImage(ResourceManager.getImage(ResourceManager.getImageDescriptor(NewZnodeWizardPage.class, "/resources/images/png/objects/zookeeper-connection.png")));
        connectionImageLabel.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));

        Label connectionValueLabel = new Label(container, SWT.LEAD);
        connectionValueLabel.setText(zkElement.getServerName());
        connectionValueLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

        Label parentLabel = new Label(container, SWT.LEAD);
        parentLabel.setText("Parent:");
        parentLabel.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));

        Label parentImageLabel = new Label(container, SWT.LEAD);
        parentImageLabel.setImage(getAdjustImage());
        parentImageLabel.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));

        parentValueLabel = new Label(container, SWT.LEAD);
        parentValueLabel.setText(((ZNode)zkElement.getNode()).getPath());
        parentValueLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        
        
        Label pathLabel = new Label(container, SWT.LEAD);
        pathLabel.setText("Path:");
        pathLabel.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
        pathLabel.pack();
        
        pathText = new Text(container, SWT.BORDER);
        pathText.setData("ControlName","Path");
        pathText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
        
        ControlDecoration controlDecoration = new ControlDecoration(pathText, SWT.LEFT | SWT.TOP, container);
        controlDecoration.setShowHover(true);
        controlDecorations.put((String)pathText.getData("ControlName"), controlDecoration);
        
        final Text finalText = pathText;
        
        //增加监听器
        pathText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                modified(finalText);
            }
        });
        
        //设置初始的标记
        this.modified(pathText);
        
        //第二列
        Group createModeGroup = new Group(container, SWT.NULL);
        createModeGroup.setText("Create Mode");
        createModeGroup.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false, 3, 1));
        createModeGroup.setLayout(getGridLayout2());

        persistentButton = new Button(createModeGroup, SWT.RADIO);
        persistentButton.setText("Pe&rsistent");
        persistentButton.setSelection(true);

        ephemeralButton = new Button(createModeGroup, SWT.RADIO);
        ephemeralButton.setText("&Ephemeral");
        ephemeralButton.setSelection(false);

        sequentialButton = new Button(createModeGroup, SWT.CHECK);
        sequentialButton.setText("&Sequential");
        sequentialButton.setSelection(false);
        
        //Data
        Group dataGroup = new Group(container, SWT.NULL);
        dataGroup.setText("Data");
        dataGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
        dataGroup.setLayout(getGridLayout3());
        new Label(dataGroup, SWT.NONE);
        
        zkText = new Text(dataGroup, SWT.BORDER);
        zkText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
	}

	protected void modified(Text finalText) {
		
		 // Hide the decorations.
        for (ControlDecoration controlDecoration : controlDecorations.values()) {
            controlDecoration.hide();
        }
        ControlDecoration controlDecoration = controlDecorations.get((String)pathText.getData("ControlName"));
		String value = finalText.getText();
		System.out.println("modify value final text." + value);
		if(null == value || value.trim().isEmpty())
		{
			controlDecoration.setImage(DEC_REQUIRED);
			controlDecoration.setDescriptionText("Path must be not empty.");
			controlDecoration.show();
			updateErrorStatus(true, "Path value can not be empty.");
		}
		else
		{
			String childPath = parentValueLabel.getText() + "/" + value; 
			ZNode node = (ZNode)zkElement.getNode();
			if (node.getChildren().contains(value))
			{
				updateErrorStatus(true,"Znode " + childPath + " has exists!");
			}
			else
			{
				updateErrorStatus(false,null);
			}
		}
	}
	
	public void updateErrorStatus(boolean isError,String message)
	{
		if(isError)
		{
			this.setErrorMessage(message);
			this.setPageComplete(false);
		}
		else//ok status
		{
			this.setErrorMessage(message);
			this.setPageComplete(true);
		}
	}

	private GridLayout getGridLayout() {
		GridLayout gl_container = new GridLayout(3, false);
		gl_container.marginWidth = 8;
		gl_container.marginHeight = 8;
		gl_container.verticalSpacing = 8;
		gl_container.horizontalSpacing = 8;
		return gl_container;
	}
	
	private GridLayout getGridLayout2() {
		GridLayout container = new GridLayout(3, false);
		container.marginWidth = 8;
		container.marginHeight = 8;
		container.verticalSpacing = 8;
		container.horizontalSpacing = 8;
		return container;
	}
	
	private GridLayout getGridLayout3() {
		GridLayout container = new GridLayout(1, false);
		container.marginWidth = 8;
		container.marginHeight = 8;
		container.verticalSpacing = 8;
		container.horizontalSpacing = 8;
		return container;
	}

	private Image getAdjustImage() {
		ZNode node = (ZNode)zkElement.getNode();
		if(node.isLeaf())
		{
			//临时节点
			if(node.isEphemeral())
			{
				return ResourceManager.getImage(ResourceManager.getImageDescriptor(NewZnodeWizardPage.class, "/resources/images/png/objects/znode-ephemeral.png"));
			}
			else
			{
				return ResourceManager.getImage(ResourceManager.getImageDescriptor(NewZnodeWizardPage.class, "/resources/images/png/objects/znode-leaf.png"));
			}
		}
		else
		{
			return ResourceManager.getImage(ResourceManager.getImageDescriptor(NewZnodeWizardPage.class, "/resources/images/png/objects/znode.png"));
		}
	}

	public Text getZkText() {
		return zkText;
	}

	public void setZkText(Text zkText) {
		this.zkText = zkText;
	}

	public Label getParentValueLabel() {
		return parentValueLabel;
	}

	public void setParentValueLabel(Label parentValueLabel) {
		this.parentValueLabel = parentValueLabel;
	}

	public Text getPathText() {
		return pathText;
	}

	public void setPathText(Text pathText) {
		this.pathText = pathText;
	}

	public Button getPersistentButton() {
		return persistentButton;
	}

	public void setPersistentButton(Button persistentButton) {
		this.persistentButton = persistentButton;
	}

	public Button getEphemeralButton() {
		return ephemeralButton;
	}

	public void setEphemeralButton(Button ephemeralButton) {
		this.ephemeralButton = ephemeralButton;
	}

	public Button getSequentialButton() {
		return sequentialButton;
	}

	public void setSequentialButton(Button sequentialButton) {
		this.sequentialButton = sequentialButton;
	}

}
