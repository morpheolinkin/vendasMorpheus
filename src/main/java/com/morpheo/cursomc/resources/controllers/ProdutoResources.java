package com.morpheo.cursomc.resources.controllers;

/*
Todas as classes que são controladoras REST
vão ficar no pacote RESOURCE
*/

import com.morpheo.cursomc.domain.models.Produto;
import com.morpheo.cursomc.dto.ProdutoDTO;
import com.morpheo.cursomc.resources.controllers.utils.URL;
import com.morpheo.cursomc.services.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value = "/produtos")
public class ProdutoResources {

    @Autowired
    private ProdutoService service;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Produto> find(@PathVariable Integer id) {
        Produto obj = service.find(id);

        return ResponseEntity.ok().body(obj);
    }

    @GetMapping
    public ResponseEntity<Page<ProdutoDTO>> findPage(
            @RequestParam(value = "nome", defaultValue = "") String nome,
            @RequestParam(value = "categoria", defaultValue = "") String categoria,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction,
            @RequestParam(value = "orderBy", defaultValue = "nome") String orderBy) {

        String nomeDecoded = URL.decodeParam(nome); //Para evitar que o usuário passe parâmetos com espaços
        List<Integer> ids = URL.decodeIntList(categoria); //String convertida em Integer, os parâmetros GET só aceitam String
        Page<Produto> list = service.search(nomeDecoded, ids, page, linesPerPage, direction, orderBy);
        Page<ProdutoDTO> dtoList = list.map(ProdutoDTO::new);

        return ResponseEntity.ok().body(dtoList);
    }
}
