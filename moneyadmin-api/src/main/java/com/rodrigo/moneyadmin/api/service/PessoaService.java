package com.rodrigo.moneyadmin.api.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.rodrigo.moneyadmin.api.model.Pessoa;
import com.rodrigo.moneyadmin.api.repository.PessoaRepository;

@Service
public class PessoaService {

	@Autowired
	private PessoaRepository pessoaRepository;

	public Pessoa salvar(Pessoa pessoa) {
		pessoa.getContatos().forEach(c -> c.setPessoa(pessoa));
		return pessoaRepository.save(pessoa);
	}
	 
	public Pessoa atualizar(Long codigo, Pessoa pessoa) {
		Pessoa pessoaSalva = this.pessoaRepository.findById(codigo)
				.orElseThrow(() -> new EmptyResultDataAccessException(1));
		
		pessoaSalva.getContatos().clear();
		pessoaSalva.getContatos().addAll(pessoa.getContatos());
		pessoaSalva.getContatos().forEach(c -> c.setPessoa(pessoaSalva));

//O findById nos retorna um optional.
//Neste caso, utilizamos o método orElseThrow(...) de Optional, o que significa que caso o 
//Optional obtido pela consulta esteja vazio, iremos lançar uma exceção.		

//O que significa o () e o -> do metodo .orElseThrow	
//"()" - Indicam uma função anônima, é como se você declarasse um método que não recebe 
//nenhum parâmetro, por isso os parênteses vazios.
//"->" - A seta indica o início da implementação de uma função lambda.
//"new EmptyResultDataAccessException(1)" - Como temos uma linha apenas nessa função, 
//o conteúdo dela, é o retorno da função.
//Logo, podemos dizer que essa função é um método que não recebe argumentos e que retorna 
//uma instância de EmptyResultDataAccessException.
//Temos um artigo bem legal sobre lambdas, acho que 
//vai ajudar bastante: https://blog.algaworks.com/introducao-ao-lambda-do-java-8/
			
//OBS: O EmptyResultDataAccessException(1)) é a exceção de acesso a dados é lançada 
// quando se esperava algum resultado e passando no parametro do construtor o 1
// significa que esperavamos pelo menos um resultado de retorno.
						
	
		BeanUtils.copyProperties(pessoa, pessoaSalva, "codigo", "contatos");
    //O BeanUtils nos ajudar a copiar as propiedades de um o objeto para outro objeto
	//como estamos recebendo novos dados no paramentro pessoa do metodo atualizar precisamos
	//sobrepor essas infomacoes na pessoa que foi pesquisada na nossa api pelo parametro codigo	
	//No primerio parametro 'pessoa' e de onde queremos copiar o segundo para qual objeto sera
	//copiado e o terceiro o que queremos ignorar.
	//ignoramos o codigo porque o que estamos fazendo e uma atualizacao e nao precisamos modificar o
	//o codigo e alem de tudo quando enviamos o objeto pessoa para ser atualizado o codigo vem
	//como null por nao incluimos ele no objeto pessoa usamos o codigo apenas para pegar a pessoa
	//referente ao codigo e o objeto pessoa que estar no parametro pegamos seus novos dados para
	//atualizacao.		
	//OBS2: Poderiamos nao usar a classe BeanUtils e fazer a mudanca de cada proidade uma a uma
    //mas essa classe utiliaria que o SRPING prover nos ajuda bastante!		
		
		return this.pessoaRepository.save(pessoaSalva);
	}

	public void atualizarPropriedadeAtivo(Long codigo, Boolean ativo) {
		Pessoa pessoaSalva = buscarPessoaPeloCodigo(codigo);
		pessoaSalva.setAtivo(ativo);
		pessoaRepository.save(pessoaSalva);
	}

	public Pessoa buscarPessoaPeloCodigo(Long codigo) {

		Pessoa pessoaSalva = this.pessoaRepository.findById(codigo)
				.orElseThrow(() -> new EmptyResultDataAccessException(1));

		return pessoaSalva;
	}

}