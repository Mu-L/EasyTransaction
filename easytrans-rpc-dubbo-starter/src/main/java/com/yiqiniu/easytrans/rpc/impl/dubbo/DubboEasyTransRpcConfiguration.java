package com.yiqiniu.easytrans.rpc.impl.dubbo;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ConsumerConfig;
import com.alibaba.dubbo.config.ModuleConfig;
import com.alibaba.dubbo.config.MonitorConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.ProviderConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.spring.context.annotation.EnableDubboConfig;
import com.yiqiniu.easytrans.filter.EasyTransFilterChainFactory;
import com.yiqiniu.easytrans.monitor.EtMonitor;
import com.yiqiniu.easytrans.monitor.MonitorConsumerFactory;
import com.yiqiniu.easytrans.rpc.EasyTransRpcConsumer;
import com.yiqiniu.easytrans.rpc.EasyTransRpcProvider;

/** 
* @author xudeyou 
*/
@Configuration
@ConditionalOnProperty(name="easytrans.rpc.dubbo.enabled",havingValue="true",matchIfMissing=true)
@EnableConfigurationProperties(DubboEasyTransRpcProperties.class)
@EnableDubboConfig
public class DubboEasyTransRpcConfiguration {
    
    @Value("${spring.application.name}")
    private String applicationName;
	
	@Bean
	@ConditionalOnMissingBean(EasyTransRpcConsumer.class)
	public DubboEasyTransRpcConsumerImpl dubboEasyTransRpcConsumerImpl(Optional<ApplicationConfig> applicationConfig, Optional<RegistryConfig> registryConfig,Optional<ProtocolConfig> protocolConfig,Optional<ConsumerConfig> consumerConfig,Optional<ModuleConfig> moduleConfig,Optional<MonitorConfig> monitorConfig, Optional<DubboReferanceCustomizationer> customizationer){
		return new DubboEasyTransRpcConsumerImpl(applicationConfig, registryConfig, protocolConfig, consumerConfig, moduleConfig, monitorConfig, customizationer);
	}
	
	@Bean
	@ConditionalOnMissingBean(EasyTransRpcProvider.class)
	public DubboEasyTransRpcProviderImpl onsEasyTransMsgPublisherImpl(EasyTransFilterChainFactory filterChainFactory, Optional<ApplicationConfig> applicationConfig,
			Optional<RegistryConfig> registryConfig,Optional<ProtocolConfig> protocolConfig,Optional<ProviderConfig> providerConfig,Optional<ModuleConfig> moduleConfig,Optional<MonitorConfig> monitorConfig, Optional<DubboServiceCustomizationer> customizationer){
		return new DubboEasyTransRpcProviderImpl(filterChainFactory, applicationConfig, registryConfig, protocolConfig, providerConfig, moduleConfig, monitorConfig, customizationer);
	}
	
	@Bean
	@ConditionalOnProperty(name="easytrans.rpc.dubbo.monitor.enabled",havingValue="true",matchIfMissing=true)
	public DubboMonitorProvider dubboMonitorProvider(EasyTransFilterChainFactory filterChainFactory, Optional<ApplicationConfig> applicationConfig,
            Optional<RegistryConfig> registryConfig,Optional<ProtocolConfig> protocolConfig,Optional<ProviderConfig> providerConfig,Optional<ModuleConfig> moduleConfig,Optional<MonitorConfig> monitorConfig, Optional<DubboServiceCustomizationer> customizationer,List<EtMonitor> etMonitorList) {
	    return new DubboMonitorProvider(applicationName, filterChainFactory, applicationConfig, registryConfig, protocolConfig, providerConfig, moduleConfig, monitorConfig, customizationer, etMonitorList);
	}
	
	@Bean
	@ConditionalOnMissingBean(MonitorConsumerFactory.class)
    public MonitorConsumerFactory monitorConsumerFactory(Optional<ApplicationConfig> applicationConfig, Optional<RegistryConfig> registryConfig,Optional<ProtocolConfig> protocolConfig,Optional<ConsumerConfig> consumerConfig,Optional<ModuleConfig> moduleConfig,Optional<MonitorConfig> monitorConfig, Optional<DubboReferanceCustomizationer> customizationer) {
        return new DubboMonitorConsumerFactory(applicationConfig, registryConfig, protocolConfig, consumerConfig, moduleConfig, monitorConfig, customizationer);
    }

	
}
