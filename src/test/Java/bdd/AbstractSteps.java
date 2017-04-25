package bdd;

// Created by Kishor on 1/25/2017.

import com.relevantcodes.extentreports.ExtentReports;
import expertchat.apioperation.apiresponse.ApiResponse;
import expertchat.apioperation.apiresponse.ParseResponse;
import expertchat.report.Report;
import expertchat.util.ResponseLogger;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import expertchat.params.parameter;
public abstract class AbstractSteps extends Report {

    protected ApiResponse response = ApiResponse.getObject();
    protected ParseResponse jsonParser = new ParseResponse(response);
    protected ResponseLogger responseLogger = new ResponseLogger(jsonParser);



    protected AbstractSteps ( ExtentReports reports, String casName ) {

        super ( reports, casName );
    }
    @Given ("complete $flow")
    public void flow(@Named ("flow") String flow) {

        info("...." + flow + "....");
    }

    @Given("an user")
    @Then ("an user")
    @When ("an user")
    public void user() {
        parameter.setExpert ( false );
    }

    @Given("an expert")
    @Then("an expert")
    @When("an expert")
    public void expert() {
        parameter.setExpert ( true );
    }

    @Given("negative scenario")
    public void negative() {
        parameter.setIsNegative ( true );
    }

}
