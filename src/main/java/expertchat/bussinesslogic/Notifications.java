/**
 * Business logic for appointment booked and cancelled Notification to User and Expert
 * Author: Rajdeep
 * Created On 16 Oct 2017
 */
package expertchat.bussinesslogic;
import expertchat.apioperation.AbstractApiFactory;
import expertchat.apioperation.ExpertChatEndPoints;
import expertchat.apioperation.apiresponse.ApiResponse;
import expertchat.apioperation.apiresponse.HTTPCode;
import expertchat.apioperation.apiresponse.ParseResponse;
import expertchat.apioperation.apiresponse.ResponseDataType;
import expertchat.apioperation.session.SessionManagement;
import expertchat.params.parameter;

import java.util.ArrayList;
import java.util.List;
import static expertchat.usermap.TestUserMap.getMap;

public class Notifications extends AbstractApiFactory implements HTTPCode,ExpertChatEndPoints {

    private  ApiResponse response=ApiResponse.getObject();
    private ParseResponse jsonParser = new ParseResponse ( response );
    private SessionManagement session= SessionManagement.session();

    private List notificationId_list = new ArrayList<String>( );
    private List notificationIDs = new ArrayList<String>();

    public void getAllNotifications(){

        String sessionID=getMap().get("scheduled_session_id");
        notificationId_list.clear();
        notificationIDs.clear();

        if (parameter.isExpert()){
            response.setResponse(this.get(NOTIFICATION,session.getExpertToken(),true));
            notificationId_list.addAll( response.getResponse().jsonPath().getList("results.id"));
            System.out.println("List size "+notificationId_list.size());
            String notification_session_id=null;

            for (int i = 0; i< notificationId_list.size(); i++){
                response.setResponse(this.get(NOTIFICATION+ notificationId_list.get(i),session.getExpertToken(),true));
                notification_session_id=jsonParser.getJsonData("results.data.session_id", ResponseDataType.STRING);
                if(notification_session_id.equals(sessionID)){
                    notificationIDs.add(notificationId_list.get(i));
                    //System.out.println("Adding notification ids -"+notificationId_list.get(i));
                }
                notification_session_id=null;
            }
            System.out.println("Expert Notification ids against session id "+sessionID+" are: "+ notificationIDs);

        }else{

            response.setResponse(this.get(NOTIFICATION,session.getUserToken(),true));
            notificationId_list.addAll( response.getResponse().jsonPath().getList("results.id"));
            System.out.println("List size "+notificationId_list.size());
            String notification_session_id=null;

            for (int i = 0; i< notificationId_list.size(); i++){
                response.setResponse(this.get(NOTIFICATION+ notificationId_list.get(i),session.getUserToken(),true));
                notification_session_id=jsonParser.getJsonData("results.data.session_id", ResponseDataType.STRING);
                if(notification_session_id.equals(sessionID)){
                    notificationIDs.add(notificationId_list.get(i));
                    //System.out.println("Adding notification ids -"+notificationId_list.get(i));
                }
                notification_session_id=null;
            }
            System.out.println("User Notification ids against session id "+sessionID+" are: "+ notificationIDs);
        }
        sessionID=null;
    }

    public void getNotification(String NotificationStatus){

        this.getAllNotifications();

        String code=null;
        boolean flag=false;

        if (parameter.isExpert()) {

            for (int i = 0; i < notificationIDs.size(); i++) {

                response.setResponse(this.get(NOTIFICATION + notificationIDs.get(i).toString(), session.getExpertToken(), true));

                if (response.statusCode() == HTTP_OK || response.statusCode() == HTTP_ACCEPTED) {
                    if(NotificationStatus.equals(jsonParser.getJsonData("results.code",ResponseDataType.STRING))){
                        getMap().put("notification_text", jsonParser.getJsonData("results.message", ResponseDataType.STRING));

                        flag = true;
                       }

                }else {
                    System.out.println("+++SERVER ERROR+++");
                    response.printResponse();

                }

            }

            if(flag==false){
                System.out.println("Some problem occur ,expert Notification was not sent");
            }

        }else{

            System.out.println("count of user noti--"+notificationIDs.size());
            for (int i = 0; i < notificationIDs.size(); i++) {

                response.setResponse(this.get(NOTIFICATION + notificationIDs.get(i).toString(), session.getUserToken(), true));

                if (response.statusCode() == HTTP_OK || response.statusCode() == HTTP_ACCEPTED) {

                    if(NotificationStatus.equals(jsonParser.getJsonData("results.code",ResponseDataType.STRING))){

                        getMap().put("notification_text", jsonParser.getJsonData("results.message", ResponseDataType.STRING));
                        flag = true;

                    }

                }else {
                    System.out.println("+++SERVER ERROR+++");
                    response.printResponse();

                }

            }

            if(flag==false){
                System.out.println("Some problem occur ,user Notification was not sent");
            }

        }
    }

