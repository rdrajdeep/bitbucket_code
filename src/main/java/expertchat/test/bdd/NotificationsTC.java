package expertchat.test.bdd;

import com.relevantcodes.extentreports.ExtentReports;
import expertchat.bussinesslogic.Notifications;

public class NotificationsTC extends AbstractSteps{


    public NotificationsTC(ExtentReports reports, String casName) {
        super(reports, casName);
    }

    public void checkNotification(String token){
        Notifications obj= new Notifications();
        obj.getAllNotifications(token);

    }

    /**
     * For testing purpose..
     * @param args
     */
    public static void main(String[] args){

        Notifications obj= new Notifications();
        String token="eyJ1c2VyX2lkIjozLCJpcF9hZGRyZXNzIjoiNjEuMjQ2LjQ3LjkzIiwidGltZXN0YW1wIjoxNTA3ODA3MTIwLjk0ODM0M30:1e2bVg:RSkm9p61bNqWp2_vB_S0ezgbl3w";
        obj.getAllNotifications(token);
        obj.getNotification(token);

       // NotificationsTC.getAllNotifications(token);


    }

}
