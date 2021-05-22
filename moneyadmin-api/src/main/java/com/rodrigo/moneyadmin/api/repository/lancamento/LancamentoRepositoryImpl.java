
package com.rodrigo.moneyadmin.api.repository.lancamento;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import com.rodrigo.moneyadmin.api.dto.LancamentoEstatisticaCategoria;
import com.rodrigo.moneyadmin.api.dto.LancamentoEstatisticaDia;
import com.rodrigo.moneyadmin.api.dto.LancamentoEstatisticaPessoa;
import com.rodrigo.moneyadmin.api.model.Categoria_;
import com.rodrigo.moneyadmin.api.model.Lancamento;
import com.rodrigo.moneyadmin.api.model.Lancamento_;
import com.rodrigo.moneyadmin.api.model.Pessoa_;
import com.rodrigo.moneyadmin.api.model.TipoLancamento;
import com.rodrigo.moneyadmin.api.repository.filter.LancamentoFilter;
import com.rodrigo.moneyadmin.api.repository.projection.ResumoLancamento;

public class LancamentoRepositoryImpl implements LancamentoRepositoryQuery {

	@PersistenceContext
//  Vamos injetar um PersistenceContext na variavel manager abaixo
//  Um PersistenceContext (Contexto Persistente) é um local onde ficam armazenados
//  os objetos (entidades) que estão sendo manipulados pelo EntityManager corrente

	private EntityManager manager;
//	O EntityManager mantem as entidades que ele gerencia em um estado denominado de 
//	managed - sao essas as entidades que ele monitora. Essas entidades sao aquelas 
//	que ele cria por meio do metodo find, por meio de JPQL ou que recebe no metodo 
//	persist ou merge. Ele realiza cache dessas entidades, de forma que ler duas vezes
//	a mesma entidade do banco de dados nao produzira duas instancias diferentes - a mesma 
//	instancia e retornada na segunda leitura.
//	Entretanto, nem todas as instancias de entidades sao managed - a saber aquelas que 
//  acabaram de ser instanciadas e nao foram repassadas ainda ao EntityManager (estado new),
//	aquelas que foram excluidas por meio do metodo remove (estado removed) e aquelas que foram
//	desvinculadas do EntityManager por meio dos metodos clear() ou detach(Object)
//	(estado detached).

