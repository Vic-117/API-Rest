/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vPerez.ProgramacionNCapasNov2025.RestController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vPerez.ProgramacionNCapasNov2025.DAO.DireccionJpaDAOImplementation;
import vPerez.ProgramacionNCapasNov2025.DAO.PaisJpaDAOImplementation;
import vPerez.ProgramacionNCapasNov2025.DAO.UsuarioJpaDAOImplementation;
import vPerez.ProgramacionNCapasNov2025.JPA.Direccion;
import vPerez.ProgramacionNCapasNov2025.JPA.Result;
import vPerez.ProgramacionNCapasNov2025.JPA.Usuario;

/**
 *
 * @author digis
 */
@RestController
@RequestMapping("api/usuarios")
public class UsuarioRestController {

    @Autowired
    UsuarioJpaDAOImplementation usuarioJpaDaoImplementation;
    
    @GetMapping
    public ResponseEntity getAll() {
        Result result = usuarioJpaDaoImplementation.getAll();
        return ResponseEntity.status(result.StatusCode).body(result);
    }

    @GetMapping("{idUsuario}")
    public ResponseEntity getById(@PathVariable("idUsuario") int idUsuario) {
        Result result = usuarioJpaDaoImplementation.getDireccionUsuarioById(idUsuario);

        return ResponseEntity.status(result.StatusCode).body(result);

    }

    @PostMapping
    public ResponseEntity addUsuario(@RequestBody Usuario body) {
        Result result = usuarioJpaDaoImplementation.add(body);

        return ResponseEntity.status(result.StatusCode).body(result.Correct);
    }
    
    @PutMapping("{idUsuario}")
    public ResponseEntity updateUsuario(@RequestBody Usuario usuarioBody){
        Result result = usuarioJpaDaoImplementation.update(usuarioBody);
        
        return ResponseEntity.status(result.StatusCode).body(result);
        
    }
    
    @DeleteMapping("{idUsuario}")
    public ResponseEntity deleteUsuario(@PathVariable("idUsuario") int idUsuario){
        Result result = usuarioJpaDaoImplementation.delete(idUsuario);
        return ResponseEntity.status(result.StatusCode).body(result);
    }
    
    @PostMapping("/busqueda")
    public ResponseEntity busquedaAbierta(@RequestBody Usuario usuarioBody){
        Result result = usuarioJpaDaoImplementation.GetAllDinamico(usuarioBody);
        
        return ResponseEntity.status(result.StatusCode).body(result);
    }
    
//    @PatchMapping("{idUsuario}")
//    public ResponseEntity bajaLogica(@PathVariable("idUsuario") int idUsuario, @RequestBody Usuario usuarioBody){
//        usuarioBody.setIdUsuario(idUsuario);
//        Result result = usuarioJpaDaoImplementation.softDelete(usuarioBody);
//        return ResponseEntity.status(result.StatusCode).build();
//    }
    
    @PatchMapping("/{idUsuario}/{estatus}")
    public ResponseEntity bajaLogica(@PathVariable("idUsuario") int idUsuario, @PathVariable("estatus") int estatus) {
        Usuario usuarioBody = new Usuario();
        usuarioBody.setIdUsuario(idUsuario);
        usuarioBody.setEstatus(estatus);
        Result result = usuarioJpaDaoImplementation.softDelete(usuarioBody);
        return ResponseEntity.status(result.StatusCode).body(result);
    }
    
    
    
    
    

}
 