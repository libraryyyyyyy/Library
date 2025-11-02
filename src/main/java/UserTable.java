import java.util.Objects;

public class UserTable {

    user admin = new user ("admin1" , "1234");
    private  boolean log_in = false;

      boolean login(String name, String password){
         if (admin.getUser_name().equalsIgnoreCase(name.trim()) && admin.getPassword().equalsIgnoreCase(password.trim())){
             System.out.println( admin.getUser_name()+ " " + admin.getPassword());
            log_in = true;
        }
        return log_in;
    }
    boolean logout ( boolean state){
         if(state) {
             log_in = false;
             return state;
         }
         return false;
    }

}