	@Override
	public List<LancamentoEstatisticaPessoa> porPessoaReceitaDespesaQuitadas(LocalDate inicio, LocalDate fim) {		
		 
		CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();

		CriteriaQuery<LancamentoEstatisticaPessoa> criteriaQuery = criteriaBuilder.
				createQuery(LancamentoEstatisticaPessoa.class);
		
		Root<Lancamento> root = criteriaQuery.from(Lancamento.class);

//		        //***Como usar o JPQL no Spring***
//				//Usando o JPQL para Pegar o nome de todas pessoas que tem lancamentos
//				ArrayList<String> nome = new ArrayList<>();		
//	            List<Lancamento> lanc = manager.createQuery(
//	            "select l from Lancamento as l where l.descricao = 'Multa'",Lancamento.class)
//				.getResultList();
//				//Podemos mandar retornar A lista de todos os lancamentos fazendo um select e retornando no
//				//com no getResult (Obs: Podemos fazer uma select mas refinado usando o createNativeQuery)
//				//DEpois de termos o resultado podemos iterar nessa lista pegando um campo específico como no 
//				//exemplo do for abaixo 
//				for(Lancamento lancamento : lanc) {
//				nome.add(lancamento.getPessoa().getNome());//Podemos adicionar todos os nomes refente
//				//a descricacao Multa em um Array de String nome
//				//para depois quando quiser usar o array de nomes
//				System.out.println(lancamento.getPessoa().getNome());
//				}
		
			criteriaQuery.select(criteriaBuilder.construct(LancamentoEstatisticaPessoa.class, 
					root.get(Lancamento_.tipo),
					root.get(Lancamento_.pessoa),
					criteriaBuilder.sum(root.get(Lancamento_.valor))));
		
		criteriaQuery.where(
				criteriaBuilder.greaterThanOrEqualTo(root.get(Lancamento_.dataVencimento), inicio),
				criteriaBuilder.lessThanOrEqualTo(root.get(Lancamento_.dataVencimento), fim),
				criteriaBuilder.isNotNull(root.get(Lancamento_.dataPagamento)));
			
		criteriaQuery.groupBy(
				root.get(Lancamento_.tipo), 
				root.get(Lancamento_.pessoa));
	          //root.get(Lancamento_.valor) --> vai agrupar pelos valores unicos de cada pessoa
		      //o normal e que agrupe com o valor total mas se coloca esse parametro
		      //cada pessoa vai vim com o seu valor de lancamento valore unicos


		TypedQuery<LancamentoEstatisticaPessoa> typedQuery = manager
				.createQuery(criteriaQuery
				.orderBy(criteriaBuilder.asc(root.get(Lancamento_.pessoa).get(Pessoa_.nome))));
		// OBS: orderBy(criteriaBuilder.asc(root.get(Lancamento_.pessoa).get(Pessoa_.nome))));
		// para ordernar o resultado de forma ascendente no caso os nomes das pessoas em ordem alfabetica
		// caso queira ordernar o tipo por ordem alfabetica e so substituir o 
		// orderBy(criteriaBuilder.asc(root.get(Lancamento_.pessoa).get(Pessoa_.nome)))); por
		// orderBy(criteriaBuilder.asc(root.get(Lancamento_.tipo))));

		
//******Caso queira iterar pela lista de LancamentoEstatisticaPesso
//		List<LancamentoEstatisticaPessoa> lancamentos = 
//				manager.createQuery(criteriaQuery).getResultList();
//			
//		for(LancamentoEstatisticaPessoa lancamento : lancamentos) {
//		System.out.println(lancamento.getPessoa().getNome());
//	   	System.out.println(lancamento.getTotal());
//		}
		
		return typedQuery.getResultList();
	}
	
	
	@Override
	public List<LancamentoEstatisticaPessoa> porPessoaReceitaDespesaEmAberto(LocalDate inicio, LocalDate fim) {
		CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();

		CriteriaQuery<LancamentoEstatisticaPessoa> criteriaQuery = criteriaBuilder.
				createQuery(LancamentoEstatisticaPessoa.class);
		
		Root<Lancamento> root = criteriaQuery.from(Lancamento.class);

//		        //***Como usar o JPQL no Spring***
//				//Usando o JPQL para Pegar o nome de todas pessoas que tem lancamentos
//				ArrayList<String> nome = new ArrayList<>();		
//	            List<Lancamento> lanc = manager.createQuery(
//	            "select l from Lancamento as l where l.descricao = 'Multa'",Lancamento.class)
//				.getResultList();
//				//Podemos mandar retornar A lista de todos os lancamentos fazendo um select e retornando no
//				//com no getResult (Obs: Podemos fazer uma select mas refinado usando o createNativeQuery)
//				//DEpois de termos o resultado podemos iterar nessa lista pegando um campo específico como no 
//				//exemplo do for abaixo 
//				for(Lancamento lancamento : lanc) {
//				nome.add(lancamento.getPessoa().getNome());//Podemos adicionar todos os nomes refente
//				//a descricacao Multa em um Array de String nome
//				//para depois quando quiser usar o array de nomes
//				System.out.println(lancamento.getPessoa().getNome());
//				}
		
			criteriaQuery.select(criteriaBuilder.construct(LancamentoEstatisticaPessoa.class, 
					root.get(Lancamento_.tipo),
					root.get(Lancamento_.pessoa),
					criteriaBuilder.sum(root.get(Lancamento_.valor))));
		
		criteriaQuery.where(
				criteriaBuilder.greaterThanOrEqualTo(root.get(Lancamento_.dataVencimento), inicio),
				criteriaBuilder.lessThanOrEqualTo(root.get(Lancamento_.dataVencimento), fim),
				criteriaBuilder.isNull(root.get(Lancamento_.dataPagamento)));
			
		criteriaQuery.groupBy(
				root.get(Lancamento_.tipo), 
				root.get(Lancamento_.pessoa));
	          //root.get(Lancamento_.valor) --> vai agrupar pelos valores unicos de cada pessoa
		      //o normal e que agrupe com o valor total mas se coloca esse parametro
		      //cada pessoa vai vim com o seu valor de lancamento valores unicos


		TypedQuery<LancamentoEstatisticaPessoa> typedQuery = manager
				.createQuery(criteriaQuery
				.orderBy(criteriaBuilder.asc(root.get(Lancamento_.pessoa).get(Pessoa_.nome))));
		// OBS: orderBy(criteriaBuilder.asc(root.get(Lancamento_.pessoa).get(Pessoa_.nome))));
		// para ordernar o resultado de forma ascendente no caso os nomes das pessoas em ordem alfabetica
		// caso queira ordernar o tipo por ordem alfabetica e so substituir o 
		// orderBy(criteriaBuilder.asc(root.get(Lancamento_.pessoa).get(Pessoa_.nome)))); por
		// orderBy(criteriaBuilder.asc(root.get(Lancamento_.tipo))));

		
//******Caso queira iterar pela lista de LancamentoEstatisticaPesso
//		List<LancamentoEstatisticaPessoa> lancamentos = 
//				manager.createQuery(criteriaQuery).getResultList();
//			
//		for(LancamentoEstatisticaPessoa lancamento : lancamentos) {
//		System.out.println(lancamento.getPessoa().getNome());
//	   	System.out.println(lancamento.getTotal());
//		}
		
		return typedQuery.getResultList();
	}

	
	@Override
	public List<LancamentoEstatisticaDia> porDia(LocalDate mesReferencia) {
	     CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
	     
	     CriteriaQuery<LancamentoEstatisticaDia> criteriaQuery = criteriaBuilder.
	    		 createQuery(LancamentoEstatisticaDia.class);	
	     Root<Lancamento> root = criteriaQuery.from(Lancamento.class);	
	     
	     criteriaQuery.select(criteriaBuilder.construct(LancamentoEstatisticaDia.class,
	    		 root.get(Lancamento_.tipo),
	    		 root.get(Lancamento_.dataVencimento),
	    		 criteriaBuilder.sum(root.get(Lancamento_.valor))));
	     
	     LocalDate  primeiroDia = mesReferencia.withDayOfMonth(1);
	     LocalDate ultimoDia = mesReferencia.withDayOfMonth(mesReferencia.lengthOfMonth());
	     
	     criteriaQuery.where(
	             criteriaBuilder.greaterThanOrEqualTo(root.get(Lancamento_.dataVencimento), //A data de vencimento tem que ser maior ou igual ao dia primeiro
	            		 primeiroDia),
	             criteriaBuilder.lessThanOrEqualTo(root.get(Lancamento_.dataVencimento),  //A data de vencimento tem que ser menor ou igual ao ultima dia
	            		 ultimoDia));
    
	     criteriaQuery.groupBy(root.get(Lancamento_.tipo),
	    		 root.get(Lancamento_.dataVencimento));
	     
	     TypedQuery<LancamentoEstatisticaDia> typedQuery =
	    		 manager.createQuery(criteriaQuery);
	     	
		return typedQuery.getResultList();
	}	
	
