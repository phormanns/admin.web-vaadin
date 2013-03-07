package de.hsadmin.web.vaadin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Select;
import com.vaadin.ui.VerticalLayout;

import de.hsadmin.web.DomainModule;
import de.hsadmin.web.HsarwebInternalException;
import de.hsadmin.web.ListOfStringsProperty;
import de.hsadmin.web.Module;
import de.hsadmin.web.XmlrpcProperty;
import de.hsadmin.web.config.ModuleConfig;
import de.hsadmin.web.config.PropertyConfig;
import de.hsadmin.web.config.PropertyFieldFactory;

/**
 * @author Purodha Blissenbach
 * 
 */
public class DomainOptionsPropertyFieldFactory implements PropertyFieldFactory {
	private final Module module;
	private final ModuleConfig config;
	private static String[] OPTION_NAMES; // {"backupmxforexternalmx", "php", ...};
//	private static String[] OPTION_NAMES = {"backupmxforexternalmx", "php"};
	// private final Map<String,AbstractProperty> optionTypes ; // TODO: auf Vorrat hier
	private boolean readOnly = false;
	private boolean writeOnce = false;
	private VerticalLayout layout;
	private final List<SingleDomainOption> optionLayout;

	public DomainOptionsPropertyFieldFactory(Module module) throws HsarwebInternalException {
		this.module = module;
		this.config = this.module.getModuleConfig();
		// Liste der Namen der Domainoptions besorgen. TODO: mögliche Werte.
//if(true)
//		throw new HsarwebInternalException("p9");
	//	DomainModule domainModule = (DomainModule) module;
	//	Map<String, Map<String, Object>> moduleProps = domainModule.getModuleProps();
		Map<String, Map<String, Object>> moduleProps = module.getModuleProps();
		if (moduleProps == null)OPTION_NAMES[1] ="XXXXX"+module.getModuleConfig().getName();
//		Object p0 = moduleProps.get("domain");
//		Object p1 = moduleProps.get("domain").get("domainoptions");
		Object p1 = moduleProps.get("domainoptions");
		Map<String, Object> p2 = (Map<String, Object>) p1;
		Object p3 = p2.get("selectableValues");
		
		if (p3 instanceof Map<?, ?>) {
			Map<String, Object> p4 = (Map<String, Object>) p3;
			if (p4.get("kind").equals("DOMAINOPTIONS")) {
				Object p5 = p4.get("values");
				if (p5 instanceof Object[]) {
					Object[] p6 = (Object[]) p5;
					OPTION_NAMES = new String[p6.length];
					int i = 0;
					for (Object p7 : p6) {
						if (p7 instanceof Map<?, ?>) {
							Map<String, ?> p8 = (Map<String, ?>) p7;
							OPTION_NAMES[i++] = p8.keySet().iterator().next();
						} else {
							throw new HsarwebInternalException("p7");
						}
					}
				} else {
					throw new HsarwebInternalException("p5");
				}
			} else {
				throw new HsarwebInternalException("p4");
			}
		} else {
			throw new HsarwebInternalException("p3");
		}
	/* */
		optionLayout = new ArrayList<SingleDomainOption>();
	}

	private void repaint() {
		layout.removeAllComponents();
		for (int idx = 0; idx < OPTION_NAMES.length; ++idx) {
			layout.addComponent(optionLayout.get(idx).getComponent());
		}
	}

	@Override
	public Object createFieldComponent(PropertyConfig prop, XmlrpcProperty value)
			throws HsarwebInternalException {
		layout = new VerticalLayout();
		layout.setCaption(prop.getLabel());
		layout.setData(prop.getId());
		optionLayout.clear();
		if (value instanceof ListOfStringsProperty) {
			ListOfStringsProperty list = (ListOfStringsProperty) value;
			for (int idx = 0; idx < OPTION_NAMES.length; ++idx) {
				optionLayout.add(new SingleDomainOption(this,
						OPTION_NAMES[idx], list.contains(OPTION_NAMES[idx])));
			}
		} else {
			// Eine leere Liste von Domainoptionen wird angezeigt werden.
			throw new HsarwebInternalException(
					"Keine Liste: ListOfStringsProperty");
		}
		repaint();
		return layout;
	}

	@Override
	public XmlrpcProperty getValue(PropertyConfig prop, Object component) {
		ListOfStringsProperty setOptions = new ListOfStringsProperty();
		for (int idx = 0; idx < OPTION_NAMES.length; ++idx) {
			if (Boolean.TRUE.equals(optionLayout.get(idx).getValue())) {
				setOptions.add(OPTION_NAMES[idx]);
			}
		}
		return setOptions;
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

	class SingleDomainOption {
		private final DomainOptionsPropertyFieldFactory owner;
		private final String optionName;
		private HorizontalLayout targetLine;
		private Select select;

		protected SingleDomainOption(DomainOptionsPropertyFieldFactory owner,
				String optionName, boolean optionValue) {
			this.owner = owner;
			this.optionName = optionName;
			targetLine = new HorizontalLayout();
			targetLine.setWidth(500.0f, Sizeable.UNITS_PIXELS);
			HorizontalLayout leftPart = new HorizontalLayout();
			leftPart.setWidth(220.0f, Sizeable.UNITS_PIXELS);
			Label label = new Label(owner.config.getLabel("domainoption."
					+ optionName), Label.CONTENT_RAW);
			leftPart.addComponent(label); // setCaption(owner.config.getLabel("domainoption."+optionName));
			HorizontalLayout rightPart = new HorizontalLayout();
			// ToDO: Fallunterscheidungen nach Optionsart. Z.Z. nur Boolean:
			{
				this.select = new Select();
				this.select.setWidth(90.0f, Sizeable.UNITS_PIXELS);
				this.select.setImmediate(true);
				this.select.setNewItemsAllowed(false);
				this.select.setNullSelectionAllowed(false);
				this.select.addItem("y");
				this.select.setItemCaption("y", owner.config.getLabel("yes"));
				this.select.addItem("n");
				this.select.setItemCaption("n", owner.config.getLabel("no"));
				this.select.setValue(optionValue ? "y" : "n");
				rightPart.addComponent(this.select);
			}
			targetLine.addComponent(leftPart);
			targetLine.addComponent(rightPart);
		}

		public Component getComponent() {
			return targetLine;
		}

		public Boolean getValue() {
			Boolean value = null;
			String val = (String) this.select.getValue();
			if (val.equals("y") || val.equals("n")) {
				value = (val.equals("y"));
			}
			return value;
		}
	}
}