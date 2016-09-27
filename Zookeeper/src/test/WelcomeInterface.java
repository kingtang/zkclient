package test;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
/**
 * 
 * @author zhong39
 *
 */
public class WelcomeInterface {
 
	private static Shell shell;
	 public static void main(String[] args) {
	  Display display = new Display();
	  shell = new Shell(display, SWT.NO_TRIM |SWT.ON_TOP);
	  createContents(shell);
	  shell.setBounds(400, 200, 310, 250);
	  shell.open();
	  while (!shell.isDisposed()) {
	   if (!display.readAndDispatch())
	    display.sleep();
	  }
	  display.dispose();
	 }
	 protected static void createContents(Shell shell) {
	  shell.setLayout(new FillLayout());
	  Composite cm = new Composite(shell ,SWT.None);
	  cm.setLayout(new GridLayout(1,false));
	  Image ico = new Image(Display.getDefault(), "E:\\workspace_kepler\\Zookeeper\\src\\icons\\timg.jpg");
	  Label label = new Label(cm,SWT.None);
	  label.setImage(ico);
	  final ProgressBar progressBar = new ProgressBar(cm, SWT.INDETERMINATE);
	  progressBar.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	  
	 }
	}