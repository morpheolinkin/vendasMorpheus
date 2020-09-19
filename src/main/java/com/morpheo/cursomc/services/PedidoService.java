package com.morpheo.cursomc.services;

import com.morpheo.cursomc.dao.repository.ItemPedidoRepository;
import com.morpheo.cursomc.dao.repository.PagamentoRepository;
import com.morpheo.cursomc.dao.repository.PedidoRepository;
import com.morpheo.cursomc.domain.models.*;
import com.morpheo.cursomc.domain.models.enums.EstadoPagamento;
import com.morpheo.cursomc.security.UserSS;
import com.morpheo.cursomc.services.exceptions.AuthorizationException;
import com.morpheo.cursomc.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

import static org.springframework.data.domain.Sort.Direction.valueOf;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private BoletoService boletoService;

    @Autowired
    private PagamentoRepository pagamentoRepository;

    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ItemPedidoRepository itemPedidoRepository;

    @Autowired
    private EmailService emailService;

    public Pedido find(Integer id) {
        Optional<Pedido> repositoryById = pedidoRepository.findById(id);
        return repositoryById.orElseThrow(() -> new ObjectNotFoundException(
                "Objeto não encontrado! ID" + id + ", tipo: " + Pedido.class.getName()));

    }

    @Transactional
    public Pedido insert(Pedido pedido) {
        pedido.setId(null); //Para garantir que realmente esta inserindo um novo pedido
        pedido.setInstante(new Date()); //Cria uma nova data com um instante atual
        pedido.setCliente(clienteService.find(pedido.getCliente().getId()));
        pedido.getPagamento().setEstado(EstadoPagamento.PENDENTE);
        pedido.getPagamento().setPedido(pedido);

        if (pedido.getPagamento() instanceof PagamentoComBoleto) {
            PagamentoComBoleto pagto = (PagamentoComBoleto) pedido.getPagamento();
            boletoService.preencherPagamentoComBoleto(pagto, pedido.getInstante());
        }

        pedido = pedidoRepository.save(pedido);
        pagamentoRepository.save(pedido.getPagamento());
        for (ItemPedido ip : pedido.getItens()) {
            ip.setDesconto(0.0);
            ip.setProduto(produtoService.find(ip.getProduto().getId()));
            ip.setPreco(ip.getProduto().getPreco());
            ip.setPedido(pedido);
        }

        itemPedidoRepository.saveAll(pedido.getItens());
        emailService.sendOrderConfirmationHtmlEmail(pedido);
        return pedido;
    }

    public Page<Pedido> findPage(Integer page, Integer linesPerPage, String direction, String orderBy ){
        UserSS user = UserService.authenticated();
        if (user == null) {
            throw new AuthorizationException("Acesso Negado");
        }

        PageRequest pageRequest = PageRequest.of(page, linesPerPage, valueOf(direction), orderBy);
        Cliente cliente = clienteService.find(user.getId());
        return pedidoRepository.findByCliente(cliente, pageRequest);
    }

}