	  @Override
	    public List<LancamentoEstatisticaCategoria> porReceitaCategoria(LocalDate mesReferencia) {
	        CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();

	        CriteriaQuery<LancamentoEstatisticaCategoria> criteriaQuery = criteriaBuilder.createQuery(LancamentoEstatisticaCategoria.class);

	        Root<Lancamento> root = criteriaQuery.from(Lancamento.class);

	        criteriaQuery.select(criteriaBuilder.construct(LancamentoEstatisticaCategoria.class, root.get(Lancamento_.categoria),
	                criteriaBuilder.sum(root.get(Lancamento_.valor))));

	        LocalDate primeiroDia = mesReferencia.withDayOfMonth(1);
	        LocalDate ultimoDia = mesReferencia.withDayOfMonth(mesReferencia.lengthOfMonth());

	        criteriaQuery.where(
	                criteriaBuilder.equal(root.get(Lancamento_.tipo), TipoLancamento.RECEITA), // nesta linha defino que o tipo de lançamento
	                criteriaBuilder.greaterThanOrEqualTo(root.get(Lancamento_.dataVencimento), primeiroDia),
	                criteriaBuilder.lessThanOrEqualTo(root.get(Lancamento_.dataVencimento), ultimoDia));
// Observe que a primeira condição do WHERE define o tipo de lançamento desejado. Se quiser tornar a 
// consulta mais dinâmica ainda pode definir o tipo de lançamento por parâmetro no método, obrigando o 
// cliente a passar o tipo como parâmetro na consulta ma caso não queira você mesmo pode escolher e
// adcionar diretamente no codigo a sua preferencia de retorno de lançamento, que no caso escolhemos so os
// lançamentos que contem o parametro RECEITA.
	        
	        criteriaQuery.groupBy(root.get(Lancamento_.categoria));

	        TypedQuery<LancamentoEstatisticaCategoria> typedQuery = manager.createQuery(criteriaQuery);

	        return typedQuery.getResultList();
	    }
	  
