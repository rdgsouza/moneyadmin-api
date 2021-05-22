package com.rodrigo.moneyadmin.api.service;

import java.io.InputStream;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.rodrigo.moneyadmin.api.dto.LancamentoEstatisticaPessoa;
import com.rodrigo.moneyadmin.api.mail.Mailer;
import com.rodrigo.moneyadmin.api.model.Lancamento;
import com.rodrigo.moneyadmin.api.model.Pessoa;
import com.rodrigo.moneyadmin.api.model.Usuario;
import com.rodrigo.moneyadmin.api.repository.LancamentoRepository;
import com.rodrigo.moneyadmin.api.repository.PessoaRepository;
import com.rodrigo.moneyadmin.api.repository.UsuarioRepository;
import com.rodrigo.moneyadmin.api.service.exception.PessoaInexistenteOuInativaException;
import com.rodrigo.moneyadmin.api.storage.S3;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class LancamentoService {

	
//	Criamos uma variavel estatica e qual a função de uma variavel estatica?
//	Uma variável estática é comum a todas as instâncias (ou objetos) da classe porque é uma variável 
//	no nível da classe. Em outras palavras, você pode dizer que apenas uma única cópia da variável 
//	estática é criada e compartilhada entre todas as instâncias da classe.
	private static final String DESTINATARIOS = "ROLE_PESQUISAR_LANCAMENTO";
	
	private static final Logger logger = LoggerFactory.getLogger(LancamentoService.class); 
	
	@Autowired
	private PessoaRepository pessoaRepository;

	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	@Autowired
	private UsuarioRepository usuarioRepository; // para o STS sugerir o nome de uma variavel comece a 
	// escrever o nome da variavel coloque ; no final da variavel e em seguida clique em cima da variavel
	// e aperte ctr + espaço. Vai aparecer sugestões de nome para sua variavel.
	
	@Autowired
	private Mailer mailer;
	
	@Autowired
	private S3 s3;
	
	// @Scheduled(fixedDelay = 1000 * 60 * 30)	
    // @Scheduled(cron = "0 22 55 * * *")
	@Scheduled(cron = "0 30 13 * * *", zone = "America/Sao_Paulo")
	@Scheduled(cron = "0 30 13 * * *", zone = "America/Manaus")
	public void avisarSobreLancamentosVencidos() {
		
  		if (logger.isDebugEnabled()) {
  			logger.debug("Preparando envio de e-mails de "
  					+ "aviso de lançamentos vencidos.");
  		}
  		
  		List<Lancamento> vencidos = lancamentoRepository
				.findByDataVencimentoLessThanEqualAndDataPagamentoIsNull(LocalDate.now());
  		//O metodo .findByDataVencimentoLessThanEqualAndDataPagamentoIsNull(LocalDate.now()) faz
  		//a pesquisa DataVencimento menor ate a data atual.
  		// obs: pegamos somente os lançamentos vencidos 
  		// Usamos o DataPagamentoIsNull para nao pegar a data com pagamento e ficar somente 
  		// DataVencimento ate a data a data atual
  		// Como estamos pegando somente os vencidos entao deve existit apenas o campo DataVencimento
  		// e no campo DataPagamento deve estar null indicando que o lançamento ainda nao foi pago.
        		
  		if(vencidos.isEmpty()) {
  			logger.info("Sem lançamentos vencidos para aviso.");
  			
  			return;	
  		}
  		
  		logger.info("Existem {} lançamentos vencidos.", vencidos.size());
  		
		List<Usuario> destinatarios = usuarioRepository
				.findByPermissoesDescricao(DESTINATARIOS);
		
       if (destinatarios.isEmpty()) {
    	   	logger.warn("Existem lançamentos vencidos, mas o"
    	   			+ " sistema não encontrou destinatários.");
    	   	
    	   	return;
       }
		
       mailer.avisarSobreLancamentosVencidos(vencidos, destinatarios);
		
			logger.info("Envio de e-mail de aviso concluído.");
	}
	
	public byte[] relatorioPorPessoaReceitaDespesaQuitadas(LocalDate inicio, LocalDate fim) 
			throws Exception {
		List<LancamentoEstatisticaPessoa> dados = lancamentoRepository
				.porPessoaReceitaDespesaQuitadas(inicio, fim);
		
		Map<String, Object> parametros = new HashMap<>();
		parametros.put("DT_INICIO", Date.valueOf(inicio));
		parametros.put("DT_FIM", Date.valueOf(fim));
		parametros.put("REPORT_LOCALE", new Locale("pt", "BR"));
		
		InputStream inputStream = this.getClass().getResourceAsStream(
				"/relatorios/lancamentos-por-pessoa-receitas-despesas-quitadas.jasper");
		
		JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, parametros,
				new JRBeanCollectionDataSource(dados));

		if (vericarDadosVazio(dados) == true) {
			return null;
		} else {
			return JasperExportManager.exportReportToPdf(jasperPrint);	
		}
		
//		if(dados.isEmpty()){
//			return null;
//		} else {
//			return JasperExportManager.exportReportToPdf(jasperPrint);
// 		}
	  }
		

	public byte[] relatorioPorPessoaReceitaDespesaEmAberto(LocalDate inicio, LocalDate fim) throws Exception {
		List<LancamentoEstatisticaPessoa> dados = lancamentoRepository
				.porPessoaReceitaDespesaEmAberto(inicio, fim);
		
		Map<String, Object> parametros = new HashMap<>();
		parametros.put("DT_INICIO", Date.valueOf(inicio));
		parametros.put("DT_FIM", Date.valueOf(fim));
		parametros.put("REPORT_LOCALE", new Locale("pt", "BR"));
		
		InputStream inputStream = this.getClass().getResourceAsStream(
				"/relatorios/lancamentos-por-pessoa-receitas-despesas-em-aberto.jasper");
		
		JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, parametros,
				new JRBeanCollectionDataSource(dados));
		
		if (vericarDadosVazio(dados) == true) {
			return null;
	   	} else 	{
			return JasperExportManager.exportReportToPdf(jasperPrint);	
		}
		
