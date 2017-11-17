package expertchat.bussinesslogic;

import expertchat.apioperation.AbstractApiFactory;
import expertchat.apioperation.ExpertChatEndPoints;
import expertchat.apioperation.apiresponse.ApiResponse;
import expertchat.apioperation.apiresponse.HTTPCode;
import expertchat.apioperation.apiresponse.ParseResponse;
import expertchat.apioperation.apiresponse.ResponseDataType;
import expertchat.apioperation.session.SessionManagement;
import expertchat.params.parameter;
import org.jbehave.core.annotations.Then;

import static expertchat.usermap.TestUserMap.getMap;

public class ExpertProfileReview extends AbstractApiFactory implements HTTPCode, ExpertChatEndPoints{

    private boolean isProfileSubmitted;
    private ApiResponse response = ApiResponse.getObject();
    private ParseResponse jsonParser= new ParseResponse(response);
    private SessionManagement session  = SessionManagement.session();


    public void setProfileSubmitted(boolean profileSubmitted) {
        isProfileSubmitted = profileSubmitted;
    }

    public boolean isProfileSubmitted() {
        return isProfileSubmitted;
    }

    public void getBasicProfile(){

        String url="expert/me-basic-info/";
        if (parameter.isExpert()) {

            System.out.println("I m in basic infor method");
            response.setResponse(this.get(url, session.getExpertToken()));

            if (isResponseOK()) {
                getMap().put("name", jsonParser.getJsonData("results.name", ResponseDataType.STRING));
                getMap().put("is_phone_number_verified", jsonParser.getJsonData("results.is_phone_number_verified", ResponseDataType.BOOLEAN));
                getMap().put("profile_photo", jsonParser.getJsonData("results.profile_photo", ResponseDataType.STRING));
                getMap().put("display_name", jsonParser.getJsonData("results.display_name", ResponseDataType.STRING));
                getMap().put("toc_and_privacy_policy_accepted", jsonParser.getJsonData("results.toc_and_privacy_policy_accepted", ResponseDataType.BOOLEAN));
                response.printResponse();

            } else {
                System.out.println("++BAD Response+++");
                response.printResponse();
            }
        }else{
            response.setResponse(this.get(url, session.getUserToken()));
            if (isResponseOK()) {
                getMap().put("name", jsonParser.getJsonData("results.name", ResponseDataType.STRING));
                getMap().put("is_phone_number_verified", jsonParser.getJsonData("results.is_phone_number_verified", ResponseDataType.BOOLEAN));
                getMap().put("profile_photo", jsonParser.getJsonData("results.profile_photo", ResponseDataType.STRING));
                getMap().put("display_name", jsonParser.getJsonData("results.display_name", ResponseDataType.STRING));
                getMap().put("toc_and_privacy_policy_accepted", jsonParser.getJsonData("results.toc_and_privacy_policy_accepted", ResponseDataType.BOOLEAN));
            } else {
                System.out.println("++BAD Response+++");
                response.printResponse();
            }
        }
    }

    public void getExpertprofile(){

        if (parameter.isExpert()){
            System.out.println("I m in load expert profile");

            response.setResponse(this.get(EXPERT_PROFILE, session.getExpertToken()));

            if (this.isResponseOK()) {
                String expert_profile_id=jsonParser.getJsonData("results.id ", ResponseDataType.STRING);
                String profileID=expert_profile_id.substring(expert_profile_id.lastIndexOf('[')+1, expert_profile_id.lastIndexOf(']'));

                getMap().put("expert_profile_id",profileID);
                getMap().put("profile_submission_timestamp", jsonParser.getJsonData("results.profile_submission_timestamp ", ResponseDataType.STRING));
                getMap().put("review_status",jsonParser.getJsonData("results.review_status",ResponseDataType.STRING));
               response.printResponse();

            }else
            {
                System.out.println("++Bad Response++");
                response.printResponse();
            }
        }else {
            response.setResponse(this.get(EXPERT_PROFILE, session.getUserToken()));
            if (this.isResponseOK()) {
                String expert_profile_id=jsonParser.getJsonData("results.id ", ResponseDataType.STRING);
                String profileID=expert_profile_id.substring(expert_profile_id.lastIndexOf('[')+1, expert_profile_id.lastIndexOf(']'));

                getMap().put("expert_profile_id",profileID);
                getMap().put("profile_submission_timestamp", jsonParser.getJsonData("results.profile_submission_timestamp ", ResponseDataType.STRING));
                getMap().put("review_status",jsonParser.getJsonData("results.review_status",ResponseDataType.STRING));
                response.printResponse();
            } else {
                System.out.println("++Bad Response++");
                response.printResponse();
            }
        }
    }

