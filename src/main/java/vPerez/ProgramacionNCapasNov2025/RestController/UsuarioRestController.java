/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vPerez.ProgramacionNCapasNov2025.RestController;

import jakarta.servlet.http.HttpSession;
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
@RestController
@RequestMapping("api/usuarios")
public class UsuarioRestController {

    @Autowired
    UsuarioJpaDAOImplementation usuarioJpaDaoImplementation;
    @Autowired
    private ValidationService ValidationService;

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
    public ResponseEntity updateUsuario(@RequestBody Usuario usuarioBody) {
        Result result = usuarioJpaDaoImplementation.update(usuarioBody);

        return ResponseEntity.status(result.StatusCode).body(result);

    }

    @DeleteMapping("{idUsuario}")
    public ResponseEntity deleteUsuario(@PathVariable("idUsuario") int idUsuario) {
        Result result = usuarioJpaDaoImplementation.delete(idUsuario);
        return ResponseEntity.status(result.StatusCode).body(result);
    }

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
    @PatchMapping("/{idUsuario}/{estatus}")
    public ResponseEntity bajaLogica(@PathVariable("idUsuario") int idUsuario, @PathVariable("estatus") int estatus) {
        Usuario usuarioBody = new Usuario();
        usuarioBody.setIdUsuario(idUsuario);
        usuarioBody.setEstatus(estatus);
        Result result = usuarioJpaDaoImplementation.softDelete(usuarioBody);
        return ResponseEntity.status(result.StatusCode).body(result);
    }

    @PostMapping("/Imagen/{idUsuario}")
    public ResponseEntity cambiarImagen(@RequestBody Usuario usuarioBody, @PathVariable("idUsuario") int idUsuario) {

        Result result = usuarioJpaDaoImplementation.updateFoto(usuarioBody);
        return ResponseEntity.status(result.StatusCode).body(result);
    }

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

        String nombre = (archivo.getOriginalFilename().split("\\.")[0])+fecha;
        MessageDigest md = MessageDigest.getInstance("SHA3-256");
        byte[] resultado = md.digest(nombre.getBytes());
        StringBuilder hexString = new StringBuilder();
        for(byte b : resultado){
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
            result.Correct = true;
            result.Object = new ArrayList<>();
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

    public void EscribirArchivo(String fecha, String token,String rutaCarga,Boolean estatus) throws IOException{
         String ruta = System.getProperty("user.dir")+"\\src\\main\\resources\\Logs\\logProcesamiento.txt";
        try( BufferedWriter escritor = new BufferedWriter(new FileWriter(ruta,true))){
            escritor.newLine();
            escritor.write(token+"|"+rutaCarga+"|"+fecha+"|"+"activo");
        }catch(Exception ex){
            System.out.println( ex.getCause() + ex.getLocalizedMessage());
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

    @GetMapping("/CargaMasiva/Procesar/{token}")
    public ResponseEntity ProcesarArchivo(@PathVariable("token")String token,HttpSession sesion) {
         //Recuperar el archivo guardado 
        
        //leer token y comparar
        
        // si el token es igual se puede procesar
        
        //si el token no es igual no procesar
        
        // si la fecha supera la fecha limite no procesar
        
        String rutaLog = System.getProperty("user.dir")+"\\src\\main\\resources\\Logs\\logProcesamiento.txt";
        try( BufferedReader lector = new BufferedReader(new FileReader(rutaLog))){
            String linea;
            String horaActual = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
             lector.readLine();
            while((linea = lector.readLine())!= null){
                String tokenArchivo = linea.split("\\|")[0];
                String tiempo = linea.split("\\|")[2].split(" ")[1];//cambiar, el formato está incorrecto
                
                if(tokenArchivo == token){
                    System.out.println(token);
                }else{
                
                }
                
            }
        }catch(Exception ex){
            System.out.println( ex.getCause() + ex.getLocalizedMessage());
        }
        
        //Obteniendo ruta del archivo que se registró en metodo CargaMasiva()
        String ruta = sesion.getAttribute("archivoCargaMasiva").toString();
        String extensionArchivo = new File(ruta).getName().split("\\.")[1];
//        Result result;

        if (extensionArchivo.equals("txt")) {
            List<Usuario> usuarios = LeerArchivo(new File(ruta));
//            usuarioDaoImplementation.AddMany(usuarios);
//            ModelMapper modelMapper = new ModelMapper();
//            List<vPerez.ProgramacionNCapasNov2025.JPA.Usuario> usuariosJPA = new ArrayList<>();
//            for (Usuario usuario : usuarios) {
//                vPerez.ProgramacionNCapasNov2025.JPA.Usuario usuarioJPA = modelMapper.map(usuario, vPerez.ProgramacionNCapasNov2025.JPA.Usuario.class);
//                usuariosJPA.add(usuarioJPA);
//            }
//            usuarioDaoImplementation.AddMany(usuarios);
//            Result resultCargaMasiva = usuarioJpaDAOImplementation.addMany(usuariosJPA);

        } else {
            //Guardando usuarios de la lista de usuarios creada con el metodo leer archivo
            List<Usuario> usuarios = LeerArchivoExcel(new File(ruta));
//            ModelMapper modelMapper = new ModelMapper();
//            List<vPerez.ProgramacionNCapasNov2025.JPA.Usuario> usuariosJPA = new ArrayList<>();
//            for (Usuario usuario : usuarios) {
//                vPerez.ProgramacionNCapasNov2025.JPA.Usuario usuarioJPA = modelMapper.map(usuario, vPerez.ProgramacionNCapasNov2025.JPA.Usuario.class);
//                usuariosJPA.add(usuarioJPA);
//            }
//            usuarioDaoImplementation.AddMany(usuarios);
//            usuarioJpaDAOImplementation.addMany(usuariosJPA);

        }
        sesion.removeAttribute("archivoCargaMasiva");
//        new File(ruta).delete();//Ya cuando se terminaron las operaciones con el archivo, se elimina de la carpeta

        return ResponseEntity.status(200).build();
    }

}