//		return JasperExportManager.exportReportToPdf(jasperPrint);	
	}
	
	 public boolean vericarDadosVazio(List<?> dados) {
		 if(dados.isEmpty()) {
        return true; 
		 } else {
		return false;
		 }	
	 }	
	
	public Lancamento salvar(Lancamento lancamento) {

//		Pessoa pessoa = pessoaRepository.findById(lancamento.getPessoa().getCodigo())
//				.orElseThrow(() -> new EmptyResultDataAccessException(1));
//
//		if (pessoa == null || pessoa.isInativo()) {
//			throw new PessoaInexistenteOuInativaException();
//		}
//     Acima era como validavamos a pessoa criamos o metodo abaixo validar por que para nao repertir 
//    codigo ja que em atualizar lancamento vamos precias validar a pessoa tambem 		
			
	// Depois que é anexado o arquivo ele vai estar temporariamente salvo na s3 o metodo abaixo
	// vai de fato salvar o arquivo na s3. Vai tirar ele de um arquivo temporario e salvar na s3
		if(StringUtils.hasText(lancamento.getAnexo())) {
			s3.salvar(lancamento.getAnexo());
		}
		
		 validarPessoa(lancamento);	
		
		return lancamentoRepository.save(lancamento);
	}

	public Lancamento atualizar(Long codigo, Lancamento lancamento) {
		Lancamento lancamentoSalvo = buscarLancamentoExistente(codigo);
		if (!lancamento.getPessoa().equals(lancamentoSalvo.getPessoa())) {
			validarPessoa(lancamento);
		}

		//Vamos criar dois if de verificação para o anexo
		//O primerio verifica a propiedade lancamento.getAnexo() estar vazia
		//e se no lancamentoSalvo a propiedade lancamento.getAnexo() tem algum anexo.
		//Entao o que nos vamos fazer e remover o anexo que estar em lancamentoSalvo
		//Porque se a proidade lancamento.getAnexo() estar vindo vazia nos vamos entender como uma 
		//intensao de remoção do anexo que estar em lancamentoSalvo
		//Agora se na popriedade lancamento.getAnexo() tiver um anexo e ele nao for igual ao anexo
		//que ja estar salvo o entao vamos fazer a substituição.
		
		  if (StringUtils.isEmpty(lancamento.getAnexo())   //a propiedade estar vazia ?
				&& StringUtils.hasText(lancamentoSalvo.getAnexo())) { //a propiedade de 
//		  lancamentoSalvo.getAnexo() nao estar vazia entao segnifica que de fato em lancamentoSalvo
//		  existe um anexo.
//		  Entao veio um anexo vazio no parametro e em lancamentoSalvo.getAnexo() tem um anexo então 
//		  a logica que é para ser aplicada é a de remoção.
		 s3.remove(lancamentoSalvo.getAnexo()); // então remove vamos informar para o s3 a remoção do anexo
		} else if(StringUtils.hasText(lancamento.getAnexo())
		&& !lancamento.getAnexo().equals(lancamentoSalvo.getAnexo())) {
          s3.substituir(lancamentoSalvo.getAnexo(), lancamento.getAnexo());
		}		
		
		BeanUtils.copyProperties(lancamento, lancamentoSalvo, "codigo");

		return lancamentoRepository.save(lancamentoSalvo);
	}

	private void validarPessoa(Lancamento lancamento) {
		Pessoa pessoa = null;
		if (lancamento.getPessoa().getCodigo() != null) {
		pessoa = pessoaRepository.findById(lancamento.getPessoa().getCodigo())
				.orElseThrow(() -> new EmptyResultDataAccessException(1));
		}
				
		if (pessoa == null || pessoa.isInativo()) {
			throw new PessoaInexistenteOuInativaException();
		}
	}

	private Lancamento buscarLancamentoExistente(Long codigo) {
		Lancamento lancamentoSalvo = lancamentoRepository.findById(codigo)
				.orElseThrow(() -> new EmptyResultDataAccessException(1));
		if (lancamentoSalvo == null) {
			throw new IllegalArgumentException();
		}
		return lancamentoSalvo;
	}	
	
}