    public void getExpertprofile(String profile_id){

        if (parameter.isExpert()){

            response.setResponse(this.get(EXPERT_PROFILE+profile_id+"/", session.getExpertToken()));
            if (this.isResponseOK()) {
                getMap().put("review_status",jsonParser.getJsonData("results.review_status",ResponseDataType.STRING));
                response.printResponse();
            }else {
                System.out.println("++Bad Response++");
                response.printResponse();
            }
        }else {
            response.setResponse(this.get(EXPERT_PROFILE+profile_id+"/", session.getUserToken()));
            if (this.isResponseOK()) {
                getMap().put("review_status",jsonParser.getJsonData("results.review_status",ResponseDataType.STRING));
            } else {
                System.out.println("++Bad Response from expertprofiles api++");
                response.printResponse();
            }
        }
    }

    public void submitProfile(String profileID){

        String URL="expert/expertprofiles/"+profileID+"/submit_for_review/";
        response.setResponse(this.put("{}",URL,session.getExpertToken()));

        if (isResponseOK()){
            System.out.println("profile is submitted successfully");
            getMap().put("profile_submission_time",jsonParser.getJsonData("results.profile_submission_timestamp",ResponseDataType.STRING));
            setProfileSubmitted(true);
            response.printResponse();

        }else{
            System.out.println("Bad response====");
            getMap().put("error_code",jsonParser.getJsonData("errors.non_field_errors.code",ResponseDataType.INT));
            response.printResponse();
            setProfileSubmitted(false);
        }

    }

    public void approveProfile(String profileID){

        response.setResponse(this.put("{}",EXPERT_PROFILE+profileID+"/approve_profile/",session.getUserToken()));
        if (isResponseOK()){
            response.printResponse();

        }else {
            System.out.println("+++BAD Response for approve profile+++");
            response.printResponse();
        }
    }

    public void updateReviewStatus(String profileId, String reviewStatus){

        String json="{\n" +
                "    \"review_status\": "+reviewStatus +
                "}";
        String url=EXPERT_PROFILE+profileId+"/update_review_status/";
        response.setResponse(this.put(json,url,session.getUserToken()));

        if (isResponseOK()){
            response.printResponse();
            System.out.println("Review status updated successfully");
        }else{
            response.printResponse();
            System.out.println("Review status cannot be updated");
        }
    }


    public boolean isVerifiedReviewUpdate() {
        boolean flag=false;
        if (isResponseOK()) {
            if (jsonParser.getJsonData("results.review_status", ResponseDataType.STRING).equals("3")) {
                flag= true;

            } else {
                flag= false;
                getMap().put("error_code",jsonParser.getJsonData("errors.non_field_errors.code",ResponseDataType.STRING));
                getMap().put("error_message",jsonParser.getJsonData("errors.non_field_errors.message",ResponseDataType.STRING));
            }
        }
        return flag;

    }



    public boolean isResponseOK(){
        if (response.statusCode()==HTTP_OK||response.statusCode()==HTTP_ACCEPTED){return true;}
        else{return false;}
    }


}
