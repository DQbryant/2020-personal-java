/**
 * @author dqbryant
 * @create 2020/9/13 16:33
 */
public class Test {
    @org.junit.Test
    public void testInit(){
        Main.main(new String[]{"--init","D:\\json"});
        Main.main(new String[]{"-r","tschortsch/gulp-bootlint","-u","tschortsch","-e","PushEvent"});
        Main.main(new String[]{"-u","tschortsch","-e","PushEvent"});
        Main.main(new String[]{"-r","fujimura/hi","-e","PushEvent"});
    }
}
