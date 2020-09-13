import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author dqbryant
 * @create 2020/9/10 22:12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result {
    private int pushEventNum;
    private int issueCommentEventNum;
    private int issuesEventNum;
    private int pullRequestEventNum;
    public void inc(String type){
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
}
