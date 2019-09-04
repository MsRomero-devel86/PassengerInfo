
package passengerBean;

import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author Meghan
 */
@Named(value = "passengerBean")
//@ViewScoped
public class PassengerBeans 
{
    String pname;
    int pid;
    int age;

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
    
    /**
     * Creates a new instance of passengerBean
     */
    public PassengerBeans() {
    }
    
}
