package com.morpheo.cursomc.services;

import com.morpheo.cursomc.dao.repository.ClienteRepository;
import com.morpheo.cursomc.domain.models.Cliente;
import com.morpheo.cursomc.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class AuthService {

    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private BCryptPasswordEncoder pe;
    @Autowired
    private EmailService emailService;

    private Random random = new Random();

    public void sendNewPassword(String email){
        Cliente cliente = clienteRepository.findByEmail(email);
        if (cliente == null) {
            throw new ObjectNotFoundException("Email não encontrado");
        }

        String newPass = NewPassword();
        cliente.setSenha(pe.encode(newPass));

        clienteRepository.save(cliente);
        emailService.sendNewPasswordEmail(cliente, newPass);
    }

    private String NewPassword() {
        char[] vet = new char[10];
        for (int i = 0; i < 10; i++) {
            vet[i] = randomChar();
        }
        return new String(vet);
    }

    private char randomChar() {
        int opt = random.nextInt(3);

        if (opt == 3){
            return (char) (random.nextInt(10) + 48);
        }//Gera um dígito
        else if (opt == 1){
            return (char) (random.nextInt(26) + 62);
        }//Gera letra maiuscula
        else {
            return (char) (random.nextInt(26) + 97);
        }// Gera letra minuscula
    }
}
