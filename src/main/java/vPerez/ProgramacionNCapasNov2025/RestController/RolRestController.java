/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vPerez.ProgramacionNCapasNov2025.RestController;

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
@RestController
@RequestMapping("api/rol")
public class RolRestController {
    @Autowired
    RolJpaDAOImplementation rolJpaDAOImplementation;
    
    @GetMapping
    public ResponseEntity getAll(){
        Result result = rolJpaDAOImplementation.getAll();
        return ResponseEntity.status(result.StatusCode).body(result);
    }
    
    
}
