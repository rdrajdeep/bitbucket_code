package expertchat.bussinesslogic;

import expertchat.apioperation.AbstractApiFactory;
import expertchat.apioperation.ExpertChatEndPoints;
import expertchat.apioperation.apiresponse.ApiResponse;
import expertchat.apioperation.apiresponse.ParseResponse;
import expertchat.apioperation.apiresponse.ResponseDataType;

import java.util.List;

public class Searching extends AbstractApiFactory implements ExpertChatEndPoints{

    public enum SearchType{

        BY_ID,
        BY_TAG,
        BY_TEXT
    }

    private ApiResponse response=ApiResponse.getObject();
    private ParseResponse pr=new ParseResponse(response);

    private static String searchText;
    private static String tagId;
    private static String expertId;

    public  String getTagId() {
        return tagId;
    }

    public  void setTagId(String tagId) {
        Searching.tagId = tagId;
    }

    public  String getExpertId() {
        return expertId;
    }

    public void setExpertId(String expertId) {
        Searching.expertId = expertId;
    }


    public void setSearchText(String searchText){

        Searching.searchText=searchText;
    }

    public String getSearchText(){

        return searchText;
    }

    public void search(String value , SearchType sType) {

        System.out.println(SEARCH_BY_TEXT);

        if (sType.ordinal() == 2) {

            response.setResponse(
                    this.get(SEARCH_BY_TEXT + value)
            );

            response.printResponse();
            return;
        } else if (sType.ordinal() == 1) {
            response.setResponse(

                    this.get(SEARCH_BY_TAG_ID + value)
            );
            response.printResponse();
            return;
        } else if (sType.ordinal() == 0) {

            response.setResponse(

                    this.get(SEARCH_BY_EXPERT_ID+value+"/")
            );
            response.printResponse();
            return;
        }
    }


    public boolean verifyExpertInResult(String expertId) {

       String id=pr.getJsonData ( "results.id", ResponseDataType.INT );

           if(expertId.equals ( id )){

              return true;
       }
       return false;
    }


  public static void main(String [] args){


  }

}
