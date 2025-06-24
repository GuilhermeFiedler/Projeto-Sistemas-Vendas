package com.finalpoo.sistemavendas.controller;

import com.finalpoo.sistemavendas.entity.Pedido;
import com.finalpoo.sistemavendas.entity.ItemPedido;
import com.finalpoo.sistemavendas.service.ClienteService;
import com.finalpoo.sistemavendas.service.PedidoService;
import com.finalpoo.sistemavendas.service.ProdutoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;
    private final ClienteService clienteService;
    private final ProdutoService produtoService;

    public PedidoController(PedidoService pedidoService, ClienteService clienteService, ProdutoService produtoService) {
        this.pedidoService = pedidoService;
        this.clienteService = clienteService;
        this.produtoService = produtoService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("pedidos", pedidoService.listarTodos());
        return "pedido/lista";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("pedido", new Pedido());
        model.addAttribute("clientes", clienteService.listarTodos());
        model.addAttribute("produtos", produtoService.listarTodos());
        return "pedido/formulario";
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Pedido pedido, Model model) {
        try {
            if (pedido.getItens() == null || pedido.getItens().isEmpty()) {
                throw new IllegalArgumentException("O pedido deve conter pelo menos um produto.");
            }

            for (ItemPedido item : pedido.getItens()) {
                item.setPedido(pedido);
            }

            pedidoService.salvar(pedido);
            return "redirect:/pedidos";

        } catch (IllegalArgumentException e) {
            model.addAttribute("mensagemErro", e.getMessage());
            model.addAttribute("pedido", pedido);
            model.addAttribute("clientes", clienteService.listarTodos());
            model.addAttribute("produtos", produtoService.listarTodos());
            return "pedido/formulario";
        }
    }


    @GetMapping("/detalhes/{id}")
    public String detalhes(@PathVariable Long id, Model model) {
        Pedido pedido = pedidoService.buscarPorId(id).orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado"));
        model.addAttribute("pedido", pedido);
        return "pedido/detalhes";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id) {
        pedidoService.deletar(id);
        return "redirect:/pedidos";
    }
}
