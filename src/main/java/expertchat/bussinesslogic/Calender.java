package expertchat.bussinesslogic;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import expertchat.apioperation.AbstractApiFactory;
import expertchat.apioperation.ExpertChatEndPoints;
import expertchat.apioperation.apiresponse.ApiResponse;
import expertchat.apioperation.apiresponse.HTTPCode;
import expertchat.apioperation.apiresponse.ParseResponse;
import expertchat.apioperation.apiresponse.ResponseDataType;
import expertchat.apioperation.session.SessionManagement;

import javax.xml.transform.Result;
import java.lang.reflect.Type;
import java.util.Map;

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
                this.post(json, SLOTS, session.getToken())
        );

        response.printResponse();

        System.out.println(response.statusCode());
        if(response.statusCode()==HTTP_OK || response.statusCode()==HTTP_ACCEPTED ){

            calenderId=pr.getJsonData("results.id", ResponseDataType.INT);
        }
    }

    public void getCalender(String id){

        response.setResponse(
                this.get(SLOTS+id+"/",session.getToken())
        );
    }

    public void updateCalender(String json, String id){

        response.setResponse(
                this.put( json, SLOTS+id+"/", session.getToken())
        );
    }

    public String getCalenderId(){

        return calenderId;
    }

    public void getAvilableSlot(String epId) {

        response.setResponse(
                this.get(AVILABLE_SLOTS+epId+"/",session.getToken())
        );

    }

}