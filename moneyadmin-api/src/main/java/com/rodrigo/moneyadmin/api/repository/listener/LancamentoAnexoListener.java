//package com.rodrigo.moneyadmin.api.repository.listener;
//
//import javax.persistence.PostLoad;
//
//import org.springframework.util.StringUtils;
//
//import com.rodrigo.moneyadmin.api.MoneyAdminApiApplication;
//import com.rodrigo.moneyadmin.api.model.Lancamento;
//import com.rodrigo.moneyadmin.api.storage.S3;
//
//public class LancamentoAnexoListener {
//
////Metodo para setar a url depois que a classe Lancamento for carregada 	
//	@PostLoad       
//	public void postLoad(Lancamento lancamento) {
//		if (StringUtils.hasText(lancamento.getAnexo())) {
//			S3 s3 = MoneyAdminApiApplication.getBean(S3.class);//usando o meto getBean para criar a instancia
//			lancamento.setUrlAnexo(s3.configurarUrl(lancamento.getAnexo()));//faz a configuração da url de
//			//acesso ao arquivo que foi salco na s3
//		}
//	}	 
//}