    public void markNotificationRead(String notification){

        if (parameter.isExpert()){
            for (int i = 0; i < notificationIDs.size(); i++) {
                response.setResponse(this.get(NOTIFICATION+notificationIDs.get(i).toString(),session.getExpertToken(),true));
                if(isResponseOk()){
                    String code=jsonParser.getJsonData("results.code",ResponseDataType.STRING);
                    if(code.equals(notification)){
                        response.setResponse(this.put("{}", NOTIFICATION + notificationIDs.get(i).toString() + "/mark_read/", session.getExpertToken(),true));

                        if (isResponseOk()){
                            System.out.println(notification+" notification marked as read");
                        }else{
                            System.out.println("Error in Api call---");
                            response.printResponse();
                        }

                    }
                }else {
                    System.out.println("== SERVER ERROR==");
                    response.printResponse();
                }
            }

        }else {
            for (int i = 0; i < notificationIDs.size(); i++) {
                response.setResponse(this.get(NOTIFICATION+notificationIDs.get(i).toString(),session.getUserToken(),true));
                if(isResponseOk()){
                    String code=jsonParser.getJsonData("results.code",ResponseDataType.STRING);
                    if(code.equals(notification)){
                        response.setResponse(this.put("{}", NOTIFICATION + notificationIDs.get(i).toString() + "/mark_read/", session.getUserToken(),true));

                        if (isResponseOk()){
                            System.out.println(notification+" notification marked as read");
                        }else{
                            System.out.println("Error in Api call---");
                            response.printResponse();
                        }

                    }
                }else {
                    System.out.println("== SERVER ERROR==");
                    response.printResponse();
                }
            }
        }

    }

    public boolean isMarkRead(String type){
        boolean isMarkRead=false;
        String code=null;
        String isRead=null;

        if (parameter.isExpert()){
            for (int i=0;i<notificationIDs.size();i++){
                response.setResponse(this.get(NOTIFICATION+notificationIDs.get(i).toString(),session.getExpertToken(),true));

                if (isResponseOk()){

                    isRead=jsonParser.getJsonData("results.is_read",ResponseDataType.BOOLEAN);
                    code=jsonParser.getJsonData("results.code",ResponseDataType.STRING);
                    // System.out.println("API code--" +code+" parameter--"+type);

                    if (code.equals(type) && isRead.equals("true")){
                        //System.out.println("I am true");
                        isMarkRead= true;
                    }
                }else {
                    System.out.println("++Server response error++");
                    response.printResponse();
                }
            }
        }else{
            for (int i=0;i<notificationIDs.size();i++){

                response.setResponse(this.get(NOTIFICATION+notificationIDs.get(i).toString(),session.getUserToken(),true));

                if (isResponseOk()){

                    isRead=jsonParser.getJsonData("results.is_read",ResponseDataType.BOOLEAN);
                    code=jsonParser.getJsonData("results.code",ResponseDataType.STRING);
                   // System.out.println("API code--" +code+" parameter--"+type);

                    if (code.equals(type) && isRead.equals("true")){
                        //System.out.println("I am true");
                        isMarkRead= true;
                    }
                }else {
                    System.out.println("++3333 Server response error++");
                    response.printResponse();
                }
            }
        }
    return isMarkRead;

    }

    public boolean isResponseOk(){
        if(response.statusCode()==HTTP_OK||response.statusCode()==HTTP_ACCEPTED||response.statusCode()==HTTP_NO_CONTENT){
            return  true;
        }
        return false;
    }


}
