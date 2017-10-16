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
    private List notificationId_list = new ArrayList<String>();
    private List notificationIDs = new ArrayList();

    public void getAllNotifications(){

        String sessionID=getMap().get("scheduled_session_id");

        if (parameter.isExpert()){
            response.setResponse(this.get(NOTIFICATION,session.getExpertToken(),true));
            notificationId_list.addAll( response.getResponse().jsonPath().getList("results.id"));
            String notification_session_id=null;

            for (int i = 0; i< notificationId_list.size(); i++){
                response.setResponse(this.get(NOTIFICATION+ notificationId_list.get(i),session.getExpertToken(),true));
                notification_session_id=response.getResponse().jsonPath().getString("results.data.session_id");
                if(notification_session_id.equals(sessionID)){
                    notificationIDs.add(notificationId_list.get(i));
                }
            }
            System.out.println("Notification ids against session id "+sessionID+" are: "+ notificationIDs);

        }else{
            response.setResponse(this.get(NOTIFICATION,session.getUserToken(),true));
            notificationId_list.addAll( response.getResponse().jsonPath().getList("results.id"));
            String notification_session_id=null;

            for (int i = 0; i< notificationId_list.size(); i++){
                response.setResponse(this.get(NOTIFICATION+ notificationId_list.get(i),session.getUserToken(),true));
                notification_session_id=jsonParser.getJsonData("results.data.session_id", ResponseDataType.STRING);
                if(notification_session_id.equals(sessionID)){
                    notificationIDs.add(notificationId_list.get(i));
                }
            }
            System.out.println("Notification ids against session id "+sessionID+" are: "+ notificationIDs);
        }

    }

    public void getNotification(){

        this.getAllNotifications();
        String code=null;
        if (parameter.isExpert()) {

            for (int i = 0; i < notificationIDs.size(); i++) {
                response.setResponse(this.get(NOTIFICATION + notificationIDs.get(i).toString(), session.getExpertToken(), true));
                System.out.println(notificationIDs.get(i));
                if (response.statusCode() == HTTP_OK || response.statusCode() == HTTP_ACCEPTED) {
                    response.printResponse();
                    code = jsonParser.getJsonData("results.code", ResponseDataType.STRING);
                    if (code.equals(NotificationCodes.SCHEDULE_NOTIFICATION)) {
                        getMap().put("schedule_notification_code", code);
                        getMap().put("schedule_notification_text", jsonParser.getJsonData("results.message", ResponseDataType.STRING));
                        getMap().put("notification_text", jsonParser.getJsonData("results.message", ResponseDataType.STRING));

                        code = null;
                    } else if (code.equals(NotificationCodes.CANCEL_NOTIFICATION)) {
                        getMap().put("cancel_notification_code", code);
                        getMap().put("cancel_notification_text", jsonParser.getJsonData("results.message", ResponseDataType.STRING));
                        getMap().put("notification_text", jsonParser.getJsonData("results.message", ResponseDataType.STRING));

                        code = null;
                    }

                }else {
                    System.out.println("+++SERVER ERROR+++");
                    response.printResponse();
                }
            }
        }else{

            for (int i = 0; i < notificationIDs.size(); i++) {
                response.setResponse(this.get(NOTIFICATION + notificationIDs.get(i).toString(), session.getUserToken(),true));
                if (response.statusCode()==HTTP_OK||response.statusCode()==HTTP_ACCEPTED) {
                    System.out.println(notificationIDs.get(i));
                    response.printResponse();
                    code = jsonParser.getJsonData("results.code", ResponseDataType.STRING);
                    if (code.equals(NotificationCodes.SCHEDULE_NOTIFICATION)) {
                        getMap().put("schedule_notification_code", code);
                        getMap().put("schedule_notification_text", jsonParser.getJsonData("results.message", ResponseDataType.STRING));
                        getMap().put("notification_text", jsonParser.getJsonData("results.message", ResponseDataType.STRING));
                        code = null;
                    } else if (code.equals(NotificationCodes.CANCEL_NOTIFICATION)) {
                        getMap().put("cancel_notification_code", code);
                        getMap().put("cancel_notification_text", jsonParser.getJsonData("results.message", ResponseDataType.STRING));
                        getMap().put("notification_text", jsonParser.getJsonData("results.message", ResponseDataType.STRING));
                        code = null;
                    }
                }else{
                    System.out.println("+++SERVER ERROR++++");
                    response.printResponse();
                }
            }
        }
    }
}
