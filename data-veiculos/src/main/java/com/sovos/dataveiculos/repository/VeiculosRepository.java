package com.sovos.dataveiculos.repository;

import com.sovos.dataveiculos.entity.Veiculos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VeiculosRepository extends JpaRepository<Veiculos, Integer> { }