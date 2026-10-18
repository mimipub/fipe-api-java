package br.com.tabelafipe.tabelafipe.model;

import br.com.tabelafipe.tabelafipe.service.ConsumoApi;
import br.com.tabelafipe.tabelafipe.service.ConverteDados;
import org.springframework.boot.Banner;

import javax.swing.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Principal {
    private final String ENDERECO = "https://parallelum.com.br/fipe/api/v1/";
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados converteDados = new ConverteDados();
    private Scanner sc = new Scanner(System.in);

    public void exibirMenu(){
        String mensagem = "***OPÇÕES***" + "\n"
                + "Carros" + "\n"
                + "Motos" + "\n"
                + "caminhoes" + "\n";

        System.out.println(mensagem);
        System.out.println("Digite uma das opções para consultar valores:");
        String opcao = sc.nextLine();
        String endereco;


        if(opcao.toLowerCase().contains("carr")){
           endereco = ENDERECO + "carros/marcas/";
        } else if (opcao.toLowerCase().contains("mot")) {
            endereco = ENDERECO + "motos/marcas/";
            var json = consumo.obterDados(endereco);
        }else {
            endereco = ENDERECO + "caminhoes/marcas/";
        }
        var json = consumo.obterDados(endereco);
        System.out.println(json);
        var listaDados = converteDados.obterLista(json, Dados.class);

        listaDados.stream()
                .sorted(Comparator.comparing(Dados::codigo))
                .forEach(System.out::println);


        System.out.println("Informe o código da marca para consulta: ");
        String codigo = sc.next();

        endereco = endereco + codigo + "/modelos/";
        json = consumo.obterDados(endereco);
        var listaModelo = converteDados.conversor(json, Modelos.class);

        listaModelo.modelos().stream()
                .sorted(Comparator.comparing(Dados::codigo))
                .forEach(System.out::println);

        System.out.println("Digite um trecho do veiculo para consulta: ");
        String veiculo = sc.next();

        System.out.println("\nMODELOS FILTRADOS");
        List<Dados> nomeVeiculo = listaModelo.modelos().stream()
                .filter(m-> m.nome().toLowerCase().contains(veiculo))
                .collect(Collectors.toList());

        nomeVeiculo.forEach(System.out::println);

        System.out.println("Por favor digite os códigos modelos para buscar os valores de avaliação:");
        String codigoModelo = sc.next();

        endereco = endereco + codigoModelo + "/anos/";
        json = consumo.obterDados(endereco);


        List<Dados> anos = converteDados.obterLista(json, Dados.class);
        List<Veiculos> listarVeiculos = new ArrayList<>() ;

        for (int i = 0; i < anos.size() ; i++) {
            var enderecoAno = endereco + anos.get(i).codigo();
            json = consumo.obterDados(enderecoAno);
            Veiculos vehicle = converteDados.conversor(json, Veiculos.class);
            listarVeiculos.add(vehicle);
        }
        System.out.println("Todos os veiculos filtrado com avaliações por ano:");
        listarVeiculos.forEach(System.out::println);

    }
}
