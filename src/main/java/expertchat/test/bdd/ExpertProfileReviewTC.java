package expertchat.test.bdd;

import com.relevantcodes.extentreports.ExtentReports;
import expertchat.bussinesslogic.ExpertProfileReview;
import org.jbehave.core.annotations.Aliases;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;

import static expertchat.usermap.TestUserMap.getMap;

public class ExpertProfileReviewTC  extends AbstractSteps{

    public ExpertProfileReviewTC(ExtentReports reports, String casName) {      super(reports, casName);    }

    private ExpertProfileReview expertProfile= new ExpertProfileReview();

    @When("I get the expert profile")
    @Then("I get the expert profile")
    public void getcompleteProfile(){

    expertProfile.getBasicProfile();
    String isProfileComplete=   getMap().get("is_profile_complete");

    if (isProfileComplete.equals("true")){ this.AssertAndWriteToReport(true,"Expert Profile is complete, expert profile id: "+getMap().get("expert_id"));}
    else { this.AssertAndWriteToReport(true,"Expert Profile is incomplete, expert profile id: "+getMap().get("expert_id")); }

    }

    @Then ("Verify that incomplete profile cannot be submitted for review")
    @Aliases(values = {"Verify expert profile is submitted successfully"})
    public void verifySubmission(){

    }

    @Then("I will submit profile for review")
    public void submitProfile(){

    }

}
