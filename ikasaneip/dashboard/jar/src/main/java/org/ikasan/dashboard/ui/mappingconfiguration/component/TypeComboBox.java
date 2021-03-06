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
package org.ikasan.dashboard.ui.mappingconfiguration.component;

import java.util.List;

import org.ikasan.dashboard.ui.framework.group.Refreshable;
import org.ikasan.mapping.model.ConfigurationType;
import org.ikasan.mapping.service.MappingManagementService;

import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.ui.ComboBox;

/**
 * @author Ikasan Development Team
 *
 */
public class TypeComboBox extends ComboBox implements Refreshable, FocusListener
{
    private static final long serialVersionUID = -2305511116954830348L;

    private MappingManagementService mappingConfigurationService;
    private String clientName = null;

    /**
     * Constructor
     * 
     * @param mappingConfigurationService
     */
    public TypeComboBox(MappingManagementService mappingConfigurationService)
    {
        this.mappingConfigurationService = mappingConfigurationService;
        this.init();
    }

    /**
     * Helper method to initialise the component.
     */
    @SuppressWarnings("serial")
    protected void init()
    {
        this.setWidth(140, Unit.PIXELS);
    }

    /**
     * Helper method to load client values.
     */
    public void loadClientTypeValues()
    {
        List<ConfigurationType> types = this.mappingConfigurationService.getAllConfigurationTypes();

        for(ConfigurationType type: types)
        {
            this.addItem(type);
            this.setItemCaption(type, type.getName());
        }
    }

    /**
     * Helper method to load client values.
     */
    public void loadClientTypeValues(String clientName)
    {
        List<ConfigurationType> types = this.mappingConfigurationService
                .getConfigurationTypesByClientName(clientName);

        for(ConfigurationType type: types)
        {
            this.addItem(type);
            this.setItemCaption(type, type.getName());
        }
    }

    /**
     * Helper method to refresh the component.
     */
    public void refresh()
    {
        ConfigurationType type = (ConfigurationType)this.getValue();
        boolean isReadOnly = this.isReadOnly();
        this.setReadOnly(false);
        this.removeAllItems();
        this.loadClientTypeValues();
        this.setValue(type);
        this.setReadOnly(isReadOnly);
    }

    /**
     * Helper method to refresh the component.
     */
    public void refresh(String clientName)
    {
        this.clientName = clientName;

        ConfigurationType type = (ConfigurationType)this.getValue();
        boolean isReadOnly = this.isReadOnly();
        this.setReadOnly(false);
        this.removeAllItems();
        this.loadClientTypeValues(clientName);
        this.setValue(type);
        this.setReadOnly(isReadOnly);
    }

    /* (non-Javadoc)
     * @see com.vaadin.event.FieldEvents.FocusListener#focus(com.vaadin.event.FieldEvents.FocusEvent)
     */
    @Override
    public void focus(FocusEvent event)
    {
        this.refresh(this.clientName);
    }
}
