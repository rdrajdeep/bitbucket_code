package expertchat.test.bdd;

import com.relevantcodes.extentreports.ExtentReports;
import expertchat.bussinesslogic.Calling;
import expertchat.bussinesslogic.FollowUp;
import expertchat.bussinesslogic.SessionStatus;
import expertchat.params.parameter;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import static expertchat.usermap.TestUserMap.getMap;

public class FollowUpTC extends AbstractSteps {
    public FollowUpTC(ExtentReports reports, String casName) {      super(reports, casName);    }

    private     FollowUp followup = new FollowUp();
    private     Calling call = new Calling();

    @When("The session is completed")
        public void getSessionStatus(){
            if ((getMap().get("session_status")).equals(SessionStatus.COMPLETED)){
                this.AssertAndWriteToReport(true,"Session is in completed state");
            }else {
                this.AssertAndWriteToReport(false);
            }
    }

    @When("I send a followup to user")
    @Then("I send a followup to user")
        public void sendFollowUp(){

            info("Sendig follow Up");
           followup.sendingFollowup();
            this.checkAndWriteToReport(response.statusCode(),"Follow up send successfully", parameter.isNegative());
    }

    @Then("Followup should be successfully send")
    public void verifyFollowup(){

            info("Verify follow up is send successfully");
            followup.getFollowupDetails(getMap().get("scheduled_session_id"));

            String followup_status=getMap().get("follow_up_text");
            if (followup_status!=null){
                this.AssertAndWriteToReport(true,"Verified, Follow up sent successfully");
            }else {
                this.AssertAndWriteToReport(false);
            }
        }

}
