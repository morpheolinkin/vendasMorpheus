package com.morpheo.cursomc.resources.controllers;

/*
Todas as classes que são controladoras REST
vão ficar no pacote RESOURCE
*/

import com.morpheo.cursomc.domain.models.Categoria;
import com.morpheo.cursomc.dto.CategoriaDTO;
import com.morpheo.cursomc.services.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping(value = "/categorias")
public class CategoriaResources {

    //Camada que vai servir como base para implementar os métodos
    @Autowired
    private CategoriaService service;

    //@RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @GetMapping(value = "/{id}")
    public ResponseEntity<Categoria> find(@PathVariable Integer id) {
        Categoria obj = service.find(id);

        return ResponseEntity.ok().body(obj);
    }

    //@RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<CategoriaDTO>> findAll() {
        List<Categoria> list = service.findAll();
        List<CategoriaDTO> dtoList = list
                .stream()
                .map(CategoriaDTO::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(dtoList);
    }

    //@RequestMapping(value = "/page", method = RequestMethod.GET)
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping(value = "/page")
    public ResponseEntity<Page<CategoriaDTO>> findPage(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction,
            @RequestParam(value = "orderBy", defaultValue = "nome") String orderBy) {

        Page<Categoria> list = service.findPage(page, linesPerPage, direction, orderBy);
        Page<CategoriaDTO> dtoList = list.map(CategoriaDTO::new);

        return ResponseEntity.ok().body(dtoList);
    }

    /*
        O status HTTP "201 Created" é utilizado como resposta de sucesso,
        indica que a requisição foi bem sucedida e que um novo recurso foi criado.
        Este novo recurso é efetivamente criado antes do retorno da resposta e o
        novo recurso é enviado no corpo da mensagem (pode vir na URL ou na header "Location").
        Comumente, este status é utilizado em requisições do tipo POST.
    */

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Void> insert(@Valid @RequestBody CategoriaDTO objDTO) { //Faz o Json ser convertido para o objeto Java
        Categoria categoria = service.fromDTO(objDTO);
        categoria = service.insert(categoria);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(categoria.getId())
                .toUri();
        return ResponseEntity.created(uri).build(); //Gera o código 201 do protocolo http
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping(value = "/{id}")
    public ResponseEntity<Void> update(@Valid @RequestBody CategoriaDTO categoriaDTO, @PathVariable Integer id) {
        Categoria categoria = service.fromDTO(categoriaDTO);
        categoria.setId(id);
        service.update(categoria);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
