
import javax.xml.bind.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.security.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.Clock.system;

@XmlRootElement
public class Principal {

    @XmlElement
    public  HashMap<String, Doctor> doctores;
    @XmlElement
    public  HashMap<String, Paciente> pacientes;
    @XmlElement
    public  HashMap<String, Cita> citas;
    @XmlElement
    public  HashMap<String, Usuario> usuarios;

    Usuario loggedUser;

    static Principal principal = new Principal();

     final static String dbFilePath = "db/datos.xml";

    public static void main(String[] args){

        loadXml();
        if (principal!=null){
            try {
                principal.init();
            }catch(Exception ex){
                System.out.println("Saliendo de la app sin guardar los cambios, por favor use el menu para  salir!");
            }
        }else{
            System.out.println("Error al crear la clase principal!");
        }

    }

    public void init(){

        if (usuarios==null) usuarios = new  HashMap<String, Usuario>();
        if (doctores==null) doctores = new HashMap<String,Doctor>();
        if (pacientes ==null) pacientes = new HashMap<String, Paciente>();
        if (citas ==null) citas =  new HashMap<String, Cita>();

        Scanner scanner = new Scanner(System.in);

        System.out.println("Administracion de Citas - Evidencia Final - al02619826@tecmilenio.mx");
        while(true) {
            if (loggedUser == null) {
                if (usuarios.size() == 0) {
                    System.out.println("Registro por primera vez!");
                    registerUser();
                }else{
                    loginUser();
                }

            }else{
                printMainMenu();
                switch (readReadMenuChoice()){
                    case 1:
                        listarCitas();
                        break;
                    case 2:
                        listarDoctores();
                        break;
                    case 3:
                        listarPacientes();
                        break;
                    case 4:
                        registrarPaciente();
                        break;
                    case 5:
                        registrarDoctor();
                        break;
                    case 6:
                        registrarCita();
                        break;
                    case 7:
                        registerUser();
                        break;
                    case 8:
                        System.out.println("Good bye "+loggedUser.UserName+'!');
                        saveXml();
                        loggedUser = null;
                        break;
                    case 9:
                        saveXml();
                        System.out.println("Good bye!");
                        return;
                }
            }

            System.out.println("Presione ENTER para continuar");
            scanner.nextLine();
        }

    }

    int readReadMenuChoice(){
        while(true) {
            try {
                Scanner scanner = new Scanner(System.in);
                return scanner.nextInt();
            } catch (Exception ex) {
                System.out.println("Opción inválida, seleccione una opción!");
            }
        }

    }


    void listarCitas(){
        if (citas!=null){
            if (citas.size()>0) {
                System.out.println("Listado de citas:");
                citas.forEach((k, v) -> {
                    /*
                    String nombrePaciente = v.IdPaciente;
                    String nombreDoctor = v.IdDoctor;
                    if (pacientes.containsKey(v.IdPaciente)){
                        nombrePaciente = pacientes.get(v.IdPaciente).NombreCompleto;
                    }
                    if (doctores.containsKey(v.IdDoctor)){
                        nombreDoctor = doctores.get(v.IdDoctor).NombreCompleto;
                    }
                    */
                    System.out.println(
                            String.format("Id cita: %s, Paciente: %s, Motivo: %s, Doctor: %s, Fecha y hora: %s",
                                    v.Id, v.paciente.NombreCompleto, v.MotivoDeLaCita, v.doctor.NombreCompleto,
                                    v.FechaYHora)
                    );
                });
                System.out.println(Integer.toString(citas.size())+" Registros!");
            }else{
                System.out.println("No hay citas registradas!");
            }
        }
    }
    void listarPacientes(){
        if (pacientes!=null){
            if (pacientes.size()>0) {
                System.out.println("Listado de pacientes:");
                pacientes.forEach((k, v) -> {
                    System.out.println(
                            String.format("Id paciente: %s, Nombre completo: %s", v.Id, v.NombreCompleto)
                    );
                });
                System.out.println(Integer.toString(pacientes.size())+" Registros!");
            }else{
                System.out.println("No hay pacientes registrados!");
            }
        }
    }
    void listarDoctores(){
        if (doctores!=null){
            if (doctores.size()>0) {
                System.out.println("Listado de doctores:");
                doctores.forEach((k, v) -> {
                    System.out.println(
                            String.format("Id doctor: %s, Nombre completo: %s, Especialidad: %s", v.Id, v.NombreCompleto, v.Especialidad)
                    );
                });
                System.out.println(Integer.toString(doctores.size())+" Registros!");
            }else{
                System.out.println("No hay doctores registrados!");
            }
        }
    }

