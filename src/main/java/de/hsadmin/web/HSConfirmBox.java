package de.hsadmin.web;

import java.util.HashMap;
import java.util.Map;

import org.apache.xmlrpc.XmlRpcException;

import com.vaadin.server.ErrorMessage;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HasComponents;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;

import de.hsadmin.model.IRemote;

public class HSConfirmBox extends HorizontalLayout {

	private static final long serialVersionUID = 1L;
	Button okButton, cancelButton;

	public HSConfirmBox(final IRemote iRemote, final String action, final String runAsUser, final String ticket) {
		okButton = new Button("OK");
		okButton.addClickListener(new ClickListener() 
		{
			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {
				final HasComponents confirmBox = okButton.getParent();
				if (confirmBox != null) {
					final HasComponents formLayout = confirmBox.getParent();
					if (formLayout != null) {
						
						final FormLayout form = (FormLayout) formLayout;
						final int count = form.getComponentCount();
						final Map<String, Object> setParams = new HashMap<String, Object>();
						for (int idx=0; idx < count; idx++) {
							final Component component = form.getComponent(idx);
							if (component instanceof TextField) {
								final TextField tf = (TextField) component;
								if (tf.isEnabled() && tf.isValid()) {
									final String key = tf.getCaption();
									final String value = tf.getValue();
									setParams.put(key, value);
								}
							}
						}
						boolean success = false;
						if ("new".equals(action)) {
							try {
								iRemote.add(runAsUser, ticket, setParams);
								success = true;
							} catch (final XmlRpcException e) {
								Button btn = event.getButton();
								btn.setComponentError(new ErrorMessage() {
									private static final long serialVersionUID = 1L;
									@Override
									public ErrorLevel getErrorLevel() {
										return ErrorLevel.CRITICAL;
									}
									@Override
									public String getFormattedHtmlMessage() {
										return e.getLocalizedMessage();
									}
								});
								
							}
						}
						final HasComponents window = formLayout.getParent();
						if (window != null && success) {
							((Window) window).close();
						}
					}
				}
			}
		});
		cancelButton = new Button("Cancel");
		cancelButton.addClickListener(new ClickListener() 
		{
			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {
				final HasComponents confirmBox = okButton.getParent();
				if (confirmBox != null) {
					final HasComponents formLayout = confirmBox.getParent();
					if (formLayout != null) {
						final HasComponents window = formLayout.getParent();
						if (window != null) {
							((Window) window).close();
						}
					}
				}
			}
		});

		setSpacing(true);
		addComponent(okButton);
		addComponent(cancelButton);
	}
}
