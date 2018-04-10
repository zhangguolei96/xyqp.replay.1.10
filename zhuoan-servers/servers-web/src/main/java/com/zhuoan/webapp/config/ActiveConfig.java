package com.zhuoan.webapp.config;

import com.zhuoan.webapp.listener.event.BaseQueueMessageListener;
import com.zhuoan.webapp.listener.event.SSSQueueMessageListener;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.DefaultMessageListenerContainer;

import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.Queue;

/**
 * ActiveConfig
 *
 * @author weixiang.wu
 * @date 2018 -04-09 21:02
 */
@Configuration
public class ActiveConfig {

    @Resource
    private BaseQueueMessageListener baseQueueMessageListener;

    @Resource
    private SSSQueueMessageListener sssQueueMessageListener;


    /**
     * Base queue destination queue.
     *
     * @return the queue
     */
    @Bean
    public Queue baseQueueDestination() {
        return new ActiveMQQueue("ZA_GAMES_BASE");
    }

    /**
     * Sss queue destination queue.
     *
     * @return the queue
     */
    @Bean
    public Queue sssQueueDestination() {
        return new ActiveMQQueue("ZA_GAMES_SSS");
    }

    /**
     * Jms template jms template.
     *
     * @param connectionFactory the connection factory
     * @return the jms template
     */
    @Bean
    public JmsTemplate jmsTemplate(ConnectionFactory connectionFactory) {
        JmsTemplate jmsTemplate = new JmsTemplate();
        jmsTemplate.setConnectionFactory(connectionFactory);
        jmsTemplate.setDefaultDestination(baseQueueDestination());
        jmsTemplate.setReceiveTimeout(10000);
        jmsTemplate.setPubSubDomain(false);
        return jmsTemplate;
    }

    /**
     * Queue listener container default message listener container.
     *
     * @param connectionFactory the connection factory
     * @return the default message listener container
     */
    @Bean
    public DefaultMessageListenerContainer queueListenerContainer(ConnectionFactory connectionFactory) {
        DefaultMessageListenerContainer queueListenerContainer = new DefaultMessageListenerContainer();
        queueListenerContainer.setConnectionFactory(connectionFactory);
        queueListenerContainer.setMessageListener(baseQueueMessageListener);
        queueListenerContainer.setDestination(baseQueueDestination());
        return queueListenerContainer;
    }

    /**
     * Queue listener container 2 default message listener container.
     *
     * @param connectionFactory the connection factory
     * @return the default message listener container
     */
    @Bean
    public DefaultMessageListenerContainer queueListenerContainer2(ConnectionFactory connectionFactory) {
        DefaultMessageListenerContainer queueListenerContainer = new DefaultMessageListenerContainer();
        queueListenerContainer.setConnectionFactory(connectionFactory);
        queueListenerContainer.setMessageListener(sssQueueMessageListener);
        queueListenerContainer.setDestination(sssQueueDestination());
        return queueListenerContainer;
    }


}