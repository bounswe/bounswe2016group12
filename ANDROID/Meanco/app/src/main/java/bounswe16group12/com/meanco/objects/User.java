package bounswe16group12.com.meanco.objects;

/**
 * Created by feper on 11/9/2016.
 */

public class User {
    public int userId;
    public String username;
    public String password;

    public User(){

    }

    public User( String username, String password){
        this.username = username;
        this.password = password;
    }
}
