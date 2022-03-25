
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Doctor extends Persona {

    @XmlElement(name="Especialidad")
    public String Especialidad;

    @XmlElement(name="Id")
    public String Id;

}
