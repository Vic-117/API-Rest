/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vPerez.ProgramacionNCapasNov2025.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import vPerez.ProgramacionNCapasNov2025.DAO.DireccionJpaDAOImplementation;
import vPerez.ProgramacionNCapasNov2025.DAO.PaisJpaDAOImplementation;
import vPerez.ProgramacionNCapasNov2025.DAO.UsuarioJpaDAOImplementation;
import vPerez.ProgramacionNCapasNov2025.JPA.Colonia;
import vPerez.ProgramacionNCapasNov2025.JPA.Direccion;
import vPerez.ProgramacionNCapasNov2025.JPA.Result;
import vPerez.ProgramacionNCapasNov2025.JPA.Usuario;
import vPerez.ProgramacionNCapasNov2025.JPA.ErrorCarga;
import vPerez.ProgramacionNCapasNov2025.JPA.Rol;
import vPerez.ProgramacionNCapasNov2025.service.ValidationService;

/**
 *
 * @author digis
 */
@Tag(name = "UsuarioRestController", description = "maneja las peticiones que engloban al usuario en el sistema")
@RestController
@RequestMapping("api/usuarios")
public class UsuarioRestController {

    @Autowired
    UsuarioJpaDAOImplementation usuarioJpaDaoImplementation;
    @Autowired
    private ValidationService ValidationService;

    @Operation(summary = "getAll", description = "Obtiene todos los usuarios")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "500", description = "Ha sucedio un error desconocido que puede tener que ver con la petición"),
        @ApiResponse(responseCode = "400", description = "Peticion incorrecta, por lo que no se encontraron usuarios"),
        @ApiResponse(responseCode = "204", description = "Peticion correcta, pero no se encontraron usuarios"),
        @ApiResponse(responseCode = "200", description = "Todo correcto")
    })
    @GetMapping
    public ResponseEntity getAll() {
        Result result = usuarioJpaDaoImplementation.getAll();
        return ResponseEntity.status(result.StatusCode).body(result);
    }
    
    @Operation(summary = "getById",description = "Obtiene el usuario por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Correcto al cargar"),
        @ApiResponse(responseCode = "400", description = "Algo falló en la petición"),
        @ApiResponse(responseCode = "404", description = "No hay usuarios que mostrar")
    })
    @GetMapping("{idUsuario}")
    public ResponseEntity getById(@PathVariable("idUsuario") int idUsuario) {
        Result result = usuarioJpaDaoImplementation.getDireccionUsuarioById(idUsuario);

        return ResponseEntity.status(result.StatusCode).body(result);

    }

    @Operation(summary = "addUsuario", description = "Agrega un nuevo usuario")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Usuario guardado"),
        @ApiResponse(responseCode = "400", description = "Hay algun error con la información"),
        @ApiResponse(responseCode = "500", description = "Hay algun error en el servidor")
    })
    @PostMapping
    public ResponseEntity addUsuario(@RequestBody Usuario body) {
        Result result = usuarioJpaDaoImplementation.add(body);

        return ResponseEntity.status(result.StatusCode).body(result.Correct);
    }

    @Operation(summary = "updateUsuario",description="actualiza el usuario")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description="Actualizado"),
        @ApiResponse(responseCode = "400", description="No se pudo actualizar, falta algún dato o estan incorrectos"),
        @ApiResponse(responseCode = "500", description="Error desconocido en el servidor")
    })
    @PutMapping("{idUsuario}")
    public ResponseEntity updateUsuario(@RequestBody Usuario usuarioBody) {
        Result result = new Result();
//            result.Correct = false;
//            result.StatusCode = 202;
            result = usuarioJpaDaoImplementation.update(usuarioBody);
            return ResponseEntity.status(result.StatusCode).body(result);

    }

    @Operation(summary = "deleteUsuario", description="Borra el usuario")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",description="Usuario eliminado"),
        @ApiResponse(responseCode = "404",description="Usuario no encontrado por lo que no se pudo eliminar"),
        @ApiResponse(responseCode = "500",description="Algo falló en el seridor"),
    })
    @DeleteMapping("{idUsuario}")
    public ResponseEntity deleteUsuario(@PathVariable("idUsuario") int idUsuario) {
        Result result = usuarioJpaDaoImplementation.delete(idUsuario);
        return ResponseEntity.status(result.StatusCode).body(result);
    }

    @Operation(summary = "busquedaAbierta",description = "Busca el usuario por nombre, apellidos y rol")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description="Busqueda exitosa con usuarios encontrados"),
        @ApiResponse(responseCode = "204", description="La busqueda se realizó con exito pero no hay ningun usuario que coincida con los parametros especificados"),
        @ApiResponse(responseCode = "400", description="Algun parametro fue llenado erroneamente por lo que no se pudo completar la busqueda"),
        @ApiResponse(responseCode = "500", description="Error desconocido en el servidor")
    })
    @PostMapping("/busqueda")
    public ResponseEntity busquedaAbierta(@RequestBody Usuario usuarioBody) {
        Result result = usuarioJpaDaoImplementation.GetAllDinamico(usuarioBody);

        return ResponseEntity.status(result.StatusCode).body(result);
    }

