package com.finalpoo.sistemavendas.repository;

import com.finalpoo.sistemavendas.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}
