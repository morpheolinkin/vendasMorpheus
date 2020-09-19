package com.morpheo.cursomc.resources.controllers;

/*
Todas as classes que são controladoras REST
vão ficar no pacote RESOURCE
*/

import com.morpheo.cursomc.domain.models.Categoria;
import com.morpheo.cursomc.domain.models.Pedido;
import com.morpheo.cursomc.dto.CategoriaDTO;
import com.morpheo.cursomc.services.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;


@RestController
@RequestMapping(value = "/pedidos")
public class PedidoResources {

    @Autowired
    private PedidoService service;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Pedido> find(@PathVariable Integer id) {
        Pedido obj = service.find(id);

        return ResponseEntity.ok().body(obj);
    }

    @PostMapping
    public ResponseEntity<Void> insert(@Valid @RequestBody Pedido obj) { //Faz o Json ser convertido para o objeto Java
        obj = service.insert(obj);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(obj.getId())
                .toUri();
        return ResponseEntity.created(uri).build(); //Gera o código 201 do protocolo http
    }

    @GetMapping
    public ResponseEntity<Page<Pedido>> findPage(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage,
            @RequestParam(value = "direction", defaultValue = "DESC") String direction,
            @RequestParam(value = "orderBy", defaultValue = "instante") String orderBy) {

        Page<Pedido> list = service.findPage(page, linesPerPage, direction, orderBy);

        return ResponseEntity.ok().body(list);
    }
}