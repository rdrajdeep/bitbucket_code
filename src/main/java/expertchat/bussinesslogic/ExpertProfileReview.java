package expertchat.bussinesslogic;

import expertchat.apioperation.AbstractApiFactory;
import expertchat.apioperation.ExpertChatEndPoints;
import expertchat.apioperation.apiresponse.ApiResponse;
import expertchat.apioperation.apiresponse.HTTPCode;
import expertchat.apioperation.apiresponse.ParseResponse;
import expertchat.apioperation.apiresponse.ResponseDataType;
import expertchat.apioperation.session.SessionManagement;

import static expertchat.usermap.TestUserMap.getMap;


public class ExpertProfileReview extends AbstractApiFactory implements HTTPCode, ExpertChatEndPoints{

    private ApiResponse response= ApiResponse.getObject();
    private ParseResponse jsonParser= new ParseResponse(response);
    private SessionManagement session= SessionManagement.session();


    public void getBasicProfile(){

        String url="me-basic-info/";
        response.setResponse(this.get(url,session.getExpertToken()));
        if (isResponseOK()) {
            getMap().put("expert_id", jsonParser.getJsonData("results.id", ResponseDataType.INT));
            getMap().put("is_profile_complete", jsonParser.getJsonData("results.is_profile_complete", ResponseDataType.BOOLEAN));
            getMap().put("review_status", jsonParser.getJsonData("results.review_status", ResponseDataType.INT));
        }else {
            System.out.println("++BAD Response+++");
            response.printResponse();
        }
    }

    public void getExpertprofile(){

        response.setResponse( this.get("expertprofiles",session.getExpertToken()));
        if (isResponseOK()) {
            getMap().put("expert_profile_id", jsonParser.getJsonData("results.expert.expert_profile_id ", ResponseDataType.INT));
            getMap().put("profile_submission_timestamp", jsonParser.getJsonData("results.profile_submission_timestamp ", ResponseDataType.STRING));
        }else
        {
            System.out.println("++Bad Response++");
            response.printResponse();
        }
    }

    public boolean isResponseOK(){
        if (response.statusCode()==HTTP_OK||response.statusCode()==HTTP_ACCEPTED){return true;}
        else{return false;}
    }


}
