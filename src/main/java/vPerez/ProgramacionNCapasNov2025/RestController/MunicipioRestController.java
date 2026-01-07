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
import vPerez.ProgramacionNCapasNov2025.DAO.MunicipioJpaDAOImplementation;
import vPerez.ProgramacionNCapasNov2025.JPA.Result;

/**
 *
 * @author digis
 */
@Tag(name = "MunicipioRestController", description = "Controlador que maneja peticiones para Municipio")
@RestController
@RequestMapping("api/municipio")
public class MunicipioRestController {

    @Autowired
    MunicipioJpaDAOImplementation municipioJpaDAOImplementation;

    @Operation(summary = "getByEstado", description = "Obtiene los Municipios por Estado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "500", description = "Error desconocido al obtener los Municipios"),
        @ApiResponse(responseCode = "400", description = "Error en la petición, solicitud incorrecta"),
        @ApiResponse(responseCode = "204", description = "Exito al resolver la solicitud pero no hay Municipios guardados"),
        @ApiResponse(responseCode = "200", description = "Exito al resolver la solicitud y trae los Municipios del Estado")
    })
    @GetMapping("estado/{idEstado}")
    public ResponseEntity getByIdEstado(@PathVariable("idEstado") int idEstado) {
        Result result = municipioJpaDAOImplementation.getByEstado(idEstado);

        return ResponseEntity.status(result.StatusCode).body(result);

    }

}
