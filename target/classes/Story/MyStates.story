Meta:

Narrative:
As a user
I want to perform an action
So that I can achieve a business goal

Scenario: scenario description
Given a complete Expert analytics flow

Given an expert
When register with {"email": "kishor+expert78@atlogys.com","password": "testing123" } as expert2
Then Verify Email
Then login with expert2
And get profile

Then chek the profile visits and count should be 0

Then logout the expert

Then search the expert profile anonymously

Then login with expert2
And chek the profile visits and count should be 1

Given an user
Then login with {"email": "kishor+user@atlogys.com","password": "testing123" }
Then search the expert profile

Given an expert
Then login with expert2
And chek the profile visits and count should be 2

Then logout

Given an user
When login with {"email": "kishor+user@atlogys.com","password": "testing123" }

Then register a device as {
                          "device_type": "ios",
                          "device_name": "MyStates Testing+User",
                          "device_sub_type": "sas",
                          "device_id": "sas",
                          "device_token": "sas",
                          "device_os": "sas"
                          }


Given an expert
When login with expert2
And get expert profile
Then register a device as {
                          "device_type": "ios",
                          "device_name": "MyStates Testing+Expert",
                          "device_sub_type": "sas",
                          "device_id": "sas",
                          "device_token": "sas",
                          "device_os": "sas"
                          }


Given an user
When login with {"email": "kishor+user@atlogys.com","password": "testing123" }
Then initiate a call of scheduled_duration 10

Given an expert
Then login with expert2
And accept the call

Then disconnect the call

Then login with expert2
Then check the session count and count should be 1
