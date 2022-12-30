package jcprojects.registro.repository;

import jcprojects.registro.entity.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, Long> {
    boolean existsByDni(String dni);
    boolean existsByProvincia(String provincia);
    Persona getById(Long id);
}