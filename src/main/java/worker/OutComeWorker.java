package worker;

import ui.OutcomeFrame;
import util.Progress;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.swing.*;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.function.Predicate;

public class OutComeWorker extends SwingWorker<Map<String,String>,Progress> {

    public static final String OUTCOME = "outcome";
    public static final String SUPERVISED = "supervised";
    public static final String NOSPECRGEX = "[^A-Za-z0-9]";

    private String url;
    private String username;
    private String password;
    private Map<String, String> gameMap;
    private OutcomeFrame.UIUpdate update;
    private OutcomeFrame.FinishProcess finish;

    public OutComeWorker(String url, String username, String password, Map<String, String> gameMap, OutcomeFrame.UIUpdate update, OutcomeFrame.FinishProcess finish) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.gameMap = gameMap;
        this.update = update;
        this.finish = finish;
    }

    @Override
    protected void process(List<Progress> chunks) {
        update.perform(chunks.get(chunks.size()-1));
        super.process(chunks);
    }

    @Override
    protected void done() {
        try {
            finish.perform(get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        super.done();
    }

    @Override
    protected Map<String, String> doInBackground() throws Exception {

        Map<String, String> urlMap = new HashMap<>();
        String login = username + ":" + password;
        String base64login = Base64.getEncoder().encodeToString(login.getBytes());
        int iter = 0;

        for (Map.Entry<String, String> entry : gameMap.entrySet()) {
            String urlFound = null;
            String strippedName = entry.getKey().replaceAll(NOSPECRGEX, "");
            String strippedVersion = entry.getValue().split("-")[0];

            String nameUrl = findUrl(url, base64login, c -> c.html().replaceAll(NOSPECRGEX, "").equalsIgnoreCase(strippedName));
            if (nameUrl != null) {
                String verUrl = findUrl(nameUrl, base64login, c -> c.html().replaceAll("/", "").equalsIgnoreCase(strippedVersion));
                if (verUrl != null) {
                    String fileUrl = findUrl(verUrl, base64login, c -> c.html().contains(OUTCOME));
                    if (fileUrl != null) {
                        urlFound = fileUrl;
                    } else {
                        fileUrl = findUrl(verUrl, base64login, c -> c.html().contains(SUPERVISED));
                        if (fileUrl != null) {
                            fileUrl = findUrl(fileUrl, base64login, c -> c.html().contains(OUTCOME));
                        }
                        urlFound = fileUrl;
                    }
                }
            }
            //listened to by @worker.WorkerListener
            setProgress((++iter * 100) / gameMap.size());
            urlMap.put(entry.getKey(),urlFound);
            publish(new Progress(entry.getKey(),urlFound));
        }
        return urlMap;
    }

    public String findUrl(String url, String authString, Predicate<?super Element> predicate)  {
        Document document = null;
        try {
            document = Jsoup
                    .connect(url)
                    .header("Authorization", "Basic " + authString)
                    .get();
        } catch (IOException e) {
            return null;
        }
        Elements links = document.select("a[href]");
        Element match = links.stream().filter(predicate).findFirst().orElse(null);
        if(match == null){
            return null;
        }
        return match.absUrl("href");
    }

}
