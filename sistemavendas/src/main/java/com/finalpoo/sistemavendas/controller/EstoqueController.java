package com.finalpoo.sistemavendas.controller;

import com.finalpoo.sistemavendas.entity.Produto;
import com.finalpoo.sistemavendas.service.ProdutoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/estoque")
public class EstoqueController {

    private final ProdutoService produtoService;

    public EstoqueController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @GetMapping
    public String visualizarEstoque(Model model) {
        List<Produto> produtos = produtoService.listarTodos();
        model.addAttribute("produtos", produtos);
        return "estoque/lista";
    }
}
