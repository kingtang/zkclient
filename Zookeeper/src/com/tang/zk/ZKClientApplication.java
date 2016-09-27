package com.tang.zk;

import java.awt.Toolkit;
import java.util.List;
import java.util.Random;

import org.eclipse.jface.action.ControlContribution;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.StatusLineManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.IOpenListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.OpenEvent;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.ResourceManager;
import org.eclipse.wb.swt.SWTResourceManager;

import com.tang.zk.action.DeleteZooKeeperAction;
import com.tang.zk.action.NewZnodeAction;
import com.tang.zk.action.NewZookeeperConnectionAction;
import com.tang.zk.action.RefreshZooKeeperAction;
import com.tang.zk.data.Node;
import com.tang.zk.data.ServerNode;
import com.tang.zk.data.ZNode;
import com.tang.zk.data.provider.ZKDataContentProvider;
import com.tang.zk.data.provider.ZKNodeLabelProvider;
import com.tang.zk.listener.ZKValueModifyListener;
import com.tang.zk.model.RootElement;
import com.tang.zk.model.TreeVirtualRootElement;
import com.tang.zk.model.ZKElement;
import com.tang.zk.store.DataStore;
/**
 * ZKmanager主窗口
 * @author Administrator
 *
 */
public class ZKClientApplication extends ApplicationWindow {

	private NewZookeeperConnectionAction newConnection;
	
	private DeleteZooKeeperAction delectAction;
	
	private RefreshZooKeeperAction refreshAction;
	
	private NewZnodeAction newZnodeAction;
	
	private TreeViewer treeViewer;
	
	private Text zkValueText;
	
	private Button update;
	
	private StatusLineComposite statusBar;
	
	private StatusLineManager statusLineManager;
	
	/**
	 * Create the application window.
	 */
	public ZKClientApplication() {
		super(null);
		createActions();
		addToolBar(SWT.FLAT | SWT.WRAP);
		addMenuBar();
		addStatusLine();
		mkAction();
	}

	/**
	 * Create contents of the application window.
	 * @param parent
	 */
	protected Control createContents(Composite parent) {
		setStatus("");
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new FillLayout(SWT.HORIZONTAL ));
		
		TreeViewer treeViewer_1 = new TreeViewer(container, SWT.MULTI | SWT.VIRTUAL | SWT.H_SCROLL
                | SWT.V_SCROLL);
		//Tree tree = treeViewer_1.getTree();
		
		treeViewer = treeViewer_1;
		{
			Composite composite = new Composite(container, SWT.NONE);
			composite.setLayout(new GridLayout(1, false));
			{
				zkValueText = new Text(composite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
				GridData gd_zkValueText = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
				gd_zkValueText.heightHint = 111;
				gd_zkValueText.widthHint = 184;
				zkValueText.setLayoutData(gd_zkValueText);
				
			}
			{
				Composite composite_1 = new Composite(composite, SWT.NONE);
				FillLayout fl_composite_1 = new FillLayout(SWT.HORIZONTAL);
				fl_composite_1.spacing = 5;
				fl_composite_1.marginHeight = 3;
				composite_1.setLayout(fl_composite_1);
				GridData gd_composite_1 = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
				gd_composite_1.heightHint = 33;
				composite_1.setLayoutData(gd_composite_1);
				
				Button updateButton = new Button(composite_1, SWT.NONE);
				updateButton.setToolTipText("update value to zk");
				updateButton.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
					}
				});
				updateButton.setText("Update");
				
				//初始状态不可更新
				updateButton.setEnabled(false);
				
