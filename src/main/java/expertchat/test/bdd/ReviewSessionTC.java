package expertchat.test.bdd;

import com.relevantcodes.extentreports.ExtentReports;
import expertchat.bussinesslogic.Calling;
import expertchat.bussinesslogic.ReviewSession;
import expertchat.bussinesslogic.SessionStatus;
import expertchat.params.parameter;
import org.jbehave.core.annotations.Aliases;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;

import static expertchat.usermap.TestUserMap.getMap;


public class ReviewSessionTC extends AbstractSteps{

     public ReviewSessionTC(ExtentReports reports, String casName) {  super(reports, casName);   }

     private Calling call = new Calling();
     private ReviewSession reviewSession=new ReviewSession();

     private boolean isStatusCompleted=false;
     private boolean isSessionEnded=false;

    @When("I completed a call")
    public void getCallStatus(){
        info("Checking if call is completed");
        String sessionID=getMap().get("scheduled_session_id");

         isSessionEnded= call.endSession();
        this.AssertAndWriteToReport(isSessionEnded,"Call is  completed, you can send a review now");

    }

    @Then("get session details")
    public void getDetails(){
        info("Getting sesion details");

        call.getSessionDetails(getMap().get("scheduled_session_id"), parameter.isExpert());
        String sessionStatus=getMap().get("session_status");

        if ((isSessionEnded) && (sessionStatus.equals(SessionStatus.COMPLETED))){
            isStatusCompleted=true;

        }else {
            isStatusCompleted=false;

        }

        System.out.println("Session ststus== "+sessionStatus);
         this.checkAndWriteToReport(response.statusCode(), "Session status is "+getMap().get("session_status"),parameter.isNegative());

    }

    /**
     * Sending user review after a session is over
     * @param review
     */
    @Then("Send a review as $review")
    @When("I send review for same session once again as $review")
    @Aliases(values = {"I send a review as $review"})
    public void sendReview(@Named("review") String review){

        info("Sending review");
        if (isStatusCompleted){
            reviewSession.sendUserReview(review);
            this.checkAndWriteToReport(response.statusCode(),"Review is send successfully", parameter.isNegative());

        }else{
            this.AssertAndWriteToReport(isStatusCompleted);
            System.out.println("Review cannot be send as call is not in completed status");
        }

    }

    /**
     * Verifying review is successfully sent.
     * Or If review sent once it cannot be send once again
     */
    @Then("Verify review is successfully send")
    @Aliases(values = {"I should not allowed to send review"})
    public void verifyReview(){

    }


}
