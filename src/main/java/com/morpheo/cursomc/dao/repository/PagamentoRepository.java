package com.morpheo.cursomc.dao.repository;

import com.morpheo.cursomc.domain.models.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//Integer porque para o atributo indentificador (ID) de categoria é Integer
@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, Integer> {

}
