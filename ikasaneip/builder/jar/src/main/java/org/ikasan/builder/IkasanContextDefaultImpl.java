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
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.jta.JtaTransactionManager;

/**
 * Implementation of the Ikasan Context.
 *
 * @author Ikasan Development Team
 */
public class IkasanContextDefaultImpl implements IkasanContext
{
    private Scheduler scheduler;
    private ScheduledJobFactory scheduledJobFactory;
    private JtaTransactionManager transactionManager;
    private AopProxyProvider aopProxyProvider;
    private DuplicateFilterService duplicateFilterService;

    // TODO - not really core services or replaceable ?
    private BaseFileTransferDao baseFileTransferDao;
    private FileChunkDao fileChunkDao;
    private TransactionalResourceCommandDAO transactionalResourceCommandDAO;

    public IkasanContextDefaultImpl(ApplicationContext context)
    {
        this.scheduler = context.getBean(Scheduler.class);
        this.scheduledJobFactory = context.getBean(ScheduledJobFactory.class);
        this.transactionManager = context.getBean(JtaTransactionManager.class);
        this.aopProxyProvider = context.getBean(AopProxyProvider.class);
        this.duplicateFilterService = context.getBean(DuplicateFilterService.class);
        this.baseFileTransferDao = context.getBean(BaseFileTransferDao.class);
        this.fileChunkDao = context.getBean(FileChunkDao.class);
        this.transactionalResourceCommandDAO = context.getBean(TransactionalResourceCommandDAO.class);
    }

    private IkasanContextDefaultImpl(){};

    @Override
    public void setScheduler(Scheduler scheduler)
    {
        this.scheduler = scheduler;
    }

    @Override
    public Scheduler getScheduler() {
        return this.scheduler;
    }

    @Override
    public void setScheduledJobFactory(ScheduledJobFactory scheduledJobFactory)
    {
        this.scheduledJobFactory = scheduledJobFactory;
    }

    @Override
    public DuplicateFilterService getDuplicateFilterService() {
        return duplicateFilterService;
    }

    @Override
    public void setDuplicateFilterService(DuplicateFilterService duplicateFilterService) {
        this.duplicateFilterService = duplicateFilterService;
    }

    @Override
    public BaseFileTransferDao getBaseFileTransferDao() {
        return baseFileTransferDao;
    }

    @Override
    public void setBaseFileTransferDao(BaseFileTransferDao baseFileTransferDao) {
        this.baseFileTransferDao = baseFileTransferDao;
    }

    @Override
    public FileChunkDao getFileChunkDao() {
        return fileChunkDao;
    }

    @Override
    public void setFileChunkDao(FileChunkDao fileChunkDao) {
        this.fileChunkDao = fileChunkDao;
    }

    @Override
    public TransactionalResourceCommandDAO getTransactionalResourceCommandDAO() {
        return transactionalResourceCommandDAO;
    }

    @Override
    public void setTransactionalResourceCommandDAO(TransactionalResourceCommandDAO transactionalResourceCommandDAO) {
        this.transactionalResourceCommandDAO = transactionalResourceCommandDAO;
    }

    @Override
    public IkasanContext clone()
    {
        IkasanContext ikasanContext = new IkasanContextDefaultImpl();
        ikasanContext.setJtaTransactionManager(this.transactionManager);
        ikasanContext.setAopProxyProvider(this.aopProxyProvider);
        ikasanContext.setScheduledJobFactory(this.scheduledJobFactory);
        ikasanContext.setScheduler(this.scheduler);
        ikasanContext.setDuplicateFilterService(this.duplicateFilterService);
        ikasanContext.setBaseFileTransferDao(this.baseFileTransferDao);
        ikasanContext.setFileChunkDao(this.fileChunkDao);
        ikasanContext.setTransactionalResourceCommandDAO(this.transactionalResourceCommandDAO);

        return ikasanContext;
    }

    @Override
    public ScheduledJobFactory getScheduledJobFactory() {
        return this.scheduledJobFactory;
    }

    @Override
    public JtaTransactionManager getJtaTransactionManager() {
        return this.transactionManager;
    }

    @Override
    public void setJtaTransactionManager(JtaTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    @Override
    public void setAopProxyProvider(AopProxyProvider aopProxyProvider)
    {
        this.aopProxyProvider = aopProxyProvider;
    }

    @Override
    public AopProxyProvider getAopProxyProvider() {
        return this.aopProxyProvider;
    }
}
