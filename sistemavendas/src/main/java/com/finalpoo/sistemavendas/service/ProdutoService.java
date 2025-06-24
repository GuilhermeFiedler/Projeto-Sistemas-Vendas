package com.finalpoo.sistemavendas.service;

import com.finalpoo.sistemavendas.entity.Produto;
import com.finalpoo.sistemavendas.repository.ProdutoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {

    private final ProdutoRepository repository;

    public ProdutoService(ProdutoRepository repository) {
        this.repository = repository;
    }

    public List<Produto> listarTodos() {
        return repository.findAll();
    }

    public Optional<Produto> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public Produto salvar(Produto produto) {
        return repository.save(produto);
    }

    public void deletar(Long id) {
        repository.deleteById(id);
    }

    public void atualizarEstoque(Long idProduto, int quantidadeVendida) {
        Produto produto = repository.findById(idProduto).orElseThrow();
        int novoEstoque = produto.getQuantidadeEstoque() - quantidadeVendida;
        produto.setQuantidadeEstoque(novoEstoque);
        repository.save(produto);
    }

    public long contarEstoqueCritico() {
        return listarTodos().stream()
                .filter(p -> p.getQuantidadeEstoque() <= p.getQuantidadeMinima())
                .count();
    }

}
