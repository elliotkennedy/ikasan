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
package org.ikasan.builder.component.endpoint;

import org.ikasan.builder.component.Builder;
import org.ikasan.endpoint.ftp.producer.FtpProducerConfiguration;
import org.ikasan.spec.component.endpoint.Producer;
import org.ikasan.spec.management.ManagedResourceRecoveryManager;

/**
 * Contract for a default ftpProducerBuilder.
 *
 * @author Ikasan Development Team.
 */
public interface FtpProducerBuilder extends Builder<Producer>
{
    FtpProducerBuilder setCriticalOnStartup(boolean criticalOnStartup);

    FtpProducerBuilder setConfiguredResourceId(String configuredResourceId);

    FtpProducerBuilder setConfiguration(FtpProducerConfiguration fftpProducerConfiguration);

    FtpProducerBuilder setManagedResourceRecoveryManager(ManagedResourceRecoveryManager managedResourceRecoveryManager);

    FtpProducerBuilder setOutputDirectory(String outputDirectory);

    FtpProducerBuilder setRenameExtension(String renameExtension);

    FtpProducerBuilder setTempFileName(String tempFileName);

    FtpProducerBuilder setChecksumDelivered(Boolean checksumDelivered);

    FtpProducerBuilder setClientID(String clientID);

    FtpProducerBuilder setCleanupJournalOnComplete(Boolean cleanupJournalOnComplete);

    FtpProducerBuilder setCreateParentDirectory(Boolean createParentDirectory);

    FtpProducerBuilder setOverwrite(Boolean overwrite);

    FtpProducerBuilder setActive(Boolean active);

    FtpProducerBuilder setUnzip(Boolean unzip);

    FtpProducerBuilder setRemoteHost(String remoteHost);

    FtpProducerBuilder setMaxRetryAttempts(Integer maxRetryAttempts);

    FtpProducerBuilder setRemotePort(Integer remotePort);

    FtpProducerBuilder setUsername(String username);

    FtpProducerBuilder setPassword(String password);

    FtpProducerBuilder setConnectionTimeout(Integer connectionTimeout);

    FtpProducerBuilder setSystemKey(String systemKey);

    FtpProducerBuilder setSocketTimeout(Integer socketTimeout);

    FtpProducerBuilder setDataTimeout(Integer dataTimeout);

    FtpProducerBuilder setFTPS(Boolean FTPS);

    FtpProducerBuilder setFtpsPort(Integer ftpsPort);

    FtpProducerBuilder setFtpsProtocol(String ftpsProtocol);

    FtpProducerBuilder setFtpsIsImplicit(Boolean ftpsIsImplicit);

    FtpProducerBuilder setFtpsKeyStoreFilePath(String ftpsKeyStoreFilePath);

    FtpProducerBuilder setFtpsKeyStoreFilePassword(String ftpsKeyStoreFilePassword);
}

