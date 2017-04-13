package expertchat.bussinesslogic;// Created by Kishor on 4/13/2017.

import expertchat.apioperation.AbstractApiFactory;
import expertchat.apioperation.ExpertChatEndPoints;
import expertchat.apioperation.apiresponse.ApiResponse;
import expertchat.apioperation.apiresponse.HTTPCode;
import expertchat.apioperation.apiresponse.ParseResponse;
import expertchat.apioperation.apiresponse.ResponseDataType;
import expertchat.apioperation.session.SessionManagement;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/* Get the states of expert*/

public class MeStats extends AbstractApiFactory implements HTTPCode, ExpertChatEndPoints {

    private ApiResponse response=ApiResponse.getObject ();
    private ParseResponse pr=new ParseResponse ( response );
    private SessionManagement session=SessionManagement.session ();
    private LinkedHashMap<String, String> sessionCount=new LinkedHashMap <> ();
    private LinkedHashMap<String, String> profileVisits=new LinkedHashMap <> ();


    public void  getAllcounts() {


        String date = new SimpleDateFormat ( "yyyy-mm-dd" ).format ( new Date ( ) );

        response.setResponse ( this.get ( SEARCH_BASE + "me-stats/", session.getToken ( ) ) );

        response.printResponse ( );

    }

    public Map getProfileVisits(){

        return  profileVisits;
    }
}
