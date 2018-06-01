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
package org.ikasan.builder.component;

import org.ikasan.builder.IkasanContext;
import org.ikasan.builder.component.endpoint.*;
import org.ikasan.builder.component.filter.MessageFilterBuilder;
import org.ikasan.builder.component.filter.MessageFilterBuilderImpl;
import org.ikasan.builder.component.splitting.ListSplitterBuilderImpl;
import org.ikasan.component.endpoint.filesystem.messageprovider.FileMessageProvider;
import org.ikasan.component.endpoint.jms.spring.consumer.JmsContainerConsumer;
import org.ikasan.component.endpoint.quartz.consumer.ScheduledConsumer;
import org.ikasan.component.endpoint.util.consumer.EventGeneratingConsumer;
import org.ikasan.component.endpoint.util.consumer.MessageGenerator;
import org.ikasan.component.endpoint.util.consumer.SimpleMessageGenerator;
import org.ikasan.component.endpoint.util.producer.LogProducer;
import org.ikasan.component.splitter.DefaultListSplitter;
import org.ikasan.spec.component.endpoint.Producer;
import org.ikasan.spec.component.splitting.Splitter;
import org.springframework.jms.core.IkasanJmsTemplate;
import org.springframework.transaction.jta.JtaTransactionManager;

import java.util.concurrent.Executors;

/**
 * A simple Component builder.
 * 
 * @author Ikasan Development Team
 */
public class ComponentBuilder
{
    /** context for core services */
    IkasanContext ikasanContext;

    /**
     * Constructor
     * @param ikasanContext
     */
    public ComponentBuilder(IkasanContext ikasanContext)
    {
        this.ikasanContext = ikasanContext;
        if(ikasanContext == null)
        {
            throw new IllegalArgumentException("ikasanContext cannot be 'null'");
        }
    }

    /**
     * Get an instance of an Ikasan default scheduledConsumer
     * @return scheduledConsumerBuilder
     */
    public ScheduledConsumerBuilder scheduledConsumer()
    {
        ScheduledConsumer scheduledConsumer = new org.ikasan.component.endpoint.quartz.consumer.ScheduledConsumer( this.ikasanContext.getScheduler() );
        ScheduledConsumerBuilder scheduledConsumerBuilder = new ScheduledConsumerBuilderImpl(scheduledConsumer,
                this.ikasanContext.getScheduledJobFactory(), this.ikasanContext.getAopProxyProvider());
        return scheduledConsumerBuilder;
    }

    /**
     * Get an instance of an Ikasan default scheduledConsumer using scheduledJobName and defaulted job group based on the scheduled job name
     * @param scheduledJobName
     * @return scheduledConsumerBuilder
     */
    public ScheduledConsumerBuilder scheduledConsumer(String scheduledJobName)
    {
        ScheduledConsumer scheduledConsumer = new org.ikasan.component.endpoint.quartz.consumer.ScheduledConsumer( this.ikasanContext.getScheduler() );
        ScheduledConsumerBuilder scheduledConsumerBuilder = new ScheduledConsumerBuilderImpl(scheduledConsumer,
                this.ikasanContext.getScheduledJobFactory(), this.ikasanContext.getAopProxyProvider());
        scheduledConsumerBuilder.setScheduledJobName(scheduledJobName);
        scheduledConsumerBuilder.setScheduledJobGroupName(scheduledJobName + "_group");
        return scheduledConsumerBuilder;
    }

    /**
     * Get an instance of an Ikasan default scheduledConsumer with SFTP message Provider
     * @return sftpConsumerBuilder
     */
    public SftpConsumerBuilder sftpConsumer()
    {
        ScheduledConsumer scheduledConsumer = new org.ikasan.component.endpoint.quartz.consumer.ScheduledConsumer( this.ikasanContext.getScheduler() );
        SftpConsumerBuilder sftpConsumerBuilder = new SftpConsumerBuilderImpl(scheduledConsumer,
                this.ikasanContext.getScheduledJobFactory(), this.ikasanContext.getAopProxyProvider()
                ,this.ikasanContext.getJtaTransactionManager()
                ,this.ikasanContext.getBaseFileTransferDao()
                ,this.ikasanContext.getFileChunkDao()
                ,this.ikasanContext.getTransactionalResourceCommandDAO()

        );
        return sftpConsumerBuilder;
    }

    /**
     * Get an instance of an Ikasan default SFTP Producer
     * @return sftpProducerBuilder
     */
    public SftpProducerBuilder sftpProducer()
    {

        SftpProducerBuilder sftpProducerBuilder = new SftpProducerBuilderImpl(
                this.ikasanContext.getJtaTransactionManager()
                ,this.ikasanContext.getBaseFileTransferDao()
                ,this.ikasanContext.getFileChunkDao()
                ,this.ikasanContext.getTransactionalResourceCommandDAO()
        );
        return sftpProducerBuilder;
    }

