package expertchat.bussinesslogic;

import expertchat.apioperation.AbstractApiFactory;
import expertchat.apioperation.ExpertChatEndPoints;
import expertchat.apioperation.apiresponse.ApiResponse;
import expertchat.apioperation.apiresponse.HTTPCode;
import expertchat.apioperation.apiresponse.ParseResponse;
import expertchat.apioperation.apiresponse.ResponseDataType;
import expertchat.apioperation.session.SessionManagement;

/**
 * Created by joker on 12/4/17.
 */
public class Calender extends AbstractApiFactory implements HTTPCode, ExpertChatEndPoints {

    private String calenderId;
    private ApiResponse response=ApiResponse.getObject();
    private SessionManagement  session=SessionManagement.session();
    private ParseResponse pr=new ParseResponse(response);

    /****
     *
     * @param json
     */
    public void createCalender(String json){

        response.setResponse(
                this.post(json, SLOT, session.getToken())
        );

        response.printResponse();

        System.out.println(response.statusCode());
        if(response.statusCode()==HTTP_OK || response.statusCode()==HTTP_ACCEPTED ){

            calenderId=pr.getJsonData("results.id", ResponseDataType.INT);
        }
    }

    public void getCalender(String id){

        response.setResponse(
                this.get(SLOT+id+"/",session.getToken())
        );
    }

    public void updateCalender(String json, String id){

        response.setResponse(
                this.put( json, SLOT+id+"/", session.getToken())
        );
    }

    public String getCalenderId(){

        return calenderId;
    }

}