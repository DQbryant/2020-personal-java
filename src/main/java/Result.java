import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author dqbryant
 * @create 2020/9/10 22:12
 */
public class Result implements Serializable {
    private int pushEventNum;
    private int issueCommentEventNum;
    private int issuesEventNum;
    private int pullRequestEventNum;
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
