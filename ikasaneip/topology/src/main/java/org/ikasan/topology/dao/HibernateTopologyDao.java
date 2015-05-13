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
package org.ikasan.topology.dao;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.ikasan.topology.model.BusinessStream;
import org.ikasan.topology.model.Flow;
import org.ikasan.topology.model.Module;
import org.ikasan.topology.model.Server;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;

/**
 * Hibernate implementation of <code>TopologyDao</code>
 * 
 * @author Ikasan Development Team
 *
 */
public class HibernateTopologyDao extends HibernateDaoSupport implements TopologyDao
{

	/* (non-Javadoc)
	 * @see org.ikasan.topology.dao.TopologyDao#getAllServers()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Server> getAllServers()
	{
		DetachedCriteria criteria = DetachedCriteria.forClass(Server.class);

        return (List<Server>)this.getHibernateTemplate().findByCriteria(criteria);
	}

	/* (non-Javadoc)
	 * @see org.ikasan.topology.dao.TopologyDao#save(org.ikasan.topology.model.Server)
	 */
	@Override
	public void save(Server server)
	{
		this.getHibernateTemplate().saveOrUpdate(server);		
	}

	/* (non-Javadoc)
	 * @see org.ikasan.topology.dao.TopologyDao#getAllModules()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Module> getAllModules()
	{
		DetachedCriteria criteria = DetachedCriteria.forClass(Module.class);

        return (List<Module>)this.getHibernateTemplate().findByCriteria(criteria);
	}

	/* (non-Javadoc)
	 * @see org.ikasan.topology.dao.TopologyDao#save(org.ikasan.topology.model.Module)
	 */
	@Override
	public void save(Module module)
	{
		this.getHibernateTemplate().saveOrUpdate(module);
	}

	/* (non-Javadoc)
	 * @see org.ikasan.topology.dao.TopologyDao#getAllFlows()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Flow> getAllFlows()
	{
		DetachedCriteria criteria = DetachedCriteria.forClass(Flow.class);

        return (List<Flow>)this.getHibernateTemplate().findByCriteria(criteria);
	}

	/* (non-Javadoc)
	 * @see org.ikasan.topology.dao.TopologyDao#save(org.ikasan.topology.model.Flow)
	 */
	@Override
	public void save(Flow flow)
	{
		this.getHibernateTemplate().saveOrUpdate(flow);
	}

	/* (non-Javadoc)
	 * @see org.ikasan.topology.dao.TopologyDao#getAllBusinessStreams()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<BusinessStream> getAllBusinessStreams()
	{
		DetachedCriteria criteria = DetachedCriteria.forClass(BusinessStream.class);

        return (List<BusinessStream>)this.getHibernateTemplate().findByCriteria(criteria);
	}

	/* (non-Javadoc)
	 * @see org.ikasan.topology.dao.TopologyDao#saveBusinessStream()
	 */
	@Override
	public void saveBusinessStream(BusinessStream businessStream)
	{
		this.getHibernateTemplate().saveOrUpdate(businessStream);		
	}

	/* (non-Javadoc)
	 * @see org.ikasan.topology.dao.TopologyDao#getBusinessStreamsByUserId()
	 */
	@Override
	public List<BusinessStream> getBusinessStreamsByUserId()
	{
		// TODO Auto-generated method stub
		return null;
	}

    
   
}
