package expertchat.bussinesslogic;

import expertchat.apioperation.AbstractApiFactory;
import expertchat.apioperation.ExpertChatEndPoints;
import expertchat.apioperation.apiresponse.ApiResponse;
import expertchat.apioperation.apiresponse.HTTPCode;
import expertchat.apioperation.apiresponse.ParseResponse;
import expertchat.apioperation.apiresponse.ResponseDataType;
import expertchat.apioperation.session.SessionManagement;
import expertchat.util.ExpertChatException;
import java.time.Instant;
import java.util.*;
import static expertchat.apioperation.ExpertChatEndPoints.*;
import static expertchat.usermap.TestUserMap.getMap;


public class SocialLinks extends AbstractApiFactory implements ExpertChatEndPoints, HTTPCode {


    private ApiResponse response=ApiResponse.getObject();

    private ParseResponse parseResponse=new ParseResponse(response);

    private String url;

    private List<String> socialLinkId=new ArrayList<>();

    private HashMap<String,String > info=new LinkedHashMap<>();

    private  String countOfFeeds;

    private  String contentID;

    private  String publishedContentId;

    public String getPublishedContentId() {
        return publishedContentId;
    }

    public void setPublishedContentId(String publishedContentId) {
        this.publishedContentId = publishedContentId;
    }

    public void setCountOfFeeds(String countOfFeeds){
        this.countOfFeeds=countOfFeeds;
    }

    public String getcountOfFeeds(){

        return countOfFeeds;
    }
    public void setContentID(String contentID){

        this.contentID=contentID;
    }

    public String getContentID(){

        return contentID;
    }

    public String getUrl(){

        return url;
    }


     private void setResponse(String social){
         response.setResponse(

                 this.get("http://api.qa.experchat.com/v1/social/"+social+"/", SessionManagement.session().getToken())
         );
         response.printResponse();

         url=parseResponse.getJsonData("results.url", ResponseDataType.STRING);
         System.out.println("Insta-->"+url);

     }

    private void GetTheURLforSocialLinkDevices(String social){

          switch (social.toLowerCase()){

              case "instagram": setResponse("instagram"); break;

              case "facebook":  setResponse("facebook");  break;

              case "youtube":   setResponse("youtube");   break;

              default: throw new ExpertChatException("Invalid Social media");

          }
    }

    /**
     *
     * @param social
     */
    private void getTheInfoOfUser(String social){

     //   String code=new OpenBrowser().getCodeFromBrowser(this.getUrl(),social);

      //  String url="http://api.qa.experchat.com/v1/social/"+social+"/get_token/"+code+"/";

        System.out.println("Code url-->"+url);

        response.setResponse(
                this.get(url,SessionManagement.session().getToken())
        );

        response.printResponse();

        if(social.toLowerCase().equals("instagram")){

            info.put("instaUserID",parseResponse.getJsonData("results[0].id",ResponseDataType.STRING));

            info.put("instaAccount",parseResponse.getJsonData("results.metadata.id",ResponseDataType.INT));

        } else

            if(social.toLowerCase().equals("facebook")){

            info.put("fbUserID",parseResponse.getJsonData("results[0].id",ResponseDataType.STRING));

            info.put("fbPageID",parseResponse.getJsonData("results[1].id",ResponseDataType.STRING));

            info.put("fbAccount",parseResponse.getJsonData("results.metadata.id",ResponseDataType.INT));
        }

    }


    public void postSocialLink(String socialMedia){

        GetTheURLforSocialLinkDevices(socialMedia);
        getTheInfoOfUser(socialMedia);

        int account;
        String detail;
        String json=null;

        switch (socialMedia.toLowerCase()){

            case "instagram": account=Integer.parseInt(info.get("instaAccount"));
                              detail=info.get("instaUserID");
                              json="{\"feed_type\":2,\"account\":"+account+",\"detail\": \""+detail+"\"}";

                              break;

            case "facebook-user" : account=Integer.parseInt(info.get("fbAccount"));
                                   detail=info.get("fbUserID");
                                   json="{\"feed_type\":1,\"account\":"+account+",\"detail\": \""+detail+"\"}";

                                   break;

            case "facebook-page" : account=Integer.parseInt(info.get("fbAccount"));
                                   detail=info.get("fbPageID");
                                   json="{\"feed_type\":1,\"account\":"+account+",\"detail\": \""+detail+"\"}";

                                   break;

        }

        response.setResponse(
                this.post(json, SOCIAL_LINKS, SessionManagement.session().getToken())
        );

        response.printResponse();
    }


