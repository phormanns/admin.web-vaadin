package de.hsadmin.web;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jasig.cas.client.authentication.AttributePrincipal;
import org.jasig.cas.client.authentication.AuthenticationFilter;
import org.jasig.cas.client.validation.Assertion;

import com.vaadin.Application;
import com.vaadin.terminal.Terminal;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.terminal.gwt.server.HttpServletRequestListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.Notification;

import de.hsadmin.web.config.LocaleConfig;
import de.hsadmin.web.config.ModuleConfig;

public class MainApplication extends Application implements HttpServletRequestListener, TabSheet.SelectedTabChangeListener {

	private static final long serialVersionUID = 1L;
	private static final String LOGIN_URL = "https://login.hostsharing.net:443/cas/v1/tickets";
	private static boolean isTestEnvironment = false;

	static {
		Config config = Config.getInstance();
		Object loginURL = config.getProperty("loginURL", LOGIN_URL);
		isTestEnvironment = "TestUmgebung".equals(loginURL);
	}
	
	private HttpSession httpSession;
	private ServletContext servletContext;
	private AttributePrincipal userPrincipal;
	private LocaleConfig localeConfig;
	private Remote remote;
	private Map<String, Module> modules;
	private Locale requestLocale;
	private String role = "NONE";
	private String runAs = null;
	private TabSheet tabSheet;


	@Override
	public void init() {
		Locale locale = requestLocale;
		if (locale == null) {
			locale = Locale.getDefault();
		}
		localeConfig = new LocaleConfig(locale, "main");
		remote = new Remote(this);
		try {
			Object rolesArrayObj = remote.callSearch("role", null);
			if (rolesArrayObj != null && rolesArrayObj instanceof Object[]) {
				Object[] rolesArray = (Object[]) rolesArrayObj;
				if (rolesArray.length > 0 && rolesArray[0] instanceof Map<?, ?>) {
					role = (String) ((Map<?, ?>) rolesArray[0]).get("role");
				}
			}
		} catch (HsarwebException e) {
			showSystemException(e);
		}
		Window mainWindow = new Window(localeConfig.getText("applicationtitle"));
		tabSheet = new TabSheet();
		tabSheet.setSizeFull();
		String modulesParamString = localeConfig.getText("modules." + role);
		modules = new HashMap<String, Module>();
		Module firstModule = null;
		for (String className : modulesParamString.split(",")) {
			try {
				Module module = (Module) Class.forName(className).newInstance();
				module.setApplication(this);
				if (firstModule == null) {
					firstModule = module;
				}
				ModuleConfig moduleConfig = module.getModuleConfig();
				String label = moduleConfig.getLabel("moduletitle");
				modules.put(label, module);
				tabSheet.addTab((Component) module.getComponent(), label, new ThemeResource(moduleConfig.getLabel("moduleicon")));
			} catch (Exception e) {
				showSystemException(e);
			}
		}
		tabSheet.addListener(this);
		mainWindow.setContent(tabSheet);
		setMainWindow(mainWindow);
		setErrorHandler(new Terminal.ErrorListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void terminalError(Terminal.ErrorEvent event) {
				event.getThrowable().printStackTrace();
			}
		});
		try {
			firstModule.reload();
		} catch (HsarwebException e) {
			showSystemException(e);
		}
	}
	
	public String getLoginUserRole() {
		return role;
	}

	public String getProxyTicket() {
		return userPrincipal.getProxyTicketFor(servletContext.getInitParameter("backendURL"));
	}

	public String getContextParam(String string) {
		return servletContext.getInitParameter(string);
	}

	public String getLogin() {
		return userPrincipal.getName();
	}

	public Remote getRemote() {
		return remote;
	}

	public LocaleConfig getLocaleConfig() {
		return localeConfig;
	}

	public Locale getRequestLocale() {
		return requestLocale;
	}

	@Override
	public void onRequestStart(HttpServletRequest request,
			HttpServletResponse response) {
		requestLocale = request.getLocale();
		httpSession = request.getSession();
		servletContext = httpSession.getServletContext();
		if (isTestEnvironment) {
			userPrincipal = new AttributePrincipal() {
				private static final long serialVersionUID = 1L;
				@Override
				public String getName() {
					return "ad";
				}
				@Override
				public String getProxyTicketFor(String arg0) {
					return "user:ad";
				}
				@SuppressWarnings("rawtypes")
				@Override
				public Map getAttributes() {
					return new HashMap();
				}
			};
		} else {
			userPrincipal = ((Assertion) httpSession.getAttribute(AuthenticationFilter.CONST_CAS_ASSERTION)).getPrincipal();
		}
	}

	@Override
	public void onRequestEnd(HttpServletRequest request,
			HttpServletResponse response) {
		
	}

	@Override
	public void selectedTabChange(SelectedTabChangeEvent event) {
		TabSheet tabSheet = event.getTabSheet();
		Component selectedTab = tabSheet.getSelectedTab();
		Tab tab = tabSheet.getTab(selectedTab);
		Module module = modules.get(tab.getCaption());
		try {
			module.reload();
		} catch (HsarwebException e) {
			e.printStackTrace();
			showSystemException(e);
		}
	}

	public void showUserException(Exception e) {
		getMainWindow().showNotification("Anwendungs-Fehler", "<br/ >" + e.getMessage(), Notification.TYPE_ERROR_MESSAGE);			
	}

	public void showSystemException(Exception e) {
		getMainWindow().showNotification("System-Fehler", "<br />Bitte informieren Sie den Support<br/ >" + e.getMessage(), Notification.TYPE_ERROR_MESSAGE);			
	}

	public String getRunAs() {
		if (runAs == null) {
			return getLogin();
		}
		return runAs;
	}

	public void setRunAs(String runAs) {
		this.runAs = runAs;
		Component selectedTab = tabSheet.getSelectedTab();
		Tab tab = tabSheet.getTab(selectedTab);
		Module module = modules.get(tab.getCaption());
		try {
			module.reload();
		} catch (HsarwebException e) {
			e.printStackTrace();
			showSystemException(e);
		}
	}

}
