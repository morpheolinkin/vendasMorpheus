package com.morpheo.cursomc.dao.repository;

import com.morpheo.cursomc.domain.models.Categoria;
import com.morpheo.cursomc.domain.models.Produto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

//Integer porque para o atributo indentificador (ID) de Produto é Integer
@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Integer> {

    /*É possível usar o padrão de nomes do Spring Data, assim
    * o @Query não precisaria usar. É só combinar termos de acordo com a documentação
    * do Spring Data. Vou mudar para o padrão de nome que não
    * precisaria usar essa query abaixo (nome antigo do método era search).
    * Pela regra, se quiser deixar a query, ela tem prioridade sobre o padrão de nome, vai sobrepor.
    *
    * https://docs.spring.io/spring-data/jpa/docs/1.6.0.RELEASE/reference/html/jpa.repositories.html#jpa.query-methods*/

    @Transactional
    /*@Query("SELECT DISTINCT obj " +
            "FROM Produto obj " +
            "INNER JOIN obj.categorias cat " +
            "WHERE " +
                "obj.nome LIKE %:nome% AND " +
                "cat IN :categorias")*/
    Page<Produto> findDistinctByNomeContainingAndCategoriasIn(
            String nome, List<Categoria> categorias, Pageable pageRequest);
}
