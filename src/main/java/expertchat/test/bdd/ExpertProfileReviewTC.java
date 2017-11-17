package expertchat.test.bdd;

import com.relevantcodes.extentreports.ExtentReports;
import expertchat.apioperation.apiresponse.ApiResponse;
import expertchat.apioperation.apiresponse.ResponseDataType;
import expertchat.apioperation.session.SessionManagement;
import expertchat.bussinesslogic.ExpertChatApi;
import expertchat.bussinesslogic.ExpertProfileReview;
import expertchat.bussinesslogic.ProfileComplete;
import expertchat.params.Credentials;
import expertchat.params.parameter;
import expertchat.test.TestStoryConfig;
import expertchat.usermap.TestUserMap;
import org.jbehave.core.annotations.*;

import static expertchat.usermap.TestUserMap.getMap;

public class ExpertProfileReviewTC  extends AbstractSteps{

    public ExpertProfileReviewTC(ExtentReports reports, String casName) {      super(reports, casName);    }

    private ExpertProfileReview expertProfile= new ExpertProfileReview();
    private ProfileComplete profileComplete= new ProfileComplete();
   private ExpertChatApi expertChatApi= new ExpertChatApi();
    private Credentials credentials= Credentials.getCredentials();

    @When("please login with $user")
    @Then("please login with $user")
    @Alias("i login with $user")
    public void login(@Named("user") String user) {

        info("Login");

        System.out.println("-- Login  --");


        if (user.contains("{") && parameter.isExpert()) {

            expertChatApi.doLogIn(user, true);

            credentials.setExpertCredential(user);

            this.checkAndWriteToReport(response.statusCode(), "Logged in to experChat by expert " + jsonParser.getJsonData("results.email", ResponseDataType.STRING), parameter.isNegative());
            user=null;
        } else if (user.contains("{") && parameter.isExpert() == false) {

            expertChatApi.doLogIn(user, false);

            credentials.setuserCredential(user);

            this.checkAndWriteToReport(response.statusCode(), "Logged in to experChat by user " + jsonParser.getJsonData("results.email", ResponseDataType.STRING), parameter.isNegative());
            user=null;
        } else if (user.contains("user")) {

            expertChatApi.doLogIn(TestUserMap.getUserCredentialsByKey(user), false);

            credentials.setuserCredential(TestUserMap.getUserCredentialsByKey(user));

            this.checkAndWriteToReport(response.statusCode(), "Logged in to experChat by user " +
                    TestUserMap.getUserCredentialsByKey(user), parameter.isNegative());


        } else {
            expertChatApi.doLogIn(TestUserMap.getUserCredentialsByKey(user), true);

            credentials.setExpertCredential(TestUserMap.getUserCredentialsByKey(user));

            this.checkAndWriteToReport(response.statusCode(), "Logged in to experChat by expert " +
                    TestUserMap.getUserCredentialsByKey(user), parameter.isNegative());
        }

    }


    @When("I get the expert profile")
    @Then("I get the expert profile")
    public void getcompleteProfile(){

        info("Loading expert profile");
        expertProfile.getExpertprofile();
        this.checkAndWriteToReport(response.statusCode(),"Expert profile id is: "+getMap().get("expert_profile_id"), parameter.isNegative());

    }

    @Then("I will check the profile completeness")
    @When("The profile is not complete")
    public  void profileCompleteness(){

        info("Checking if profile is complete");
        profileComplete.checkProfileCompletemness();

        boolean isProfileComplete=profileComplete.isProfileComplete();

        if (isProfileComplete){

            this.AssertAndWriteToReport(isProfileComplete,"Profile is completed and canbe submit for review");

        }else if(isProfileComplete==false && parameter.isNegative()){

            this.checkAndWriteToReport(response.statusCode(),jsonParser.printError(),true);

        }else {

            this.AssertAndWriteToReport(isProfileComplete,"Profile is not completed yet, hence cannot be submitted for review");
        }

    }

    @Then("I will submit profile for review")
    public void submitProfile(){
    info("Submitting profile for review");

        profileComplete.checkProfileCompletemness();
        boolean isProfileComplete=profileComplete.isProfileComplete();
        String expert_profile_id=getMap().get("expert_profile_id");
       /* String profileID=expert_profile_id.substring(expert_profile_id.lastIndexOf('[')+1, expert_profile_id.lastIndexOf(']'));
        getMap().put("profile_id",profileID);
*/
        System.out.println(expert_profile_id);
        if (isProfileComplete){

            expertProfile.submitProfile(expert_profile_id);
            String submissiontime=getMap().get("profile_submission_time");
            this.checkAndWriteToReport(response.statusCode(),"Profile submitted successfully, submission timestamp: "+submissiontime,parameter.isNegative());

        }else {
            this.AssertAndWriteToReport(false,"Unable to submit profile as profile is not complete yet");
        }

    }