	  @Override
	    public List<LancamentoEstatisticaCategoria> porDespesaCategoria(LocalDate mesReferencia) {
	        CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();

	        CriteriaQuery<LancamentoEstatisticaCategoria> criteriaQuery = criteriaBuilder.createQuery(LancamentoEstatisticaCategoria.class);

	        Root<Lancamento> root = criteriaQuery.from(Lancamento.class);

	        criteriaQuery.select(criteriaBuilder.construct(LancamentoEstatisticaCategoria.class, root.get(Lancamento_.categoria),
	                criteriaBuilder.sum(root.get(Lancamento_.valor))));

	        LocalDate primeiroDia = mesReferencia.withDayOfMonth(1);
	        LocalDate ultimoDia = mesReferencia.withDayOfMonth(mesReferencia.lengthOfMonth());

	        criteriaQuery.where(
	                criteriaBuilder.equal(root.get(Lancamento_.tipo), TipoLancamento.DESPESA), // nesta linha defino que o tipo de lançamento
	                criteriaBuilder.greaterThanOrEqualTo(root.get(Lancamento_.dataVencimento), primeiroDia),
	                criteriaBuilder.lessThanOrEqualTo(root.get(Lancamento_.dataVencimento), ultimoDia));
//Observe que a primeira condição do WHERE define o tipo de lançamento desejado. Se quiser tornar a 
//consulta mais dinâmica ainda pode definir o tipo de lançamento por parâmetro no método, obrigando o 
//cliente a passar o tipo como parâmetro na consulta ma caso não queira você mesmo pode escolher e
//adcionar diretamente no codigo a sua preferencia de retorno de lançamento, que no caso escolhemos so os
//lançamentos que contem o parametro DESPESA.
	        
	        criteriaQuery.groupBy(root.get(Lancamento_.categoria));

	        TypedQuery<LancamentoEstatisticaCategoria> typedQuery = manager.createQuery(criteriaQuery);

	        return typedQuery.getResultList();
	    }
	
