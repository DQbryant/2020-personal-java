import java.io.IOException;

/**
 * @author dqbryant
 */
public class Test {
    /**
     * 测试初始化
     */
    @org.junit.Test
    public void testInit(){
        Main.main(new String[]{"--init","D:\\json"});
    }

    /**
     * 测试 getPersonalThings
     * "-u","tschortsch","-e","PushEvent",输出是30
     * @throws IOException
     */
    @org.junit.Test
    public void testGetPersonalThings() throws IOException {
        int num = Main.getPersonalThings("tschortsch","PushEvent");
        assert num == 30;
    }

    /**
     * 测试 getRepoThings
     * "-r","fujimura/hi","-e","PushEvent",输出是27
     */


    @org.junit.Test
    public void testGetRepoThings() throws IOException {
        int num = Main.getRepoThings("fujimura/hi","PushEvent");
        assert num == 27;
    }

    /**
     * 测试 getPersonalAndRepoThings
     * "-r","tschortsch/gulp-bootlint","-u","tschortsch","-e","PushEvent",输出是21
     */
    @org.junit.Test
    public void testGetPersonalAndRepoThings() throws IOException {
        int num = Main.getPersonalAndRepoThings("tschortsch","tschortsch/gulp-bootlint","PushEvent");
        assert num == 21;
    }

    /**
     * 测试参数异常: 未传入参数
     */
    @org.junit.Test
    public void testArgs(){
        Main.main(new String[]{"-u"});
    }
}
