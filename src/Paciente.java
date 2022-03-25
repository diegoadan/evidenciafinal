import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Paciente extends Persona {
    @XmlElement(name="Id")
    public String Id;

}