	@Override
	public Page<Lancamento> filtrar(LancamentoFilter lancamentoFilter, Pageable pageable) {

		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Lancamento> criteria = builder.createQuery(Lancamento.class);
		Root<Lancamento> root = criteria.from(Lancamento.class);

		Predicate[] predicates = criarRestricoes(lancamentoFilter, builder, root);
		// passando os predicates para um array de predicates

		criteria.where(predicates);// Pegando todos os predicates que estao no array de predicates
		
		TypedQuery<Lancamento> query = manager.createQuery(criteria);
		// O createQuery e usado para construcao das consultas digitadas porque a
		// sequencia de consultas digitadas pode ser criada dinamicamente em tempo de
		// execucao entao vamos executar as criterias e passar para o atributo query
		// TypedQuery - Interface usada para controlar a execução de consultas
		// digitadas.
		// como temos as consultas feitas em uma lista do tipo TypeQuery
		// podemos fazer o controle como por exemplo trazer aquilo que foi passado pelo usuario
		// no peageabe se o usuario digitou na string query pagina 1 e 3 para quantide de pagina
		// e elementos temos que fazer essa retricacao em cima dessa query
		// para isso vamos criar o metodo abaixo
		
		adicionarRestricoesDePaginacao(query, pageable);
		//Vamos passar o query como parametro pois vamos usalo para trabalhar em cima dessa
		//lista dos predicates que foram executados e que agora estao nessa lista do query
		//e o pageable para pegar as informacoes de paginacao passadas pelo usuario
		
		return new PageImpl<>(query.getResultList(), pageable, total(lancamentoFilter));
		// Por fim retornamos o PageImpl com os parametros para paginacao
		// query.getResultList() e o resultado da lista da variavel query que no caso e o
		// parametro de conteudo que 
		// tem a infomacao que precisamos como a quantidade de elementos a ser paginados
		// o pageable sao os numeros passados na query string para ser usado como base
		// para o resultado da paginacao.
		// E o total e para informar a quantidade total de
		// elementos na pagina
		// Por fim isso tudo e colocado no parametro do
		// construtor new PageImpl<>
		// que pos sua vez pega essas informacoes e faz a paginacao do nosso filtro
	}

