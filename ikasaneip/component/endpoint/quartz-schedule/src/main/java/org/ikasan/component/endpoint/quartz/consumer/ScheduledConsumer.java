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
package org.ikasan.component.endpoint.quartz.consumer;

import org.apache.log4j.Logger;
import org.ikasan.component.endpoint.quartz.HashedEventIdentifierServiceImpl;
import org.ikasan.scheduler.ScheduledJobFactory;
import org.ikasan.spec.component.endpoint.Consumer;
import org.ikasan.spec.configuration.Configured;
import org.ikasan.spec.configuration.ConfiguredResource;
import org.ikasan.spec.event.EventFactory;
import org.ikasan.spec.event.EventListener;
import org.ikasan.spec.event.ManagedEventIdentifierService;
import org.ikasan.spec.flow.FlowEvent;
import org.ikasan.spec.management.ManagedResource;
import org.ikasan.spec.management.ManagedResourceRecoveryManager;
import org.quartz.*;

import java.text.ParseException;
import java.util.Date;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * This test class supports the <code>Consumer</code> class.
 *
 * @author Ikasan Development Team
 */
@DisallowConcurrentExecution
public class ScheduledConsumer<T>
        implements ManagedResource, Consumer<EventListener, EventFactory>, ConfiguredResource<ScheduledConsumerConfiguration>, Job
{
    /**
     * logger
     */
    private static Logger logger = Logger.getLogger(ScheduledConsumer.class);

    /**
     * Scheduler
     */
    private Scheduler scheduler;

    /**
     * scheduled job factory
     */
    private ScheduledJobFactory scheduledJobFactory;

    /**
     * consumer event factory
     */
    private EventFactory<FlowEvent<?, ?>> flowEventFactory;

    /**
     * default event identifier service - can be overridden via the setter
     */
    protected ManagedEventIdentifierService<?, T> managedEventIdentifierService = new HashedEventIdentifierServiceImpl();

    /**
     * consumer event listener
     */
    private EventListener eventListener;

    /**
     * configuredResourceId
     */
    private String configuredResourceId;

    /**
     * consumer configuration
     */
    private ScheduledConsumerConfiguration consumerConfiguration;

    /** is this a critical resource to cause startup failure */
    private boolean criticalOnStartup;

    /**
     * job identifying name
     */
    private String name;

    /**
     * job identifying group
     */
    private String group;

    /**
     * default messageProvider is set to QuartzMessageProvider - can be overridden via the setter
     */
    private MessageProvider<?> messageProvider = new QuartzMessageProvider();

    /**
     * Constructor
     *
     * @param scheduler
     * @param scheduledJobFactory
     * @param name
     * @param group
     */
    public ScheduledConsumer(Scheduler scheduler, ScheduledJobFactory scheduledJobFactory, String name, String group)
    {
        this.scheduler = scheduler;
        if (scheduler == null)
        {
            throw new IllegalArgumentException("scheduler cannot be 'null'");
        }
        this.scheduledJobFactory = scheduledJobFactory;
        if (scheduledJobFactory == null)
        {
            throw new IllegalArgumentException("scheduledJobFactory cannot be 'null'");
        }
        this.name = name;
        if (name == null)
        {
            throw new IllegalArgumentException("name cannot be 'null'");
        }
        this.group = group;
        if (group == null)
        {
            throw new IllegalArgumentException("group cannot be 'null'");
        }
    }

    /**
     * Start the underlying tech
     */
    public void start()
    {
        try
        {
            JobDetail jobDetail = scheduledJobFactory.createJobDetail(this, this.name, this.group);
            // create trigger
            // TODO - allow configuration to support multiple triggers
            JobKey jobkey = jobDetail.getKey();
            Trigger trigger = getCronTrigger(jobkey, this.consumerConfiguration.getCronExpression());
            Date scheduledDate = scheduler.scheduleJob(jobDetail, trigger);
            logger.info("Scheduled consumer for flow ["
                    + jobkey.getName()
                    + "] module [" + jobkey.getGroup()
                    + "] starting at [" + scheduledDate + "]");
        }
        catch (SchedulerException e)
        {
            throw new RuntimeException(e);
        }
        catch (ParseException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Stop the scheduled job and triggers
     */
    public void stop()
    {
        try
        {
            JobKey jobKey = new JobKey(name, group);
            if (this.scheduler.checkExists(jobKey))
            {
                this.scheduler.deleteJob(jobKey);
            }
        }
        catch (SchedulerException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Is the underlying tech actively running
     *
     * @return isRunning
     */
    public boolean isRunning()
    {
        try
        {
            if (this.scheduler.isShutdown() || this.scheduler.isInStandbyMode())
            {
                return false;
            }
            JobKey jobKey = new JobKey(this.name, this.group);
            if (this.scheduler.checkExists(jobKey))
            {
                return true;
            }
            return false;
        }
        catch (SchedulerException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Callback from the scheduler.
     *
     * @param context
     */
    public void execute(JobExecutionContext context)
    {
        T message = (T) messageProvider.invoke(context);
        if (message != null)
        {
            FlowEvent<?, ?> flowEvent = createFlowEvent(message);
            this.eventListener.invoke(flowEvent);
        }
    }

    /**
     * Override this is you want control over the flow event created by this
     * consumer
     *
     * @param message
     * @return
     */
    protected FlowEvent<?, ?> createFlowEvent(T message)
    {
        FlowEvent<?, ?> flowEvent = this.flowEventFactory
                .newEvent(this.managedEventIdentifierService.getEventIdentifier(message), message);
        return flowEvent;
    }

    public void setEventFactory(EventFactory flowEventFactory)
    {
        this.flowEventFactory = flowEventFactory;
    }

    public ManagedEventIdentifierService<?, T> getManagedEventIdentifierService()
    {
        return managedEventIdentifierService;
    }

    public void setManagedEventIdentifierService(ManagedEventIdentifierService<?, T> managedEventIdentifierService)
    {
        this.managedEventIdentifierService = managedEventIdentifierService;
    }

    public EventListener getEventListener()
    {
        return eventListener;
    }

    public void setEventListener(EventListener eventListener)
    {
        this.eventListener = eventListener;
    }

    /**
     * Set the consumer event listener
     *
     * @param eventListener
     */
    public void setListener(EventListener eventListener)
    {
        this.eventListener = eventListener;
    }

    public ScheduledConsumerConfiguration getConfiguration()
    {
        return consumerConfiguration;
    }

    public String getConfiguredResourceId()
    {
        return this.configuredResourceId;
    }

    public void setConfiguration(ScheduledConsumerConfiguration consumerConfiguration)
    {
        this.consumerConfiguration = consumerConfiguration;

        // pass configuration to messageProvider if this is configured
        if(messageProvider instanceof Configured)
        {
            ((Configured)messageProvider).setConfiguration(consumerConfiguration);
        }
    }

    public void setConfiguredResourceId(String configuredResourceId)
    {
        this.configuredResourceId = configuredResourceId;
    }

    public void setMessageProvider(MessageProvider<T> messageProvider)
    {
        this.messageProvider = messageProvider;
    }

    /**
     * Method factory for creating a cron trigger
     *
     * @return jobDetail
     * @throws java.text.ParseException
     */
    protected Trigger getCronTrigger(JobKey jobkey, String cronExpression) throws ParseException
    {
        return newTrigger().withIdentity(jobkey.getName(), jobkey.getGroup()).withSchedule(cronSchedule(cronExpression))
                .build();
    }

    /* (non-Javadoc)
     * @see org.ikasan.spec.component.endpoint.Consumer#getEventFactory()
     */
    public EventFactory getEventFactory()
    {
        return this.flowEventFactory;
    }

    @Override
    public void startManagedResource()
    {
        if(messageProvider instanceof ManagedResource)
        {
            ((ManagedResource)messageProvider).startManagedResource();
        }
    }

    @Override
    public void stopManagedResource()
    {
        if(messageProvider instanceof ManagedResource)
        {
            ((ManagedResource)messageProvider).stopManagedResource();
        }
    }

    @Override
    public void setManagedResourceRecoveryManager(ManagedResourceRecoveryManager managedResourceRecoveryManager)
    {
        if(messageProvider instanceof ManagedResource)
        {
            ((ManagedResource)messageProvider).setManagedResourceRecoveryManager(managedResourceRecoveryManager);
        }
    }

    @Override
    public boolean isCriticalOnStartup()
    {
        return this.criticalOnStartup;
    }

    @Override
    public void setCriticalOnStartup(boolean criticalOnStartup)
    {
        this.criticalOnStartup = criticalOnStartup;
    }
}