    void registerUser(){

        Usuario user = new Usuario();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Registro de nuevos usuarios Administradores");
        System.out.println("Nuevo usuario:");
        user.UserName = scanner.nextLine();
        System.out.println("Password:");
        user.Password = scanner.nextLine();

        if (user.UserName.length()>0 && user.Password.length()>0){
            usuarios.put(getUID(),user);
            System.out.println("Registro exitoso!");
        }else {
            System.out.println("Registro fallido!");
        }

    }
    void registrarCita(){

        if (pacientes.size()<1 || doctores.size()<1){
            System.out.println("Primero debe registrar al menos un paciente y un doctor!");
            return;
        }

        Cita cita = new Cita();
        Scanner scanner = new Scanner(System.in);
        cita.Id = getUID();
        int choice;
        System.out.println("Registro de citas, por favor seleccione un paciente: ");

        List<Map.Entry<String,Paciente>> pacientesList = pacientes.entrySet().stream().collect(Collectors.toList());
        List<Map.Entry<String,Doctor>> doctoresList = doctores.entrySet().stream().collect(Collectors.toList());

        for (int i = 0; i < pacientesList.size(); i++) {
            System.out.println(String.format("[%d] Nombre: %s",(i+1),pacientesList.get(i).getValue().NombreCompleto));
        }
        choice = readReadMenuChoice();
        if (choice >= 1 && choice <= pacientesList.size()){
            cita.paciente =  pacientesList.get(choice-1).getValue();
            System.out.println("Por favor seleccione un doctor: ");
            for (int i = 0; i < doctoresList.size(); i++) {
                System.out.println(String.format("[%d] Nombre: %s, Especialidad: %s",
                        (i+1),doctoresList.get(i).getValue().NombreCompleto,doctoresList.get(i).getValue().Especialidad));
            }
            choice = readReadMenuChoice();
            if (choice >= 1 && choice <= doctoresList.size()){
                cita.doctor = doctoresList.get(choice-1).getValue();

                System.out.println("Describa el motivo de la cita:");
                cita.MotivoDeLaCita = scanner.nextLine();


                boolean dateError=true;

                while(dateError) {
                    System.out.println("Ingrese la fecha y hora en formato dd/MM/yyyy hh:mm :");
                    cita.FechaYHora = scanner.nextLine();

                    try {
                        Date date1 = new SimpleDateFormat("dd/MM/yyyy hh:mm").parse(cita.FechaYHora);
                        dateError=false;
                    } catch (ParseException e) {
                        dateError=true;
                    }
                }

                citas.put(cita.Id,cita);
                System.out.println("Registro exitoso!");
                return;
            }

        }


        System.out.println("Registro fallido, intente nuevamente!");

    }

    void printMainMenu(){
        System.out.println("Menu principal del sistema de deministración de citas, seleccione:"+System.lineSeparator());
        System.out.println("Usuario: "+loggedUser.UserName);
        System.out.println("--");
        System.out.println("[1] Listar citas");
        System.out.println("[2] Listar doctores");
        System.out.println("[3] Listar pacientes");
        System.out.println("--");
        System.out.println("[4] Registrar paciente");
        System.out.println("[5] Registrar doctor");
        System.out.println("[6] Registrar cita");
        System.out.println("--");
        System.out.println("[7] Registrar nuevo usuario administrador");
        System.out.println("--");
        System.out.println("[8] Cerrar sesión");
        System.out.println("[9] Salir de aplicación");
        System.out.println("--");
        System.out.println("Opción:");
    }
    void loginUser() {
        Scanner scanner = new Scanner(System.in);
        Usuario user = new Usuario();
        System.out.println("Inicio de sesion, por favor ingrese su usuario y password");
        System.out.println("Nombre de usuario:");
        user.UserName = scanner.nextLine();
        System.out.println("Password:");
        user.Password = scanner.nextLine();
        usuarios.forEach((k,v)->{
            if (v.UserName.equals(user.UserName) && v.Password.equals(user.Password)){
                loggedUser = v;
                System.out.println("Bienvenido de nuevo "+v.UserName+'!');
                return;
            }
        });
    }



    void saveXml(){
        try {

            JAXBContext contexto = JAXBContext.newInstance(
                    principal.getClass() );
            Marshaller marshaller = contexto.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
                    Boolean.TRUE);
            try (OutputStream outputFile = new FileOutputStream(new File(dbFilePath))) {
                marshaller.marshal(principal, outputFile);
            }catch (Exception e){
                System.out.println("Error al crear el archivo de base datos XML...");
            }
        } catch (JAXBException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    static void  loadXml(){

        try {
            JAXBContext context = JAXBContext.newInstance(Principal.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            principal =  (Principal)(unmarshaller.unmarshal(
                    new File(dbFilePath) ));
        } catch (Exception e) {
            principal = new Principal();
            System.out.println("Error al cargar el archivo XML, se volverá a crear la base de datos!");
            File dbDir = new File("db");
            //if (!dbDir.exists()){
            try{
                dbDir.mkdirs();
            }catch(Exception ex){

            }
            //}
            //e.printStackTrace();

        }


    }


    public static String getUID(){

        return (
                Long.toString(System.currentTimeMillis()
                + new Random().nextInt(1000)
        ));
    }

}