	@Override
	public Page<ResumoLancamento> resumir(LancamentoFilter lancamentoFilter, Pageable pageable) {
//      A API de Criteria comumente é utilizada para fazer filtros dinamicos
//		Porem, a grande vantagem da Criteria do JPA e nos impedir de criar consultas 
//		com erros de sintaxe, atraves de checagem em tempo de compilacao. Ao inves de definir
//		as consultas em uma String, temos uma definicao programatica, com codigo Java.
//		Para utilizar a API de Criteria, o primeiro passo e obter um objeto do tipo CriteriaBuilder
//      a partir do EntityManager:

		CriteriaBuilder builder = manager.getCriteriaBuilder();
//      CriteriaBuilder e usado para construir consultas de criterios, selecoes compostas,
//		expressoes, predicados, pedidos.
//		E utilizamos o CriteriaBuilder para criar um objeto do CriteriaQuery,
//		passando o retorno esperado da nossa consulta

		CriteriaQuery<ResumoLancamento> criteria = builder.createQuery(ResumoLancamento.class);
//      OBS: So que o ResumoLancamento nao e uma entidade entao 
//      usamos o Root para dizer o tipo raiz da consulta que de fato vai ser uma entidade 
//		que e a entidade Lancamento.
//		Apartir dai podemos pegar as informacoes dessa entidade pois ela estar
//		relacionada

		Root<Lancamento> root = criteria.from(Lancamento.class);
//O Root recebera como parametro entidade raiz que sera pesquisada
//o JPA precisara saber quais serao os atributos que serao 
//usados para consulta e os atributos tem que ser de entidades que foram mapeadas no nosso projeto. Como 
//a classe ResumoLancamento nao foi mapeada entao temos que passar a Classe Lancamento pois ele pegara esses
//atributos que estao mapeados e com isso ele podera usar para fazer por ex um futuro select com
//o criteria.select
//o criteria.from diz que a pesquisa sera feita a partida classe Lancamento.class

		criteria.select(builder.construct(ResumoLancamento.class, root.get(Lancamento_.codigo),
				root.get(Lancamento_.descricao), root.get(Lancamento_.dataVencimento),
				root.get(Lancamento_.dataPagamento), root.get(Lancamento_.valor), root.get(Lancamento_.tipo),
				root.get(Lancamento_.categoria).get(Categoria_.nome), root.get(Lancamento_.pessoa).get(Pessoa_.nome)));
// Precisamos fazer um select de um objeto do tipo ResumoLancamento com seus atributos
// Quando fazemos o um select com o criteria ele retorna um objeto
// Para retornar objetos definidos como (nao entidades - que no caso e o
// ResumoLancamento) como resultados da consulta, podemos usar CriteriaBuilder.construct() acima
// e dentro do metodo construtor vamaos fazer ele buscar os patametros no banco apartir da entidade
// que passamos para o root lembra que falamos que pegariamos os atributos da 
// entidade mapeada que no caso e Lancamento pois passamos essa classe como parametro no diamante do root.
// o root nos dara os atributos que necessitamos para passar ao construtor da classe
// ResumoLancamento para depois ser retornado um objeto ResumoLancamento com os atributos que
// foram passados atraves do root no parametro do construtor dessa classe. 		
// Pois o objetivo retornar um objeto do tipo ResumoLancamento com atributos
// resumidos da classe Lancamento
		
//Agora que temos o objeto do tipo ResumoLancamento com os atributos podemos criar 
//as retricoes para o filtro
		
		Predicate[] predicates = criarRestricoes(lancamentoFilter, builder, root);//vai retorna
		// passando os predicates para um array de predicates
        
    	criteria.where(predicates);// Pegando todos os predicates que foram retornados pelo metodo criarRestricoes
	// e passado para o array [predicates] acima.	
    	
    	criteria.orderBy(builder.asc(root.get(Lancamento_.pessoa).get(Pessoa_.nome)));	
   
        TypedQuery<ResumoLancamento> query = manager.createQuery(criteria);	
    	// O createQuery e usado para construcao das consultas digitadas e esta consultas digitadas estão na 
        //(criteria) e que anteriormente estavam no array de predicate que foi passado para o criteria atraves do
        //criteria.where(presicates);. 
        //Porque a sequencia de consultas digitadas pode ser criada dinamicamente em tempo de
		// execucao entao vamos executar as criterias e passar para o atributo query
		// TypedQuery - Interface usada para controlar a execução de consultas
		// digitadas.
 //***#****  como temos as consultas feitas em uma lista do tipo TypeQuery
		// podemos fazer o controle de paginacao.
        //Por exemplo podemos trazer aquilo que foi passado pelo usuario na string query na requisicao no parametro
		// (peageabe) se o usuario digitou o numero da pagina 1 e para quantidade
        //de elementos por pagina o numero 3 entao temos que fazer essa restrincao em cima dessa (query) que acabamos
        //de criar para isso vamos criar o metodo adicionarRestricoesDePaginacao.
	        
        adicionarRestricoesDePaginacao(query, pageable);
       //Adiciona restricoes de paginacao com base no resultado que foi gerado na query e passado no pageable
        
		return new PageImpl<>(query.getResultList(), pageable, total(lancamentoFilter));
		// Por fim retornamos o PageImpl com os parametros para paginacao
		// query.getResultList() e o resultado da lista da variavel query que no caso e o
		// parametro de conteudo que 
		// tem a infomacao que precisamos como a quantidade de elementos a ser paginados
		// o pageable sao os numeros passados na query string para ser usado como base
		// para o resultado da paginacao.
		// E o total e para informar a quantidade total de
		// elementos na pagina
		// Por fim isso tudo e colocado no parametro do
		// construtor new PageImpl<>
		// que pos sua vez pega essas informacoes e faz a paginacao do nosso filtro
		
	}	

