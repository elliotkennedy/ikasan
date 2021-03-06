 /*
 * $Id$
 * $URL$
 *
 * ====================================================================
 * Ikasan Enterprise Integration Platform
 *
 * Distributed under the Modified BSD License.
 * Copyright notice: The copyright for this software and a full listing
 * of individual contributors are as shown in the packaged copyright.txt
 * file.
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  - Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  - Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 *  - Neither the name of the ORGANIZATION nor the names of its contributors may
 *    be used to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
 * USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * ====================================================================
 */
package org.ikasan.dashboard.ui.administration.panel;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.vaadin.server.VaadinService;
import com.vaadin.ui.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ikasan.configurationService.model.ConfigurationParameterIntegerImpl;
import org.ikasan.configurationService.model.ConfigurationParameterLongImpl;
import org.ikasan.configurationService.model.ConfigurationParameterMapImpl;
import org.ikasan.configurationService.model.PlatformConfiguration;
import org.ikasan.configurationService.model.PlatformConfigurationConfiguredResource;
import org.ikasan.dashboard.ui.framework.constants.ConfigurationConstants;
import org.ikasan.dashboard.ui.framework.constants.SecurityConstants;
import org.ikasan.dashboard.ui.framework.util.DashboardSessionValueConstants;
import org.ikasan.dashboard.ui.framework.validator.NonZeroLengthStringValidator;
import org.ikasan.security.service.authentication.IkasanAuthentication;
import org.ikasan.spec.configuration.*;
import org.vaadin.teemu.VaadinIcons;

import com.vaadin.data.Validator;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.converter.StringToIntegerConverter;
import com.vaadin.data.util.converter.StringToLongConverter;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.themes.ValoTheme;

/**
 * @author CMI2 Development Team
 * 
 */
public class GeneralConfigurationPanel extends Panel implements View
{
	private static final long serialVersionUID = 6005593259860222561L;

	private Logger logger = LoggerFactory.getLogger(GeneralConfigurationPanel.class);
	
	private PlatformConfigurationService platformConfigurationService;
	

	/**
	 * Constructor
	 *
	 * @param platformConfigurationService
     */
	public GeneralConfigurationPanel(PlatformConfigurationService platformConfigurationService)
	{
		super();
		this.platformConfigurationService = platformConfigurationService;
		if (this.platformConfigurationService == null)
		{
			throw new IllegalArgumentException("platformConfigurationService cannot be null!");
		}

		init();
	}


	protected void init()
	{
		
	}

	protected Panel createConfigurationPanel()
	{
		Panel paramPanel = new Panel();
		paramPanel.addStyleName(ValoTheme.PANEL_BORDERLESS);
		paramPanel.setWidth("100%");

		GridLayout paramLayout = new GridLayout(2, 4);
		paramLayout.setSpacing(true);
		paramLayout.setSizeFull();
		paramLayout.setMargin(true);
		paramLayout.setColumnExpandRatio(0, .25f);
		paramLayout.setColumnExpandRatio(1, .75f);

		Label label = new Label("Replay Target Servers (comma separated)");
		label.addStyleName(ValoTheme.LABEL_LARGE);
		label.addStyleName(ValoTheme.LABEL_BOLD);
		label.setSizeUndefined();
		paramLayout.addComponent(label, 0, 0);
		paramLayout.setComponentAlignment(label, Alignment.TOP_RIGHT);

		TextArea replayServers = new TextArea();
		replayServers.setWidth(500, Unit.PIXELS);
		replayServers.setRows(4);
		replayServers.setValue(this.platformConfigurationService.getConfigurationValue(ConfigurationConstants.REPLAY_TARGET_SERVERS));

		paramLayout.addComponent(replayServers, 1, 0);
		paramLayout.setComponentAlignment(replayServers, Alignment.MIDDLE_LEFT);

		label = new Label("Search result set sizes");
		label.addStyleName(ValoTheme.LABEL_LARGE);
		label.addStyleName(ValoTheme.LABEL_BOLD);
		label.setSizeUndefined();
		paramLayout.addComponent(label, 0, 1);
		paramLayout.setComponentAlignment(label, Alignment.MIDDLE_RIGHT);

		TextField searchResultSize = new TextField();
		searchResultSize.setWidth(200, Unit.PIXELS);
		searchResultSize.setValue(this.platformConfigurationService.getConfigurationValue(ConfigurationConstants.SEARCH_RESULT_SET_SIZE));

		paramLayout.addComponent(searchResultSize, 1, 1);
		paramLayout.setComponentAlignment(searchResultSize, Alignment.MIDDLE_LEFT);

		label = new Label("Notification interval minutes");
		label.addStyleName(ValoTheme.LABEL_LARGE);
		label.addStyleName(ValoTheme.LABEL_BOLD);
		label.setSizeUndefined();
		paramLayout.addComponent(label, 0, 2);
		paramLayout.setComponentAlignment(label, Alignment.MIDDLE_RIGHT);

		TextField notificationIntervalMinutes = new TextField();
		notificationIntervalMinutes.setWidth(200, Unit.PIXELS);
		notificationIntervalMinutes.setValue(this.platformConfigurationService.getConfigurationValue(ConfigurationConstants.NOTIFICATION_INTERVAL_MINUTES));

		paramLayout.addComponent(notificationIntervalMinutes, 1, 2);
		paramLayout.setComponentAlignment(notificationIntervalMinutes, Alignment.MIDDLE_LEFT);

		Button saveButton = new Button("Save");
		saveButton.addStyleName(ValoTheme.BUTTON_SMALL);

		saveButton.addClickListener(new Button.ClickListener()
		{
			@Override
			public void buttonClick(ClickEvent clickEvent)
			{
				platformConfigurationService.saveConfigurationValue
						(ConfigurationConstants.REPLAY_TARGET_SERVERS, replayServers.getValue());
				platformConfigurationService.saveConfigurationValue
						(ConfigurationConstants.NOTIFICATION_INTERVAL_MINUTES, notificationIntervalMinutes.getValue());
				platformConfigurationService.saveConfigurationValue
						(ConfigurationConstants.SEARCH_RESULT_SET_SIZE, searchResultSize.getValue());

				Notification notification = new Notification(
						"Saved",
						"The configuration has been saved successfully!",
						Type.HUMANIZED_MESSAGE);
				notification.setStyleName(ValoTheme.NOTIFICATION_CLOSABLE);
				notification.show(Page.getCurrent());
			}
		});

		paramLayout.addComponent(saveButton, 0, 3, 1, 3);
		paramLayout.setComponentAlignment(saveButton, Alignment.MIDDLE_CENTER);

		paramPanel.setContent(paramLayout);

		return paramPanel;
	}


	/*
     * (non-Javadoc)
     *
     * @see
     * com.vaadin.navigator.View#enter(com.vaadin.navigator.ViewChangeListener
     * .ViewChangeEvent)
     */
	@Override
	public void enter(ViewChangeEvent event)
	{
		refresh();
	}

	public void refresh()
	{
		GridLayout layout = new GridLayout();
		layout.setWidth("100%");
		layout.setSpacing(true);
		layout.setMargin(true);


		Label configLabel = new Label("General Configuration");
		configLabel.addStyleName(ValoTheme.LABEL_HUGE);
		configLabel.setSizeUndefined();

		layout.addComponent(configLabel);
		layout.addComponent(this.createConfigurationPanel());

		this.setContent(layout);
	}
}
