package com.morpheo.cursomc.services;

import com.morpheo.cursomc.domain.models.Categoria;
import com.morpheo.cursomc.domain.models.Produto;
import com.morpheo.cursomc.dao.repository.CategoriaRepository;
import com.morpheo.cursomc.dao.repository.ProdutoRepository;
import com.morpheo.cursomc.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static org.springframework.data.domain.Sort.Direction.valueOf;

@Service
public class ProdutoService {

    @Autowired
    CategoriaRepository categoriaRepository;
    @Autowired
    private ProdutoRepository produtoRepository;

    public Produto find(Integer id) {
        Optional<Produto> obj = produtoRepository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException(
                "Objeto não encontrado! Id: " + id + ", Tipo: " + Produto.class.getName()));
    }

    public Page<Produto> search(String nome, List<Integer> ids,
                                Integer page, Integer linesPerPage,
                                String direction, String orderBy )
    {
        PageRequest pageRequest = PageRequest.of(page, linesPerPage, valueOf(direction), orderBy);
        List<Categoria> categorias = categoriaRepository.findAllById(ids);

        return produtoRepository.findDistinctByNomeContainingAndCategoriasIn(nome, categorias, pageRequest);

    }

}
