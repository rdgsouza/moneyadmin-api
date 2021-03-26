package com.rodrigo.moneyadmin.api.config;
//package com.rodrigo.moneyadmin.api.config;
//
//import java.util.Locale;
//
//import org.springframework.context.MessageSource;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.support.ReloadableResourceBundleMessageSource;
//import org.springframework.web.servlet.LocaleResolver;
//import org.springframework.web.servlet.i18n.FixedLocaleResolver;
//
//@Configuration
//public class LocaleConfig {
	
 //	Há duas formas de configurar a internacionalização das mensagens padrões enviadas pela api
 // em vez de mostrar a frase padrão que vem inglês podemos escolher um idioma para tradução	
 // ex: de mensagens padrões enviadas pela api:   
 //	  "error": "invalid_grant",
 //    "error_description": "Bad credentials"
 //  A mensagem acima é quando o usario envia credenciais erradas para autenificação
 //  E existem outras mensagens como a de acesso negado: 
 //	 {
 //    "error": "access_denied",
 //    "error_description": "Access is denied"
 //  }
	// e etc.
// OBS:  as mensagens passaram a ser exibidas de acordo com locale da pessoa após a
//	atualização para o Spring Boot 2. Se a pessoa estar no Brasil a aplicação retorna o locale	
//  "local" e traduz de acordo com a linguagem local. 	
	
	// Então nossa primeiro tipo de configuração:
 //	1. Definir um idioma padrão que não pode ser alterado pelo 
 //	cliente front end através do header "Accept-Language". Para isso, crie a classe LocaleConfig
 // no pacote config e em cima da classe coloque a anotaçao @Configuration e na classe o 
 //	seguinte conteúdo:	
   
	// Aqui definimos o idioma "pt-BR" como padrão ou qualquer outro
// 	   @Bean
//	    public LocaleResolver localeResolver() {
//	        return new FixedLocaleResolver(new Locale("en", "US"));
//	    }

    // Método necessário apenas se não estiver usando o Spring Boot 2.
    // Aqui adicionamos as mensagens do Spring Security nos basenames do MessageSource
//    @Bean
//    public MessageSource messageSource() {
//        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
//        messageSource.addBasenames("classpath:org/springframework/security/messages");
//        messageSource.addBasenames("classpath:/messages");
//
//        return messageSource;
//    }
//	
//}	
	// 2. Esperar a definição do idioma por parte do cliente. Diferentemente do código anterior,
    // aqui não definimos um locale fixo:
	// import org.springframework.context.MessageSource;
	// import org.springframework.context.annotation.Bean;
	// import org.springframework.context.annotation.Configuration;
	// import org.springframework.context.support.ReloadableResourceBundleMessageSource;
	//
	// LocaleConfig.java
	// @Configuration
	// public class LocaleConfig {
	//
	//    // Método necessário apenas se não estiver usando o Spring Boot 2.
	//    // Aqui adicionamos as mensagens do Spring Security nos basenames do MessageSource
	//    @Bean
	//    public MessageSource messageSource() {
	// ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
	//        messageSource.addBasenames("classpath:org/springframework/security/messages");
	//        messageSource.addBasenames("classpath:/messages");
	//
	//        return messageSource;
	//    }
	//
	// }
	// Com isso, o cliente front end poderá definir o idioma das mensagens da API através do
    // header "Accept-Language" da seguinte forma:
	// Exemplo com pt-BR: https://snipboard.io/DBJKlx.jpg
	// Exemplo com en-US: https://snipboard.io/Oz2wST.jpg
	// Exemplo com es-ES: https://snipboard.io/T26ogX.jpg
	//
	// O detalhe importante é que usando o Spring Boot 2 não é necessário configurar o MessageSource como feito no código acima, pois ele já adiciona automaticamente as mensagens do Spring Security.
	//
