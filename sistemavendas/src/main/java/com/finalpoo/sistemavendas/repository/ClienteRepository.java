package com.finalpoo.sistemavendas.repository;

import com.finalpoo.sistemavendas.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}
