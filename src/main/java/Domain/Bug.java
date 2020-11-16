package Domain;


import javax.persistence.*;
import java.io.Serializable;

public class Bug implements Serializable {
    private Integer id;

    private String name;
    private String description;
    private Integer testerID;
    private String status;

    public Bug(){

    }

    public Bug(Integer id, String name, String description, Integer testerId, String status){
        this.id = id;
        this.name = name;
        this.description = description;
        this.testerID = testerId;
        this.status = status;

    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return this.id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getTesterID() {
        return testerID;
    }

    public void setTesterID(Integer testerID) {
        this.testerID = testerID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
