import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author dqbryant
 */
public class FileHandleThread implements Runnable{
    /**
     * 存主类的三个集合的引用
     */
    private Map<String,Result> userToResult;
    private Map<String,Result> repoToResult;
    private Map<String,Result> userAndRepoToResult;
    /**
     * 统一的文件夹中文件名的列表
     */
    private List<String> fileNames;
    /**
     * 文件夹的路径
     */
    private String path;
    /**
     * 当前线程解析的json文件在文件名列表中的下标
     */
    private int num;
    /**
     * 文件夹中文件的数量,直接等于列表的大小
     */
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

    /**
     * 判断事件类型是否符合要求
     * @param type 事件类型
     * @return 符合返回true
     */
    boolean typeCorrect(String type){
        return "PushEvent".equals(type)||"IssueCommentEvent".equals(type)||"IssuesEvent".equals(type)||"PullRequestEvent".equals(type);
    }

    /**
     * 线程的主方法，也就是解析json文件并将数据存储的方法
     */
    @Override
    public void run() {
        while(num<fileNum){
            try {
                //获得文件的行迭代器，可以逐行读取文件
                LineIterator it = FileUtils.lineIterator(new File(path+"/"+fileNames.get(num)),"UTF-8");
                //如果文件还未读完
                while (it.hasNext()){
                    //解析当前迭代器的一行，迭代器自动往下移动一行
                    JSONObject jsonObject = JSONObject.parseObject(it.nextLine());
                    //获得json的type属性
                    String type = jsonObject.getString("type");
                    //事件类型应该是四种事件中的一种
                    if(typeCorrect(type)){
                        //获得actor的login属性
                        String username = jsonObject.getJSONObject("actor").getString("login");
                        Result userResult = userToResult.get(username);
                        //先要判断集合中该用户的结果对象是否有被创建
                        if(userResult==null) {
                            //没有创建需要创建
                            userResult = new Result();
                            //增加该结果对象的对应事件的数量
                            userResult.inc(type);
                            //将新创建的对象放回map中
                            userToResult.put(username,userResult);
                        }else {
                            //如果集合中已经存在了该用户的对象,直接增加该结果对象的对应事件的数量即可
                            userResult.inc(type);
                        }
                        //同上,只是map的键变成了项目名repoName
                        String repoName = jsonObject.getJSONObject("repo").getString("name");
                        Result repoResult = repoToResult.get(repoName);
                        if(repoResult==null) {
                            repoResult = new Result();
                            repoResult.inc(type);
                            repoToResult.put(repoName,repoResult);
                        }else {
                            repoResult.inc(type);
                        }
                        //同上,只是map的键变成了用户名+项目名
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
            //执行完毕后(无论有无报错)下标自动加3,保证三个线程互不干扰,各自解析属于自己下标的json文件
            num+=3;
        }
    }
}
