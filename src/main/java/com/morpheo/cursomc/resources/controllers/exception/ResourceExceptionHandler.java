package com.morpheo.cursomc.resources.controllers.exception;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.morpheo.cursomc.services.exceptions.AuthorizationException;
import com.morpheo.cursomc.services.exceptions.DataIntegrityException;
import com.morpheo.cursomc.services.exceptions.FileException;
import com.morpheo.cursomc.services.exceptions.ObjectNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;


@ControllerAdvice
public class ResourceExceptionHandler {

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<StandardError> objectNotFound(ObjectNotFoundException e, HttpServletRequest request) {

        StandardError err = new StandardError(System.currentTimeMillis(),
                HttpStatus.NOT_FOUND.value(),
                "Não encontrado",
                e.getMessage(),
                request.getRequestURI());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
    }

    @ExceptionHandler(DataIntegrityException.class)
    public ResponseEntity<StandardError> dataIntegrity(DataIntegrityException e, HttpServletRequest request) {

        StandardError err = new StandardError(System.currentTimeMillis(),
                HttpStatus.BAD_REQUEST.value(),
                "Integridade de dados",
                e.getMessage(),
                request.getRequestURI());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardError> validation(MethodArgumentNotValidException e, HttpServletRequest request) {

        ValidationError err = new ValidationError(System.currentTimeMillis(),
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "Erro de validação",
                e.getMessage(),
                request.getRequestURI());

        for (FieldError x : e.getBindingResult().getFieldErrors()) {
            err.addError(x.getField(), x.getDefaultMessage());
        }
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(err);
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<StandardError> authorization(AuthorizationException e, HttpServletRequest request) {

        StandardError err = new StandardError(System.currentTimeMillis(),
                HttpStatus.FORBIDDEN.value(),
                "Sem autorização",
                e.getMessage(),
                request.getRequestURI());

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(err);
    }

    @ExceptionHandler(FileException.class)
    public ResponseEntity<StandardError> file(FileException e, HttpServletRequest request) {

        StandardError err = new StandardError(System.currentTimeMillis(),
                HttpStatus.BAD_REQUEST.value(),
                "Erro de arquivo",
                e.getMessage(),
                request.getRequestURI());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }

    @ExceptionHandler(AmazonServiceException.class)
    public ResponseEntity<StandardError> amazon(AmazonServiceException e, HttpServletRequest request) {
        HttpStatus code = HttpStatus.valueOf(e.getStatusCode());

        StandardError err = new StandardError(System.currentTimeMillis(),
                code.value(),
                "Problema do service da Amazon",
                e.getMessage(),
                request.getRequestURI());

        return ResponseEntity.status(code).body(err);
    }

    @ExceptionHandler(AmazonClientException.class)
    public ResponseEntity<StandardError> amazonClient(AmazonClientException e, HttpServletRequest request) {

        StandardError err = new StandardError(System.currentTimeMillis(),
                HttpStatus.BAD_REQUEST.value(),
                "Problema no Cliente Amazon",
                e.getMessage(),
                request.getRequestURI());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }

    @ExceptionHandler(AmazonS3Exception.class)
    public ResponseEntity<StandardError> amazonS3(AmazonS3Exception e, HttpServletRequest request) {

        StandardError err = new StandardError(System.currentTimeMillis(),
                HttpStatus.BAD_REQUEST.value(),
                "Problema no S3 da Amazon",
                e.getMessage(),
                request.getRequestURI());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }
}