//    @PatchMapping("{idUsuario}")
//    public ResponseEntity bajaLogica(@PathVariable("idUsuario") int idUsuario, @RequestBody Usuario usuarioBody){
//        usuarioBody.setIdUsuario(idUsuario);
//        Result result = usuarioJpaDaoImplementation.softDelete(usuarioBody);
//        return ResponseEntity.status(result.StatusCode).build();
//    }
    
    @Operation(summary = "bajaLogica", description = "Desactiva el usuario pero no lo elimina de la base de datos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "500", description = "Error desconocido en el servidor"),
        @ApiResponse(responseCode = "404", description = "Estatus Incorrecto"),
        @ApiResponse(responseCode = "200", description = "Desactivacion realizada")
    })
    @PatchMapping("/{idUsuario}/{estatus}")
    public ResponseEntity bajaLogica(@PathVariable("idUsuario") int idUsuario, @PathVariable("estatus") int estatus) {
        Usuario usuarioBody = new Usuario();
        usuarioBody.setIdUsuario(idUsuario);
        usuarioBody.setEstatus(estatus);
        Result result = usuarioJpaDaoImplementation.softDelete(usuarioBody);
        return ResponseEntity.status(result.StatusCode).body(result);
    }

    @Operation(summary = "cambiarImagen", description="Actualiza solo la imagen del usuario")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description="Foto actualizado"),
        @ApiResponse(responseCode = "404", description="Usuario inexistente"),
        @ApiResponse(responseCode = "400", description="Peticion no valida")
    })
    @PostMapping("/Imagen/{idUsuario}")
    public ResponseEntity cambiarImagen(@RequestBody Usuario usuarioBody, @PathVariable("idUsuario") int idUsuario) {

        Result result = usuarioJpaDaoImplementation.updateFoto(usuarioBody);
        return ResponseEntity.status(result.StatusCode).body(result);
    }

    @Operation(summary = "CargaMasiva", description="Carga del archivo")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "archivo guardado en el servidor"),
        @ApiResponse(responseCode = "422", description = "Error en los datos cargados en el archivo"),
        @ApiResponse(responseCode = "500", description = "Falla al guardar el archivo")
        
    })
    @PostMapping("/CargaMasiva")
    public ResponseEntity CargaMasiva(@RequestParam("archivo") MultipartFile archivo, Model model, HttpSession sesion) throws IOException, NoSuchAlgorithmException {

        //CARGA DE ARCHIVOS
        //divide el nombre del archivo en 2 partes, una es el nombre y la otra es despues del punto(extension) 
        //Para revisar que sea la extensión solicitada
        String extension = archivo.getOriginalFilename().split("\\.")[1];

        //Obteniendo la ruta base la que viene del disco del sistema
        String ruta = System.getProperty("user.dir");

        // Ruta desde el proyecto
        String rutaArchivo = "src\\main\\resources\\archivos";

        //Obteniendo la fecha para que sirva de id
        String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String fechaLog = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));

        //Esta es la ruta absoluta del archivo(donde se va a guardar en el proyecto)
        String rutaAbsoluta = ruta + "/" + rutaArchivo + "/" + fecha + archivo.getOriginalFilename();

