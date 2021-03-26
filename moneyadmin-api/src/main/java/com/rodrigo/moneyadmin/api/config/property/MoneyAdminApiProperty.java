package com.rodrigo.moneyadmin.api.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("moneyadmin") //Vc escolhe um nome para configuracao. 
// La no arquivo aplication-prod.properties na hora de setar as configuracoes quando for
// para producao vc vai digitar: algamoney.seguranca.enable-https=true -> true para dizer que o 
// cookie e seguro vai usar https estamos uasando por padrao o cookie sem seguranca porque estamos
// em desenvolvimento mas quando for para producao vamo setar ele true para ficar https
// algamoney.origin-permitida=https://algamoney-angular.herokuapp.com -> E aqui e para setar
// a origem permitida quando for levantar a aplicacao em producao
public class MoneyAdminApiProperty {

	private String originPermitida = "http://localhost:8000";
	
	private final Seguranca seguranca = new Seguranca();
	
	private final Mail mail = new Mail();
	
	private final S3 s3 = new S3();
	
	public S3 getS3() {
		return s3;
	}
	
	public Mail getMail() {
		 return mail;
	}
	
	//OBS: Para configura a api para receber requisições de todas as origens
	//basta adicionar o sinal de *
	//ex: private String originPermitida = *
	//Serve tanto aqui na originPermitida padrao de classe como na hora de passar o parametro 
    //no arquivo application-prod.properties na propiedade algamoney.origin-permitida=*	
	//ou na hora de executar o jar da aplicação pelo terminal
	public String getOriginPermitida() {
		return originPermitida;
	}

	public void setOriginPermitida(String originPermitida) {
		this.originPermitida = originPermitida;
	}

	public Seguranca getSeguranca() {
		return seguranca;
	}

	public static class S3 {
		
		private String accessKeyId;
		
		private String secretAccessKey;
		
		private String bucket = "rd-s-arquivos";

		public String getBucket() {
			return bucket;
		}
		
		public void setBucket(String bucket) {
			this.bucket = bucket;
		}	
		
		public String getAccessKeyId() {
			return accessKeyId;
		}

		public void setAccessKeyId(String accessKeyId) {
			this.accessKeyId = accessKeyId;
		}

		public String getSecretAccessKey() {
			return secretAccessKey;
		}

		public void setSecretAccessKey(String secretAccessKey) {
			this.secretAccessKey = secretAccessKey;
		}
		
	}
	
	public static class Seguranca {
		
		private boolean enableHttps;

		public boolean isEnableHttps() {
			return enableHttps;
		}

		public void setEnableHttps(boolean enableHttps) {
			this.enableHttps = enableHttps;
		}
	 }
		
	public static class Mail {
			
		private String host;
			
		private Integer port;
			
		private String username;
			
		private String password;

		public String getHost() {
			return host;
			}

		public void setHost(String host) {
			this.host = host;
			}

		public Integer getPort() {
			return port;
			}

		public void setPort(Integer port) {
			this.port = port;
			}

		public String getUsername() {
				return username;
			}

		public void setUsername(String username) {
			this.username = username;
			}

		public String getPassword() {
			return password;
			}

		public void setPassword(String password) {
			this.password = password;
			}
		
				
		}
	
	}
