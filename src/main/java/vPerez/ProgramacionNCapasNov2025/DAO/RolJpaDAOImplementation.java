/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vPerez.ProgramacionNCapasNov2025.DAO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import vPerez.ProgramacionNCapasNov2025.JPA.Rol;
import vPerez.ProgramacionNCapasNov2025.JPA.Result;

/**
 *
 * @author digis
 */
@Repository
public class RolJpaDAOImplementation implements IRolJPA{

    @Autowired
    EntityManager entityManager;
    
    @Autowired
    ModelMapper modelMapper;
    
    @Override
    public Result getAll() {
        Result result = new Result();
        try{
            TypedQuery<Rol> typedQuery = entityManager.createQuery("FROM Rol", Rol.class);
       List<Rol> roles = typedQuery.getResultList(); //obteniendo los multiples resultados arrojados 
       
       result.Object = roles;
       
        if(roles == null){
                 result.StatusCode = 400;
            }else if(roles.size() == 0){
                 result.StatusCode = 204;
                  result.Correct = true;
            }else{
                result.StatusCode = 200;
                 result.Correct = true;
            
            }

        }catch(Exception ex){
            result.StatusCode = 500;
            result.Correct = false;
            result.ErrorMesagge = ex.getLocalizedMessage();
            result.ex = ex;
        }
        return result;
        
    }
    
    
}
