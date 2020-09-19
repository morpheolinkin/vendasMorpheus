package com.morpheo.cursomc.dao.repository;

import com.morpheo.cursomc.domain.models.Cidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CidadeRepository extends JpaRepository<Cidade, Integer>{
}