	private Predicate[] criarRestricoes(LancamentoFilter lancamentoFilter, 
			CriteriaBuilder builder, Root<Lancamento> root) {

		List<Predicate> predicates = new ArrayList<>();

		if (!StringUtils.isEmpty(lancamentoFilter.getDescricao())) {
			predicates.add(builder.like(builder.lower(root.get(Lancamento_.descricao)),
					"%" + lancamentoFilter.getDescricao().toLowerCase() + "%"));
		}
		//OBS***: Quando adicionamos um predicate no ArrayList de predicate estamos adicionando
		//o seguinte -> builder.like(builder.lower(root.get(Lancamento_.descricao)),
		//"%" + lancamentoFilter.getDescricao().toLowerCase() + "%"));
		//ele vai na verdaed armazenar essas intruções para eventualmente executar cada
		//predicate que no caso seram executados dessa forma exemplo:

		//criteria.where(predicates); primeiro pelo criteria para pegar todos o predicates 
		//que foram armazenados no array de predicates
        //TypedQuery<ResumoLancamento> query = manager.createQuery(criteria); segundo criando
		//a consulta com base nas criterias e em seguida passada para uma variavel de 
		//instancia query do tipo TypedQuery dessa forma irar ter todos os resultado para uma
		//eventual paginacao.
 

		if (lancamentoFilter.getDataVencimentoDe() != null) {
			predicates.add(builder.greaterThanOrEqualTo(root.get(Lancamento_.dataVencimento),
					lancamentoFilter.getDataVencimentoDe()));

			// greaterThanOrEqualTo é a restrição (maior que ou igual a >=
//  A constraint greaterThanOrEqualTo que sera adicionada como um predicate foi chamado para testar
//  se o primeiro argumento e maior ou igual ao segundo.
//  quadno o predicate for executado o primerio argumento vai trazer a ultima data de vencimento
//  que o banco de dados tem que e a data de vencimento do dia 20/02/2020
//  JPA vai colocar essa data do primeiro argumento para comparacao
//  em seguida ele vai pegar o segundo argumento que e a data de vencimento passado
//  pelo usuario com o metodo lancamentoFilter.getDataVencimentoDe() ai vamos supor
//  que o usuario passou a
//  data 10/04/2017 entao o JPA vai pegar o primerio argumento 20/02/2020 e
//  comparar com o segundo que e 10/04/2017 sendo o primeiro argumento maior que o segundo 
//	o JPA vai pegar o segundo argumento que e o menor e ir ate o maior argumento que e o maior
//  e entre o comeco do segundo argumento que e a menor data e ate o primeiro argumento
//	que a maior data ele vai trazer todas as datas entre o comeco de um e fim do outro 
//	entao vai ficar de 10/04/2017 a 20/02/2020	>= as datas entre esse periodo serao mostradas

		}

		if (lancamentoFilter.getDataVencimentoAte() != null) {
			predicates.add(builder.lessThanOrEqualTo(root.get(Lancamento_.dataVencimento),
					lancamentoFilter.getDataVencimentoAte()));
		}

		// lessThanOrEqualTo e a restricao (menor que ou igual a) <=

		// É o inverso do que falamos acima quando predicate for executado ele vai do
		// menor parametro ate o maior parametro
		// caso vc passe essa constraint depois da constraint lessThanOrEqualTo o que
		// ele vai fazer e da continuidade se a data da constrains greaterThanOrEqualTo
		// foi ate 20/02/2020
		// e agora que vc tem uma nova constraint lessThanOrEqualTo significa que ele
		// vai da menor data
		// ate a maior data que sera estipulada pelo usuario entao se o usuario colocou
		// a data
		// 10/06/2017 entao ele vai da menor data de vencimento ate essa nova data
		// e a menor data de vencimento nao e mais a que estar no banco de dados e sim a
		// que foi
		// estipulada na constrainst anterior a greaterThanOrEqualTo que no caso foi a
		// data 10/04/2017
		// essa nova data sera usada como data inicial
		// entao fica 10/04/2017 <= 10/06/2017

		if (lancamentoFilter.getPessoa() != null && lancamentoFilter.getPessoa().getNome() != null) {

			predicates.add(builder.like(builder.lower(root.get(Lancamento_.pessoa).get(Pessoa_.nome)),
					"%" + lancamentoFilter.getPessoa().getNome().toLowerCase() + "%"));
		}

		// Obs: Aqui em cima vamos filtrar pelo nome da pessoa eu coloquei
		// if (lancamentoFilter.getPessoa() != null &&
		// lancamentoFilter.getPessoa().getNome() != null)
		// para adicionar a listar so se objeto pessoa realamente nao for null e get
		// nome não for nulo porque para verificar o nome da pessoa e nulo temos que ve
		// se o
		// objeto pessoa realmente nao estar nulo antes de pegar o nome da pessoa entao
		// um tem que chamar o
		// o outro.

		// Obs2: no endereco abaixo fala como foi criado os MetaModels no nosso projeto
		// assista a aula:
		// https://www.loom.com/share/ae1338a584904c74a1dc5d78cdf47c09 se não estiver
		// mas disponível nesse endereco
		// Eu gravei a aula e salvei no hd externo na pasta Angula e Spring - Assuntos
		// do Curso Fullstack Angular Spring- Gerando MetaModels com Maven.mp4

		return predicates.toArray(new Predicate[predicates.size()]);
		// retorna os predicates em um array e inclui seu tamanho como parametro do
		// toArray
	}

