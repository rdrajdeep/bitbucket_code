package expertchat.bussinesslogic;

import expertchat.apioperation.AbstractApiFactory;
import expertchat.apioperation.ExpertChatEndPoints;
import expertchat.apioperation.apiresponse.ApiResponse;
import expertchat.apioperation.apiresponse.HTTPCode;
import expertchat.apioperation.apiresponse.ParseResponse;
import expertchat.apioperation.session.SessionManagement;
import expertchat.util.DatetimeUtility;
import expertchat.util.ResponseParser;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.*;
import static expertchat.usermap.TestUserMap.getMap;

public class SessionUtil extends AbstractApiFactory implements HTTPCode, ExpertChatEndPoints {

    private ApiResponse response = ApiResponse.getObject();
    private List<String> slots=new ArrayList<String>();
    private DatetimeUtility dateUtil=new DatetimeUtility();
    private ParseResponse jsonParser = new ParseResponse ( response );
    private SessionManagement session=SessionManagement.session();
    private LocalTime localTime = LocalTime.now();
    private int startHour;
    private int startMint;
    private int endHour;
    private int endMint;
    private long inMiliisSchedule;

    public long getScheduleTimeInMillSecond(){

        return inMiliisSchedule;

    }


    public void createPromoCode(String json){

         response.setResponse(this.post(json,ExpertChatEndPoints.PROMO_CODE, SessionManagement.session ( ).getUserToken(),true));

        if(response.statusCode()==HTTP_OK|| response.statusCode()==HTTP_ACCEPTED)

            System.out.println("Promo code created Success fully");
        
        else
            System.out.println("Ahh Ohh! Something went wrong");

    }

    public boolean isValidCoupon(String promocode) {

        System.out.println("Validatin coupon");
        String expertID=getMap().get("expertId");
        String slot_datetime= getMap().get("slot_datetime");
        String json="{\n" +
                "    \"promo_code\": \""+promocode+"\",\n" +
                "    \"expert_id\": "+expertID+",\n"+
                "    \"session_price\": 15,\n" +
                "    \"scheduled_datetime\": \""+slot_datetime+"\"\n"+
                "}";
        System.out.println("Print json "+json);

        String url=SESSION+"validate-promo-code/";
        System.out.println("Printing URL "+url);
        response.setResponse(this.post(json,url,session.getUserToken(),true));

        if (response.statusCode()==HTTP_OK||response.statusCode()==HTTP_ACCEPTED){
            response.printResponse();
            return true;
        }else {
            response.printResponse();
            return false;
        }
    }


    public List<Long> getaSlot(){

        String url= SEARCH_BY_EXPERT_ID+ getMap().get("expertProfileId");
        String temp=null;
        String calenderDate=null;
        List<String> calender=new ArrayList<>();
        List<Long> calenderInUnix=new ArrayList<>();
        response.setResponse(this.get(url, session.getUserToken()));

        //response.printResponse();
        System.out.println("***This are available slots in UTC time****"+response.getResponse().jsonPath().getList("results.calendars"));


        if (response.statusCode()==HTTP_ACCEPTED || response.statusCode()==HTTP_OK){

            calender=response.getResponse().jsonPath().getList("results.calendars");

        }
        for (String item:calender){
            temp=item;
            calenderDate=temp+"Z";
            calenderInUnix.add(dateUtil.convertToUnixTimestamp(calenderDate));
        }

     return calenderInUnix; // Returning list of long unix datetime

    }

    public List getAllSlots( String min){

        System.out.println("---Getting all slots---");

        String expert_id=getMap().get("expertId");

        System.out.println("Exprt base Id "+expert_id);

        String api=USER_AVILABLE_SLOTS+expert_id+"/";

        response.setResponse(

               this.get(api, session.getUserToken())
        );

        if(response.statusCode()!=HTTP_BAD || response.statusCode()!=HTTP_UNAVAILABLE) {

            slots= new ResponseParser().onSuccess(response.getResponse().asString(), min);
        }

        System.out.println("*****I am in getAllSlots>>>>***"+slots);


        return slots;
    }



    public String getCurrentTimefromServer(){

        response.setResponse(
                this.get("http://connect.qa.experchat.com/now/")
        );

        return response.getResponse().jsonPath().getString("results.datetime");

    }

