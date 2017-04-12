package expertchat.driver;

// Created by Kishor on 1/23/2017.

import org.junit.runner.JUnitCore;

public class AppRunner {

    /*My superhero class*/
    /*It is JUnit runner inside main class*/
    public static void main ( String args[] ) {

        JUnitCore.runClasses ( StoryConfig.class );
        System.exit ( 0 );
    }

}
