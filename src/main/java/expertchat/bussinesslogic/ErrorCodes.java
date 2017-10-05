package expertchat.bussinesslogic;

public interface ErrorCodes {

    int NO_SLOT=5014;
    int EXTN_IN_FUTURE=5013;

    //Review session Error codes
    int ALREADY_REVIEW=5011;//You have already reviewed this session
    int INVALID_RATING=1000; // Invalid rating provided, such as 0, >5, <0, null

}
