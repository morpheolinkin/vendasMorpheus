package com.morpheo.cursomc.services;

import com.amazonaws.services.dynamodbv2.xspec.B;
import com.morpheo.cursomc.services.exceptions.FileException;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class ImageService {
    public BufferedImage getJpgImageFromFile(MultipartFile uploadFile){
        /*1ª Passo - Pegar a extenção do arquivo:
        *
        * FilenameUtils do (commons.io)
        * Necessário para extrair o nome e a extenção de um arquivo facilmente*/
        String ext = FilenameUtils.getExtension(uploadFile.getOriginalFilename());

        /*2º Passo - Testar as extenções:*/
        if (!"png".equals(ext) && !"jpg".equals(ext))
            throw new FileException("Somente imagens PNG e JPG são permitidas");

        /*3º Passo - Ler a imagem através do ImageIO e testar as extenções.
        *
        * O padrão adotado aqui para os arquivos foi o jpg, então se o arquivos
        * enviado foi o png, converte ele para o jpg*/
        try {
            BufferedImage img = ImageIO.read(uploadFile.getInputStream());
            if("png".equals(ext))
                img = pngToJpg(img);
            return img;
        } catch (IOException e) {
            throw new FileException("Erro ao ler arquivo");
        }
    }

    //Código muito específico, pode ser encontrado na NET
    public BufferedImage pngToJpg(BufferedImage img) {
        BufferedImage jpgImage = new BufferedImage(
                img.getWidth(),
                img.getHeight(),
                BufferedImage.TYPE_INT_BGR);
        jpgImage.createGraphics().drawImage(img, 0, 0, Color.WHITE, null);

        return jpgImage;
    }

    //Código muito específico, pode ser encontrado na NET
    public InputStream getInputStream(BufferedImage img, String extension){
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(img, extension, os);
            return new ByteArrayInputStream(os.toByteArray());
        } catch (IOException e) {
            throw new FileException("Erro ao ler o arquivo");
        }
    }

}
