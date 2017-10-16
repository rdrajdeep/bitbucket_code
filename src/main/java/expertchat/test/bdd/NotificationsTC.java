package expertchat.test.bdd;

import com.relevantcodes.extentreports.ExtentReports;
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

    @Then("Appointment confirmation notification should be sent to user")
    @Aliases(values = {"Appointment cancelled notification should be sent to user"})
    public void checkNotification(){

        info("Checking Notification");
        notification.getNotification();
        this.checkAndWriteToReport(response.statusCode(),"Notification sent successfully, message is: "+getMap().get("notification_text"), parameter.isNegative());

    }

    @Then("Verify that notification message sent is $message")
    public void verifyNotification(){

    }

    /**
     * For testing purpose..
     * @param args
     */
    public static void main(String[] args){

        Notifications obj= new Notifications();
        String token="eyJpcF9hZGRyZXNzIjoiNjEuMjQ2LjQ3LjkzIiwidGltZXN0YW1wIjoxNTA4MTQwMzkzLjU1NTk4OSwidXNlcl9pZCI6M30:1e40D3:TUxtwe3k3mOfM1Y88qBRpTzdEb0";
        obj.getAllNotifications();
        obj.getNotification();

       // NotificationsTC.getAllNotifications(token);

    }

}
