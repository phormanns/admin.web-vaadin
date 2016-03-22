package de.hsadmin.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vaadin.data.Validator;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.VerticalLayout;

import de.hsadmin.rpc.HSAdminSession;
import de.hsadmin.rpc.PropertyInfo;

public class DomainOptionsEditor extends CustomComponent implements IHSEditor {

	private static final long serialVersionUID = 1L;

	private static final String[] OPTIONS = new String[] { "greylisting", "multiviews", "indexes", "htdocsfallback", "includes", "backupmxforexternalmx" };
	
	private final PropertyInfo propertyInfo;
	private final VerticalLayout layout;
	private final Map<String, CheckBox> checkboxes;

	public DomainOptionsEditor(final PropertyInfo propertyInfo, final HSAdminSession session, final Map<String, String> whereContext) {
		this.checkboxes = new HashMap<>();
		this.propertyInfo = propertyInfo;
		this.setCaption(I18N.getText(propertyInfo.getName()));
		layout = new VerticalLayout();
		for (String opt : OPTIONS) {
			final CheckBox checkBox = new CheckBox(I18N.getText("domainoption." + opt));
			checkboxes.put(opt, checkBox);
			layout.addComponent(checkBox);
		}
		layout.setCaption(I18N.getText(propertyInfo.getName()));
		setCompositionRoot(layout);
	}

	@Override
	public void setValues(final Map<String, Object> valuesMap) {
		final Object optionsObject = valuesMap.get(propertyInfo.getName());
		if (optionsObject == null) {
			for (String opt : OPTIONS) {
				checkboxes.get(opt).setValue(!"backupmxforexternalmx".equals(opt));
			}
		}
		if (optionsObject instanceof Object[]) {
			final List<Object> options = Arrays.asList((Object[]) optionsObject);
			for (String opt : OPTIONS) {
				checkboxes.get(opt).setValue(options.contains(opt));
			}
		}
	}

	@Override
	public Object getValue() {
		final List<String> values = new ArrayList<>();
		for (final String opt : OPTIONS) {
			if (checkboxes.get(opt).getValue()) {
				values.add(opt);
			}
		}
		return values.toArray();
	}

	@Override
	public void addValidator(Validator validator) {
		
	}

	@Override
	public boolean isValid() {
		return true;
	}

}
