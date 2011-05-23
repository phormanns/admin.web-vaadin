package de.hsadmin.web.vaadin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Select;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import de.hsadmin.web.GenericModule;
import de.hsadmin.web.HsarwebException;
import de.hsadmin.web.Module;
import de.hsadmin.web.config.PropertyConfig;
import de.hsadmin.web.config.PropertyFieldFactory;

public class EMailTargetPropertyFieldFactory implements PropertyFieldFactory {

	private Module module;
	private boolean readOnly = false;
	private boolean writeOnce = false;
	private List<String> users;
	private List<String> mailAliases;
	private Map<Integer, SingleEMailTarget> targets;
	private int lastIndex;
	private VerticalLayout layout;

	public EMailTargetPropertyFieldFactory(Module module) {
		this.module = module;
	}
	
	public void removeTarget(SingleEMailTarget target) {
		if (target.getIndex() < lastIndex) {
			targets.remove(target.getIndex());
			repaint();
		}
	}

	public void appendTarget(SingleEMailTarget lastTarget) {
		if (lastTarget.getIndex() == lastIndex) {
			lastIndex++;
			targets.put(lastIndex, new SingleEMailTarget(this, lastIndex, ""));
			repaint();
		}
	}

	private void repaint() {
		layout.removeAllComponents();
		for (Integer idx : targets.keySet()) {
			layout.addComponent(targets.get(idx).getComponent());
		}
	}

	@Override
	public Object createFieldComponent(PropertyConfig prop, Object value) {
		GenericModule genModule = (GenericModule) module;
		users = genModule.getUsers();
		mailAliases = genModule.getEMailAliases();
		layout = new VerticalLayout();
		layout.setCaption(prop.getLabel());
		layout.setData(prop.getId());
		
		targets = new HashMap<Integer, SingleEMailTarget>();
		lastIndex = 0;
		if (value instanceof String) {
			StringTokenizer tokenizer = new StringTokenizer((String) value, ",");
			while (tokenizer.hasMoreTokens()) {
				String target = tokenizer.nextToken().trim();
				targets.put(lastIndex, new SingleEMailTarget(this, lastIndex, target));
				lastIndex++;
			}
		}
		targets.put(lastIndex, new SingleEMailTarget(this, lastIndex, ""));
		repaint();
		return layout;
	}

	@Override
	public String getValue(PropertyConfig prop, Object component) throws HsarwebException {
		StringBuffer target = new StringBuffer();
		boolean insertKomma = false;
		for (Integer key : targets.keySet()) {
			SingleEMailTarget mailTarget = targets.get(key);
			String value = mailTarget.getValue();
			if (value != null && value.length() > 0) {
				if (insertKomma) {
					target.append(",");
				} 
				target.append(value);
				insertKomma = true;
			}
		}
		return target.toString();
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	@Override
	public boolean isReadOnly() {
		return readOnly;
	}

	@Override
	public void setWriteOnce(boolean writeOnce) {
		this.writeOnce = writeOnce;
	}

	@Override
	public boolean isWriteOnce() {
		return writeOnce;
	}

	class SingleEMailTarget {
		
		private HorizontalLayout targetLine;
		private Select leftPart;
		private HorizontalLayout rightPart;
		private int index;
		private EMailTargetPropertyFieldFactory owner;
		
		protected SingleEMailTarget(EMailTargetPropertyFieldFactory owner, int key, String target) {
			this.owner = owner;
			this.index = key;
			targetLine = new HorizontalLayout();
			targetLine.setWidth(480.0f, Sizeable.UNITS_PIXELS);
			rightPart = new HorizontalLayout();
			rightPart.setWidth(380.0f, Sizeable.UNITS_PIXELS);
			leftPart = new Select();
			leftPart.setWidth(100.0f, Sizeable.UNITS_PIXELS);
			leftPart.setImmediate(true);
			leftPart.setNewItemsAllowed(false);
			leftPart.setNullSelectionAllowed(false);
			leftPart.addItem(" ");
			if (users != null && users.size() > 0) {
				leftPart.addItem("User");
			}
			if (mailAliases != null && mailAliases.size() > 0) {
				leftPart.addItem("Alias");
			}
			leftPart.addItem("EMail");
			String type = "";
			if (target != null && target.length() > 0) {
				for (String alias : mailAliases) {
					if (target.equals(alias)) {
						type = "Alias";
						break;
					}
				}
				if (type == null || type.length() == 0) {
					for (String user : users) {
						if (target.equals(user)) {
							type = "User";
							break;
						}
					}
				}
				if (type == null || type.length() == 0) {
					type = "EMail";
				}
			}
			leftPart.setValue(type);
			leftPart.addListener(new Property.ValueChangeListener() {
				private static final long serialVersionUID = -4319017121072808451L;
				@Override
				public void valueChange(ValueChangeEvent event) {
					Object targetType = event.getProperty().getValue();
					if (" ".equals(targetType)) {
						removeMe();
					} else {
						appendTarget();
					}
					changeRightPart(targetType);
				}
			});
			changeRightPart(type);
			if (rightPart.getComponentCount() > 0) {
				((AbstractField) rightPart.getComponent(0)).setValue(target);
			}
			targetLine.addComponent(leftPart);
			targetLine.addComponent(rightPart);
		}

		public void removeMe() {
			owner.removeTarget(this);
		}
		
		public void appendTarget() {
			owner.appendTarget(this);
		}
		
		public Component getComponent() {
			return targetLine;
		}

		public int getIndex() {
			return index;
		}

		public void changeRightPart(Object targetType) {
			if ("User".equals(targetType)) {
				rightPart.removeAllComponents();
				Select selUsers = new Select(null, users);
				selUsers.setNewItemsAllowed(false);
				selUsers.setNullSelectionAllowed(false);
				selUsers.setImmediate(true);
				selUsers.setWidth(380.0f, Sizeable.UNITS_PIXELS);
				rightPart.addComponent(selUsers);
			}
			if ("Alias".equals(targetType)) {
				rightPart.removeAllComponents();
				Select selAliases = new Select(null, mailAliases);
				selAliases.setNewItemsAllowed(false);
				selAliases.setNullSelectionAllowed(false);
				selAliases.setImmediate(true);
				selAliases.setWidth(380.0f, Sizeable.UNITS_PIXELS);
				rightPart.addComponent(selAliases);
			}
			if ("EMail".equals(targetType)) {
				rightPart.removeAllComponents();
				TextField freeText = new TextField();
				freeText.setImmediate(true);
				freeText.setWidth(380.0f, Sizeable.UNITS_PIXELS);
				rightPart.addComponent(freeText);
			}
		}
		
		public String getValue() {
			String value = null;
			if (rightPart.getComponentCount() > 0) {
				Object object = ((AbstractField) rightPart.getComponent(0)).getValue();
				if (object != null && object instanceof String) {
					value = ((String) object).trim();
				}
			}
			return value;
		}
	}

}
