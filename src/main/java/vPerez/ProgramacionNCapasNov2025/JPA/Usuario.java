/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vPerez.ProgramacionNCapasNov2025.JPA;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author digis
 */
@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idusuario")
    private int idUsuario;

    @NotNull(message = "LLena el campo")
    @Size(min = 3, max = 50)
    @NotNull
    @Column(name = "username")
    private String nombre;

    @NotNull
    @Column(name = "apellidopaterno")
    private String apellidoPaterno;

    @NotNull
    @Column(name = "apellidomaterno")
    private String apellidoMaterno;

    @NotNull
    private String email;

    @NotNull
    private String password;

    @NotNull
    @Column(name = "fechanacimiento")
    private Date fechaNacimiento;

    @NotNull
    @JoinColumn(name = "idrol")
    @ManyToOne
    public Rol rol;

    @NotNull
    private String sexo;

    @NotNull
    private String telefono;

    @NotNull
    private String celular;

    @NotNull
    private String curp;

    private int estatus;
    private String imagen;

//    @OneToMany(mappedBy = "Usuario",  cascade = CascadeType.ALL,  orphanRemoval = true,fetch = FetchType.LAZY)//Con cacadeType.ALL las operaciones afectan tambien a las otras entidades
    @OneToMany(mappedBy = "Usuario", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)//Con cacadeType.ALL las operaciones afectan tambien a las otras entidades
    public List<Direccion> direcciones = new ArrayList<>();//Relacion del lado de 1, un usuario tiene muchas direcciones, esta vez inicializado

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

//    public Rol getRol() {
//        return rol;
//    }
//
//    public void setRol(Rol rol) {
//        this.rol = rol;
//    }
    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getCurp() {
        return curp;
    }

    public void setCurp(String curp) {
        this.curp = curp;
    }

    public int getEstatus() {
        return estatus;
    }

    public void setEstatus(int estatus) {
        this.estatus = estatus;
    }

//    public List<Direccion> getDirecciones() {
//        return direcciones;
//    }
//
//    public void setDirecciones(List<Direccion> direcciones) {
//        this.direcciones = direcciones;
//    }
    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

}
