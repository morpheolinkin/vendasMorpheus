package com.morpheo.cursomc.domain.models.enums;

public enum EstadoPagamento {
    PENDENTE(1, "Pagamento pendende"),
    QUITADO(2, "Pagamento quitado"),
    CANCELADO(3, "Pagamento cancelado");

    private Integer cod;
    private String descricao;

    EstadoPagamento(Integer cod, String descricao) {
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
    public static EstadoPagamento toEnum(Integer cod){
        //Condição defenciva
        if (cod == null)
            return null;
        for (EstadoPagamento x : EstadoPagamento.values()) {
            if (cod.equals(x.getCod()))
                return x;
        }
        throw new IllegalArgumentException("Id inválido: " + cod);
    }
}
