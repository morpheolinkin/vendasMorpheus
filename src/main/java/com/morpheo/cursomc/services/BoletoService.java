package com.morpheo.cursomc.services;

import com.morpheo.cursomc.domain.models.PagamentoComBoleto;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

@Service
public class BoletoService {

    public void preencherPagamentoComBoleto(PagamentoComBoleto pagto, Date instanceDoPedido){
        Calendar cal = Calendar.getInstance();
        cal.setTime(instanceDoPedido);
        cal.add(Calendar.DAY_OF_MONTH, 7);
        pagto.setDataVencimento(cal.getTime());
    }
}
