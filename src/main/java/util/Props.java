package util;

import java.util.ResourceBundle;

public class Props {

    public static String artifactory_url;
    public static String artifactory_username;
    public static String artifactory_password;
    public static String APP_TITLE = "Get Games OutCome";

    public static void loadProperties(String[] args){
        ResourceBundle rb = ResourceBundle.getBundle("config");
        artifactory_url = rb.getString("art.url");
        if(args.length > 0){
            artifactory_username = args[0];
            artifactory_password = args[1];
            return;
        }
        artifactory_username = rb.getString("art.uname");
        artifactory_password = rb.getString("art.pass");
    }

    public static boolean credsExists(){
        if(artifactory_username != null && artifactory_password != null){
            return true;
        }
        return false;
    }
}

