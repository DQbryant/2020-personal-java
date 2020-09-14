
import org.apache.commons.cli.*;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Main {
    static Map<String,Result> userToResult = new ConcurrentHashMap<>();
    static Map<String,Result> repoToResult = new ConcurrentHashMap<>();
    static Map<String,Result> userAndRepoToResult = new ConcurrentHashMap<>();
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
                String path = cmd.getOptionValue('i');
                File file = new File(path);
//                init(path);
                List<String> fileNames = Arrays.asList(file.list());
                FileHandleThread fileHandleThread1 = new FileHandleThread(userToResult,repoToResult,userAndRepoToResult,fileNames,path,0);
                FileHandleThread fileHandleThread2 = new FileHandleThread(userToResult,repoToResult,userAndRepoToResult,fileNames,path,1);
                FileHandleThread fileHandleThread3 = new FileHandleThread(userToResult,repoToResult,userAndRepoToResult,fileNames,path,2);
                Thread thread = new Thread(fileHandleThread1);
                Thread thread1 = new Thread(fileHandleThread2);
                Thread thread2 = new Thread(fileHandleThread3);
                thread.start();
                thread1.start();
                thread2.start();
                try {
                    thread.join();
                    thread1.join();
                    thread2.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
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
