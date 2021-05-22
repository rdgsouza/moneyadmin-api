package com.rodrigo.moneyadmin.api.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rodrigo.moneyadmin.api.model.Estado;
import com.rodrigo.moneyadmin.api.repository.EstadoRepository;
	
@RestController
@RequestMapping("/estados")
public class EstadoResource {

	@Autowired	
	private  EstadoRepository estadoRepository;  
	
	@GetMapping	
	@PreAuthorize("isAuthenticated()")
	public List<Estado> listar() {
			
		return estadoRepository.findAll(Sort.by(Sort.Direction.ASC, "nome"));
		//return estadoRepository.findAll();
	}
}
