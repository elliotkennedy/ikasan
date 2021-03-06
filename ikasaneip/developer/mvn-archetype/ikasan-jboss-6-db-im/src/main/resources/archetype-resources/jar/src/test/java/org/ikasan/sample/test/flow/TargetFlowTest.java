package org.ikasan.sample.test.flow;

import org.ikasan.component.endpoint.jms.spring.consumer.SpringMessageConsumerConfiguration;
import org.ikasan.component.endpoint.jms.spring.producer.SpringMessageProducerConfiguration;
import org.ikasan.flow.visitorPattern.VisitingInvokerFlow;
import org.ikasan.platform.IkasanEIPTest;
import org.ikasan.spec.component.endpoint.Producer;
import org.ikasan.spec.configuration.ConfiguredResource;
import org.ikasan.spec.flow.FlowElement;
import org.ikasan.spec.management.ManagedResource;
import org.jmock.Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Sample Scheduled Flow test.
 *
 * @author Ikasan Developmnet Team
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
// specifies the Spring configuration to load for this test fixture
@ContextConfiguration(locations = {
        "/monitor-service-conf.xml",
        "/monitor-conf.xml",
        "/shared-conf.xml",
        "/target-db-flow-conf.xml",
        "/db-conf.xml",
        "/jms-conf.xml",
        "/scheduled-conf.xml",
        "/exception-conf.xml",
        "/mock-conf.xml",
        "/substitute-components.xml",
        "/test-conf.xml",
        "/h2db-datasource-conf.xml"
})
public class TargetFlowTest extends IkasanEIPTest
{
    /** mockery instance */
    @Resource
    Mockery mockery;

    /** flow on test */
    @Resource
    VisitingInvokerFlow targetFlow;

    @Resource
    Producer testDataProducer;

    /**
     * Tests flow operation for Sample Flow.
     */
    @SuppressWarnings("unchecked")
    @Test
    @DirtiesContext
    public void test_successful_sampleFlow_invocation() throws IOException
    {
        // setup the expected component invocations
        ikasanFlowTestRule.withFlow(targetFlow)
                .consumer("Consumer Name")
                .producer("Producer Name");


        // configure AMQ to provide in-memory destinations for the test
        Map<String,String> jndiProperties = new HashMap<>();
        jndiProperties.put("java.naming.factory.initial", "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        jndiProperties.put("java.naming.provider.url", "vm://localhost?broker.persistent=false");

        // configure the JMS db for the test
        FlowElement<?> consumerFlowElement = this.targetFlow.getFlowElement("Consumer Name");
        ConfiguredResource<SpringMessageConsumerConfiguration> configuredConsumer = (ConfiguredResource)consumerFlowElement.getFlowComponent();
        SpringMessageConsumerConfiguration consumerConfiguration = configuredConsumer.getConfiguration();
        consumerConfiguration.setConnectionFactoryName("ConnectionFactory");
        consumerConfiguration.setConnectionFactoryJndiProperties(jndiProperties);
        consumerConfiguration.setDestinationJndiName("dynamicQueues/queue");
        consumerConfiguration.setDurable(false);
        consumerConfiguration.setDestinationJndiProperties(jndiProperties);

        // test data producer
        SpringMessageProducerConfiguration testProducerConfiguration = ((ConfiguredResource<SpringMessageProducerConfiguration>) testDataProducer).getConfiguration();
        testProducerConfiguration.setConnectionFactoryJndiProperties(jndiProperties);
        testProducerConfiguration.setConnectionFactoryName("ConnectionFactory");
        testProducerConfiguration.setDestinationJndiName("dynamicQueues/queue");
        testProducerConfiguration.setDestinationJndiProperties(jndiProperties);
        ((ManagedResource)testDataProducer).startManagedResource();

        // start the flow and assert it runs
        ikasanFlowTestRule.startFlow(testHarnessFlowEventListener);

        this.testDataProducer.invoke("test data");

        // wait for a brief while to let the flow complete
        ikasanFlowTestRule.sleep(4000L);

        // mocked assertions
        mockery.assertIsSatisfied();

        // no need to stop flow or check harness assertions - that is done by the IkasanFlowTestRule
    }

}