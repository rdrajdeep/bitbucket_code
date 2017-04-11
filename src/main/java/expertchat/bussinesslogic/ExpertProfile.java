package expertchat.bussinesslogic;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import expertchat.apioperation.AbstractApiFactory;
import expertchat.apioperation.apiresponse.ApiResponse;
import expertchat.apioperation.apiresponse.HTTPCode;
import expertchat.apioperation.apiresponse.ParseResponse;
import expertchat.apioperation.apiresponse.ResponseDataType;
import expertchat.apioperation.session.SessionManagement;
import static expertchat.apioperation.ExpertChatEndPoints.EXPERT_PROFILE;
import static expertchat.apioperation.ExpertChatEndPoints.EXPERT_PROFILE_BY_USER;
import static expertchat.usermap.TestUserMap.getMap;

public class ExpertProfile extends AbstractApiFactory implements HTTPCode{

    private ApiResponse response = ApiResponse.getObject();

    private ParseResponse parseResponse = new ParseResponse(response);

    private SessionManagement session = SessionManagement.session();

    private FileUpload fileUpload=new FileUpload();

    private String expertProfileID;

    private String expertID;

    private String expertCredential;

    private String userCredential;

    public void setExpertProfileID(String expertProfileID) {
        this.expertProfileID = expertProfileID;
    }

    public String getExpertProfileID() {
        return expertProfileID;
    }

    public void setExpertID(String expertID) {

        this.expertID = expertID;
    }

    public String getExpertID() {

        return expertID;
    }

    /**
     * @param profile
     */
    public void addExpertyProfile(String profile) {

        response.setResponse(
                this.post(profile, EXPERT_PROFILE, session.getToken())
        );

        if(response.statusCode()== HTTP_ACCEPTED ||
                response.statusCode()==HTTP_OK) {

            setExpertProfileID(
                    parseResponse.getJsonData("results.id", ResponseDataType.INT));


            setExpertID(
                    parseResponse.getJsonData("results.expert.id", ResponseDataType.INT));

            getMap().put("expertProfileId", getExpertProfileID());
            getMap().put("expertId",getExpertID());
        }

        response.printResponse();
    }

    /**
     * @param byExpert
     */
    public void getProfileOfExpert(boolean byExpert) {

        if ( byExpert ) {

        response.setResponse (
                this.get ( EXPERT_PROFILE , session.getToken ( ) )
        );
        response.printResponse ();
    }else {

            response.setResponse (
                    this.get ( EXPERT_PROFILE_BY_USER , session.getToken ( ) )
            );
        }

        if(response.statusCode ()==HTTP_OK){

            setExpertProfileID(
                    parseResponse.getJsonData("results[0].id", ResponseDataType.INT));


            setExpertID(
                    parseResponse.getJsonData("results[0].expert.id", ResponseDataType.INT));

            getMap().put("expertProfileId", getExpertProfileID());
            getMap().put("expertId",getExpertID());
        }

        response.printResponse();
    }

    public void getAllProfileOfExpert() {

        response.setResponse(
                this.get(EXPERT_PROFILE, session.getToken())
        );

        response.printResponse();
    }

    public String getProfileCount() {

        return parseResponse.getJsonData("metadata.count", ResponseDataType.INT);
    }

    public void getNextPage() {

        String page = parseResponse.getJsonData("metadata.next", ResponseDataType.STRING);
        response.setResponse(
                this.get(page, session.getToken())
        );

        response.printResponse();
    }

    public void updateExpertProfile(String profile, String id) {

        response.setResponse(
                this.patch(profile, EXPERT_PROFILE + id + "/", session.getToken())
        );

        response.printResponse();
    }

    public boolean deleteProfile(String id) {

        return this.isDelete(EXPERT_PROFILE + id + "/", session.getToken());

    }

    public void setExpertCredential(String expertCredential){

        this.expertCredential=expertCredential;

    }

    public void setuserCredential(String userCredential){

        this.userCredential=userCredential;

    }

    public String [] getExpertCredential(){

        JsonObject jsonObject= (JsonObject) new JsonParser().parse(this.expertCredential);

        String credential[]={jsonObject.get("email").getAsString(), jsonObject.get("password").getAsString()};

        return credential;
    }

    public String [] getUserCredential(){

        JsonObject jsonObject= (JsonObject) new JsonParser().parse(this.userCredential);

        String credential[]={jsonObject.get("email").getAsString(), jsonObject.get("password").getAsString()};

        return credential;
    }

    public void uploadMedia(String mediaPath, boolean isExpert) {

        if (isExpert) {

            fileUpload.uploadMedia(mediaPath, getExpertCredential()[0], getExpertCredential()[1],true);

        } else {

            fileUpload.uploadMedia(mediaPath, getUserCredential()[0], getUserCredential()[1],false);
        }
    }

    public String getResponseOfMediaUpload(){

       return FileUpload.getJson();
    }
}
