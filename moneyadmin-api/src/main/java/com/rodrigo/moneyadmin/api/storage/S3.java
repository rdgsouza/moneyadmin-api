package com.rodrigo.moneyadmin.api.storage;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GroupGrantee;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.ObjectTagging;
import com.amazonaws.services.s3.model.Permission;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.SetObjectTaggingRequest;
import com.amazonaws.services.s3.model.Tag;
import com.rodrigo.moneyadmin.api.config.property.MoneyAdminApiProperty;

@Component
public class S3 {
	
	private static final Logger logger = LoggerFactory.getLogger(S3.class);
	
	@Autowired
	private MoneyAdminApiProperty property;
	
	@Autowired
	private AmazonS3 amazonS3;
	
	public String salvarTemporariamente(MultipartFile arquivo) {
		// Precisamos de 3 cosias antes criar a requisição para enviar o ("arquivo" = objeto).
		AccessControlList acl = new AccessControlList(); // 1 - Qual é a (Grantee = Permissão) desse objeto
		acl.grantPermission(GroupGrantee.AllUsers, Permission.Read);
		
		ObjectMetadata objectMetadata = new ObjectMetadata();  // 2 - Os metadados
		objectMetadata.setContentType(arquivo.getContentType());
		objectMetadata.setContentLength(arquivo.getSize());
		
		String nomeUnico = gerarNomeUnico(arquivo.getOriginalFilename()); // 3 - Criar um nome unico para o arquivo
		
		//Enviar o arquivo
		try {
			PutObjectRequest putObjectRequest = new PutObjectRequest(
					property.getS3().getBucket(),
					nomeUnico,
					arquivo.getInputStream(), 
					objectMetadata)
					.withAccessControlList(acl);
 //Abaixo colocamos que nessa requisição o ("arquivo" = objeto) vai ter a tag expirar que informa que o
 //arquivo vai expirar em um dia e que vai ser limpado dos arquivos temporarios caso nao seja 
 //autenticado e enviado para S3 essa configuração fizemos na classe S3Config na criação e 
 //configuração do nosso Bucket.			
			putObjectRequest.setTagging(new ObjectTagging(
					Arrays.asList(new Tag("expirar", "true"))));
	//Agora de fato vamos enviar a requisição para S3 para enviar o ("arquivo" = objeto) 
			amazonS3.putObject(putObjectRequest);
	//Vamos fazer um logger de Debug para ver se o metodo se encerrou normalmente.
			if (logger.isDebugEnabled()) {
				logger.debug("Arquivo {} enviado com sucesso para o S3.", 
						arquivo.getOriginalFilename());
			}
			
			return nomeUnico;
		} catch (IOException e) {
			throw new RuntimeException("Problemas ao tentar enviar o arquivo para o S3.", e);
		}
	}
	
	public String configurarUrl(String objeto) {
		return "\\\\" + property.getS3().getBucket() +
				".s3.amazonaws.com/" + objeto;
	}
	// Acima colocamos \\\\ para que não importe se o protocolo é http ou htttps
	// para qualquer um dos protocolos a url vai se ajustar
		
	public void salvar(String objeto) {
		SetObjectTaggingRequest setObjectTaggingRequest = new SetObjectTaggingRequest(
				property.getS3().getBucket(),
				objeto, 
				new ObjectTagging(Collections.emptyList()));	
		
		amazonS3.setObjectTagging(setObjectTaggingRequest);	 		
	}

	public void remove(String objeto) {
	 DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(
			 property.getS3().getBucket(), objeto);
	 
	  amazonS3.deleteObject(deleteObjectRequest);	
	}
	
	public void substituir(String objetoAntigo, String objetoNovo) {
	
		if (StringUtils.hasText(objetoAntigo)) { 
				
				this.remove(objetoAntigo);	
		}
		
		salvar(objetoNovo);	
	}
	
	private String gerarNomeUnico(String originalFilename) {
		return UUID.randomUUID().toString() + "_" + originalFilename;
	}
		
}	
	