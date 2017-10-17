
package expertchat.bussinesslogic;
import expertchat.apioperation.apiresponse.ApiResponse;
import expertchat.apioperation.AbstractApiFactory;
import expertchat.apioperation.ExpertChatEndPoints;
import expertchat.apioperation.apiresponse.HTTPCode;
import java.util.ArrayList;
import java.util.List;

public class DeleteAllExpertSlot  extends AbstractApiFactory implements HTTPCode,ExpertChatEndPoints{
    private ApiResponse response=ApiResponse.getObject();

    public void deleteAllExpertSlots(String token){

        //String token="eyJ1c2VyX2lkIjo1NDYsImlwX2FkZHJlc3MiOiI2MS4yNDYuNDcuOTMiLCJ0aW1lc3RhbXAiOjE1MDgyMTk4NDEuMTYyOTgyfQ:1e4KsT:k6-qDxDLcd6XOCT5X3KPnPab1Qs";
        String url="http://api.qa.experchat.com/v1/expert/slots/";
        String json1="{}";
        List slot_ids = new ArrayList<String>();

        response.setResponse(this.get(url,token));
        if (response.statusCode()==HTTP_ACCEPTED||response.statusCode()==HTTP_OK){
            slot_ids.addAll(response.getResponse().jsonPath().getList("results.id"));
        }else {
            System.out.println("error in getting slots ids");
            response.printResponse();
        }
        System.out.println("count of slot id: "+slot_ids.size());
        for (int i=0;i<slot_ids.size();i++) {
            response.setResponse(this.delete(json1, url + slot_ids.get(i)+"/", token));
            if (response.statusCode() == HTTP_OK || response.statusCode() == HTTP_ACCEPTED || response.statusCode() == HTTP_NO_CONTENT) {
                System.out.println(slot_ids.get(i)+" deleted -----");
            } else {
                System.out.println("Not deleted");
                response.printResponse();
                System.out.println(response.statusCode());
            }
        }
    }

    public void deleteAllFutureSession(String token){

        //String token="eyJ1c2VyX2lkIjo0NSwiaXBfYWRkcmVzcyI6IjYxLjI0Ni40Ny45MyIsInRpbWVzdGFtcCI6MTUwODIyOTE5OS41MTk3MzZ9:1e4NJP:1BAInVtUU1zPBUwQMeQMd0QZ420";
        String url="http://connect.qa.experchat.com/v1/future-sessions/";
        String url2="http://connect.qa.experchat.com/v1/sessions/";
        String json1="{}";
        List listOfIDs = new ArrayList<String>();

        response.setResponse(this.get(url,token));
        if (response.statusCode()==HTTP_ACCEPTED||response.statusCode()==HTTP_OK){
            listOfIDs.addAll(response.getResponse().jsonPath().getList("results.id"));
        }else {
            System.out.println("error in getting session ids");
            response.printResponse();
        }
        System.out.println("count of session id: "+listOfIDs.size());
        for (int i=0;i<listOfIDs.size();i++) {
            response.setResponse(this.delete(json1, url2 + listOfIDs.get(i)+"/cancel/", token,true));
            if (response.statusCode() == HTTP_OK || response.statusCode() == HTTP_ACCEPTED || response.statusCode() == HTTP_NO_CONTENT) {
                System.out.println(listOfIDs.get(i)+" deleted -----");
            } else {
                System.out.println("Not deleted");
                response.printResponse();
                System.out.println(response.statusCode());
            }
        }
    }
}
