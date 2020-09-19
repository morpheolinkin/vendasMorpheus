package com.morpheo.cursomc.dao.repository;

import com.morpheo.cursomc.domain.models.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//Integer porque para o atributo indentificador (ID) de categoria é Integer
@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {

}
