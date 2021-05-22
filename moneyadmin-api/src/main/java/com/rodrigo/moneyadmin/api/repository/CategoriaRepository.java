package com.rodrigo.moneyadmin.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rodrigo.moneyadmin.api.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

}
