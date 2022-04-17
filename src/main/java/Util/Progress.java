package Util;

public class Progress {

    String key;
    String value;
    Integer progress;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public Progress(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public Progress(String key, String value, Integer progress) {
        this.key = key;
        this.value = value;
        this.progress = progress;
    }
}
