package expertchat.apioperation.session;

// Created by Kishor on 1/23/2017.


public final class SessionManagement {

    private static SessionManagement sm = new SessionManagement ( );
    private String token;

    private SessionManagement ( ) {

    }

    public static SessionManagement session ( ) {

        return sm;
    }

    public String getToken ( ) {

        return token;
    }

    public void setToken ( String token ) {

        this.token = token;
    }

}