	private void adicionarRestricoesDePaginacao(TypedQuery<?> query, Pageable pageable) {

// OBS: Ao escrever nomeDaClasse<?>, você está declarando um Classe que vai aceitar um objeto
// que pode ser de qualquer tipo ou seja a classe não estipula o tipo de objeto que vai ser
// passado como parmetro e altomaticamente ela retorna o objeto que foi passado
		
		int paginaAtual = pageable.getPageNumber();
		int totalRegistrosPorPagina = pageable.getPageSize();
		int primeiroRegistroDaPagina = paginaAtual * totalRegistrosPorPagina;

		//		Os metodos setFirstResult e setMaxResults permitem definir um resultado
		
		query.setFirstResult(primeiroRegistroDaPagina);
		// Define a posição do primeiro resultado a ser recuperado
        //posição do primeiro resultado, numerada de 0
		//ex: vamos supor que o atributo query tem 12 elementos 
		//e vc colocou na query string para paginacao a pagina 2 
		//e a qtd de elementos por pagina 3 
		//entao multiplicamos 2*3 que e = 6
		//entao esse 6 e ate onde vai a quantidade de elementos para ser exibido no resultado
		//e nao mas os 12 elementos 
		//Entao vai de 0 ate 6 e nao de 0 ate 12
		//E onde cai os 6 vai ser a pagina 2 so que nao com os 6 elementos
	
		query.setMaxResults(totalRegistrosPorPagina);
		//Quando for feita a paginacao teremos 6 elementos e ja que passamos o 3 para
		//a qtd de elementos vamos passar ele no parametro do
		//query.setResults(totalRegistrosPorPagina)
		//sendo assim teremos ate 6 elementos sendo consultados com maximo de resultados
		//de 3 elementos por consulta
		//entao o resultado vai ser exibido de 3 em 3 elementos
		
		//Essas informacoes vao ser uteis para o retorno do contrutor pageImpl
		//pois essas informacoes vao ser 1 dos parametros desse construtor
		//e chamamos esse parametro de conteudo pois ai que estao as informacoes
		//para a classe PageImpl retornar a paginacao
	}

	private Long total(LancamentoFilter lancamentoFilter) {

		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);// retornara um tipo long
		Root<Lancamento> root = criteria.from(Lancamento.class);

		Predicate[] predicates = criarRestricoes(lancamentoFilter, builder, root);

		criteria.where(predicates);// Em vez de termos os elementos vamos ter a quantidade de elementos pois o tipo
									// do criteria e Long entao ele vai pegar a quantidade de elementos e nao os
									// elementos

		criteria.select(builder.count(root));
		// Vamos selecionar os criterias que sao a quantidade de elementos
		// Como selecionamos a quantidade de elementos
		// Entao podemos contar a quantidade de elementos com o count do builder e
		// passamos o parametro root para informa a que classe essa contagem representa
		// para no fim ele retornar um total de atributos

		return manager.createQuery(criteria).getSingleResult();// Retornamos o total de atributos aqui
	}
	
}
