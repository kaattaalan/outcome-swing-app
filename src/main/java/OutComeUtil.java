import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.network.Network;
import org.openqa.selenium.devtools.network.model.Headers;

import java.io.BufferedReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
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


}
