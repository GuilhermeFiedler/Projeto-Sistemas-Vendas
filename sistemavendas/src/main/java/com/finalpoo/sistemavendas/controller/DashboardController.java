package com.finalpoo.sistemavendas.controller;

import com.finalpoo.sistemavendas.service.ProdutoService;
import com.finalpoo.sistemavendas.service.PedidoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final ProdutoService produtoService;
    private final PedidoService pedidoService;

    public DashboardController(ProdutoService produtoService, PedidoService pedidoService) {
        this.produtoService = produtoService;
        this.pedidoService = pedidoService;
    }

    @GetMapping("/dashboard")
    public String mostrarDashboard(Model model) {
        long produtosCriticos = produtoService.contarEstoqueCritico();
        long pedidosHoje = pedidoService.contarPedidosHoje();
        double resumoVendasHoje = pedidoService.calcularTotalVendasHoje();

        int anoAtual = java.time.LocalDate.now().getYear();
        var faturamentoMensal = pedidoService.faturamentoMensalMap(anoAtual);

        model.addAttribute("produtosCriticos", produtosCriticos);
        model.addAttribute("pedidosHoje", pedidosHoje);
        model.addAttribute("resumoVendas", String.format("R$ %.2f", resumoVendasHoje));
        model.addAttribute("meses", faturamentoMensal.keySet());
        model.addAttribute("valores", faturamentoMensal.values());

        return "dashboard";
    }
}
