Meta:

Narrative:
This Story is for Expert profile review
API name: "Submit Expert Profile for Review": "http://api.qa.experchat.com/v1/expert/expertprofiles/1/submit_for_review/"

Scenario: scenario description

Given an expert
When please login with {"email": "rajdeep+x1@atlogys.com","password": "testing123" }

Given an expert
When I get the expert profile
Then I will check the profile completeness
Then I will submit profile for review
And Verify expert profile is submitted successfully


Given an user
Then please login with {"email":"kishor+super@atlogys.com","password":"testing123"} as superUser

When I get the expert profile
When I approved the expert profile
Then Verify the profile is approved


Given negative scenario
When I reject the same profile
Then Verify already approved profile cannot be Rejected


Given negative scenario
When I update the review status to REJECTED_BY_SUPER_ADMIN
Then Verify already approved profile cannot be changed to submitted status

Given negative scenario
When I update the review status to NOT_SUBMITTED_FOR_REVIEW
Then Verify review status of already approved profile cannot be changed to NOT_SUBMITTED_FOR_REVIEW

Given negative scenario
When I update the review status to SUBMITTED_FOR_REVIEW
Then Verify already approved profile cannot be changed to submitted status