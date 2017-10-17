package expertchat.test.bdd;

import com.relevantcodes.extentreports.ExtentReports;
import expertchat.bussinesslogic.DeleteAllExpertSlot;
import expertchat.bussinesslogic.NotificationCodes;
import expertchat.bussinesslogic.Notifications;
import expertchat.bussinesslogic.SessionStatus;
import expertchat.params.parameter;
import org.jbehave.core.annotations.Aliases;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import static expertchat.usermap.TestUserMap.getMap;

public class NotificationsTC extends AbstractSteps{

    public NotificationsTC(ExtentReports reports, String casName) {
        super(reports, casName);
    }

    private Notifications notification= new Notifications();

    @When("A appointment is $appointmentStatus")
    public void checkNotificationStatus(@Named("appointmentStatus")String appointmentStatus){

        info("Notification for appointment "+appointmentStatus);
        String sessionStatus=getMap().get("session_status");
        switch (appointmentStatus){
            case("booked"):
                if (sessionStatus.equals(SessionStatus.SCHEDULED)){
                    this.AssertAndWriteToReport(true,"Appointment is booked. A notification will soon be send to user and expert");
                    break;
                }else {
                    System.out.println("Appointment is not booked");
                }

            case ("cancelled"):

                if (sessionStatus.equals(SessionStatus.CANCELLED)){
                    this.AssertAndWriteToReport(true,"Appointment is cancelled. A notification will soon be send to user and expert");
                    break;
                }else {
                    System.out.println("Appointment is not cancelled");
                }

            default:
                System.out.println("Parameter "+appointmentStatus +"does not match with switch case");
                break;
        }
    }

    @Then("Verify appointment confirmation notification is sent to user")
    @Aliases(values = {"Verify appointment $status notification is sent to user"})
    public void checkNotification(@Named("status")String status){

        info("Checking Notification");
        switch (status){
            case"booked": notification.getNotification(NotificationCodes.SCHEDULE_NOTIFICATION);
                break;

            case "cancelled": notification.getNotification(NotificationCodes.CANCEL_NOTIFICATION);
                break;

                default:
                    System.out.println("incorrect input parameter, it should be from the list booked and cancelled ");
                    break;
        }
        //notification.getNotification();
        this.checkAndWriteToReport(response.statusCode(),"Notification sent successfully, message is: "+getMap().get("notification_text"), parameter.isNegative());

    }

    @When("I read the notification of $notificationType confirmation")
    public void markNotificationRead(@Named("notificationType")String notificationType){
        info("marking notification as read for notification type "+notificationType);
        notification.markNotificationRead(notificationType);
        this.checkAndWriteToReport(response.statusCode(),"Notification is marked as read",parameter.isNegative());
    }

    @Then("Verify that $notificationType confirmation notification is only marked as read")
    public void verifyMarkNotification(@Named("$notificationType")String notificationType){
        info("Verifying notification mark status");
        switch (notificationType){
            case"booked":
                this.AssertAndWriteToReport(notification.isMarkRead(NotificationCodes.SCHEDULE_NOTIFICATION),"Verified "+notificationType+" notification marked as read");
                break;
            case "cancelled":
                this.AssertAndWriteToReport(notification.isMarkRead(NotificationCodes.CANCEL_NOTIFICATION),"Verified "+notificationType+" notification marked as read");
                break;
        }

    }

    /**
     * For testing purpose..
     * @param args
     */
    public static void main(String[] args){

        //String token="eyJ1c2VyX2lkIjo1NDYsImlwX2FkZHJlc3MiOiI2MS4yNDYuNDcuOTMiLCJ0aW1lc3RhbXAiOjE1MDgyMTk4NDEuMTYyOTgyfQ:1e4KsT:k6-qDxDLcd6XOCT5X3KPnPab1Qs";
        String token="eyJ1c2VyX2lkIjozLCJ0aW1lc3RhbXAiOjE1MDgyNDQwMzcuNzA2NTY4LCJpcF9hZGRyZXNzIjoiNjEuMjQ2LjQ3LjkzIn0:1e4RAj:1jevlLgV1fQ871P2_RJ3Xn3BP3U";
        DeleteAllExpertSlot objDelete=new DeleteAllExpertSlot();
        //objDelete.deleteAllExpertSlots(token);
        objDelete.deleteAllFutureSession(token);

    }

}
