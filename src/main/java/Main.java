import com.alibaba.fastjson.JSONObject;
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
    public static void main(String[] args) {
        File file = new File("src/main/java/2015-01-01.json");
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
}
