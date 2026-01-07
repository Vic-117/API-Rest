/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vPerez.ProgramacionNCapasNov2025.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vPerez.ProgramacionNCapasNov2025.DAO.EstadoJpaDAOImplementation;
import vPerez.ProgramacionNCapasNov2025.JPA.Result;

/**
 *
 * @author digis
 */
@Tag(name ="EstadoRestController", description="Controlador que maneja las peticiones para Estado")
@RestController
@RequestMapping("api/estado")
public class EstadoRestController{
    
    @Autowired
    EstadoJpaDAOImplementation  estadoJpaDaoImplementation;
    
    
    @Operation(summary = "getByPais", description = "Obtiene el Estado por Pais")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "500", description = "Error desconocido al obtener los Estados"),
        @ApiResponse(responseCode = "400", description = "Error en la petición, solicitud incorrecta"),
        @ApiResponse(responseCode = "204", description = "Exito al resolver la solicitud pero no hay Estados"),
        @ApiResponse(responseCode = "200", description = "Exito al resolver la solicitud y trae los Estados del Pais")
    })
    @GetMapping("pais/{idPais}")
    public ResponseEntity getByPais(@PathVariable("idPais") int idPais){
        Result result = estadoJpaDaoImplementation.getByPais(idPais);
        return ResponseEntity.status(result.StatusCode).body(result);
    }
    
    
    
    
    
}
