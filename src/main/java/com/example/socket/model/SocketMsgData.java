package com.example.socket.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SocketMsgData implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String sourceUserName;

    private String targetUserName;

    private String type;

    private String content;
}
