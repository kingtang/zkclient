package com.tang.zk.listener;

import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Text;

public class ZKValueModifyListener implements ModifyListener{
	
	private String originalValue;
	
	private String path;
	
	private Button updateButton;
	
	public void modifyText(ModifyEvent e) {
		Text source = (Text)e.getSource();
		System.out.println(originalValue);
		if(!source.getText().equals(originalValue))
		{
			System.out.println("modify and enabled" + source.getText());
			updateButton.setEnabled(true);
		}
		else
		{
			System.out.println("modify and not enabled" + source.getText());
			updateButton.setEnabled(false);
		}
	}

	public String getOriginalValue() {
		return originalValue;
	}

	public void setOriginalValue(String originalValue) {
		this.originalValue = originalValue;
	}

	public Button getUpdateButton() {
		return updateButton;
	}

	public void setUpdateButton(Button updateButton) {
		this.updateButton = updateButton;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}