    public void getSocialLinks(){

        String id=null;
        response.setResponse(
                this.get(SOCIAL_LINKS, SessionManagement.session().getToken())
        );

        response.printResponse();

        List s=response.getResponse().jsonPath().getList("results");

        for(int i=0; i<s.size();i++){

            int index1=s.get(i).toString().indexOf("id");
            int index2=s.get(i).toString().indexOf("detail");
            id=s.get(i).toString().substring(index1+3,index2).replace(",","");
            socialLinkId.add(id);
            getMap().put ( "socialLinkId"+id, id);
            id=null;
        }

    }

    public List<String> getSocialLinkId(){

        return socialLinkId;
    }

    public boolean deleteSocialLink( String id){

        return this.isDelete(SOCIAL_LINKS+id+"/", SessionManagement.session().getToken());
    }


   public void addLinksToExpertProfile( String expertId) {

       int size=socialLinkId.size();

       String s="";

       for(int i=0;i<size;i++){

           s=s+socialLinkId.get(i)+",";
       }

       String mString=s.replaceFirst(".$", "");

       String json = "{\n" +
               "   \"social_link_ids\":  [\n" +
               "   " +mString+" \n" +
               "   \t]\n" +
               "}";

       System.out.println("Here is the JSON "+json);

       String url = EXPERT_PROFILE + expertId + "/update_social_links/";

       response.setResponse(

               this.put(json,url,SessionManagement.session().getToken())
       );

       response.printResponse();
   }

   public void getLinkedListsOfExpertProfile( String expertID){

        String url=PROFILE+expertID+"/"+SOCIAL_LINKS;

        response.setResponse(

                this.get(url,SessionManagement.session().getToken())
        );

        response.printResponse();

   }



    public void addFeedsToSocialLink(String rssFeedUrl) {

       response.setResponse(

               this.post(rssFeedUrl, FEED_LINK, SessionManagement.session().getToken())
       );

       response.printResponse();

    }

    public String getSocialLinkCount(){

       getMap ().put ( "countOfTotalSocialLinks", parseResponse.getJsonData("metadata.count", ResponseDataType.INT) );
       return getMap ().get ( "countOfTotalSocialLinks" );

    }

    public void getFeedListing() {

        long unixTimeStamps= Instant.now().getEpochSecond();

        System.out.println("Current unix timestamps::-->"+unixTimeStamps);

        response.setResponse(

                this.get(GET_FEEDS + unixTimeStamps, SessionManagement.session().getToken())
        );

        if(response.statusCode ()!= HTTPCode.HTTP_BAD){

            setContentID (parseResponse.getJsonData("results[0].id", ResponseDataType.STRING));
            getMap().put ("unpublishedContentId" ,getContentID ());
        }
        response.printResponse();
    }

    public void publishContent(String contentID) {

            String json = "{\"content_id\":\"" + contentID+ "\"}";

            response.setResponse(

                    this.post(json, PUBLISH_CONTENT, SessionManagement.session().getToken()));
        response.printResponse();
    }


    public void getContents(){

        response.setResponse(this.get(PUBLISH_CONTENT, SessionManagement.session().getToken()));

        if(response.statusCode ()!=HTTP_BAD){

            setPublishedContentId(parseResponse.getJsonData("results[0].id", ResponseDataType.INT));

            getMap ().put ( "publishedContentID", getPublishedContentId () );
        }
        response.printResponse();
    }


    public void getContent(String id){

        response.setResponse(

                this.get(PUBLISH_CONTENT+id+"/", SessionManagement.session().getToken()));

        response.printResponse();
    }

    public boolean deleteContent(String id){

       return this.isDelete(PUBLISH_CONTENT+id+"/", SessionManagement.session().getToken());
    }

    public boolean isPublished(String contentID){

        if(contentID.equals(

                parseResponse.getJsonData("results.content_id", ResponseDataType.STRING))){
                return true;
        }
        return false;
    }

    public boolean isIgnore
            (String contentID){

        if(contentID.equals(

                parseResponse.getJsonData("results.content_id", ResponseDataType.STRING))){
            return true;
        }
        return false;
    }

    public void ignoreContent(String contentID){

        String json="{\"content_id\":\""+contentID+"\"}";

        response.setResponse(this.post(json, IGNORE_CONTENT, SessionManagement.session().getToken()));
        response.printResponse();

    }



}
