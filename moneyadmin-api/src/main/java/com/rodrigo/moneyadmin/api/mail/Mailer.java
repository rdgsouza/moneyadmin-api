package com.rodrigo.moneyadmin.api.mail;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.rodrigo.moneyadmin.api.model.Lancamento;
import com.rodrigo.moneyadmin.api.model.Usuario;


@Component
public class Mailer {

	 @Autowired
	 private JavaMailSender mailSender;
	 
	 @Autowired
	 private TemplateEngine thymelaaf;
	  
//	 @Autowired
//	 private LancamentoRepository repo;
	 
//	 @EventListener
//	 private void teste(ApplicationReadyEvent event) {
//		 this.enviarEmail("rdsouza.c@gmail.com", Arrays.asList("rdsouza.cs10@gmail.com"),
//				 "Testando", "Olá!<br>Teste ok.");
//		 System.out.println("Terminado o envio de e-mail...");
//	 }
	 
//	 OBS: Para aplicação usar seu email para o envio de email você deve configura o seu e-mail
//	 1 - Verifique se a verificação em duas etapas está desativada
//	 2 - Permitir aplicativo menos seguro (ON) siga este link para ir para essa tela:
//	 https://myaccount.google.com/lesssecureapps
//   Por fim verifique se seu nome de usuário e senha estão corretos ou não no arquivo application.properties.

//   Fonte: https://stackoverflow.com/questions/35347269/javax-mail-authenticationfailedexception-535-5-7-8-username-and-password-not-ac	 
	 
//	 @EventListener
//	 private void teste(ApplicationReadyEvent event) {
//		 
//		 String template = "mail/aviso-lancamentos-vencidos";
//		 	
//		 List<Lancamento> lista = repo.findAll();
//		 
//		 Map<String, Object> variaveis = new HashMap<>();
//		 variaveis.put("lancamentos", lista);
//		 
//		 this.enviarEmail("management.m.a.software@gmail.com", Arrays.asList("rdsouza.c@gmail.com"),
//				 "Informações", template, variaveis);
//		 System.out.println("Terminado o envio de e-mail...");
//	 }
	 
//	 OBS: Para aplicação usar seu email para o envio de email você deve configura o seu e-mail
//	 1 - Verifique se a verificação em duas etapas está desativada
//	 2 - Permitir aplicativo menos seguro (ON) siga este link para ir para essa tela:
//	 https://myaccount.google.com/lesssecureapps
//   Por fim verifique se seu nome de usuário e senha estão corretos ou não no arquivo application.properties.
	 
	 public void avisarSobreLancamentosVencidos(
			 List<Lancamento> vencidos, List<Usuario> destinatarios) {	
		 Map<String, Object> variaveis = new HashMap<>();
		 variaveis.put("lancamentos", vencidos);
		 
		 List<String> emails = destinatarios.stream()
				 .map(u -> u.getEmail())
				 .collect(Collectors.toList());
		 
		 this.enviarEmail("management.m.a.software@gmail.com", 
				 emails, 
				 "Lançamentos vencidos",
				 "mail/aviso-lancamentos-vencidos",
				 variaveis);	 
	 }
	 
	 public void enviarEmail(String remetente,
			 List<String> destinatarios, String assunto, String template, 
			 Map<String, Object> variaveis) {
		 
		 Context context = new Context(new Locale("pt","BR"));
		 
		 variaveis.entrySet().forEach(
				 e -> context.setVariable(e.getKey(), e.getValue()));
		 
		 String menssagem = thymelaaf.process(template, context);
		 
		 this.enviarEmail(remetente, destinatarios, assunto, menssagem);
		 
	 }
	 
	 public void enviarEmail(String remetente,
			 List<String> destinatarios, String assunto, String menssagem) {
		 
	     try {
	    	 MimeMessage mimeMessage = mailSender.createMimeMessage();
		     MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
	    	 helper.setFrom(remetente);
	    	 helper.setTo(destinatarios.toArray(new String[destinatarios.size()]));
	    	 helper.setSubject(assunto);
	    	 helper.setText(menssagem, true);
	    	 
	    	 mailSender.send(mimeMessage);
		} catch (MessagingException e) {
			throw new RuntimeException("Problemas com o envio de e-mail.", e); 
		}
	}
}
