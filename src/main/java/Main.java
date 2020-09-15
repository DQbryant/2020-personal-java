
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.cli.*;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author dqbryant
 */
@SuppressWarnings("all")
public class Main {
    /**
     * 用来存储用户名和结果类的映射
     * 使用并发的Map可以防止线程不安全的问题
     */
    static Map<String,Result> userToResult = new ConcurrentHashMap<>();
    /**
     * 用来存储仓库名(项目名)和结果类的映射
     */
    static Map<String,Result> repoToResult = new ConcurrentHashMap<>();
    /**
     * 用来存储用户名+项目名和结果类的映射
     */
    static Map<String,Result> userAndRepoToResult = new ConcurrentHashMap<>();

    /**
     * 获取个人的某个事件的数量
     * @param username  用户名
     * @param type 事件类型
     * @return 个人的某个事件的数量
     * @throws IOException 读取文件可能会出现IOException
     */
    static int getPersonalThings(String username,String type) throws IOException {
        //从持久化json中得到json
        JSONObject jsonObject = JSONObject.parseObject(FileUtils.readFileToString(new File("userToResult.json"),"UTF-8"));
        //从json中解析出结果对象
        Result result = jsonObject.getObject(username,Result.class);
        //注意防范结果不存在的空指针异常
        return result==null?0:result.getAttribute(type);
    }

    /**
     * 获取某个项目的某个事件的数量
     * @param repoName 项目名
     * @param type 事件类型
     * @return 某个项目的某个事件的数量
     * @throws IOException 同上
     */
    static int getRepoThings(String repoName,String type) throws IOException {
        JSONObject jsonObject = JSONObject.parseObject(FileUtils.readFileToString(new File("repoToResult.json"),"UTF-8"));
        Result result = jsonObject.getObject(repoName,Result.class);
        return result==null?0:result.getAttribute(type);
    }

    /**
     * 获取某个人某个项目的某个事件的数量
     * @param username 用户名
     * @param repoName 项目名
     * @param type 事件类型
     * @return 某个人某个项目的某个事件的数量
     * @throws IOException 同上
     */
    static int getPersonalAndRepoThings(String username,String repoName,String type) throws IOException {
        JSONObject jsonObject = JSONObject.parseObject(FileUtils.readFileToString(new File("userAndRepoToResult.json"),"UTF-8"));
        Result result = jsonObject.getObject(username+"_"+repoName,Result.class);
        return result==null?0:result.getAttribute(type);
    }

    /**
     * 主函数
     * @param args 用于用户传参
     */
    public static void main(String[] args) {
        //解析参数
        Options options = new Options();
        options.addOption("i","init",true,"pathToData")
                .addOption("u","user",true,"username")
                .addOption("e","event",true,"eventType")
                .addOption("r","repo",true,"repoName");
        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse( options, args);
            //如果参数中存在-i说明是初始化操作
            if(cmd.hasOption('i')){
                String path = cmd.getOptionValue('i');
                File file = new File(path);
//                init(path);单线程初始化方法,已删除
                //获得文件夹中的文件名的集合
                List<String> fileNames = Arrays.asList(Objects.requireNonNull(file.list()));
                ///创建三个线程共同解析文件夹中的json文件
                FileHandleThread fileHandleThread1 = new FileHandleThread(userToResult,repoToResult,userAndRepoToResult,fileNames,path,0);
                FileHandleThread fileHandleThread2 = new FileHandleThread(userToResult,repoToResult,userAndRepoToResult,fileNames,path,1);
                FileHandleThread fileHandleThread3 = new FileHandleThread(userToResult,repoToResult,userAndRepoToResult,fileNames,path,2);
                Thread thread = new Thread(fileHandleThread1);
                Thread thread1 = new Thread(fileHandleThread2);
                Thread thread2 = new Thread(fileHandleThread3);
                thread.start();
                thread1.start();
                thread2.start();
                thread.join();
                thread1.join();
                thread2.join();
                //将三个map中的数据存入各自的json文件中,实现持久化
                FileUtils.writeStringToFile(new File("userToResult.json"), JSON.toJSONString(userToResult),"UTF-8",false);
                FileUtils.writeStringToFile(new File("repoToResult.json"), JSON.toJSONString(repoToResult),"UTF-8",false);
                FileUtils.writeStringToFile(new File("userAndRepoToResult.json"), JSON.toJSONString(userAndRepoToResult),"UTF-8",false);
            }else {//参数中未存在i说明是查询操作
                String eventType = cmd.getOptionValue('e');
                if(eventType!=null) {
                    //如果有-u参数
                    if (cmd.hasOption('u')) {
                        String username = cmd.getOptionValue('u');
                        //如果有-u参数还有-r参数说明要查询某个用户某个项目的事件数量
                        if (cmd.hasOption('r')) {
                            String repoName = cmd.getOptionValue('r');
                            System.out.println(getPersonalAndRepoThings(username,repoName,eventType));
                        } else {//如果没有-r参数说明只查询了某个用户的事件数量
                            System.out.println(getPersonalThings(username,eventType));
                        }
                    } else {//如果没有-u参数说明只查询了某个项目的事件数量
                        String repoName = cmd.getOptionValue('r');
                        System.out.println(getRepoThings(repoName,eventType));
                    }
                }
            }
        } catch (ParseException | InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }
}
