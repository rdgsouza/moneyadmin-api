package com.rodrigo.moneyadmin.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rodrigo.moneyadmin.api.model.Cidade;

public interface CidadeRepository extends JpaRepository<Cidade, Long>{

	
	//Caso queira usar anotação @Query para fazer a consulta por ordem alfabética
	
//	@Query( value = "SELECT * FROM cidade where codigo_estado = ?1 ORDER BY nome",
//	nativeQuery = true)
//	List<Cidade	> findByEstadoCodigo(@Param("estadoCodigo") Long estadoCodigo);
	
	//OBS= onde é usado: ?1 é onde vai ser passado o parametro no caso o codigo do estado que é
    //o Long
	
// Outra forma de trazer em ordem alfábetica é colocando OrderByNomeAsc no final da assinatura
// do metodo 
	List<Cidade> findByEstadoCodigoOrderByNomeAsc(Long estadoCodigo);

}
