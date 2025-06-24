package com.finalpoo.sistemavendas.service;

import com.finalpoo.sistemavendas.entity.Pedido;
import com.finalpoo.sistemavendas.entity.ItemPedido;
import com.finalpoo.sistemavendas.repository.PedidoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.*;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;

import com.finalpoo.sistemavendas.dto.FaturamentoMensalDTO;

@Service
public class PedidoService {

    private final PedidoRepository repository;
    private final ProdutoService produtoService;

    public PedidoService(PedidoRepository repository, ProdutoService produtoService) {
        this.repository = repository;
        this.produtoService = produtoService;
    }

    public List<Pedido> listarTodos() {
        return repository.findAll();
    }

    public Optional<Pedido> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public Pedido salvar(Pedido pedido) {
        if (pedido.getItens() == null || pedido.getItens().isEmpty()) {
            throw new IllegalArgumentException("O pedido deve conter pelo menos um produto.");
        }

        if (pedido.getDataHora() == null) {
            pedido.setDataHora(LocalDateTime.now());
        }

        for (ItemPedido item : pedido.getItens()) {
            item.setPedido(pedido);

            var produto = produtoService.buscarPorId(item.getProduto().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));

            item.setProduto(produto);
            item.setPrecoUnitario(produto.getPreco());

            produtoService.atualizarEstoque(produto.getId(), item.getQuantidade());
        }

        return repository.save(pedido);
    }

    public void deletar(Long id) {
        repository.deleteById(id);
    }

    public List<Object[]> vendasPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        return repository.findVendasPorPeriodo(inicio, fim);
    }

    public List<FaturamentoMensalDTO> faturamentoMensal(int ano) {
        var resultados = repository.findFaturamentoMensal(ano);

        List<FaturamentoMensalDTO> dtos = new ArrayList<>();
        for (Object[] row : resultados) {
            int mes = ((Number) row[1]).intValue();
            double total = ((Number) row[2]).doubleValue();
            dtos.add(new FaturamentoMensalDTO(mes, total));
        }

        return dtos;
    }

    public List<Object[]> topProdutosMaisVendidosPorData(LocalDate data) {
        LocalDateTime inicio = data.atStartOfDay();
        LocalDateTime fim = data.atTime(LocalTime.MAX);

        return repository.findTopProdutosMaisVendidosPorData(inicio, fim)
                .stream()
                .limit(3)
                .toList();
    }

    public long contarPedidosHoje() {
        return repository.contarPedidosHoje();
    }

    public double calcularTotalVendasHoje() {
        return repository.calcularTotalVendasHoje();
    }

    public Map<String, Double> faturamentoMensalMap(int ano) {
        List<FaturamentoMensalDTO> dtos = faturamentoMensal(ano);

        Map<String, Double> mapaMesTotal = new LinkedHashMap<>();


        for (int m = 1; m <= 12; m++) {
            String nomeMes = Month.of(m).getDisplayName(TextStyle.SHORT, new Locale("pt", "BR"));
            mapaMesTotal.put(nomeMes, 0.0);
        }


        for (FaturamentoMensalDTO dto : dtos) {
            String nomeMes = Month.of(dto.mes()).getDisplayName(TextStyle.SHORT, new Locale("pt", "BR"));
            mapaMesTotal.put(nomeMes, dto.total());
        }

        return mapaMesTotal;
    }

}
