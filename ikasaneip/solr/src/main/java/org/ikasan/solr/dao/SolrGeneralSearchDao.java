package org.ikasan.solr.dao;

import org.ikasan.solr.model.IkasanSolrDocument;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by Ikasan Development Team on 04/08/2017.
 */
public interface SolrGeneralSearchDao<RESULTS>
{
    /**
     * Perform general search against ikasan solr index.
     *
     * @param searchString
     * @param resultSize
     * @return
     */
    public RESULTS search(String searchString, String queryFilter, long startTime, long endTime, int resultSize);

    /**
     * Build a solr search query suitable for the ikasan schema.
     *
     * @param moduleNames
     * @param flowNames
     * @param componentNames
     * @param fromDate
     * @param untilDate
     * @param payloadContent
     * @param eventId
     * @param type
     * @return
     */
    public String buildQuery(Set<String> moduleNames, Set<String> flowNames, Set<String> componentNames, Date fromDate
            , Date untilDate, String payloadContent, String eventId, String type);
}