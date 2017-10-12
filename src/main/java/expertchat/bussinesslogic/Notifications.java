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
import java.util.ListIterator;

import static expertchat.usermap.TestUserMap.getMap;


public class Notifications extends AbstractApiFactory implements HTTPCode,ExpertChatEndPoints {

    private  ApiResponse response=ApiResponse.getObject();
    private ParseResponse jsonParser = new ParseResponse ( response );
    private SessionManagement session= SessionManagement.session();
    private List notificationId_list = new ArrayList<String>();
    private List notificationIDs = new ArrayList();

    public void getAllNotifications(String token){

        //String sessionID=getMap().get("scheduled_session_id");//"8939";
        parameter.setExpert(false);
        //session.setUserToken(token);
        String sessionID="8939";
        String url="http://connect.qa.experchat.com/v1/notifications/";

        if (parameter.isExpert()){
            response.setResponse(this.get(NOTIFICATION,session.getExpertToken()));
            notificationId_list.addAll( response.getResponse().jsonPath().getList("results.id"));
            String notification_session_id=null;

            for (int i = 0; i< notificationId_list.size(); i++){
                response.setResponse(this.get(NOTIFICATION+ notificationId_list.get(i),session.getExpertToken()));

                notification_session_id=response.getResponse().jsonPath().getString("results.data.session_id");
                if(notification_session_id.equals(sessionID)){
                    notificationIDs.add(notificationId_list.get(i));
                }
            }
            System.out.println("Notification ids against session id "+sessionID+" are: "+ notificationIDs);

        }else{
            response.setResponse(this.get(url,token));
            notificationId_list.addAll( response.getResponse().jsonPath().getList("results.id"));
            String notification_session_id=null;

            for (int i = 0; i< notificationId_list.size(); i++){
                response.setResponse(this.get(url+ notificationId_list.get(i),token));

                notification_session_id=jsonParser.getJsonData("results.data.session_id", ResponseDataType.STRING);
                if(notification_session_id.equals(sessionID)){
                    notificationIDs.add(notificationId_list.get(i));
                }
            }
            System.out.println("Notification ids against session id "+sessionID+" are: "+ notificationIDs);
        }

    }

    public void getNotification(String token){
        String url="http://connect.qa.experchat.com/v1/notifications/";
        ListIterator <String>iterator= null;
        iterator=notificationIDs.listIterator();
        System.out.println(notificationIDs);
        while(iterator.hasNext()){
          //  response.setResponse(this.get(url+ notificationIDs.toString(),token));
            //System.out.println(iterator);
           // response.printResponse();
        }
    }

}