    @Then ("Verify that incomplete profile cannot be submitted for review")
    @Aliases(values = {"Verify expert profile is submitted successfully"})
    public void verifySubmission(){

        info("Verifying profile submission");

        if (expertProfile.isProfileSubmitted()){

           this.AssertAndWriteToReport(true,"Verified, expert profile is submited successfully. Submission timestamp: "+getMap().get("profile_submission_time"));

        }else{

            this.AssertAndWriteToReport(true,"Verified, incomplete profile cannot be submitted"+response.getResponse().prettyPrint());

        }

    }

@When("I approved the same expert profile")
@Then("I approved the same expert profile")
    public void approveProfile(){
        info("Approve profile--");

    expertProfile.approveProfile(getMap().get("expert_profile_id"));
    this.checkAndWriteToReport(
            response.statusCode(),"Expert Profile is approved by super user",parameter.isNegative()
    );

    }

@Then("Verify the profile is approved")
    public void verifyApprove(){
        info("Verifying if profile is approved");

        expertProfile.getExpertprofile(getMap().get("expert_profile_id"));

            this.AssertAndWriteToReport(
                    getMap().get("review_status").equals("3"),"Verifed, profile is approved by super user"
            );

    }
@When("I update the review status to $updateToStatus")
    public void updateReviewProfile(@Named("updateToStatus")String updateToStatus){

        info("Update review status");
    String reviewStatus=null;
        if (updateToStatus.equalsIgnoreCase("NOT_SUBMITTED_FOR_REVIEW")||
                updateToStatus.equalsIgnoreCase("NOT_SUBMITTED")||
                updateToStatus.equalsIgnoreCase("NOT SUBMITTED")||
                updateToStatus.equalsIgnoreCase("NOT SUBMITTED FOR REVIEW")){

             reviewStatus="1";

        }
                else if (updateToStatus.equalsIgnoreCase("SUBMITTED_FOR_REVIEW")||
                updateToStatus.equalsIgnoreCase("SUBMITTED")||
                updateToStatus.equalsIgnoreCase("SUBMITTED FOR REVIEW")){

             reviewStatus="2";
         }
                else if(updateToStatus.equalsIgnoreCase("APPROVED_BY_SUPER_ADMIN")||
                updateToStatus.equalsIgnoreCase("APPROVED")||
                updateToStatus.equalsIgnoreCase("APPROVED BY SUPER ADMIN")){

                     reviewStatus="3";}
                     else if (updateToStatus.equals("1")||updateToStatus.equals("2")||updateToStatus.equals("3")){
                    reviewStatus=updateToStatus;
        }

             expertProfile.updateReviewStatus(getMap().get("expert_profile_id"),reviewStatus);
                this.checkAndWriteToReport(
                        response.statusCode(),"Review updated successfully to review_status:"
                                +response.getResponse().jsonPath().getString("results.review_status"),parameter.isNegative()
                );

}

@Then("Verify review status of already approved profile cannot be changed to NOT_SUBMITTED_FOR_REVIEW")
@Aliases(values = {"Verify submitted status cannot be updated to approved",
        "Verify already approved profile cannot be changed to submitted status"})
public void verifyReviewUpdate(){
        info("Verifying Review  update");

        if (!expertProfile.isVerifiedReviewUpdate()){
            this.AssertAndWriteToReport(true,"Verified review profile cannot be updated");
        }else {
            this.AssertAndWriteToReport(false);
        }

}
@Then("Verify review status is updated to Approved")
public void verifyReviewUpdatedToApprove(){
    info("Verify  Review  status updated to approve");

    if (expertProfile.isVerifiedReviewUpdate()){
        this.AssertAndWriteToReport(true,"Verified review profile updated to approve");
    }else {
        this.AssertAndWriteToReport(false);
    }
}



public static void main(String[] args){
    SessionManagement.session().setUserToken("eyJ0aW1lc3RhbXAiOjE1MTA4OTk2NzQuMjU4NjcsImlwX2FkZHJlc3MiOiI2MS4yNDYuNDcuOTMiLCJ1c2VyX2lkIjoxNjF9:1eFa1a:ZdYDvj_kOq7i9oTApQ7gkcL0pcY");
    SessionManagement.session().setExpertToken("");
    TestStoryConfig obj1=new TestStoryConfig();

    ExpertProfileReviewTC obj= new ExpertProfileReviewTC(obj1.getReport(),"From main function");
    obj.updateReviewProfile("2");


}

}