    public long getCurrentTimeInMilis(){

        String currentTime=this.getCurrentTimefromServer();
        LocalDateTime serverjodatime = new DateTime(currentTime).toLocalDateTime();
        DateTimeFormatter serverdtfOut = DateTimeFormat.forPattern("MMM dd yyyy, hh:mm a");
        long currentTimeInMilli=this.getTimeInMillis(serverdtfOut.print(serverjodatime), "MMM dd yyyy, hh:mm a");
        return currentTimeInMilli;

    }


   public void checkAndWaitForCall() throws Exception{

       this.getCurrentTimefromServer();

       String scheduleTime= getMap().get("scheduled_datetime");

       LocalDateTime serverjodatime = new DateTime(this.getCurrentTimefromServer()).toLocalDateTime();

       DateTimeFormatter serverdtfOut = DateTimeFormat.forPattern("MMM dd yyyy, hh:mm a");

       LocalDateTime scheduleTimeJoda = new DateTime(scheduleTime).toLocalDateTime();

       DateTimeFormatter schedule = DateTimeFormat.forPattern("MMM dd yyyy, hh:mm a");

       long inmillisLocal=getTimeInMillis(serverdtfOut.print(serverjodatime),"MMM dd yyyy, hh:mm a");
       long diff=0;

       inMiliisSchedule = getTimeInMillis(schedule.print(scheduleTimeJoda),"MMM dd yyyy, hh:mm a");

       if(inmillisLocal <=inMiliisSchedule){

           diff=inMiliisSchedule-inmillisLocal;

           System.out.println(" Waiting "+diff/60000+" minutes to initiate the call");

           Thread.sleep(diff);
       }


   }


   /**/
    public long getTimeInMillis(String dateTime, String inputFormat){

        try {

            SimpleDateFormat sdf = new SimpleDateFormat(inputFormat, Locale.getDefault());

            Date date = sdf.parse(dateTime);

            return date.getTime();

        } catch (ParseException e) {

            e.printStackTrace();
            return 0;
        }
    }


    /**
     *
     * @param args
     * @throws Exception
     */

   public static void main(String[] args) throws Exception{

       int duration=Integer.parseInt("10"); //10 min

       SessionUtil session=new SessionUtil();

       session.checkAndWaitForCall();
       long scheduleDateTime=session.getScheduleTimeInMillSecond();
       System.out.println(scheduleDateTime+"  Schedule date in mili");
       long durationToMilli=duration*60000;

       long endTime=(scheduleDateTime+durationToMilli);
       System.out.println(scheduleDateTime+"---"+endTime+"  End time after+ 10");
       long extensionTimeBeforeEnd=5*60000;
       long extensionTime=endTime-extensionTimeBeforeEnd;

       String currentTime=session.getCurrentTimefromServer();

       LocalDateTime serverjodatime = new DateTime(currentTime).toLocalDateTime();
       DateTimeFormatter serverdtfOut = DateTimeFormat.forPattern("MMM dd yyyy, hh:mm a");

       long currentTimeInMilli=session.getTimeInMillis(serverdtfOut.print(serverjodatime), "MMM dd yyyy, hh:mm a");

       System.out.println(currentTimeInMilli+"Current time "+extensionTime+" extension time");
       if(currentTimeInMilli>=extensionTime && currentTimeInMilli< endTime){
           System.out.println("TEST sucess");
           System.out.println(currentTimeInMilli-extensionTime);
       }
       else{
           System.out.println("False");

       }



   }


   public String getStrtTimeForCalender() throws Exception{

      startHour =localTime.getHour();

      startMint =localTime.getMinute();


      while (startMint % 5 !=0){

          startMint=startMint+1;
      }

       if(startMint==60||startMint==59){
           startHour=startHour+1;
           startMint=00;
       }


    return new String(String.valueOf(startHour)+":"+String.valueOf(startMint)+":00");

   }


    public String getEndTimeForCalender(int duration){

       if((startMint +duration)>60){

           endHour = startHour +1;
           endMint =(startMint +duration)- startMint;

       }else if((startMint +duration)==60) {

           endHour = startHour +1;
           endMint =00;

       } else {

               endHour=startHour;
               endMint=startMint +duration;

           }

        return new String(String.valueOf(endHour)+":"+String.valueOf(endMint)+":00");
    }

    public int today(){

        Calendar calendar=Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        if(day==7){

            return 1;

        }else {

          return  day-1;
        }
    }



}

