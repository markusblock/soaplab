package org.soaplab;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Configuration
@ConfigurationProperties(prefix = "soaplab")
@Getter
@Setter
public class SoaplabProperties {

	private String initfolder;

}
