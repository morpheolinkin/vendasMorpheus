package com.morpheo.cursomc.domain.models.enums;

public enum Perfil {
    ADMIN(1, "ROLE_ADMIN"),
    CLIENTE(2, "ROLE_CLIENTE");

    private Integer cod;
    private String descricao;

    Perfil(Integer cod, String descricao) {
        this.cod = cod;
        this.descricao = descricao;
    }

    public Integer getCod() {
        return cod;
    }

    public void setCod(Integer cod) {
        this.cod = cod;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    //Método para ser imposto no getEstado do atributo estado da classe Pagamento, alterando sua assinatura
    //Integer para EstadoPagamento e chamando esse método passando o valor do código.
    //Assim ele via retornar o estadoPagamento (Quitado ou Pendente)
    public static Perfil toEnum(Integer cod){
        //Condição defenciva
        if (cod == null)
            return null;
        for (Perfil x : Perfil.values()) {
            if (cod.equals(x.getCod()))
                return x;
        }
        throw new IllegalArgumentException("Id inválido: " + cod);
    }
}
