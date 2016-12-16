package bounswe16group12.com.meanco.objects;

/**
 * Created by feper on 11/9/2016.
 */

public class User {
    public int userId;
    public String username;
    public String password;

    public User(){}//Empty constructor to define

    /**
     * Note that we post e-mail on reqister, but we do not store it on local db.
     * @param userId Unique user ID which will be received by post request.
     * @param username Username.
     * @param password Password.
     */
    public User(int userId, String username, String password){
        this.userId = userId;
        this.username = username;
        this.password = password;
    }

    public User(String username, String password){
        //this.userId = userId;
        this.username = username;
        this.password = password;
    }
}
