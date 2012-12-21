package de.hsadmin.web.vaadin;

import java.util.HashMap;
import java.util.Map;

import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

import de.hsadmin.web.AbstractProperty;
import de.hsadmin.web.HsarwebException;
import de.hsadmin.web.ListOfStringsProperty;
import de.hsadmin.web.XmlrpcProperty;
import de.hsadmin.web.config.PropertyConfig;
import de.hsadmin.web.config.PropertyFieldFactory;

/**
 * @author pblissenbach
 *
 */
public class DomainOptionsPropertyFieldFactory implements PropertyFieldFactory {
	private Map<String,AbstractProperty> optionTypes ; // TODO: auf Vorrat hier
	private ListOfStringsProperty setOptions ;
	private boolean readOnly = false;
	private boolean writeOnce = false;
	private VerticalLayout layout;

	public DomainOptionsPropertyFieldFactory() {
		// TODO: besorge Options und ihre Typen aus der DB
		optionTypes = new HashMap<String,AbstractProperty>() ;
		optionTypes.put("backupmxforexternalmx", null);
		optionTypes.put("greylisting", null);
		optionTypes.put("htdocsfallback", null);
		optionTypes.put("includes", null);
		optionTypes.put("indexes", null);
		optionTypes.put("multiviews", null);
		optionTypes.put("nonexistiondomainoptionfortesting", null); // TESTCASE
		optionTypes.put("php", null);
		// TODO: besorge Options  .... Ende
//		setOptions = null ;
		setOptions = new ListOfStringsProperty() ;
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object createFieldComponent(PropertyConfig prop, XmlrpcProperty value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public XmlrpcProperty getValue(PropertyConfig prop, Object component)
			throws HsarwebException {
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
		private HorizontalLayout targetLine;
		private HorizontalLayout leftPart;
		private HorizontalLayout rightPart;
		private int index;
		private String optionName;
		private String testunusedoptionName;
		private DomainOptionsPropertyFieldFactory owner;
		
		protected SingleDomainOption(DomainOptionsPropertyFieldFactory owner, int key, String optionName, Object optionValue) {
			this.owner = owner;
			this.index = key;
			this.optionName = optionName;
			targetLine = new HorizontalLayout();
			targetLine.setWidth(480.0f, Sizeable.UNITS_PIXELS);
			leftPart = new HorizontalLayout();
			leftPart.setWidth(100.0f, Sizeable.UNITS_PIXELS);
			leftPart.setCaption(optionName);
			rightPart = new HorizontalLayout();
			rightPart.setWidth(380.0f, Sizeable.UNITS_PIXELS);
			// ToDO: Fallunterscheidungen nach Optionsart. Z.Z. nur Boolean.
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