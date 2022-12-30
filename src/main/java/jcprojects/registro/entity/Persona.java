package jcprojects.registro.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "persona")
public class Persona {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_persona")
    private Long idPersona;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 3, max = 30, message = "El nombre debe poseer como mínimo 3 caracteres y como máximo 30 caracteres")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(min = 2, max = 30, message = "El apellido debe poseer como mínimo 3 caracteres y como máximo 30 caracteres")
    private String apellido;

    @Min(value = 18, message = "La edad debe ser 18 o mayor")
    private int edad;

    @NotBlank(message = "El género es obligatorio")
    @Pattern(regexp = "^[M|F]$", message ="El género debe ser M o F")
    @Size(min = 1, max = 1, message ="El género debe poseer 1 caracter y ser M o F")
    private String genero;

    @NotBlank(message = "El DNI es obligatorio")
    @Size(min = 8, max = 8, message = "El DNI debe poseer 8 caracteres")
    private String dni;

    @NotBlank(message = "La provincia es obligatoria")
    private String provincia;
}