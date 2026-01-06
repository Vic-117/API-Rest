/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vPerez.ProgramacionNCapasNov2025.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vPerez.ProgramacionNCapasNov2025.DAO.DireccionJpaDAOImplementation;
import vPerez.ProgramacionNCapasNov2025.JPA.Direccion;
import vPerez.ProgramacionNCapasNov2025.JPA.Result;
import vPerez.ProgramacionNCapasNov2025.JPA.Usuario;

/**
 *
 * @author digis
 */
@Tag(name = "DireccionRestController", description = "Maneja peticiones relacionadas a direccion")
@RestController
@RequestMapping("api/direccion")
public class DireccionRestController {

    @Autowired
    DireccionJpaDAOImplementation direccionJpaDaoImplementation;

    @Operation(summary = "direccionAdd", description = "Agrega una direccion")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Direccion agregada"),
        @ApiResponse(responseCode = "400", description = "La direccion no se pudo agregar por algun fallo en algun campo"),
        @ApiResponse(responseCode = "500", description = "Fallo desconocido en el servidor")
    })
    @PostMapping("agregar/{idUsuario}")
    public ResponseEntity direccionAdd(@PathVariable("idUsuario") int idUsuario, @RequestBody Direccion direccionBody) {
        Result result = direccionJpaDaoImplementation.add(direccionBody, idUsuario);
        return ResponseEntity.status(result.StatusCode).build();
    }

    @Operation(summary = "direccionUpdate", description = "Edita una direccion")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Direccion actualziada"),
        @ApiResponse(responseCode = "404", description = "NO existe la direccion que se queire actualizar"),
        @ApiResponse(responseCode = "500", description = "Error desconocido en el servidor")
    })
    @PutMapping("{idDireccion}")
    public ResponseEntity direccionUpdate(@Valid @RequestBody Direccion direccionBody, @PathVariable("idDireccion") int idDireccion, BindingResult bindingResult) {

        Result result = new Result();
        if (bindingResult.hasErrors()) {
            result.Correct = false;
            result.StatusCode = 202;
            bindingResult.getFieldError();
            result.Object = "Tienes errores";
        } else {

            result = direccionJpaDaoImplementation.update(direccionBody);
        }
        return ResponseEntity.status(result.StatusCode).body(result);

    }

    @Operation(summary = "direccionDelete", description = "Borra una dirección del usuario")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario borrado con exito"),
        @ApiResponse(responseCode = "404", description = "El usuario que se quiere borrar no existe"),
        @ApiResponse(responseCode = "500", description = "Error desconocido en el servidor")
    })
    @DeleteMapping("{idDireccion}")
    public ResponseEntity direccionDelete(@PathVariable("idDireccion") int idDireccion) {
        Result result = direccionJpaDaoImplementation.delete(idDireccion);
        return ResponseEntity.status(result.StatusCode).body(result);
    }

}
