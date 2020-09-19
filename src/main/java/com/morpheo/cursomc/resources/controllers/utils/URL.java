package com.morpheo.cursomc.resources.controllers.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

public class URL {
    public static List<Integer> decodeIntList(String s){

        /*String [] vet = s.split(",");
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < vet.length; i++) {
            list.add(Integer.parseInt(vet[i]));
        }
        return list;*/

        return asList(s.split(",")).stream().map(x -> Integer.parseInt(x)).collect(Collectors.toList());
    }

    public static String decodeParam(String nome) {
        try {
            return URLDecoder.decode(nome, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }
}
