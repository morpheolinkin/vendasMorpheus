package com.morpheo.cursomc.domain.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.morpheo.cursomc.domain.models.enums.Perfil;
import com.morpheo.cursomc.domain.models.enums.TipoCliente;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@Entity
public class Cliente implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nome;

    @Column(unique = true)
    private String email;

    private String cpfOuCnpj;
    private Integer tipoCliente;
    /*
     * JsonIgnore porque na hora que for recuperar um Cliente, não aparecer o BCrypt da senha
     * mesmo que sendo um BCrypt, não é legal incluir um campo senha
     * quando for recuperar os dados para o sistema.
     * */
    @JsonIgnore
    private String senha;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    private List<Endereco> enderecos = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "TELEFONE")
    //Não permite repetição de valores
    private Set<String> telefones = new HashSet<>();

    /*Sempre que for buscado um cliente na
     * base dados vão ser buscados os perfis também.
     * O EAGER vai garantir que seja buscado junto.
     * */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "PERFIS")
    private Set<Integer> perfis = new HashSet<>();


    @JsonIgnore
    @OneToMany(mappedBy = "cliente")
    List<Pedido> pedidos = new ArrayList<>();

    /*
    * Toda ver que um cliente for instanciado pelo
    * construtor vazil, por padrão ele já terá um
    * perfil de cliente na segurança.
    * */
    public Cliente() {
        addPerfil(Perfil.CLIENTE);
    }

    public Cliente(Integer id, String nome, String email,
                   String cpfOuCnpj, TipoCliente tipoCliente, String senha) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.cpfOuCnpj = cpfOuCnpj;
        this.tipoCliente = (tipoCliente == null) ? null : tipoCliente.getCod();
        this.senha = senha;
        addPerfil(Perfil.CLIENTE);
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCpfOuCnpj() {
        return cpfOuCnpj;
    }

    public void setCpfOuCnpj(String cpfOUcnpj) {
        this.cpfOuCnpj = cpfOUcnpj;
    }


    //Alterado para que o tipo Enum fique gerenciado por mim sem erro
    //Retornando o valor do código
    public TipoCliente getTipoCliente() {
        return TipoCliente.toEnum(tipoCliente);
    }

    //Alterado para que o tipo Enum fique gerenciado por mim sem erro
    public void setTipoCliente(TipoCliente tipoCliente) {
        this.tipoCliente = tipoCliente.getCod();
    }

    public List<Endereco> getEnderecos() {
        return enderecos;
    }

    public void setEnderecos(List<Endereco> enderecos) {
        this.enderecos = enderecos;
    }

    public Set<String> getTelefones() {
        return telefones;
    }

    public void setTelefones(Set<String> telefones) {
        this.telefones = telefones;
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }

    public void setPedidos(List<Pedido> pedidos) {
        this.pedidos = pedidos;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    /**
     * Método que irá retornar os perfis do usuário
     * percorrendo através {@code Stream} e buscando o tipo enumerado do perfil.
     * <blockquote><pre>
     *   perfis.stream()
     *   .map(x -> Perfil.toEnum(x))
     *   .collect(Collectors.toSet());
     * </pre></blockquote>
     * <p>
     * Os perfis de usuário são dois: {@literal ADMIN, CLIENTE}.
     * </p>
     *
     * @return perfil enumerado do usuario
     */
    public Set<Perfil> getPerfis() {
        return this.perfis.stream().map(x -> Perfil.toEnum(x)).collect(Collectors.toSet());
    }

    public void addPerfil(Perfil perfil) {
        this.perfis.add(perfil.getCod());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cliente cliente = (Cliente) o;
        return Objects.equals(id, cliente.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
