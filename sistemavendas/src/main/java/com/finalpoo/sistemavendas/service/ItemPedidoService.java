package com.finalpoo.sistemavendas.service;

import com.finalpoo.sistemavendas.entity.ItemPedido;
import com.finalpoo.sistemavendas.repository.ItemPedidoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemPedidoService {

    private final ItemPedidoRepository repository;

    public ItemPedidoService(ItemPedidoRepository repository) {
        this.repository = repository;
    }

    public List<ItemPedido> listarTodos() {
        return repository.findAll();
    }

    public ItemPedido salvar(ItemPedido itemPedido) {
        return repository.save(itemPedido);
    }
}
