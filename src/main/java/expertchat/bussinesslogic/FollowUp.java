package expertchat.bussinesslogic;

import expertchat.apioperation.AbstractApiFactory;
import expertchat.apioperation.ExpertChatEndPoints;
import expertchat.apioperation.apiresponse.ApiResponse;
import expertchat.apioperation.apiresponse.HTTPCode;
import expertchat.apioperation.apiresponse.ParseResponse;
import expertchat.apioperation.apiresponse.ResponseDataType;
import expertchat.apioperation.session.SessionManagement;

import static expertchat.usermap.TestUserMap.getMap;

public class FollowUp extends AbstractApiFactory implements HTTPCode, ExpertChatEndPoints {

    private ApiResponse response = ApiResponse.getObject ( );
    private ParseResponse jsonParser = new ParseResponse ( response );
    private SessionManagement session = SessionManagement.session ( );

public  void sendingFollowup(){

    String json="{\n" +
            "    \"text\": \" Test followup\",\n" +
            "    \"attachment\": null\n" +
            "}";
    String sessionId= getMap().get("scheduled_session_id");
    String url=SESSION+sessionId+"/follow_up/";

    response.setResponse(
            this.post(json,url,session.getExpertToken(),true)
    );
    if (response.statusCode()==HTTP_ACCEPTED||response.statusCode()==HTTP_OK){
        System.out.println("Follow up send successfully");
        response.printResponse();
        getMap().put("followup_error_code",null);
        getMap().put("followup_error_message",null);

    }else{
        System.out.println("Below Error occured while sending follow up");
        getMap().put("followup_error_code",jsonParser.getJsonData("errors.non_field_errors.code", ResponseDataType.INT));
        getMap().put("followup_error_message",jsonParser.getJsonData("errors.non_field_errors.message", ResponseDataType.STRING));

        response.printResponse();
        }
    }

    public void getFollowupDetails(String sessionId){

        response.setResponse(this.get(SESSION+sessionId+"/", session.getExpertToken(), true));

        if (response.statusCode()==HTTP_OK||response.statusCode()==HTTP_ACCEPTED) {

            System.out.println("Expert Session details-->");
            response.printResponse();

            String followup_details=response.getResponse().jsonPath().getString("results.session_follow_up");
            System.out.println("followup details: "+followup_details);
            if (followup_details!=null){
                getMap().put("follow_up_text",jsonParser.getJsonData("results.session_follow_up.text",ResponseDataType.STRING));
                getMap().put("follow_up_filename",jsonParser.getJsonData("results.session_follow_up.filename",ResponseDataType.STRING));

            }
        }else {
            System.out.println("Error reponse for follow up Session details-->");
            response.printResponse();
        }

    }




}
