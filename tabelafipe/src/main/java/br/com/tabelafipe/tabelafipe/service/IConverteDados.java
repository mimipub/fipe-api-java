package br.com.tabelafipe.tabelafipe.service;

import java.util.List;

public interface IConverteDados {
    <T, classe> T conversor(String json, Class<T> classe);

    <T> List<T> obterLista(String json, Class<T> classe);
}
