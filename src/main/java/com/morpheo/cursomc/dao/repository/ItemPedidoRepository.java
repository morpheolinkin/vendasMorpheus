package com.morpheo.cursomc.dao.repository;

import com.morpheo.cursomc.domain.models.ItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//Integer porque para o atributo indentificador (ID) de categoria é Integer
@Repository
public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Integer> {

}
