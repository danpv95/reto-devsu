package com.devsu.cuenta_movimiento_service.repository;

import com.devsu.cuenta_movimiento_service.entity.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {

    List<Movimiento> findByCuentaId(Long cuentaId);

    @Query("SELECT m FROM Movimiento m WHERE m.cuenta.id = :cuentaId AND CAST(m.fecha AS date) BETWEEN :inicio AND :fin")
    List<Movimiento> buscarPorCuentaYFechas(@Param("cuentaId") Long cuentaId,
                                            @Param("inicio") LocalDate inicio,
                                            @Param("fin") LocalDate fin);

}
