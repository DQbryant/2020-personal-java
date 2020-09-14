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
     */
    @org.junit.Test
    public void testGetPersonalThings(){
        Main.main(new String[]{"--init","D:\\json"});
        Main.main(new String[]{"-u","tschortsch","-e","PushEvent"});
    }

    /**
     * 测试 getRepoThings
     * "-r","fujimura/hi","-e","PushEvent",输出是27
     */
    @org.junit.Test
    public void testGetRepoThings(){
        Main.main(new String[]{"--init","D:\\json"});
        Main.main(new String[]{"-r","fujimura/hi","-e","PushEvent"});
    }

    /**
     * 测试 getPersonalAndRepoThings
     * "-r","tschortsch/gulp-bootlint","-u","tschortsch","-e","PushEvent",输出是21
     */
    @org.junit.Test
    public void testGetPersonalAndRepoThings(){
        Main.main(new String[]{"--init","D:\\json"});
        Main.main(new String[]{"-r","tschortsch/gulp-bootlint","-u","tschortsch","-e","PushEvent"});
    }

    /**
     * 测试参数异常: 未传入参数
     */
    @org.junit.Test
    public void testArgs(){
        Main.main(new String[]{"-u"});
    }
}
