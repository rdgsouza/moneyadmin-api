package com.rodrigo.moneyadmin.api.resource;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.rodrigo.moneyadmin.api.dto.Anexo;
import com.rodrigo.moneyadmin.api.dto.LancamentoEstatisticaCategoria;
import com.rodrigo.moneyadmin.api.dto.LancamentoEstatisticaDia;
import com.rodrigo.moneyadmin.api.event.RecursoCriadoEvent;
import com.rodrigo.moneyadmin.api.exceptionhandler.MoneyAdminExceptionHandler.Erro;
import com.rodrigo.moneyadmin.api.model.Lancamento;
import com.rodrigo.moneyadmin.api.repository.LancamentoRepository;
import com.rodrigo.moneyadmin.api.repository.filter.LancamentoFilter;
import com.rodrigo.moneyadmin.api.repository.projection.ResumoLancamento;
import com.rodrigo.moneyadmin.api.service.LancamentoService;
import com.rodrigo.moneyadmin.api.service.exception.PessoaInexistenteOuInativaException;
import com.rodrigo.moneyadmin.api.storage.S3;

@RestController
@RequestMapping("/lancamentos")
public class LancamentoResource {

	@Autowired
	private LancamentoRepository lancamentoRepository;

	@Autowired
	private LancamentoService lancamentoService;

	@Autowired
	private ApplicationEventPublisher publisher;

	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private S3 s3;

//	private byte[] byteReferenciaNulo;
	
	@PostMapping("/anexo")
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_LANCAMENTO') and #oauth2.hasScope('write')")
	//Vamos Colocar o tipo de retorno Anexo para retorna o nome do arquivo e a url necessaria para 
	//acessar esse arquivo
	public Anexo uploadAnexo(@RequestParam MultipartFile anexo) throws IOException {

//      Para salvar o arquivo no computado local 
//		OutputStream out = new FileOutputStream(
//				"/home/rodrigo/Documentos/anexo--" + anexo.getOriginalFilename());
//		out.write(anexo.getBytes());
//		out.close();
		
//      Para salvar o arquivo na conta que criamos na AmazonS3
		String nome = s3.salvarTemporariamente(anexo);	
		return new Anexo(nome, s3.configurarUrl(nome));	
	}
		
	@GetMapping("/relatorios/por-pessoa-receita-despesa-quitadas")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
	public ResponseEntity<byte[]> relatorioPorPessoaDespesaReceitaQuitadas(
		@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate inicio, 
		@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fim) throws Exception {		
		
		byte[] relatorio = lancamentoService.relatorioPorPessoaReceitaDespesaQuitadas(inicio, fim);
		
		if(relatorio != null) {
			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
					.body(relatorio);		
		} else {
		    return ResponseEntity.status(HttpStatus.NO_CONTENT)
		    		.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
		    		.body(relatorio);	
         		}
			}

	@GetMapping("/relatorios/por-pessoa-receita-despesa-em-aberto")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
	public ResponseEntity<byte[]> relatorioPorPessoaReceitaDespesaEmAberto(
		@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate inicio, 
		@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fim) throws Exception {		
		
		byte[] relatorio = lancamentoService.relatorioPorPessoaReceitaDespesaEmAberto(inicio, fim);
			
		if(relatorio != null) {
			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
					.body(relatorio);		
		} else {
		    return ResponseEntity.status(HttpStatus.NO_CONTENT)
		    		.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
		    		.body(relatorio);	
         		}
			}
	
	@GetMapping("/estatisticas/por-dia")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")	
	public List<LancamentoEstatisticaDia>porDia() {
	
		return this.lancamentoRepository.porDia(LocalDate.now());	
		// Caso você queira que retorne os dados a partir de um mes especifico e so colocar:
		// return this.lancamentoRepository.porDia(LocalDate.now().withMonth(6));				
	}
		
	@GetMapping("/estatisticas/receita-por-categoria")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")	
	public List<LancamentoEstatisticaCategoria>porReceitaCategoria() {
	
		return this.lancamentoRepository.porReceitaCategoria(LocalDate.now());
	}

	@GetMapping("/estatisticas/despesa-por-categoria")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")	
	public List<LancamentoEstatisticaCategoria>porDespesaCategoria() {
	
		return this.lancamentoRepository.porDespesaCategoria(LocalDate.now());
	}	

	@PostMapping
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_LANCAMENTO') and #oauth2.hasScope('write')")
	public ResponseEntity<Lancamento> criar(@Valid @RequestBody Lancamento lancamento, HttpServletResponse response) {
		Lancamento lancamentoSalvo = lancamentoService.salvar(lancamento);
		publisher.publishEvent(new RecursoCriadoEvent(this, response, lancamentoSalvo.getCodigo()));		
		return ResponseEntity.status(HttpStatus.CREATED).body(lancamentoSalvo);
	}

	@ExceptionHandler({ PessoaInexistenteOuInativaException.class })
	public ResponseEntity<Object> handlePessoaInexistenteOuInativaException(PessoaInexistenteOuInativaException ex) {
		String mensagemUsuario = messageSource.getMessage("pessoa.inexistente-ou-inativa", null,
				LocaleContextHolder.getLocale());
		String mensagemDesenvolvedor = ex.toString();
		List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));
		return ResponseEntity.badRequest().body(erros);
	}
	
	@PutMapping("/{codigo}")
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_LANCAMENTO')")
	public ResponseEntity<Lancamento> atualizar(@PathVariable Long codigo, @Valid 
			@RequestBody Lancamento lancamento) {
		try {
			Lancamento lancamentoSalvo = lancamentoService.atualizar(codigo, lancamento);
			return ResponseEntity.ok(lancamentoSalvo);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.notFound().build();
		}
	}
		
	@GetMapping
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
	public Page<Lancamento> pesquisar(LancamentoFilter lancamentoFilter, Pageable pageable) {
		return lancamentoRepository.filtrar(lancamentoFilter, pageable);
	}

	@GetMapping(params = "resumo")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
	public Page<ResumoLancamento> resumir(LancamentoFilter lancamentoFilter, Pageable pageable) {
		return lancamentoRepository.resumir(lancamentoFilter, pageable);
	}

	@GetMapping("/{codigo}")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
	public ResponseEntity<Lancamento> buscarPeloCodigo(@PathVariable Long codigo) {
		Optional<Lancamento> lancamento = this.lancamentoRepository.findById(codigo);
		return lancamento.isPresent() ? ResponseEntity.ok(lancamento.get()) : ResponseEntity.notFound().build();
	}

	@DeleteMapping("/{codigo}")
	@PreAuthorize("hasAuthority('ROLE_REMOVER_LANCAMENTO') and #oauth2.hasScope('write')")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long codigo) {

		this.lancamentoRepository.deleteById(codigo);
	}
}