package com.finalpoo.sistemavendas.repository;

import com.finalpoo.sistemavendas.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}
