package Globals;

/**
 * A service class, its sole purpose is to check whether
 * the given Object is null or not, for reuse of code
 */

public class NotNull {

    public static void Check(Object o) throws Exception {
        if(o == null){
            throw new Exception("NO NULL OBJECTS ALLOWED!");
        }
    }



}
