package expertchat.test.bdd;

import com.relevantcodes.extentreports.ExtentReports;
import org.jbehave.core.annotations.Aliases;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;

public class ExpertProfileReviewTC  extends AbstractSteps{

    public ExpertProfileReviewTC(ExtentReports reports, String casName) {      super(reports, casName);    }


    @When("I get the expert profile")
    @Then("I get the expert profile")
    public void getcompleteProfile(){

    }

    @Then ("Verify that incomplete profile cannot be submitted for review")
    @Aliases(values = {"Verify expert profile is submitted successfully"})
    public void verifySubmission(){

    }

}
