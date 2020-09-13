import com.alibaba.fastjson.JSONObject;
import org.apache.commons.cli.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;


import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Main {
    static Map<String,Result> userToResult = new HashMap<>();
    static Map<String,Result> repoToResult = new HashMap<>();
    static Map<String,Result> userAndRepoToResult = new HashMap<>();
    static boolean typeCorrect(String type){
        return "PushEvent".equals(type)||"IssueCommentEvent".equals(type)||"IssuesEvent".equals(type)||"PullRequestEvent".equals(type);
    }
    static void init(String filePath){
        File file = new File(filePath);
        try {
            LineIterator it = FileUtils.lineIterator(file,"UTF-8");
            while (it.hasNext()){
                JSONObject jsonObject = JSONObject.parseObject(it.nextLine());
                String type = jsonObject.getString("type");
                if(typeCorrect(type)){
                    String username = jsonObject.getJSONObject("actor").getString("login");
                    Result userResult = userToResult.get(username);
                    if(userResult==null) {
                        userResult = new Result();
                        userResult.inc(type);
                        userToResult.put(username,userResult);
                    }else {
                        userResult.inc(type);
                    }
                    String repoName = jsonObject.getJSONObject("repo").getString("name");
                    Result repoResult = repoToResult.get(repoName);
                    if(repoResult==null) {
                        repoResult = new Result();
                        repoResult.inc(type);
                        repoToResult.put(repoName,repoResult);
                    }else {
                        repoResult.inc(type);
                    }
                    String name = username+"_"+repoName;
                    Result result = userAndRepoToResult.get(name);
                    if(result==null) {
                        result = new Result();
                        result.inc(type);
                        userAndRepoToResult.put(name,result);
                    }else {
                        result.inc(type);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    static int getPersonalThings(String username,String type){
        Result result = userToResult.get(username);
        return result==null?0:result.getAttribute(type);
    }
    static int getRepoThings(String repoName,String type){
        Result result = repoToResult.get(repoName);
        return result==null?0:result.getAttribute(type);
    }
    static int getPersonalAndRepoThings(String username,String repoName,String type){
        Result result = userAndRepoToResult.get(username+"_"+repoName);
        return result==null?0:result.getAttribute(type);
    }
    public static void main(String[] args) {
        Options options = new Options();
        options.addOption("i","init",true,"pathToData")
                .addOption("u","user",true,"username")
                .addOption("e","event",true,"eventType")
                .addOption("r","repo",true,"repoName");
        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse( options, args);
            if(cmd.hasOption('i')){
                init(cmd.getOptionValue('i'));
            }else {
                String eventType = cmd.getOptionValue('e');
                if(eventType!=null) {
                    if (cmd.hasOption('u')) {
                        String username = cmd.getOptionValue('u');
                        if (cmd.hasOption('r')) {
                            String repoName = cmd.getOptionValue('r');
                            System.out.println(getPersonalAndRepoThings(username,repoName,eventType));
                        } else {
                            System.out.println(getPersonalThings(username,eventType));
                        }
                    } else {
                        String repoName = cmd.getOptionValue('r');
                        System.out.println(getRepoThings(repoName,eventType));
                    }
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
