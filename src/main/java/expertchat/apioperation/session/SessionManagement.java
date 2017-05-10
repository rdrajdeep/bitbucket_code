package expertchat.apioperation.session;

// Created by Kishor on 1/23/2017.


public final class SessionManagement {

    private static SessionManagement sm = new SessionManagement ( );

    private static String expert_token="eyJ1c2VyX2lkIjozLCJ0aW1lc3RhbXAiOjE0OTQ0MjI4MTMuNjgyMDM1LCJpcF9hZGRyZXNzIjoiMTIyLjE2MC4yNTMuMjEifQ:1d8Rdl:VUTXGQL1vzawP_NB8D2qWQPvL1Q";

    private static String user_token="eyJ1c2VyX2lkIjo0LCJ0aW1lc3RhbXAiOjE0OTQ0MjI4NzIuMjYyMDI3LCJpcF9hZGRyZXNzIjoiMTIyLjE2MC4yNTMuMjEifQ:1d8Rei:ilSa-_n40TzLMasSKhgDHpJKHRg";

    private SessionManagement ( ) {

    }

    public static SessionManagement session ( ) {

        return sm;
    }

    public String getExpertToken ( ) {

        return expert_token;
    }

    public void setExpertToken ( String token ) {

        SessionManagement.expert_token= token;
    }

    public String getUserToken ( ) {

        return user_token;
    }

    public void setUserToken ( String token ) {

        SessionManagement.user_token = token;
    }

}
