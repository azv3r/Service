import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "appointment")
public class Appointment {

    private Integer id;
    private Integer docId;
    private Integer patId;
    private boolean appType;
    private String name;

    //STRING TO DATE???
    private String date;
    private String dateExpire;

    private String diagnosis;
    private Integer appResultId;
    private String notes;
    private String additionalFilePath;


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    public Integer getId()  {   return id;    }

    public void setId(Integer id) { this.id = id;   }
}
