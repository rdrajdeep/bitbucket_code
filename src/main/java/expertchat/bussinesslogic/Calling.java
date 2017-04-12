package expertchat.bussinesslogic;

import expertchat.apioperation.AbstractApiFactory;
import expertchat.apioperation.ExpertChatEndPoints;
import expertchat.apioperation.apiresponse.ApiResponse;
import expertchat.apioperation.apiresponse.HTTPCode;
import expertchat.apioperation.apiresponse.ParseResponse;
import expertchat.apioperation.apiresponse.ResponseDataType;
import expertchat.apioperation.session.SessionManagement;
import expertchat.util.ExpertChatException;

import static expertchat.usermap.TestUserMap.getMap;

/**
 * @Class contains methods to drive the calling APIs
 * Inherits AbstractApiFactory to use POST PUT GET DELETE methods
 */
public class Calling extends AbstractApiFactory implements HTTPCode, ExpertChatEndPoints {

    private ApiResponse response = ApiResponse.getObject ( );

    private ParseResponse parseResponse = new ParseResponse ( response );

    private SessionManagement session = SessionManagement.session ( );

    private String id;

    public String getId ( ) {
        return id;
    }

    /**
     *
     */
    public String getStatusOfCall ( String key ) {

        return parseResponse.getJsonData ( key, ResponseDataType.INT );
    }

    public void doCall ( String scheduled_duration ) {

        String expert_profile = getMap ( ).get ( "expertProfileId" );
        String expert = getMap ( ).get ( "expertId" );
        String user_device = getMap ( ).get ( "UserDevice" );

        String json = "{\n" +
                "    \"expert_profile\":" + expert_profile + ",\n" +
                "    \"expert\": " + expert + ",\n" +
                "    \"user_device\": " + user_device + ",\n" +
                "    \"scheduled_duration\":" + scheduled_duration + "\n" +
                "}";

        response.setResponse (

                this.post ( json, SESSION, session.getToken ( ), true )
        );

        response.printResponse ( );

        if ( response.statusCode ( ) == HTTP_ACCEPTED || response.statusCode ( ) == HTTP_OK ) {

            id = parseResponse.getJsonData ( "results.id", ResponseDataType.INT );

        } else {

            throw new ExpertChatException ( "Server Error-(Calling.doCall):->" + response.statusCode ( ) );
        }
    }

    public boolean isAcceptCall ( ) {

        String url = SESSION + getId ( ) + "/accept/";

        String expert_device = getMap ( ).get ( "ExpertDevice" );

        String json = "{\n" +
                "    \"expert_device\":" + expert_device + "\n" +
                "}";

        response.setResponse (
                this.post ( json, url, session.getToken ( ), true )
        );

        response.printResponse ( );

        return getStatusOfCall ( "results.status" ).equals ( CallStatus.ACCEPTED );
    }

    public boolean isDissconnectCall ( ) {

        String url = SESSION + getId ( ) + "/disconnect/";
        System.out.println ( url );

        String json = "{\"tokbox_session_length\":20}";

        response.setResponse (

                this.delete ( json, url, session.getToken ( ), true )
        );

        System.out.println ( response.statusCode ( ) );

        response.printResponse ( );

        if ( getStatusOfCall ( "results.status" ).equals ( CallStatus.COMPLETED ) ) {

            System.out.println ( getStatusOfCall ( "results.status" ) );
            return true;
        }
        return false;

    }

    public boolean isDecline ( ) {

        String url = SESSION + getId ( ) + "/decline/";

        response.setResponse (
                this.delete ( "", url, session.getToken ( ), true )
        );

        response.printResponse ( );

        return getStatusOfCall ( "results.status" ).equals ( CallStatus.DECLINED );
    }

    public boolean isDelay ( ) {

        String url = SESSION + getId ( ) + "/delay/";

        response.setResponse (
                this.put ( "{\"delay_time\":1}", url, session.getToken ( ), true )
        );

        response.printResponse ( );

        return getStatusOfCall ( "results.status" ).equals ( CallStatus.DELAYED );
    }

    public void registerDevice ( String json, boolean isExpert ) {

        response.setResponse (

                this.post ( json, REGISTER_DEVICE, session.getToken ( ), true ) );

        if ( isExpert ) {

            getMap ( ).put ( "ExpertDevice", parseResponse.getJsonData ( "results.id", ResponseDataType.INT ) );
        } else {

            getMap ( ).put ( "UserDevice", parseResponse.getJsonData ( "results.id", ResponseDataType.INT ) );
        }
    }
}