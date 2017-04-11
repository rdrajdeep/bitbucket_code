package expertchat.bdd;// Created by Kishor on 3/28/2017.

import com.relevantcodes.extentreports.ExtentReports;
import expertchat.apioperation.apiresponse.HTTPCode;
import org.jbehave.core.annotations.Given;

import java.util.Date;

public class testTC extends AbstractSteps implements HTTPCode {


    public testTC(ExtentReports reports, String casName) {

        super(reports, casName);
    }


    @Given("system state")
    public void date(){
        System.out.println(new Date().getTime());
    }
}
