package util;

import org.apache.commons.io.FilenameUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OutComeUtil {

    public static final String GAMEREGEX = "\\('([^']*)','([^']*)'\\)";

    public static Map<String, String> getGameMap(String filePath) {
        Map<String, String> gameMap = new HashMap<>();
        try {
            Pattern r = Pattern.compile(GAMEREGEX);

            Path absoluteFile = Paths.get(filePath);
            BufferedReader reader = Files.newBufferedReader(absoluteFile,
                    StandardCharsets.UTF_8);
            String line;
            while ((line = reader.readLine()) != null) {
                Matcher m = r.matcher(line);
                if (m.find()) {
                    gameMap.put(m.group(1).toString(), m.group(2).toString());
                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return gameMap;
    }

    //TODO: check whether file is open/exists
    public static void writeToFile(Map<String, String> urlMap, File file) {
        if (! FilenameUtils.getExtension(file.getName()).equalsIgnoreCase("csv")) {
            file = new File(file.getParentFile(), FilenameUtils.getBaseName(file.getName())+".csv");
        }
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {
            for (Map.Entry<String, String> entry :
                    urlMap.entrySet()) {
                //Writes as comma seperated values
                bufferedWriter.write(entry.getKey() + ","
                        + entry.getValue());

                // new line
                bufferedWriter.newLine();
            }
            bufferedWriter.flush();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(String.format("writing %d to %s", urlMap.size(), file.getAbsolutePath()));
        }
    }


}
