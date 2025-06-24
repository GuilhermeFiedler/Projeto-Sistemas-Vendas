package com.finalpoo.sistemavendas.controller;

import com.finalpoo.sistemavendas.entity.Produto;
import com.finalpoo.sistemavendas.service.PedidoService;
import com.finalpoo.sistemavendas.service.ProdutoService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/relatorios")
public class RelatorioController {

    private final PedidoService pedidoService;

    private final ProdutoService produtoService;

    public RelatorioController(PedidoService pedidoService, ProdutoService produtoService) {
        this.pedidoService = pedidoService;
        this.produtoService = produtoService;
    }

    @GetMapping("/vendas-por-periodo")
    public String vendasPorPeriodo(
            @RequestParam(value = "inicio", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam(value = "fim", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim,
            Model model) {

        if (inicio == null) {
            inicio = LocalDateTime.now().minusMonths(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        }
        if (fim == null) {
            fim = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59).withNano(999999999);
        }

        var dados = pedidoService.vendasPorPeriodo(inicio, fim);
        model.addAttribute("dadosVendas", dados);
        model.addAttribute("inicio", inicio);
        model.addAttribute("fim", fim);
        return "relatorios/vendas-por-periodo";
    }
    @GetMapping("/faturamento-mensal")
    public String faturamentoMensal(
            @RequestParam(value = "ano", required = false) Integer ano,
            Model model) {

        if (ano == null) {
            ano = LocalDateTime.now().getYear();
        }

        var dadosFaturamento = pedidoService.faturamentoMensal(ano);
        model.addAttribute("ano", ano);
        model.addAttribute("dadosFaturamento", dadosFaturamento);

        return "relatorios/faturamento-mensal";
    }
    @GetMapping("/estoque-critico")
    public String estoqueCritico(Model model) {
        List<Produto> produtosCriticos = produtoService.listarTodos()
                .stream()
                .filter(p -> p.getQuantidadeEstoque() <= p.getQuantidadeMinima())
                .toList();

        model.addAttribute("produtos", produtosCriticos);
        return "relatorios/estoque-critico";
    }

    @GetMapping("/produtos-mais-vendidos")
    public String produtosMaisVendidos(
            @RequestParam(value = "data", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate data,
            Model model) {

        if (data == null) {
            data = LocalDate.now();
        }

        var dados = pedidoService.topProdutosMaisVendidosPorData(data);

        List<String> nomes = new ArrayList<>();
        List<Long> quantidades = new ArrayList<>();

        for (Object[] obj : dados) {
            nomes.add((String) obj[0]);
            quantidades.add(((Number) obj[1]).longValue());
        }

        model.addAttribute("nomesProdutos", nomes);
        model.addAttribute("quantidades", quantidades);
        model.addAttribute("dataSelecionada", data);
        return "relatorios/produtos-mais-vendidos";
    }



}
