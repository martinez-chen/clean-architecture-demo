package com.martin.cleanarchitecturedemo;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <li> @ConfigurationProperties 用於將配置文件中的配置映射到對象中，從而可以方便的使用對象來訪問配置文件中的配置。</li>
 */
@Data
@ConfigurationProperties(prefix = "demo")
public class CleanArchitectureDemoConfigurationProperties {

  private long transferThreshold = Long.MAX_VALUE;
}
