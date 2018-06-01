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
package org.ikasan.builder;

import org.ikasan.connector.base.command.TransactionalResourceCommandDAO;
import org.ikasan.connector.basefiletransfer.outbound.persistence.BaseFileTransferDao;
import org.ikasan.connector.util.chunking.model.dao.FileChunkDao;
import org.ikasan.filter.duplicate.service.DuplicateFilterService;
import org.ikasan.scheduler.ScheduledJobFactory;
import org.quartz.Scheduler;
import org.springframework.transaction.jta.JtaTransactionManager;

/**
 * Contract for a shared Ikasan context.
 *
 * @author Ikasan Development Team
 */
public interface IkasanContext
{
    /**
     * Get the scheduler instance
     * @return
     */
    public Scheduler getScheduler();

    /**
     * Override the default scheduler instance
     * @param scheduler
     */
    public void setScheduler(Scheduler scheduler);

    /**
     * Get the transaction manager instance
     * @return
     */
    public JtaTransactionManager getJtaTransactionManager();

    /**
     * Override the default transaction manager instance
     * @param transactionManager
     */
    public void setJtaTransactionManager(JtaTransactionManager transactionManager);

    /**
     * Get the AOP proxy provider used to point cut code for transactions
     * @return
     */
    public AopProxyProvider getAopProxyProvider();

    /**
     * Override the default AOP proxy provider
     * @param aopProxyProvider
     */
    public void setAopProxyProvider(AopProxyProvider aopProxyProvider);

    /**
     * Get the scheduled job factory used to provision scheduled job callbacks
     * @return
     */
    public ScheduledJobFactory getScheduledJobFactory();

    /**
     * Override the default job factory
     * @param scheduledJobFactory
     */
    public void setScheduledJobFactory(ScheduledJobFactory scheduledJobFactory);

    /**
     * Get the duplicate filter service instance
     * @return
     */
    public DuplicateFilterService getDuplicateFilterService();

    /**
     * Override the default duplicate service instance
     * @param duplicateFilterService
     */
    public void setDuplicateFilterService(DuplicateFilterService duplicateFilterService);

    /**
     * Shallow clone of the Ikasan context which creates a copy of the context based on the current objects therein.
     * THis allows the context to be passed and any setters used to override defaults without impacting the parent context.
     * @return
     */
    public IkasanContext clone();

    /**
     * Get base file transfer DAO
     * @return
     */
    public BaseFileTransferDao getBaseFileTransferDao();

    /**
     * Override the default base file transafer DAO
     * @param baseFileTransferDao
     */
    public void setBaseFileTransferDao(BaseFileTransferDao baseFileTransferDao);

    /**
     * Get the file chunk DAO
     * @return
     */
    public FileChunkDao getFileChunkDao();

    /**
     * Override the default file chunk DAO
     * @param fileChunkDao
     */
    public void setFileChunkDao(FileChunkDao fileChunkDao);

    /**
     * Get the transactional resource command DAO
     * @return
     */
    public TransactionalResourceCommandDAO getTransactionalResourceCommandDAO();

    /**
     * Override the default transaction resource command DAO
     * @param transactionalResourceCommandDAO
     */
    public void setTransactionalResourceCommandDAO(TransactionalResourceCommandDAO transactionalResourceCommandDAO);

}
