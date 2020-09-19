package com.morpheo.cursomc.services;

import com.morpheo.cursomc.dao.repository.*;
import com.morpheo.cursomc.domain.models.*;
import com.morpheo.cursomc.domain.models.enums.EstadoPagamento;
import com.morpheo.cursomc.domain.models.enums.Perfil;
import com.morpheo.cursomc.domain.models.enums.TipoCliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static java.util.Arrays.asList;

@Service
public class DBService {

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private CategoriaRepository categoriaRepository;
    @Autowired
    private ProdutoRepository produtoRepository;
    @Autowired
    private CidadeRepository cidadeRepository;
    @Autowired
    private EstadoRepository estadoRepository;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private EnderecoRepository enderecoRepository;
    @Autowired
    private PedidoRepository pedidoRepository;
    @Autowired
    private PagamentoRepository pagamentoRepository;
    @Autowired
    private ItemPedidoRepository itemPedidoRepository;

    public void instantiateTestDatabase() throws ParseException {
        Categoria cat1 = new Categoria(null, "Informática");
        Categoria cat2 = new Categoria(null, "Escritório");
        Categoria cat3 = new Categoria(null, "Cama mesa e Banho");
        Categoria cat4 = new Categoria(null, "Eletrônicos");
        Categoria cat5 = new Categoria(null, "Jedinagem");
        Categoria cat6 = new Categoria(null, "Decoração");
        Categoria cat7 = new Categoria(null, "Perumaria");


        Produto p1 = new Produto(null, "Computador", 2000.00);
        Produto p2 = new Produto(null, "Impressora", 800.00);
        Produto p3 = new Produto(null, "Mouse", 80.00);
        Produto p4 = new Produto(null, "Mesa de Escitório", 300.00);
        Produto p5 = new Produto(null, "Toalha", 50.00);
        Produto p6 = new Produto(null, "Colcha", 200.00);
        Produto p7 = new Produto(null, "TV True Color", 1200.00);
        Produto p8 = new Produto(null, "Roçadeira", 800.00);
        Produto p9 = new Produto(null, "Abajour", 100.00);
        Produto p10 = new Produto(null, "Pendendte", 180.00);
        Produto p11 = new Produto(null, "Shampoo", 90.00);
        Produto p12 = new Produto(null, "Condicionador", 99.00);

        cat1.getProdutos().addAll(asList(p1, p2, p3));
        cat2.getProdutos().addAll(asList(p2, p4));
        cat3.getProdutos().addAll(asList(p5, p6));
        cat4.getProdutos().addAll(asList(p1, p2, p3, p7));
        cat5.getProdutos().add(p8);
        cat6.getProdutos().addAll(asList(p9, p10));
        cat7.getProdutos().addAll(asList(p11, p12));

        p1.getCategorias().addAll(asList(cat1, cat4));
        p2.getCategorias().addAll(asList(cat1, cat2, cat4));
        p3.getCategorias().addAll(asList(cat1, cat4));
        p4.getCategorias().add(cat2);
        p5.getCategorias().add(cat3);
        p6.getCategorias().add(cat3);
        p7.getCategorias().add(cat4);
        p8.getCategorias().add(cat5);
        p9.getCategorias().add(cat6);
        p10.getCategorias().add(cat6);
        p11.getCategorias().add(cat7);
        p12.getCategorias().add(cat7);

        categoriaRepository.saveAll(asList(cat1, cat2, cat3, cat4, cat5, cat6, cat7));
        produtoRepository.saveAll(asList(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12));
        /*===============================================================================*/
        Estado est1 = new Estado("Minas Gerais");
        Estado est2 = new Estado("São Paulo");

        Cidade c1 = new Cidade(null, "Uberlândia", est1);
        Cidade c2 = new Cidade(null, "São Paulo", est2);
        Cidade c3 = new Cidade(null, "Campinas", est2);

        est1.getCidadeList().add(c1);
        est2.getCidadeList().addAll(asList(c2, c3));

        estadoRepository.saveAll(asList(est1, est2));
        cidadeRepository.saveAll(asList(c1, c2, c3));

        /*===============================================================================*/
        Cliente cli1 = new Cliente(null, "Maria Silva",
                "jeffersonmedeirosdasilva@gmail.com", "36378912377",
                TipoCliente.PESSOAFISICA, passwordEncoder.encode("123"));
        cli1.getTelefones().addAll(asList("27363323", "93838393"));

        Cliente cli2 = new Cliente(null, "Ana Roberta",
                "primeirograudetijuacu@gmail.com", "64154687581",
                TipoCliente.PESSOAFISICA, passwordEncoder.encode("123"));
        cli2.getTelefones().addAll(asList("7498225744", "7589982452"));
        cli2.addPerfil(Perfil.ADMIN);

        Endereco e1 = new Endereco(null, "Rua Flores", "300",
                "Apto 203", "Jardim", "38220834", cli1, c1);

        Endereco e2 = new Endereco(null, "Avenida Matos", "105",
                "Sala 800", "Centro", "38777012", cli1, c2);
        Endereco e3 = new Endereco(null, "Rua João Carlos", "18",
                "Casa", "Alto do Marrocos", "447552288", cli2, c2);


        cli1.getEnderecos().addAll(asList(e1, e2));
        cli2.getEnderecos().add(e3);

        clienteRepository.saveAll(asList(cli1, cli2));
        enderecoRepository.saveAll(asList(e1, e2, e3));

        /*===============================================================================*/
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");


        Pedido ped1 = new Pedido(null, dateFormat.parse("27/03/2020 - 16:35:25"), e1, cli1);
        Pedido ped2 = new Pedido(null, dateFormat.parse("28/03/2020 - 12:47:33"), e2, cli1);

        Pagamento pagamento1 = new PagamentoComCartao(null,
                EstadoPagamento.QUITADO, ped1, 6);
        ped1.setPagamento(pagamento1);

        Pagamento pagamento2 = new PagamentoComBoleto(null,
                EstadoPagamento.PENDENTE, ped2,
                dateFormat.parse("15/03/2020 - 09:47:28"), null);

        ped2.setPagamento(pagamento2);

        cli1.getPedidos().addAll(asList(ped1, ped2));

        pedidoRepository.saveAll(asList(ped1, ped2));
        pagamentoRepository.saveAll(asList(pagamento1, pagamento2));

        /*===============================================================================*/
        ItemPedido ip1 = new ItemPedido(ped1, p1, 0.0, 1, 2000.00);
        ItemPedido ip2 = new ItemPedido(ped1, p3, 0.0, 2, 80.00);
        ItemPedido ip3 = new ItemPedido(ped2, p2, 100.00, 1, 800.00);

        ped1.getItens().addAll(asList(ip1, ip2));
        ped2.getItens().add(ip3);

        p1.getItens().add(ip1);
        p2.getItens().add(ip2);
        p3.getItens().add(ip3);

        itemPedidoRepository.saveAll(asList(ip1, ip2, ip3));
    }
}
