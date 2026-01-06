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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vPerez.ProgramacionNCapasNov2025.DAO.RolJpaDAOImplementation;
import vPerez.ProgramacionNCapasNov2025.JPA.Result;

/**
 *
 * @author digis
 */
@Tag(name="RolRestController",description = "Controlador rest del rol")
@RestController
@RequestMapping("api/rol")
public class RolRestController {
    @Autowired
    RolJpaDAOImplementation rolJpaDAOImplementation;
    
    @Operation(summary = "getAll", description="Obtiene todos los roles")
    @ApiResponses(value = {
           @ApiResponse(responseCode = "200", description="Peticion correcta, si hay roles"),
           @ApiResponse(responseCode = "204", description="Peticion correcta, pero no hay roles en la base de datos"),
           @ApiResponse(responseCode = "400", description="Error en la peticion"),
           @ApiResponse(responseCode = "500", description="Error desconocido")
        
    })
    @GetMapping
    public ResponseEntity getAll(){
        Result result = rolJpaDAOImplementation.getAll();
        return ResponseEntity.status(result.StatusCode).body(result);
    }
    
    
}
