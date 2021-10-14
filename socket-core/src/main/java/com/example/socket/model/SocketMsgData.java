package com.example.socket.model;

import java.io.Serializable;

/**
 * @author honglixiang
 */
public class SocketMsgData implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String sourceUserName;

    private String targetUserName;

    private String type;

    private String content;

    public String getSourceUserName() {
        return sourceUserName;
    }

    public void setSourceUserName(String sourceUserName) {
        this.sourceUserName = sourceUserName;
    }

    public String getTargetUserName() {
        return targetUserName;
    }

    public void setTargetUserName(String targetUserName) {
        this.targetUserName = targetUserName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "SocketMsgData{" +
                "sourceUserName='" + sourceUserName + '\'' +
                ", targetUserName='" + targetUserName + '\'' +
                ", type='" + type + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
