package com.morpheo.cursomc.services;

import com.morpheo.cursomc.domain.models.Cidade;
import com.morpheo.cursomc.domain.models.Cliente;
import com.morpheo.cursomc.domain.models.Endereco;
import com.morpheo.cursomc.domain.models.enums.Perfil;
import com.morpheo.cursomc.domain.models.enums.TipoCliente;
import com.morpheo.cursomc.dto.ClienteDTO;
import com.morpheo.cursomc.dto.ClienteNewDTO;
import com.morpheo.cursomc.security.UserSS;
import com.morpheo.cursomc.services.exceptions.AuthorizationException;
import com.morpheo.cursomc.services.exceptions.DataIntegrityException;
import com.morpheo.cursomc.services.exceptions.ObjectNotFoundException;
import com.morpheo.cursomc.dao.repository.ClienteRepository;
import com.morpheo.cursomc.dao.repository.EnderecoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.domain.Sort.Direction.valueOf;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository cliRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private S3Service s3Service;

    @Autowired
    private ImageService imageService;

    @Value("${img.prefix.client.profile}")
    private String prefix;

    //========================================================================================================================
    public Cliente find(Integer id) {
        UserSS userSS = UserService.authenticated();
        if (userSS == null || !userSS.hasHole(Perfil.ADMIN) && !id.equals(userSS.getId())) {
            throw new AuthorizationException("Acesso negado");
        }

        Optional<Cliente> repositoryById = cliRepository.findById(id);
        return repositoryById.orElseThrow(() -> new ObjectNotFoundException(
                "Objeto não encontrado! ID" + id + ", tipo: " + Cliente.class.getName()));

    }

    public List<Cliente> findAll() {
        return cliRepository.findAll();
    }

    public Page<Cliente> findPage(Integer page, Integer linesPerPage, String direction, String orderBy) {
        PageRequest pageRequest = PageRequest.of(page, linesPerPage, valueOf(direction), orderBy);

        return cliRepository.findAll(pageRequest);
    }

    //========================================================================================================================
    //Quando o id esta valendo nulo e spring sabe que é para inserir
    @Transactional
    public Cliente insert(Cliente cliente) {
        cliente.setId(null);
        cliente = cliRepository.save(cliente);
        enderecoRepository.saveAll(cliente.getEnderecos());
        return cliente;
    }

    //========================================================================================================================
    //Quando o ID não está nulo o Spring sabe que é para atualizar o objeto
    public Cliente update(Cliente cliente) {
        Cliente newOblj = find(cliente.getId());
        updateData(newOblj, cliente);
        return cliRepository.save(newOblj);
    }

    //========================================================================================================================
    private void updateData(Cliente newOblj, Cliente cliente) {
        newOblj.setNome(cliente.getNome());
        newOblj.setEmail(cliente.getEmail());
    }

    //========================================================================================================================
    public void delete(Integer id) {
        find(id);
        try {
            cliRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException(
                    "Não é possível excluir porque há entidades relacionadas");
        }
    }


    //========================================================================================================================
    public Cliente fromDTO(ClienteDTO clienteDTO) {
        return new Cliente(clienteDTO.getId(), clienteDTO.getNome(), clienteDTO.getEmail(), null, null, null);
    }

    //Sobrecarga do método fromDTO com a assinatura ClienteNewDTO
    public Cliente fromDTO(ClienteNewDTO clienteNewDTO) {
        Cidade cidade = new Cidade(clienteNewDTO.getCidadeId(), null, null);

        Cliente cliente = new Cliente(null,
                clienteNewDTO.getNome(),
                clienteNewDTO.getEmail(),
                clienteNewDTO.getCpfOuCnpj(),
                TipoCliente.toEnum(clienteNewDTO.getTipo()),
                passwordEncoder.encode(clienteNewDTO.getSenha()));//Será encodada com o algorítmo BCrypt

        Endereco endereco = new Endereco(null,
                clienteNewDTO.getLogradouro(),
                clienteNewDTO.getNumero(),
                clienteNewDTO.getComplemento(),
                clienteNewDTO.getBairro(),
                clienteNewDTO.getCep(),
                cliente,
                cidade);

        cliente.getEnderecos().add(endereco);

        cliente.getTelefones().add(clienteNewDTO.getTelefone1());

        if (clienteNewDTO.getTelefone2() != null) {
            cliente.getTelefones().add(clienteNewDTO.getTelefone2());
        }

        if (clienteNewDTO.getTelefone3() != null) {
            cliente.getTelefones().add(clienteNewDTO.getTelefone3());
        }
        return cliente;
    }

    public URI uploadProfilePicture(MultipartFile multipartFile){
        UserSS user = UserService.authenticated();
        if (user == null)
            throw new AuthorizationException("Acesso negado");

        BufferedImage jpgImage = imageService.getJpgImageFromFile(multipartFile);
        String fileName = prefix + user.getId() + ".jpg";

        return s3Service.uploadFile(imageService.getInputStream(jpgImage, "jpg"), fileName, "image");
    }
}