    /**
     * Get an instance of an Ikasan default scheduledConsumer with FTP message Provider
     * @return FtpConsumerBuilder
     */
    public FtpConsumerBuilder ftpConsumer()
    {
        ScheduledConsumer scheduledConsumer = new org.ikasan.component.endpoint.quartz.consumer.ScheduledConsumer( this.ikasanContext.getScheduler() );
        FtpConsumerBuilder ftpConsumerBuilder = new FtpConsumerBuilderImpl(scheduledConsumer,
                this.ikasanContext.getScheduledJobFactory(), this.ikasanContext.getAopProxyProvider()
                ,this.ikasanContext.getJtaTransactionManager()
                ,this.ikasanContext.getBaseFileTransferDao()
                ,this.ikasanContext.getFileChunkDao()
                ,this.ikasanContext.getTransactionalResourceCommandDAO()
        );
        return ftpConsumerBuilder;
    }

    /**
     * Get an instance of an Ikasan default FTP Producer
     * @return ftpProducerBuilder
     */
    public FtpProducerBuilder ftpProducer()
    {

        FtpProducerBuilder ftpProducerBuilder = new FtpProducerBuilderImpl(
                this.ikasanContext.getJtaTransactionManager()
                ,this.ikasanContext.getBaseFileTransferDao()
                ,this.ikasanContext.getFileChunkDao()
                ,this.ikasanContext.getTransactionalResourceCommandDAO()
        );
        return ftpProducerBuilder;
    }

    /**
     * Get an instance of an Ikasan default fileConsumer
     * @return scheduledConsumerBuilder
     */
    public FileConsumerBuilder fileConsumer()
    {
        ScheduledConsumer scheduledConsumer = new org.ikasan.component.endpoint.quartz.consumer.ScheduledConsumer( this.ikasanContext.getScheduler() );
        FileConsumerBuilder fileConsumerBuilder = new FileConsumerBuilderImpl(scheduledConsumer,
                this.ikasanContext.getScheduledJobFactory(), this.ikasanContext.getAopProxyProvider(), new FileMessageProvider() );
        return fileConsumerBuilder;
    }

    /**
     * Get an instance of an Ikasan default fileProducer
     * @return FileProducerBuilder
     */
    public FileProducerBuilder fileProducer()
    {
        return new FileProducerBuilderImpl();
    }

    /**
     * Get an instance of an Ikasan default jmsConsumer
     * @return jmsConsumerBuilder
     */
    public JmsConsumerBuilder jmsConsumer() {
        JmsContainerConsumer jmsConsumer = new JmsContainerConsumer();
        JmsConsumerBuilder jmsConsumerBuilder = new JmsConsumerBuilderImpl(jmsConsumer,
                this.ikasanContext.getJtaTransactionManager(), this.ikasanContext.getAopProxyProvider());
        return jmsConsumerBuilder;
    }

    /**
     * Get an instance of an Ikasan default jmsProducer
     * @return jmsProducerBuilder
     */
    public JmsProducerBuilder jmsProducer() {
        JmsProducerBuilder jmsProducerBuilder = new JmsProducerBuilderImpl(new IkasanJmsTemplate(), this.ikasanContext.getJtaTransactionManager().getTransactionManager());
        return jmsProducerBuilder;
    }

    /**
     * Get an instance of an Ikasan logProducerBuilder
     * @return LogProducerBuilder
     */
    public LogProducerBuilder logProducer()
    {
        return new LogProducerBuilderImpl( new LogProducer() );
    }

    /**
     * Get an instance of an Ikasan devNullProducerBuilder
     * @return Builder<Producer>
     */
    public Builder<Producer<?>> devNullProducer()
    {
        return new DevNullProducerBuilderImpl();
    }

    /**
     * Get an instance of an Ikasan listSplitterBuilder
     * @return Builder<Splitter>
     */
    public Builder<Splitter> listSplitter()
    {
        return new ListSplitterBuilderImpl( new DefaultListSplitter() );
    }

    /**
     * Get an instance of an Ikasan messageFilterBuilder
     * @return MessageFilterBuilder
     */
    public MessageFilterBuilder messageFilter()
    {
        return new MessageFilterBuilderImpl(this.ikasanContext.getDuplicateFilterService());
    }

    /**
     * Get an instance of an Ikasan EventGeneratingConsumerBuilder
     * @return EventGeneratingConsumerBuilder
     */
    public EventGeneratingConsumerBuilder eventGeneratingConsumer()
    {
        MessageGenerator messageGenerator = new SimpleMessageGenerator();
        EventGeneratingConsumer eventGeneratingConsumer = new EventGeneratingConsumer(Executors.newSingleThreadExecutor(), messageGenerator);
        return new EventGeneratingConsumerBuilderImpl( messageGenerator, eventGeneratingConsumer, this.ikasanContext.getAopProxyProvider() );
    }

}