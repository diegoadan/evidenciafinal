import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public abstract class Persona {

    @XmlElement(name="NombreCompleto")
    public String NombreCompleto;

}
