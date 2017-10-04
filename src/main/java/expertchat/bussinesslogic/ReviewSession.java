package expertchat.bussinesslogic;

import expertchat.apioperation.AbstractApiFactory;
import expertchat.apioperation.ExpertChatEndPoints;
import expertchat.apioperation.apiresponse.ApiResponse;
import expertchat.apioperation.apiresponse.HTTPCode;
import expertchat.apioperation.session.SessionManagement;


import static expertchat.usermap.TestUserMap.getMap;

public class ReviewSession extends AbstractApiFactory implements ExpertChatEndPoints, HTTPCode {
    private ApiResponse response = ApiResponse.getObject ( );
    private SessionManagement session = SessionManagement.session ( );

    public void sendUserReview( String json){
        String url=SESSION+getMap().get("scheduled_session_id")+"/review/";
        response.setResponse(
                this.post(json,url,session.getUserToken(),true)
        );

        if (response.statusCode()==HTTP_OK||response.statusCode()==HTTP_ACCEPTED){
            System.out.println("Review is sent--");
            response.printResponse();
        }else {
            System.out.println("Review sending failed, response is-->");
            response.printResponse();
        }

    }

}
