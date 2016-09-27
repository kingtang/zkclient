package com.tang.zk;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

public class CopyOfInitWindow extends Shell {

	private ProgressBar progressBar;
	
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			Display display = Display.getDefault();
			CopyOfInitWindow shell = new CopyOfInitWindow(display);
			shell.open();
			shell.layout();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the shell.
	 * @param display
	 */
	public CopyOfInitWindow(Display display) {
		super(display, SWT.NO_TRIM);
		setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		setBackgroundMode(SWT.INHERIT_FORCE);
		//setImage(null);
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.horizontalSpacing = 0;
		gridLayout.marginWidth = 0;
		gridLayout.verticalSpacing = 0;
		gridLayout.marginHeight = 0;
		setLayout(gridLayout);
		
		Label lblNewLabel = new Label(this, SWT.NONE);
		//lblNewLabel.setBackground(Display.getCurrent().getSystemColor(SWT.TRANSPARENCY_ALPHA));
		GridData gd_lblNewLabel = new GridData(SWT.LEFT, SWT.CENTER, false, true, 1, 1);
		gd_lblNewLabel.widthHint = 448;
		gd_lblNewLabel.heightHint = 331;
		lblNewLabel.setLayoutData(gd_lblNewLabel);
		Image image = SWTResourceManager.getImage(CopyOfInitWindow.class, "/icons/znode_large_init2.png");
		lblNewLabel.setImage(image);
		//aaaaaaaaaaaaaaaaaaaaaaaaa
		Region region = new Region();
		  ImageData imageData = image.getImageData();
		  if (imageData.alphaData != null) {
		   for (int y = 0; y < imageData.height; y++) {
		    for (int x = 0; x < imageData.width; x++) {
		     if (imageData.getAlpha(x, y) == 255) {
		      region.add(imageData.x + x,imageData.y + y,1,1);
		     }
		    }
		   }
		  } else {
		   ImageData mask = imageData.getTransparencyMask();
		   for (int y = 0; y < mask.height; y++) {
		    for (int x = 0; x < mask.width; x++) {
		     if (mask.getPixel(x, y) != 0) {
		      region.add(imageData.x + x,imageData.y + y,1,1);
		     }
		    }
		   }
		  }
		  this.setSize(imageData.x + imageData.width, imageData.y
				    + imageData.height);
		this.setRegion(region);
		lblNewLabel.setRegion(region);
		lblNewLabel.setSize(imageData.x + imageData.width, imageData.y
			    + imageData.height);
		//aaaaaaaaaaaaaaaaaaaaaaaaaa
		progressBar = new ProgressBar(this, SWT.NONE);
		GridData gd_progressBar = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_progressBar.heightHint = 17;
		gd_progressBar.widthHint = 459;
		progressBar.setLayoutData(gd_progressBar);
		progressBar.setMinimum(0);
		progressBar.setMaximum(100);
		createContents();
		//progressBar.setSelection(50);
		
	}

	/**
	 * Create contents of the shell.
	 */
	protected void createContents() {
		//setSize(450, 315);
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	public ProgressBar getProgressBar() {
		return progressBar;
	}

	public void setProgressBar(ProgressBar progressBar) {
		this.progressBar = progressBar;
	}
}