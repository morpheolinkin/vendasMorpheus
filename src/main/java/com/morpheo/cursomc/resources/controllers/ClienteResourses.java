package com.morpheo.cursomc.resources.controllers;

import com.morpheo.cursomc.domain.models.Cliente;
import com.morpheo.cursomc.dto.ClienteDTO;
import com.morpheo.cursomc.dto.ClienteNewDTO;
import com.morpheo.cursomc.services.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/clientes")
public class ClienteResourses {

    @Autowired
    private ClienteService service;

    //========================================================================================================================

    //@RequestMapping(method = RequestMethod.GET)
    @GetMapping
    public ResponseEntity<List<ClienteDTO>> findAll() {
        List<Cliente> list = service.findAll();
        List<ClienteDTO> dtoList = list
                .stream()
                .map(ClienteDTO::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(dtoList);
    }


    //@RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @GetMapping(value = "/{id}")
    public ResponseEntity<Cliente> find(@PathVariable Integer id) {
        Cliente cliente = service.find(id);

        return ResponseEntity.ok().body(cliente);
    }



    //@RequestMapping(value = "/page", method = RequestMethod.GET)
    @GetMapping(value = "/page")
    public ResponseEntity<Page<ClienteDTO>> findPage(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction,
            @RequestParam(value = "orderBy", defaultValue = "nome") String orderBy) {

        Page<Cliente> list = service.findPage(page, linesPerPage, direction, orderBy);
        Page<ClienteDTO> dtoList = list.map(ClienteDTO::new);

        return ResponseEntity.ok().body(dtoList);
    }

    //========================================================================================================================
    //@RequestMapping(method = RequestMethod.POST)
    @PostMapping
    public ResponseEntity<Void> insert(@Valid @RequestBody ClienteNewDTO objDTO) { //Faz o Json ser convertido para o objeto Java
        Cliente obj = service.fromDTO(objDTO);
        obj = service.insert(obj);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(obj.getId())
                .toUri();
        return ResponseEntity.created(uri).build(); //Gera o código 201 created do protocolo http
    }

    //========================================================================================================================
    //@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @PutMapping(value = "/{id}")
    public ResponseEntity<Void> update(@Valid @RequestBody ClienteDTO clienteDTO, @PathVariable Integer id) {
        Cliente cliente = service.fromDTO(clienteDTO);
        cliente.setId(id);
        service.update(cliente);
        return ResponseEntity.noContent().build();
    }

    //========================================================================================================================
    //@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    //Enviar a foto de perfil do cliente para o S3
    @PostMapping(value = "/picture")
    public ResponseEntity<Void> uploadProfilePicture(@RequestParam(name = "file")MultipartFile file) { //Faz o Json ser convertido para o objeto Java
        URI uri = service.uploadProfilePicture(file);
        return ResponseEntity.created(uri).build();
    }
}
