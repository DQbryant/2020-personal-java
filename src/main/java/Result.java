
import java.io.Serializable;

/**
 * @author dqbryant
 */
@SuppressWarnings("all")
public class Result implements Serializable {
    /**
     * 四种事件类型的数量
     */
    private int pushEventNum;
    private int issueCommentEventNum;
    private int issuesEventNum;
    private int pullRequestEventNum;
    /**
     * 根据事件类型增加对应的事件的数量
     * @param type 事件类型
     */
    public synchronized void inc(String type){
        if("PushEvent".equals(type)){
            this.pushEventNum++;
        }else if("IssueCommentEvent".equals(type)){
            this.issueCommentEventNum++;
        }else if("IssuesEvent".equals(type)){
            this.issuesEventNum++;
        }else {
            this.pullRequestEventNum++;
        }
    }
    @Override
    public String toString() {
        return "Result{" +
                "pushEventNum=" + pushEventNum +
                ", issueCommentEventNum=" + issueCommentEventNum +
                ", issuesEventNum=" + issuesEventNum +
                ", pullRequestEventNum=" + pullRequestEventNum +
                '}';
    }

    /**
     * 根据事件类型返回对应的事件的数量
     * @param type 事件类型
     * @return 对应的事件的数量
     */
    public int getAttribute(String type) {
        if("PushEvent".equals(type)){
            return this.pushEventNum;
        }else if("IssueCommentEvent".equals(type)){
            return this.issueCommentEventNum;
        }else if("IssuesEvent".equals(type)){
            return this.issuesEventNum;
        }else {
            return this.pullRequestEventNum;
        }
    }

    public Result() {
    }

    public Result(int pushEventNum, int issueCommentEventNum, int issuesEventNum, int pullRequestEventNum) {
        this.pushEventNum = pushEventNum;
        this.issueCommentEventNum = issueCommentEventNum;
        this.issuesEventNum = issuesEventNum;
        this.pullRequestEventNum = pullRequestEventNum;
    }

    public int getPushEventNum() {
        return pushEventNum;
    }

    public void setPushEventNum(int pushEventNum) {
        this.pushEventNum = pushEventNum;
    }

    public int getIssueCommentEventNum() {
        return issueCommentEventNum;
    }

    public void setIssueCommentEventNum(int issueCommentEventNum) {
        this.issueCommentEventNum = issueCommentEventNum;
    }

    public int getIssuesEventNum() {
        return issuesEventNum;
    }

    public void setIssuesEventNum(int issuesEventNum) {
        this.issuesEventNum = issuesEventNum;
    }

    public int getPullRequestEventNum() {
        return pullRequestEventNum;
    }

    public void setPullRequestEventNum(int pullRequestEventNum) {
        this.pullRequestEventNum = pullRequestEventNum;
    }
}
