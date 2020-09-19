package com.morpheo.cursomc.services;


import com.morpheo.cursomc.domain.models.Categoria;
import com.morpheo.cursomc.dto.CategoriaDTO;
import com.morpheo.cursomc.services.exceptions.DataIntegrityException;
import com.morpheo.cursomc.services.exceptions.ObjectNotFoundException;
import com.morpheo.cursomc.dao.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static org.springframework.data.domain.Sort.Direction.*;

/*
* Classe responsável por fazer a consulta no repositório/DAO
*/
@Service
public class CategoriaService {

    //Passa a ser instaciada automaticamente pelo Spring
    @Autowired
    private CategoriaRepository repository;

    public Categoria find(Integer id){
        Optional<Categoria> categoria = repository.findById(id);
        return categoria.orElseThrow(() -> new ObjectNotFoundException(
                String.format("%s %d %s %s",
                        "Objeto não encontrado! ID: ",
                        id,
                        ", tipo:",
                        Categoria.class.getName())));
    }


    //Quando o id esta valendo nulo e spring sabe que é para inserir
    public Categoria insert(Categoria categoria) {
        categoria.setId(null);
        return repository.save(categoria);
    }

    //Quando o ID não está nulo o Spring sabe que é para atualizar o objeto
    public Categoria update(Categoria cliente){
        Categoria newOblj = find(cliente.getId());
        updateData(newOblj, cliente);
        return repository.save(newOblj);
    }

    private void updateData(Categoria newOblj, Categoria cliente) {
        newOblj.setNome(cliente.getNome());
    }

    public void delete(Integer id) {
        find(id);
        try {
            repository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException(
                            "Não é possível excluir uma categoria que possui produtos associados a ela");
        }
    }

    public List<Categoria> findAll() {
        return repository.findAll();
    }

    public Page<Categoria> findPage(Integer page, Integer linesPerPage, String direction, String orderBy ){
        PageRequest pageRequest = PageRequest.of(page, linesPerPage, valueOf(direction), orderBy);

        return repository.findAll(pageRequest);
    }

    public Categoria fromDTO(CategoriaDTO categoriaDTO){
        return new Categoria(categoriaDTO.getId(), categoriaDTO.getNome());
    }
}
