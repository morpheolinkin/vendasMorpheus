package com.morpheo.cursomc.dao.repository;

import com.morpheo.cursomc.domain.models.Cliente;
import com.morpheo.cursomc.domain.models.Pedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

//Integer porque para o atributo indentificador (ID) de categoria é Integer
@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer> {
    @Transactional(readOnly = true)
    Page<Pedido> findByCliente(Cliente cliente, Pageable pageable);
}
