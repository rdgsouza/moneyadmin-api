package com.rodrigo.moneyadmin.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;

import com.rodrigo.moneyadmin.api.config.property.MoneyAdminApiProperty;

@SpringBootApplication
@EnableConfigurationProperties(MoneyAdminApiProperty.class)
public class MoneyAdminApiApplication {
	
	private static ApplicationContext APPLICATION_CONTEXT;

	public static void main(String[] args) {
		APPLICATION_CONTEXT = SpringApplication.run(MoneyAdminApiApplication.class, args);
	}
	//Metodo para pegar uma instancia por exemplo da classe s3 que no caso vamos precisar usar 
	//na classe LancamentoAnexoListener pois vamos usar uma instancia de s3 na classe 
	//LancamentoAnexoListener e a instancia dessa classe no final das contas quem vai construir e o
	//Hibernate e nao o Spring sendo assim nao podemos fazer uso da anotação @Autowired na instancia
	//de s3 para criar uma instancia apartir do Spring. Entao a solução para instanciar uma variavel de s3
	//ou de qualquer classe sem o uso do da anotação @Autowired so Spring é da forma abaixo:
	public static <T> T getBean(Class<T> type) {
		return APPLICATION_CONTEXT.getBean(type);
	}
}