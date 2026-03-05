public class Authentication {

    public boolean login(User user, String username, String password) {

        if(user.getUsername().equals(username) && 
           user.getPassword().equals(password)){
            return true;
        }

        return false;
    }
}
