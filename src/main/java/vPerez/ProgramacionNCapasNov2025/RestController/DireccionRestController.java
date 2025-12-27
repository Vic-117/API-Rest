/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vPerez.ProgramacionNCapasNov2025.RestController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
@RestController
@RequestMapping("api/direccion")
public class DireccionRestController {
      @Autowired
    DireccionJpaDAOImplementation direccionJpaDaoImplementation; 
      
      @PostMapping("agregar/{idUsuario}")
    public ResponseEntity direccionAdd(@PathVariable("idUsuario") int idUsuario, @RequestBody Direccion direccionBody){
        Result result = direccionJpaDaoImplementation.add(direccionBody, idUsuario);
        return ResponseEntity.status(result.StatusCode).build();
    }
    
    @PutMapping("editar")
    public ResponseEntity direccionUpdate( @RequestBody Usuario usuarioBody){
//        direccionBody.setIdDireccion(idDireccion);
//    usuarioBody.direcciones.add(new Direccion());
        Result result = direccionJpaDaoImplementation.update(usuarioBody.direcciones.get(0));
        return ResponseEntity.status(result.StatusCode).build();
    }
    
    @DeleteMapping("{idDireccion}")
    public ResponseEntity direccionDelete(@PathVariable("idDireccion") int idDireccion){
        Result result = direccionJpaDaoImplementation.delete(idDireccion);
        return ResponseEntity.status(result.StatusCode).body(result);
    }
    
    
}
