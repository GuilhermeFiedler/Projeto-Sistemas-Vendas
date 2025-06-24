package com.finalpoo.sistemavendas.repository;

import com.finalpoo.sistemavendas.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    @Query("SELECT CAST(p.dataHora AS date) as data, SUM(i.quantidade * i.precoUnitario) as totalVendas " +
            "FROM Pedido p JOIN p.itens i " +
            "WHERE p.dataHora BETWEEN :inicio AND :fim " +
            "GROUP BY CAST(p.dataHora AS date) " +
            "ORDER BY CAST(p.dataHora AS date)")
    List<Object[]> findVendasPorPeriodo(@Param("inicio") LocalDateTime inicio,
                                        @Param("fim") LocalDateTime fim);

    @Query("SELECT FUNCTION('YEAR', p.dataHora), FUNCTION('MONTH', p.dataHora), SUM(i.quantidade * i.precoUnitario) " +
            "FROM Pedido p JOIN p.itens i " +
            "WHERE FUNCTION('YEAR', p.dataHora) = :ano " +
            "GROUP BY FUNCTION('YEAR', p.dataHora), FUNCTION('MONTH', p.dataHora) " +
            "ORDER BY FUNCTION('MONTH', p.dataHora)")
    List<Object[]> findFaturamentoMensal(@Param("ano") int ano);

    @Query("""
    SELECT p.nome, SUM(i.quantidade) FROM Pedido pe
    JOIN pe.itens i
    JOIN i.produto p
    WHERE pe.dataHora BETWEEN :inicio AND :fim
    GROUP BY p.id, p.nome
    ORDER BY SUM(i.quantidade) DESC
""")
    List<Object[]> findTopProdutosMaisVendidosPorData(@Param("inicio") LocalDateTime inicio,
                                                      @Param("fim") LocalDateTime fim);

    @Query("SELECT COUNT(p) FROM Pedido p WHERE CAST(p.dataHora AS date) = CURRENT_DATE")
    long contarPedidosHoje();

    @Query("""
        SELECT COALESCE(SUM(i.quantidade * i.precoUnitario), 0)
        FROM ItemPedido i
        WHERE CAST(i.pedido.dataHora AS date) = CURRENT_DATE
    """)
    double calcularTotalVendasHoje();

}

