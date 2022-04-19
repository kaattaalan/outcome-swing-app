package worker;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class DownloadWorker extends SwingWorker<String, Integer> {

    String base64login;
    private Map<String, String> urlMap;
    private File file;
    private JProgressBar progressBar;
    private JTextArea textArea;

    public DownloadWorker(Map<String, String> urlMap, File file, String username, String password,JProgressBar progressBar, JTextArea textArea) {
        this.urlMap = urlMap;
        this.file = file;
        String login = username + ":" + password;
        this.base64login = Base64.getEncoder().encodeToString(login.getBytes());
        this.progressBar = progressBar;
        this.textArea = textArea;
    }

    @Override
    protected String doInBackground() throws Exception {
        int iter = 0;
        for (Map.Entry<String, String> entry : urlMap.entrySet()) {
            setProgress((++iter * 100) / urlMap.size());
            if (entry.getValue() == null){
                continue;
            }
            try {
                URL fetchUrl = new URL(entry.getValue());
                HttpURLConnection urlConnection = (HttpURLConnection) fetchUrl.openConnection();
                urlConnection.setRequestProperty("Authorization", "Basic " + base64login);
                urlConnection.connect();
                if (HttpURLConnection.HTTP_OK == urlConnection.getResponseCode()) {
                    File outFile = new File(file,FilenameUtils.getName(fetchUrl.getPath()));
                    FileOutputStream outputStream = new FileOutputStream(outFile);
                    InputStream inputStream = urlConnection.getInputStream();
                    IOUtils.copy(inputStream, outputStream);
                    outputStream.close();
                    inputStream.close();
                } else {
                    return String.format("Error accessing Download URLs : %s",urlConnection.getResponseCode());
                }
                urlConnection.disconnect();
            } catch (MalformedURLException e) {
                e.printStackTrace();
                continue;
            }
        }
        return null;
    }

    @Override
    protected void process(List<Integer> chunks) {
        super.process(chunks);
    }

    @Override
    protected void done() {
        String message = null;
        try {
            message = get();
        } catch (InterruptedException|ExecutionException e) {
            e.printStackTrace();
        }
        if(message != null){
            textArea.append(String.format("Error downloading file : %s\n",message));
        }else{
            progressBar.setValue(100);
        }
        super.done();
    }
}
