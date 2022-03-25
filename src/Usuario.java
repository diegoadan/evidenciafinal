import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Usuario extends Persona {

    @XmlElement(name="UsuarName")
    public String UserName;

    @XmlElement(name="Password")
    public String Password;

}
