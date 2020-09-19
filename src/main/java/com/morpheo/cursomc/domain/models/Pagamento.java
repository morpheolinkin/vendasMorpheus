package com.morpheo.cursomc.domain.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.morpheo.cursomc.domain.models.enums.EstadoPagamento;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
/*Para mapear Herança é ncessário inserir esta anotação @Inheritance
* passando a estrategia que normalmete são duas (mais usadas) por ser com
* tabela única ou uma tabela para cada sub-classe nessa é necessário fazer um Join.
* Geralmente quando tem muitos atributos na subclasse é recomendade fazer tabelas independentes
* quando tem pouco elementos um tabelão/única tabela.
* A estrategia para única tabela é @Inheritance(strategy = InheritanceType.SINGLE_TABLE
* A estrategia para única tabela é @Inheritance(strategy = InheritanceType.JOINED*/
@Inheritance(strategy = InheritanceType.JOINED)
/*
* Anotação do pacote jackson, basicamente ela informa que a classe pagamento vai ter um campo
* adicional que se chama @type
*/
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public abstract class Pagamento implements Serializable {
    /*
    * Não inclui o @GeneretedValues porque é necessário que o
    * ID dessa entidade não gere automaticamente para que
    * seja igual ao id da entidade Pedido
    */
    @Id
    private Integer id;
    private Integer estado;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name="PEDIDO_ID")//Nome da coluna que vai está o id do pedido
    @MapsId//Anotação que vai garantir que o Id do pagamento seja o mesmo do Pedido
    //Necessário fazer configuração na classe Pedido também, no caso no atributo pagamento.
    private Pedido pedido;

    public Pagamento() {
    }

    public Pagamento(Integer id, EstadoPagamento estado, Pedido pedido) {
        this.id = id;
        this.estado = (estado == null) ? null : estado.getCod();
        this.pedido = pedido;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public EstadoPagamento getEstado() {
        return EstadoPagamento.toEnum(estado);
    }

    public void setEstado(EstadoPagamento estado) {
        this.estado = estado.getCod();
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pagamento pagamento = (Pagamento) o;
        return Objects.equals(id, pagamento.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
