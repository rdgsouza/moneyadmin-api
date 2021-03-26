package com.rodrigo.moneyadmin.api.repository.lancamento;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.rodrigo.moneyadmin.api.dto.LancamentoEstatisticaCategoria;
import com.rodrigo.moneyadmin.api.dto.LancamentoEstatisticaDia;
import com.rodrigo.moneyadmin.api.dto.LancamentoEstatisticaPessoa;
import com.rodrigo.moneyadmin.api.model.Lancamento;
import com.rodrigo.moneyadmin.api.repository.filter.LancamentoFilter;
import com.rodrigo.moneyadmin.api.repository.projection.ResumoLancamento;

public interface LancamentoRepositoryQuery {

	public Page<Lancamento> filtrar(LancamentoFilter lancamentoFilter, Pageable pageable);
	public Page<ResumoLancamento> resumir(LancamentoFilter lancamentoFilter, Pageable pageable);
	public List<LancamentoEstatisticaCategoria> porReceitaCategoria(LocalDate mesReferencia);
	public List<LancamentoEstatisticaCategoria> porDespesaCategoria(LocalDate mesReferencia);
	public List<LancamentoEstatisticaDia> porDia(LocalDate mesReferencia);
	public List<LancamentoEstatisticaPessoa> porPessoaReceitaDespesaQuitadas(LocalDate inicio, LocalDate fim);
	public List<LancamentoEstatisticaPessoa> porPessoaReceitaDespesaEmAberto(LocalDate inicio, LocalDate fim);


//Não temos como fazer uma implementacao diretamente na interface LancamentoRepository 
//que extende do JpaRepository entao o Spring nos da uma forma de fazer implementacao de
//metodos.
//criamos essa interface LancamentoRepositoryQuery com o mesmo nome da interface
//LancamentoRepository so que no final
//colocamos o Query - LancamentoRepositoryQuery para poder o springframework.data.jpa conseguir
//entender que o que vamos fazer a partir de agora e uma implementacao.
//A interface LancamentoRepository exetende o JpaRepository entao ela se limita aos seus metodos
//entao criando essa nova interface para poder adicionar metodos personalizados que nao tem na
//interface LancamentoRepository pra isso e obrigatorio que seja o mesmo nome mudando apenas no
//final para Query.
//E ao criar essa interface vamos fazer com que a insterface LancamentoRepository nao somente 
//extenda a interface JpaRepository quanto tbm essa nova interface LancamentoRepositoryQuery	
//E quando na classe LancamentoResource	for enjetado no atributo de instancia lancamentoRepository: 
//	@Autowired 
//	private LancamentoRepository lancamentoRepository;
// vamos poder ter acesso essa nova interface LancamentoRepositoryQuery junto com seus
//novos metodos.
// E na classe LancamentoRepositoryImpl vamos fazer a implementacao desses metodos pois essa
// classe LancamentoRepositoryImpl vai herdar essa interface LancamentoRepositoryQuery
//Obs: A classe LancamentoRepositoryImpl que ira herdar e fazer a implementacao dos metodos da 
// interface LancamentoRepositoryQuery tbm tem que ter o mesmo nome inicial da interface
// LancamentoRepository e com o final Impl para poder o springframework.data.jpa conseguir
//entender que o continuamos fazendo e uma implementacao.
//Ao termino dessa implementacao vamos ter novos metodos disponiveis no 
//nosso repositoy LancamentoRepository
}
