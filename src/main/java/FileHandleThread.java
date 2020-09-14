import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author dqbryant
 * @create 2020/9/13 21:10
 */
public class FileHandleThread implements Runnable{
    private Map<String,Result> userToResult;
    private Map<String,Result> repoToResult;
    private Map<String,Result> userAndRepoToResult;
    private List<String> fileNames;
    private String path;
    private int num;
    private int fileNum;

    public FileHandleThread(Map<String, Result> userToResult, Map<String, Result> repoToResult, Map<String, Result> userAndRepoToResult, List<String> fileNames, String path, int num) {
        this.userToResult = userToResult;
        this.repoToResult = repoToResult;
        this.userAndRepoToResult = userAndRepoToResult;
        this.fileNames = fileNames;
        this.path = path;
        this.num = num;
        this.fileNum = fileNames.size();
    }

    public FileHandleThread() {
    }

    boolean typeCorrect(String type){
        return "PushEvent".equals(type)||"IssueCommentEvent".equals(type)||"IssuesEvent".equals(type)||"PullRequestEvent".equals(type);
    }
    @Override
    public void run() {
        while(num<fileNum){
            try {
                LineIterator it = FileUtils.lineIterator(new File(path+"/"+fileNames.get(num)),"UTF-8");
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
            num+=3;
        }
    }
}
