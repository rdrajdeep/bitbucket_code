Meta:

Narrative:
This Story is for Expert profile review
API name: "Submit Expert Profile for Review": "http://api.qa.experchat.com/v1/expert/expertprofiles/1/submit_for_review/"

Scenario: scenario description
"Given an expert"
"When please login with {"email": "rajdeep+x4@atlogys.com","password": "testing123" }"

Given an expert
When I get the expert profile
Then I will check the profile completeness
Then I will submit profile for review
And Verify expert profile is submitted successfully


Given an superuser
Then please login with {"email":"kishor+super@atlogys.com","password":"testing123"}

When I approved the same expert profile
Then Verify the profile is approved

When I update the review status to APPROVED_BY_SUPER_ADMIN
Then Verify review status is updated to Approved

Given negative scenario
When I update the review status to NOT_SUBMITTED_FOR_REVIEW
Then Verify review status of already approved profile cannot be changed to NOT_SUBMITTED_FOR_REVIEW

Given negative scenario
When I update the review status to SUBMITTED_FOR_REVIEW
Then Verify already approved profile cannot be changed to submitted status

