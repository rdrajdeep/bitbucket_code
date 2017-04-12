package expertchat.bdd;

// Created by Kishor on 1/25/2017.

import com.relevantcodes.extentreports.ExtentReports;
import expertchat.report.Report;

public abstract class AbstractSteps extends Report {

    protected AbstractSteps ( ExtentReports reports, String casName ) {

        super ( reports, casName );
    }

}
