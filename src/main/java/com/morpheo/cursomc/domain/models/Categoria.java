package com.morpheo.cursomc.domain.models;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Categoria implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nome;

    /*
    * Para evitar a referência ciclica,
    * de quando uma requisição tenta buscar as categorias
    * ela pega também os produtos, aí fica nesse loop infinito.
    * Para resolver, a anotação @JsonManagedReference do lado que queremos
    * que mostre, nesse caso aqui, as categorias com seus respectivos
    * produtos associados. E do outro lado, no caso Entidade Produto,
    * anota com @JsonBackReference para não mostrar os produtos,
    * já que ele vai vir junto com a categoria.
    */

    @ManyToMany(mappedBy = "categorias")
    private List<Produto> produtos = new ArrayList<>();

    @Deprecated
    public Categoria() {
    }

    public Categoria(Integer id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<Produto> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<Produto> produtos) {
        this.produtos = produtos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Categoria categoria = (Categoria) o;
        return Objects.equals(id, categoria.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


}