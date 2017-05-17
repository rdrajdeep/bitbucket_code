package expertchat.bussinesslogic;

import expertchat.apioperation.AbstractApiFactory;
import expertchat.apioperation.ExpertChatEndPoints;
import expertchat.apioperation.apiresponse.ApiResponse;
import expertchat.apioperation.apiresponse.HTTPCode;
import expertchat.apioperation.apiresponse.ParseResponse;
import expertchat.apioperation.apiresponse.ResponseDataType;
import expertchat.apioperation.session.SessionManagement;
import expertchat.util.ExpertChatException;
import static expertchat.util.ExpertChatUtility.getValue;
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

    private String card_id;


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

                this.post ( json, SESSION, session.getUserToken ( ), true )
        );

        response.printResponse ( );

        if ( response.statusCode ( ) == HTTP_ACCEPTED || response.statusCode ( ) == HTTP_OK ) {

            id = parseResponse.getJsonData ( "results.id", ResponseDataType.INT );

        } else {

            throw new ExpertChatException ( "Server Error-(Calling.doCall):->" + response.statusCode ( ) );
        }
    }

    public boolean isAcceptCall ( ) {

        String url = SESSION +"1"+ "/accept/";

        String expert_device = getMap ( ).get ( "ExpertDevice" );

        String json = "{\n" +
                "    \"expert_device\":" + expert_device + "\n" +
                "}";

        response.setResponse (
                this.post ( json, url, session.getExpertToken ( ), true )
        );

        response.printResponse ( );

        return getStatusOfCall ( "results.status" ).equals ( CallStatus.ACCEPTED );
    }

    public boolean isDissconnectCall ( ) {

        String url = SESSION + "1" + "/disconnect/";
        System.out.println ( url );

        String json = "{\"tokbox_session_length\":20}";

        response.setResponse (this.delete ( json, url, session.getExpertToken ( ), true ));

        System.out.println ( response.statusCode ( ) );

        response.printResponse ( );

        if ( getStatusOfCall ( "results.status" ).equals ( CallStatus.COMPLETED ) ) {

            System.out.println ( getStatusOfCall ( "results.status" ) );
            return true;
        }
        return false;

    }

    public boolean isDecline ( ) {

        String url = SESSION +"1"+ "/decline/";

        response.setResponse (
                this.delete ( "", url, session.getExpertToken ( ), true )
        );

        response.printResponse ( );

        return getStatusOfCall ( "results.status" ).equals ( CallStatus.DECLINED );
    }

    public boolean isDelay ( ) {

        String url = SESSION + getId ( ) + "/delay/";

        response.setResponse (
                this.put ( "{\"delay_time\":1}", url, session.getExpertToken ( ), true )
        );

        response.printResponse ( );

        return getStatusOfCall ( "results.status" ).equals ( CallStatus.DELAYED );
    }

    /**
     *
     * @param json
     * @param isExpert
     */
    public void registerDevice ( String json, boolean isExpert ) {

        if ( isExpert ) {

            response.setResponse (this.post ( json, REGISTER_DEVICE, session.getExpertToken ( ), true ) );

            getMap ( ).put ( "ExpertDevice", parseResponse.getJsonData ( "results.id", ResponseDataType.INT ) );

        } else {

            response.setResponse (this.post ( json, REGISTER_DEVICE, session.getUserToken ( ), true ) );

            getMap ( ).put ( "UserDevice", parseResponse.getJsonData ( "results.id", ResponseDataType.INT ) );
        }
    }

    /**
     *
     * @param realTime
     */
    public void extendSession ( String realTime ) {

        String url=SESSION+"1"+"extend_session/";

        String json="{\n" +
                "    \"expert_profile\": "+getMap ().get ( "expertProfileId" )+",\n" +
                "    \"expert\": "+getMap ().get ( "expertId" )+",\n" +
                "    \"user_device\": "+getMap ().get ( "UserDevice" )+",\n" +
                "    \"scheduled_duration\": "+realTime+"\n" +
                "}";

        response.setResponse (this.put(url, json, session.getUserToken (), true));

        response.printResponse ();

    }

    /**
     *
     * @param json
     */
    public void addReview(String json){

        String url=SESSION+"1"+"review/";

        response.setResponse (

                this.post ( url, json, session.getExpertToken (), true ));

        response.printResponse ();
    }

    /**
     * @param
     */
    public void scheduleSession(String json){

        String url=SESSION+"schedule/";

        response.setResponse (this.post (json, url, session.getUserToken (), true));

        if(response.statusCode ()==HTTP_ACCEPTED || response.statusCode ()==HTTP_OK) {

            getMap ( ).put ( "scheduled_session_id", parseResponse.getJsonData ( "results.id", ResponseDataType.INT ) );

            getMap ( ).put ( "scheduled_datetime", parseResponse.getJsonData ( "results.scheduled_datetime", ResponseDataType.STRING ));

        }else {

            System.out.println ("SERVER ERROR");
        }

        response.printResponse ();
    }

    public boolean isCancelSession(){

        String id= getMap ().get ( "scheduled_session_id");

        String url=SESSION+id+"cancel/";

        response.setResponse (
                this.delete ( "", url, session.getUserToken ( ), true ));

        if(response.statusCode ()==HTTP_NO_CONTENT){

            return true;
        }
        return false;
    }

    public void getPastSession(){

        String id= getMap ().get ( "scheduled_session_id");

        String url= getValue ( "qawithport" )+"past-sessions/";

        response.setResponse (this.get ( url, session.getUserToken (), true ));

        response.printResponse ();
    }

    public void getFutureSession(){

        String id= getMap ().get ( "scheduled_session_id");

        String url= getValue ( "qawithport" )+"future-sessions/";

        response.setResponse (
                this.get ( url, session.getUserToken (), true ));

        response.printResponse ();
    }

    /**
     *
     * @param nonce
     */
    public void createCard( String nonce){

        String json="{\n" +
                "\"payment_method_nonce\":\""+nonce+"\"\n" +
                "}";

        response.setResponse(this.post (json, "cards/", session.getUserToken (), true));

        if(response.statusCode ()==HTTP_UNAVAILABLE || response.statusCode ()==HTTP_BAD){

            System.out.println ("==SERVER ERROR==");

            response.printResponse ();

            System.out.println ("Requested URL::"+getValue ( "qawithport")+"cards/");

        }else {
            response.printResponse ();

            card_id=parseResponse.getJsonData ( "results.id", ResponseDataType.INT );

            getMap ().put ( "user_card_id" , card_id);
        }
    }

    public void intiate ( String s ) {

        response.setResponse(this.put ("{}", SESSION+s+"initialize/", session.getUserToken (), true));
        response.printResponse ();
    }


    public String  checkRevenue ( ) {

        response.setResponse(this.get (SESSION+"1/", session.getUserToken (), true));
        response.printResponse ();
        return parseResponse.getJsonData ( "results.revenue", ResponseDataType.STRING);
    }

    public String sessionStatus ( ) {

        response.setResponse(this.get (SESSION+"1/", session.getUserToken (), true));

        response.printResponse ();

        return parseResponse.getJsonData ( "results.status", ResponseDataType.STRING);
    }
}