//                                                                   ENCRIPTACION DEL NOMBRE 
        String nombre = (archivo.getOriginalFilename().split("\\.")[0]) + fecha;
        MessageDigest md = MessageDigest.getInstance("SHA3-256");
        byte[] resultado = md.digest(nombre.getBytes());
        StringBuilder hexString = new StringBuilder();
        for (byte b : resultado) {
            hexString.append(String.format("%02x", b));
        }

        String nombreEncriptado = hexString.toString();

        //GUARDADO DEL ARCHIVO
        archivo.transferTo(new File(rutaAbsoluta));
//        Files.copy(archivo.getInputStream(), Paths.get(rutaAbsoluta));
        List<Usuario> usuarios = new ArrayList<>();

        //¿Cual archivo debe leer?
        if (extension.equals("txt")) {
            usuarios = LeerArchivo(new File(rutaAbsoluta));
        } else {
            usuarios = LeerArchivoExcel(new File(rutaAbsoluta));
        }

        EscribirArchivo(fechaLog, nombreEncriptado, rutaAbsoluta, Boolean.TRUE);
        //validacion de archivo                                                 
        List<ErrorCarga> errores = validarDatos(usuarios);
        Result result = new Result();
//        model.addAttribute("Errores", errores);
        result.Objects = new ArrayList<>();
        if (!errores.isEmpty()) {
            result.Correct = false;
//            result.Object = new ArrayList<>();
            result.Object = errores;
//            model.addAttribute("Errores", errores);//Mandando errores
//            model.addAttribute("isError", true);
//               result.StatusCode = 400;
        } else {
//            model.addAttribute("isError", false);
            result.Correct = true;
            result.Objects.add(nombreEncriptado);
            result.StatusCode = 200;
            sesion.setAttribute("archivoCargaMasiva", rutaAbsoluta);//Añadiendo atributos a la ruta
        }

        return ResponseEntity.status(result.StatusCode).body(result);
    }

    public void EscribirArchivo(String fecha, String token, String rutaCarga, Boolean estatus) throws IOException {
        String ruta = System.getProperty("user.dir") + "\\src\\main\\resources\\Logs\\logProcesamiento.txt";
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(ruta, true))) {
            escritor.newLine();
            escritor.write(token + "|" + rutaCarga + "|" + fecha + "|" + "activo");
        } catch (Exception ex) {
            System.out.println(ex.getCause() + ex.getLocalizedMessage());
        }
    }

    public List<Usuario> LeerArchivo(File archivo) {//
        List<Usuario> usuarios = new ArrayList<>();
        try (
                //                InputStream inputStream = archivo.getInputStream(); //inpuStream lee los bytes de un archivo, en este caso el archivo que le estamos indicando
                //Lee texto desde un archivo de entrada(nuestro input stream):
                //                 BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {

                BufferedReader bufferedReader = new BufferedReader(new FileReader(archivo))) {

            bufferedReader.readLine(); //solo lee el encabezado que añadimos al txt
            String linea;
            while ((linea = bufferedReader.readLine()) != null) {
                //datos representa cada columna(campo)
                String[] datos = linea.split("\\|");

                Usuario usuario = new Usuario();

                usuario.setNombre(datos[0].trim());
                usuario.setApellidoPaterno(datos[1].trim());
                usuario.setApellidoMaterno(datos[2].trim());
                usuario.setEmail(datos[3].trim());
                usuario.setPassword(datos[4].trim());
//                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
                usuario.setFechaNacimiento(formato.parse(datos[5]));
                usuario.rol = new Rol();
                usuario.rol.setIdRol(Integer.valueOf(datos[6].trim())); // le quité lso espacios para que fuese un formato que pueda transformar
                usuario.setSexo(datos[7].trim());
                usuario.setTelefono(datos[8].trim());
                usuario.setCelular(datos[9].trim());
                usuario.setCurp(datos[10].trim());
                usuario.direcciones = new ArrayList<>();
                usuario.direcciones.add(new Direccion());
                usuario.direcciones.get(0).setCalle(datos[11].toString().trim());
                usuario.direcciones.get(0).setNumeroInterior(datos[12].toString().trim());
                usuario.direcciones.get(0).setNumeroExterior(datos[13].toString().trim());
                usuario.direcciones.get(0).colonia = new Colonia();
                usuario.direcciones.get(0).colonia.setIdColonia(Integer.valueOf(datos[14].trim()));

                usuarios.add(usuario);

                System.out.println("leyendo datos: " + linea);
            }

        } catch (Exception ex) {
            System.out.println(ex.getLocalizedMessage());
        }
        return usuarios;
    }

    public List<Usuario> LeerArchivoExcel(File archivo) {
        List<Usuario> usuarios = new ArrayList<>();
//Cambió de archivo.getInputStream() a archivo
        try (XSSFWorkbook workbook = new XSSFWorkbook(archivo)) {
            XSSFSheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
//                if (row.getRowNum() == 0) {
//                    System.out.println("Encabezados");
//                } else {

                Usuario usuario = new Usuario();
                usuario.setNombre(row.getCell(0).toString());
                usuario.setApellidoPaterno(row.getCell(1).toString());
                usuario.setApellidoMaterno(row.getCell(2).toString());
                usuario.setEmail(row.getCell(3).toString());
                usuario.setPassword(row.getCell(4).toString());
                SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
                usuario.setFechaNacimiento(formatoFecha.parse(row.getCell(5).toString()));
                usuario.rol = new Rol();
                int IdRol = Integer.parseInt(row.getCell(6).toString());
                usuario.rol.setIdRol(IdRol);
                //  usuario.rol.setIdRol((Float.valueOf(row.getCell(6).toString().trim())).intValue());
                usuario.setSexo(row.getCell(7).toString());  //datos nulos
                usuario.setTelefono(row.getCell(8).toString());//
                usuario.setCelular(row.getCell(9).toString());
                usuario.setCurp(row.getCell(10).toString());
                usuario.direcciones = new ArrayList<>();
                usuario.direcciones.add(new Direccion());
                usuario.direcciones.get(0).setCalle(row.getCell(11).toString());
                usuario.direcciones.get(0).setNumeroInterior(row.getCell(12).toString());
                usuario.direcciones.get(0).setNumeroExterior(row.getCell(13).toString());
                usuario.direcciones.get(0).colonia = new Colonia();
                usuario.direcciones.get(0).colonia.setIdColonia(Integer.parseInt(row.getCell(14).toString()));
                usuarios.add(usuario);
//                }

            }

        } catch (Exception ex) {
            System.out.println(ex.getCause() + " :" + ex.getLocalizedMessage());
        }
        return usuarios;
    }

    //Lista de errores (contendrá todos los atributos de la clase)
    public List<ErrorCarga> validarDatos(List<Usuario> usuarios) {
        List<ErrorCarga> erroresCarga = new ArrayList<>();//Se almacenarán todos los errores
        int lineaError = 0;

        //Iterando sobre la lista que le pasamos al metodo como argumento
        for (Usuario usuario : usuarios) {
            List<ObjectError> errors = new ArrayList();
            lineaError++;
            BindingResult bindingResultUsuario = ValidationService.validateObjects(usuario);//validando cada usuario
            if (bindingResultUsuario.hasErrors()) {
                errors.addAll(bindingResultUsuario.getAllErrors());
            }
            if (usuario.direcciones.get(0) != null) {
                BindingResult bindingDireccion = ValidationService.validateObjects(usuario.direcciones.get(0));
                if (bindingDireccion.hasErrors()) {
                    errors.addAll(bindingDireccion.getAllErrors());
                }
            }
//            List<ObjectError> errores = bindingResult.getAllErrors(); //Obteniendo los errores y guardandolos

            for (ObjectError error : errors) {
                FieldError fieldError = (FieldError) error;//obteniendo cada error especifico en cada campo(field)
                ErrorCarga errorCarga = new ErrorCarga();//Instancia de DTO ErrorCarga
                errorCarga.linea = lineaError;
                errorCarga.campo = fieldError.getField();//obtiendo el campo del error
                errorCarga.descripcion = fieldError.getDefaultMessage();//guardando mensaje de error
                erroresCarga.add(errorCarga); //Guardando cada error en la lista de errores
            }
        }

//        model.addAttribute("Errores",erroresCarga);
        return erroresCarga;
    }

    @Operation(summary = "ProcesarArchivo", description = "Encargado de guardar los datos del archivo en la base de datos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",description = "Usuarios guardados en la base de datos"),
        @ApiResponse(responseCode = "404",description = "Error durante el almacenamiento de los datos, usuarios no guardados"),
        @ApiResponse(responseCode = "500",description = "Error desconocido en el servidor")
    } )
    @PostMapping("/CargaMasiva/Procesar/{token}")
    public ResponseEntity ProcesarArchivo(@PathVariable("token") String token, HttpSession sesion) {
        //Recuperar el archivo guardado 

        //leer token y comparar
        // si el token es igual se puede procesar
        //si el token no es igual no procesar
        // si la fecha supera la fecha limite no procesar
        Result resultAdd = new Result();
        String rutaLog = System.getProperty("user.dir") + "\\src\\main\\resources\\Logs\\logProcesamiento.txt";
        try (BufferedReader lector = new BufferedReader(new FileReader(rutaLog))) {
            String linea;
//            String horaActualString = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
            LocalTime horaActual = LocalTime.now();
            lector.readLine();
            while ((linea = lector.readLine()) != null) {
                String tokenArchivo = linea.split("\\|")[0];
                LocalTime tiempo = LocalTime.parse((linea.split("\\|")[2].split(" ")[1]), DateTimeFormatter.ofPattern("HH:mm:ss"));

                if (tokenArchivo.equals(token)) {
                    System.out.println(token);
                    if (horaActual.isBefore(tiempo.plusMinutes(2L).plusSeconds(30L))) {
                        String ruta = (linea.split("\\|")[1]);
                        System.out.println("dentro del tiempo" + "\n");

                        String extensionArchivo = new File(ruta).getName().split("\\.")[1];

                        if (extensionArchivo.equals("txt")) {
                            List<Usuario> usuarios = LeerArchivo(new File(ruta));
                            resultAdd = usuarioJpaDaoImplementation.addMany(usuarios);

                        } else {
                            //Guardando usuarios de la lista de usuarios creada con el metodo leer archivo
                            List<Usuario> usuarios = LeerArchivoExcel(new File(ruta));

                            resultAdd = usuarioJpaDaoImplementation.addMany(usuarios);

                        }

                    } else {
                        System.out.println("Fuera de tiempo");
                        resultAdd.Object = "Excediste el tiempo limite para subir el archivo";
                        resultAdd.StatusCode = 400;

                    }
                    break;
                } else {
                    resultAdd.Object = "token no valido";
                }

            }
        } catch (Exception ex) {
            System.out.println(ex.getCause() + ex.getLocalizedMessage());
        }

//        new File(ruta).delete();//Ya cuando se terminaron las operaciones con el archivo, se elimina de la carpeta
        return ResponseEntity.status(resultAdd.StatusCode).body(resultAdd);
    }

}
