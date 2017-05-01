package expertchat.test.bdd;// Created by Kishor on 4/25/2017.

import com.relevantcodes.extentreports.ExtentReports;
import expertchat.bussinesslogic.Calling;
import expertchat.params.parameter;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.Pending;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;

public class CallingTC extends AbstractSteps{

    public CallingTC ( ExtentReports reports, String casName) {

        super(reports, casName);
    }

    private Calling call = new Calling();

    /**
     * Test cases to drive the Calling API
     */
    /*Calling API Test cases*/
    @When ("register a device as $json")
    @Then ("register a device as $json")
    public void registerDevice(@Named ("json") String json) {

        if (parameter.isNegative ()) {
            call.registerDevice(json, parameter.isExpert ());
            responseLogger.writeResponseAsLog("Register Device-Negative");
        } else {

            call.registerDevice(json, parameter.isExpert ());
            responseLogger.writeResponseAsLog("Register Device");
        }

        checkAndWriteToReport(response.statusCode(), "Device Registered", false);
    }


    @Then("initiate a call of scheduled_duration $time")
    public void initiateCall(@Named("time") String time) {

        info("Initiating a call to expert...");

        if (parameter.isNegative ()) {

            call.doCall(time);

            responseLogger.writeResponseAsLog("Call initiated by user-Negative");

        } else {

            call.doCall(time);

            responseLogger.writeResponseAsLog("Call initiated by user-Negative");

        }

        checkAndWriteToReport(response.statusCode(), "Call initiated", parameter.isNegative ());
    }


    @Then("accept the call")
    public void acceptCall() {

        this.info("accepting Call...");

        call.isAcceptCall();

        this.checkAndWriteToReport(response.statusCode(),
                "Call Accepted", parameter.isNegative ());

        responseLogger.writeResponseAsLog("Accept Call");
    }

    @Then("decline the call")
    public void declineCall() {

        this.info("Declining a Call");

        call.isDecline();

        this.checkAndWriteToReport(response.statusCode(),
                "Call Declined", parameter.isNegative ());

        responseLogger.writeResponseAsLog("Decline Call");
    }

    @Then("delay the call")
    public void delayCall() {

        this.info("Delaying the call");

        call.isDelay();

        this.checkAndWriteToReport(response.statusCode(),
                "Call delayed", parameter.isNegative ());

        responseLogger.writeResponseAsLog("Delay Call");
    }

    @Then("disconnect the call")
    public void disconnectCall() {

        this.info("Disconnecting a call...");

        call.isDissconnectCall();

        this.checkAndWriteToReport(response.statusCode(),

                "Call Disconnected", parameter.isNegative ());

        responseLogger.writeResponseAsLog("Disconnect Call");
    }

    @Then("provide review as $review")
    @Pending
    public void review(@Named ( "review" )String review){


    }

}
