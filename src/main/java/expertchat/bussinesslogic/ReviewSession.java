/**
 * Author Rajdeep. Created on 4 Oct 2017
 */

package expertchat.bussinesslogic;

import expertchat.apioperation.AbstractApiFactory;
import expertchat.apioperation.ExpertChatEndPoints;
import expertchat.apioperation.apiresponse.ApiResponse;
import expertchat.apioperation.apiresponse.HTTPCode;
import expertchat.apioperation.apiresponse.ParseResponse;
import expertchat.apioperation.apiresponse.ResponseDataType;
import expertchat.apioperation.session.SessionManagement;


import static expertchat.usermap.TestUserMap.getMap;

public class ReviewSession extends AbstractApiFactory implements ExpertChatEndPoints, HTTPCode {

    private ApiResponse response = ApiResponse.getObject ( );
    private ParseResponse jsonParser= new ParseResponse(response);
    private SessionManagement session = SessionManagement.session ( );
    private boolean isReviewSuccess=false;

    public void sendUserReview( String json){

        String url=SESSION+getMap().get("scheduled_session_id")+"/review/";

        response.setResponse(
                this.post(json,url,session.getUserToken(),true)
        );

        System.out.println("status code "+response.statusCode());

        if (response.statusCode()==HTTP_OK||response.statusCode()==HTTP_ACCEPTED){

            System.out.println("Review is sent--");
            int errorCode=0;

           getMap().put("non_field_errors_code",jsonParser.getJsonData("errors.non_field_errors.code", ResponseDataType.INT));
            errorCode=Integer.parseInt(jsonParser.getJsonData("errors.non_field_errors.code", ResponseDataType.INT));

            System.out.println("error code "+errorCode);
            if(errorCode==ErrorCodes.ALREADY_REVIEW){
                isReviewSuccess=false;
                response.printResponse();
            }else{
                isReviewSuccess=true;
                response.printResponse();
            }
        }else {
            System.out.println("Review sending failed, response is-->");

            isReviewSuccess=false;
            response.printResponse();
        }

    }

    public boolean verifyReviewSession(){

        if (isReviewSuccess){
            return true;
        }else{
            return false;
        }
    }

}
