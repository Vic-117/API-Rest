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
import org.springframework.transaction.annotation.Transactional;
import vPerez.ProgramacionNCapasNov2025.JPA.Colonia;
import vPerez.ProgramacionNCapasNov2025.JPA.Result;

/**
 *
 * @author digis
 */
@Repository
public class ColoniaJpaDAOImplementation implements IColoniaJPA {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    EntityManager entityManager;

    @Transactional
    @Override
    public Result getByMunicipio(int idMunicipio) {
        Result result = new Result();
        try {
            String jpql = "SELECT col FROM Colonia col"
                    + " JOIN FETCH col.municipio mun "
                    + "WHERE mun.idMunicipio = :idMunicipio";
            TypedQuery<Colonia> typedQuery = entityManager.createQuery(jpql, Colonia.class).setParameter("idMunicipio", idMunicipio);
            List<Colonia> colonias = typedQuery.getResultList();
            
            result.Object = colonias;
            
             if(colonias == null){
                 result.StatusCode = 400;
            }else if(colonias.size() == 0){
                 result.StatusCode = 204;
                  result.Correct = true;
            }else{
                result.StatusCode = 200;
                 result.Correct = true;
            
            }
      
          
        } catch (Exception ex) {
            result.Correct = false;
            result.ErrorMesagge = ex.getLocalizedMessage();
            result.ex = ex;
        }

        return result;

    }

}
