package expertchat.util;

// Created by Kishor on 1/24/2017.

import expertchat.driver.StoryConfig;

public class ExpertChatException extends RuntimeException{

    public static boolean EXIT=false;
    public static String message;

    public ExpertChatException(String msg){

        super(msg);
        message=msg;
        EXIT=true;
        StoryConfig.sendEmailNotificationOnException(message);
    }

}
