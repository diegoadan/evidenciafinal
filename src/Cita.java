import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Cita {

    @XmlElement(name="Id")
    public String Id;

    @XmlElement(name="MotivoDeLaCita")
    public String MotivoDeLaCita;

    @XmlElement(name="FechaYHora")
    public String FechaYHora;

    @XmlElement(name="IdPaciente")
    public Paciente paciente;

    @XmlElement(name="IdDoctor")
    public Doctor doctor;

}
