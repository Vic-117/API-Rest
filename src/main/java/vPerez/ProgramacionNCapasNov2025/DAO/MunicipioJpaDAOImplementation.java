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
import vPerez.ProgramacionNCapasNov2025.JPA.Municipio;
import vPerez.ProgramacionNCapasNov2025.JPA.Result;

/**
 *
 * @author digis
 */
@Repository
public class MunicipioJpaDAOImplementation implements IMunicipioJPA {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    EntityManager entityManager;

    @Override
    public Result getByEstado(int idEstado) {
        Result result = new Result();
        try {
            String jpql = "SELECT mun FROM Municipio mun"
                    + " JOIN FETCH mun.estado e "
                    + "WHERE e.idEstado = :idEstado ";
            TypedQuery<Municipio> typedQuery = entityManager.createQuery(jpql, Municipio.class).setParameter("idEstado", idEstado);//Consulta
            List<Municipio> municipios = typedQuery.getResultList();//resultados

            result.Object = municipios;
            if (municipios == null) {
                result.StatusCode = 400;
            } else if (municipios.size() == 0) {
                result.StatusCode = 204;
                result.Correct = true;
            } else {
                result.StatusCode = 200;
                result.Correct = true;

            }



        } catch (Exception ex) {
            result.Correct = false;
            result.StatusCode = 500;
            result.ErrorMesagge = ex.getLocalizedMessage();
            result.ex = ex;
        }
        return result;
    }

}