				//update button
				updateButton.addMouseListener(new MouseAdapter() {

					@Override
					public void mouseDown(MouseEvent e) {
						Object path = zkValueText.getData("path");
						String value = zkValueText.getText();
						Object server = zkValueText.getData("server");
						System.out.println("update button is clicked and path " + path + " and value " + value);
						ZKConnectionManager.getInstance().updateZKData((String)path, (String)server, value);
					}
					
				});
				update = updateButton;
				Button refreshButton = new Button(composite_1, SWT.NONE);
				refreshButton.setText("Refresh");
				refreshButton.addMouseListener(new MouseAdapter() {

					@Override
					public void mouseDown(MouseEvent e) {
						System.out.println("refresh zk node value");
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
						}
					}
					
				});
			}
		}
		
		ILabelProvider labelProvider = new ZKNodeLabelProvider();
		treeViewer.setLabelProvider(labelProvider);
		
		ITreeContentProvider contentProvider = new ZKDataContentProvider();
		treeViewer.setContentProvider(contentProvider);
		//RootElement root = new RootElement();
		//RootZKElement root = new RootZKElement(); 
		//treeViewer.setInput(root);
		//鼠标左键双击时进入此方法
		treeViewer.addOpenListener(new IOpenListener(){

			public void open(OpenEvent event) {
				System.out.println(event.toString());
				System.out.println("click tree node");
			}
			
		});
		final ZKValueModifyListener modifyListener = new ZKValueModifyListener();
		CacheManager.setModifyListener(modifyListener);
		modifyListener.setUpdateButton(update);
		treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			
			public void selectionChanged(SelectionChangedEvent event) {
				System.out.println("tree node is selected.");
				
				ISelection selection = event.getSelection();
				if(null != selection)
				{
					Object node = ((IStructuredSelection)selection).getFirstElement();
					if(node instanceof ZKElement)
					{
						zkValueText.removeModifyListener(modifyListener);
						@SuppressWarnings("unchecked")
						ZKElement<Node> zkNode = (ZKElement<Node>)node;
						ZNode data = (ZNode)zkNode.getNode();
						String zkValue = (String) data.getData();
						modifyListener.setOriginalValue(null == zkValue ? "" : zkValue);
						modifyListener.setPath(data.getPath());
						//给zkvalue控件加上监听
						zkValueText.addModifyListener(modifyListener);
						zkValueText.setData("path", data.getPath());
						zkValueText.setData("server", zkNode.getServerName());
						zkValueText.setText(null == zkValue ? "" : zkValue);
						
						statusLineManager.setMessage(data.getImage(), data.getPath());
						statusBar.getServer().setText(zkNode.getServerName());
						statusBar.getStatus().setText(ZKConnectionManager.getInstance().getStateByServerName(zkNode.getServerName()).name());
					}
				}
			}
		});
		
		//pop manuger
		MenuManager menuMgr = new MenuManager("#PopupMenu");
        menuMgr.setRemoveAllWhenShown(true);
        menuMgr.addMenuListener(new IMenuListener() {

            public void menuAboutToShow(IMenuManager manager) {
                fillContextMenu(manager);
            }
        });

        Menu menu = menuMgr.createContextMenu(treeViewer.getControl());
        treeViewer.getControl().setMenu(menu);
        initAction();
        initCacheData();
        ZKConnectionManager.getInstance().registerTreeViewer(treeViewer);
        
        Shell shell = this.getShell();
        Toolkit kit = Toolkit.getDefaultToolkit();
        //设置窗口默认大小
        shell.setSize(720, 480);
        //将窗口居中
        shell.setLocation((kit.getScreenSize().width - 720) / 2, (kit.getScreenSize().height - 480) / 2);
        
		return container;
	}

	private void initAction() {
		delectAction.setSelectionProvider(treeViewer);
        delectAction.setTreeViewer(treeViewer);
        newZnodeAction.setSelectionProvider(treeViewer);
        newZnodeAction.setTreeViewer(treeViewer);
        refreshAction.setSelectionProvider(treeViewer);
        refreshAction.setTreeViewer(treeViewer);
	}

	protected void fillContextMenu(IMenuManager manager) {
		System.out.println("right click and pop memu.");
		ISelection selection = treeViewer.getSelection();
		if(null != selection)
		{
			Object node = ((IStructuredSelection)selection).getFirstElement();
			System.out.println(node);
			if(node instanceof ZKElement<?>)
			{
				@SuppressWarnings("unchecked")
				ZKElement<Node> zkNode = (ZKElement<Node>) node;
				ZNode znode = (ZNode)zkNode.getNode();
				if("/".equals(znode.getPath()))
				{
					manager.add(newZnodeAction);
				}
				else
				{
					//zk节点可以新增
					manager.add(newZnodeAction);
					manager.add(new Separator());
					manager.add(delectAction);
				}
			}
			else if (node instanceof RootElement)
			{
				manager.add(newConnection);
				manager.add(new Separator());
		        manager.add(delectAction);
		        manager.add(new Separator());
		        manager.add(refreshAction);
			}
		}
	}

	private void initCacheData() {
		//创建ZKServer对象
		List<ServerNode> cacheData = DataStore.getServerNodes();
		for (ServerNode serverNode : cacheData) {
			CacheManager.addServerNode(serverNode);
			ZKConnectionManager.getInstance().initClient(serverNode,treeViewer);
		}
		TreeVirtualRootElement root = new TreeVirtualRootElement();
		getTreeViewer().setInput(root);
		CacheManager.setVirtualRoot(root);
	}

	private void mkAction() {
		delectAction = new DeleteZooKeeperAction(treeViewer);
		newZnodeAction = new NewZnodeAction(treeViewer);
		refreshAction = new RefreshZooKeeperAction();
	}
	
	/**
	 * Create the actions.
	 */
	private void createActions() {
		// Create the actions
		newConnection = new NewZookeeperConnectionAction(this);
		newConnection.setImageDescriptor(ResourceManager.getImageDescriptor(ZKClientApplication.class, "/resources/images/png/wizards/new-zookeeper-connection.png"));
		newConnection.setToolTipText("\u65B0\u5EFAzk\u8FDE\u63A5");
	}

	/**
	 * Create the menu manager.
	 * @return the menu manager
	 */
	protected MenuManager createMenuManager() {
		MenuManager menuManager = new MenuManager("menu");
		menuManager.add(newConnection);
		menuManager.add(new Separator());
		return menuManager;
	}

	/**
	 * Create the toolbar manager.
	 * @return the toolbar manager
	 */
	protected ToolBarManager createToolBarManager(int style) {
		ToolBarManager toolBarManager = new ToolBarManager(style);
		return toolBarManager;
	}

	/**
	 * Create the status line manager.
	 * @return the status line manager
	 */
	protected StatusLineManager createStatusLineManager() {
		statusLineManager = new StatusLineManager();
		ControlContribution control = new ControlContribution("leg-example") {
			@Override
			protected Control createControl(Composite parent) {
				statusBar = new StatusLineComposite(parent, SWT.LEFT_TO_RIGHT);
				return statusBar;
				
			}
		};
		statusLineManager.appendToGroup(StatusLineManager.MIDDLE_GROUP, control);
		statusLineManager.update(true);
		return statusLineManager;
	}

	public static class MyThread implements Runnable
	{
		private ProgressBar bar;
		
		public MyThread(ProgressBar bar)
		{
			this.bar = bar;
		}
		
		public void run() {
			Random r = new Random();
			int total = 0;
			while(total < 100)
			{
				int nextInt = r.nextInt(20);
				total = total + nextInt;
				/*if(total >= 100)
				{
					List<ServerNode> cacheData = DataStore.getServerNodes();
					for (ServerNode serverNode : cacheData) {
						
						String connectString = serverNode.getIp() + ":"
								+ serverNode.getPort();
						CuratorFramework zkClient = CuratorFrameworkFactory.builder()
								.connectString(connectString).connectionTimeoutMs(1000).sessionTimeoutMs(1000)
								.retryPolicy(new RetryNTimes(Integer.MAX_VALUE, 3000))
								.build();
						// 启动zk连接
						zkClient.start();
						ZKConnectionManager.getInstance().addZkClient(serverNode.getName(), zkClient);
						
						//debugLog.info(serverNode.getName() + " zk client has started.");
					}
				}*/
				bar.setSelection(total);
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			ZKClientApplication window = new ZKClientApplication();
			Display display = Display.getDefault();
			final InitWindow shell = new InitWindow(display);
			Rectangle bounds = Display.getDefault().getPrimaryMonitor().getBounds();
			Rectangle rect = shell.getBounds();
			int x = bounds.x + (bounds.width - rect.width) / 2;
			int y = bounds.y + (bounds.height - rect.height) / 2;
			shell.setLocation(x, y);
			shell.open();
			final ProgressBar progressBar = shell.getProgressBar();
			MyThread t = new MyThread(progressBar);
			
			//display.asyncExec(t);
			t.run();
			shell.layout();
			Thread tt = new Thread(new Runnable() {
				
				public void run() {
					if(progressBar.getSelection() == 100)
					{
						shell.close();
					}					
				}
			});
			display.asyncExec(tt);
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
			try {
				
				window.setBlockOnOpen(true);
				window.open();
				Display.getCurrent().dispose();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	
	/**
	 * Configure the shell.
	 * @param newShell
	 */
	protected void configureShell(Shell newShell) {
		newShell.setImage(SWTResourceManager.getImage(ZKClientApplication.class, "/resources/images/png/plugin.png"));
		super.configureShell(newShell);
		newShell.setText("ZK Manager");
	}

	/**
	 * Return the initial size of the window.
	 */
	protected Point getInitialSize() {
		return new Point(720, 480);
	}

	public TreeViewer getTreeViewer() {
		return treeViewer;
	}

	@Override
	protected void handleShellCloseEvent() {
		super.handleShellCloseEvent();
		System.out.println("close");
		ZKConnectionManager.getInstance().close();
	}
	
	
}
