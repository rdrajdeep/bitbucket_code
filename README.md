# README #

This is a detalied Technical Documentation of the experchat Automation Framework . This framewrok is wriiten in Java and support 
BDD approach like JBehave out of the box. This framework can be extended to support UI automation using Selenium/Sikuli and Mobile 
App automation using Appium . 

### What is this repository for? ###

This Repo contains the complete framework and other Utilities to run the framework . 

### How do I get set up? ###

1. Install IntelliJ IDEA or Ecllipse or Sublime . 
2. Install Maven , Git and Java8
3. After sucessfull instalation of all , create a directory in the drive and navigate to that directory 
4. Run git clone https://atlogysQA@bitbucket.org/avihoffer/ec_testing.git
5. Now Navigate to the Project root folder where pom.xml resides from terminal or Comand prompt .
6. Run mvn clean compile exec:java


### Contribution guidelines ###

This Framework wraps other third party framework like Rest Assured for API calls , JBehave for BDD , ExtentReport for reporting 
and Maven for runing the build process automatically . The primary programming language is JAVA but we are free to use any JVM 
DSL like groovy and Scala. This Framewrok is modular and each module and their specific task is outlined below :

# ec_testing / src / main / 

# resources
Contains all Stories. Story name should be the module name and start with a number sequentially . 
  
# java/expertchat/ apioperation 

This module wrap up Rest Assured APIs . AbstractApiFactory.java has all the POST , PUT, DELETE ect. methods . 
   
 Example:
 @Override
 public Response post ( String json, String url ) {
  Response r;
  r = given ( )
  .request ( )
  .spec ( Specification.setupRequestSpecBuilder ( ) )
  .body ( json )
  .post ( url );
  return r;
  }
	

# apioperation / apiresponse /

# ApiResponse.java
# HTTPCode.java
# ParseResponse.java
# ResponseDataType.java

 This module handles the API reponses . ApiResponse.java is a singleton class. 
 We need to create  object of this calss in every bussiness logic where API call is done . 
 This Module contain ParseResponse.java class that is reponsible for parsing JSON. 
    Example:
   private ApiResponse response=ApiResponse.getObject();
   private ParseResponse pr=new ParseResponse(response);  
   
ParseResponse class provides method public String getJsonData ( String keys, ResponseDataType type ) for faster Json Parsing .
HTTPCode is an interface that contais HTTP codes like 200 , 201 etc.

# apioperation / httpspecs / 
# Specification.java

Specification warps the basic API call params like base path and Content types . 

# apioperation / session
# SessionManagement.java
Handle session . Need to use it as below :

   Example:
   SessionManagement session=SessionManagement.session();
   session.getExpertToken();
   session.getUserToken();
   Login methods should set the session token Asfter a successful login . 
   session.setExpertToken("Some token");

# apioperation/ ExpertChatEndPoints.java
All API endpoints should be here as a constants .
    Example: String REGISTER = "expert/register/";

# apioperation/ ApiFactories.java
It is an Interface that has all method related to API operation like POST, PUT, GET etc.

# apioperation/ AbstractApiFactory.java
This class implements the ApiFactories interface and provide detailed implementations to all the overridden methods .

# params / parameter.java
This class is used to to set expert or user or if a test case is negative or not.

# params / Credentials.java

This class is used to get the Email or password from the Json as Array. The first element of  the array is email and second is 
password .
   
   Example:
   private String expertCredential;

    public void setuserCredential ( String userCredential ) {

     this.userCredential = userCredential;

    }
	
   public String[] getExpertCredential ( ) {

    JsonObject jsonObject = ( JsonObject ) new JsonParser ( ).parse ( this.expertCredential );

    String credential[] = { jsonObject.get ( "email" ).getAsString ( ) , jsonObject.get ( "password" ).getAsString ( ) };

    return credential;
   }

# expertchat / bussinesslogic /

All bussiness logics are written here . While Writing new scrpts for a new API the  one does not have to create everything for the scracths.
The class name should represent the  module we are working on . All bussiness logc class should extends AbstractApiFactory class and implements
ExpertChatEndPoints interface . The implemetation details of method like post, put etc are completly abstracted. One does not have to
worry about the implementations . 

   Example:
   public class Account extends AbstractApiFactory implements ExpertChatEndPoints {

    private static String accountId;
    ApiResponse response = ApiResponse.getObject ( );
    ParseResponse pr = new ParseResponse ( response );
    SessionManagement session = SessionManagement.session ( );
	
	/*Now create an account */
	 public void createAccount ( String json ) {

        response.setResponse (

                this.post ( json, EXPERT_ACCOUNT, session.getExpertToken ( ) )
        );

        if ( response.getResponse ( ).statusCode ( ) == HTTPCode.HTTP_OK ||
                response.getResponse ( ).statusCode ( ) == HTTPCode.HTTP_ACCEPTED ) {

            this.setAccountId ( pr.getJsonData ( "results.id", ResponseDataType.INT ) );
        }

        response.printResponse ( );
    }

 # expertchat / report /Report.java
 
 This class wraps up the ExtentReport Library . This is a complete abstraction . A new User who wants to contribute on ly to scripts
 does not have to worry about it's implementations .
 
# expertchat / usermap /TestUserMap.java
This is a class that wraps a hashmap . We can store test data in  the map in key value pair and use it whenever required during the 
scripting phase.

# expertchat / util /
All utility class incuding custom exception handling and Email . New Utility class should go to this module.

# expertchat / test / bdd
All test fixtures , that maps a Jbehve story line should go in to this module . Every class should extend AbstractSteps class. 
    
	Example:
    public class BasicFlowTC extends AbstractSteps{

    public BasicFlowTC ( ExtentReports reports, String casName) {

        super(reports, casName);
    }
    private ExpertChatApi expertChatApi = new ExpertChatApi();
    private Credentials credentials=Credentials.getCredentials ();
    
	@When ("register with $json as $name")
    public void register(@Named ("json") String json, @Named("name") String name) {}

# expertchat / test / TestStoryConfig.java

Once we finished writing a new  Script under bdd , We need tocall it in TestStoryConfig class . Below is the example of  how to use it.
  
   Example:
   @Override
   public InjectableStepsFactory stepsFactory ( ) {

        return new InstanceStepsFactory ( configuration ( ),

                new BasicFlowTC ( getReport (), "Log-in and registration flow"),
            }

One should not change any other method or configuration for a smooth program executions . 

# expertchat / test / TestRunner.java

This class is the Entry point of the framework , where execution begins . The PSV method this class can be use to drive the execution from IDE
without using command line.
  
   Example:
   public class TestRunner {
   public static void main(String args[]) {

        JUnitCore.runClasses ( TestStoryConfig.class );
    }

   }

 

### Who do I talk to? ###
Kishor Jyoti Sarma

kishor@atlogys.com
kishorjyoti@live.com
8447053658
Senior Quality Analyst
ATLOGYS, Delhi
