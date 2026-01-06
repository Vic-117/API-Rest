/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vPerez.ProgramacionNCapasNov2025.DAO;

import jakarta.persistence.EntityManager;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import vPerez.ProgramacionNCapasNov2025.JPA.Direccion;
import vPerez.ProgramacionNCapasNov2025.JPA.Usuario;
import vPerez.ProgramacionNCapasNov2025.JPA.Result;

/**
 *
 * @author digis
 */
@Repository
public class DireccionJpaDAOImplementation implements IDireccionJPA {

    @Autowired
    private EntityManager entityManager;

    @Transactional
    @Override
    public Result update(Direccion direccion) {
        Result result = new Result();
        try {
            Direccion dir = entityManager.find(direccion.getClass(), direccion.getIdDireccion());
            direccion.Usuario = new Usuario();
            direccion.Usuario.setIdUsuario(dir.Usuario.getIdUsuario());
            if(dir !=null){
            entityManager.merge(direccion);
            result.Correct = true;
            result.StatusCode = 200;
            }else{
                result.Correct = false;
                result.StatusCode = 404;
            }

          

        } catch (Exception ex) {
            result.Correct = false;
            result.StatusCode = 500;
            result.ErrorMesagge = ex.getLocalizedMessage();
            result.ex = ex;
        }

        return result;
    }

    @Transactional
    @Override
    public Result delete(int idDireccion) {
        Result result = new Result();
        try {
            Direccion direccion = entityManager.find(new Direccion().getClass(), idDireccion);

            if (direccion != null) {
                entityManager.remove(direccion);
//                result.Object = "Operacion realizada con exito";
                result.Correct = true;
                result.StatusCode = 200;

            } else {
//                result.Object = "La operación fracasó con exito xd";
                result.Correct = false;
                result.StatusCode = 404;
            }
            

        } catch (Exception ex) {
              result.StatusCode = 500;
            result.Correct = false;
            result.ErrorMesagge = ex.getLocalizedMessage();
            result.ex = ex;
        }
        return result;
    }

    @Transactional
    @Override
    public Result add(Direccion direccion, int idUsuario) {
        Result result = new Result();
        try {
            
            if(direccion != null){
                direccion.Usuario = new Usuario();
                direccion.Usuario.setIdUsuario(idUsuario);
                entityManager.persist(direccion);
                result.Correct = true;
                  result.Object = "Realizado";
                  result.StatusCode = 201;
                
            }else{
                result.Correct = false;
                result.StatusCode = 400;
                 result.Object = "Fracaso";
            }

//            if (result.Correct) {
//                result.Object = "Realizado";
//                result.StatusCode = 201;
//            } else {
//                result.StatusCode = 500;
//                result.Object = "Fracaso";
//            }
        } catch (Exception ex) {
            result.StatusCode = 500;
            result.Correct = false;
            result.ErrorMesagge = ex.getLocalizedMessage();
            result.ex = ex;
        }
        return result;
    }

}
