Meta:

Narrative:
As a user
I want to perform an action
So that I can achieve a business goal

Scenario: scenario description

Given an user
When login with user1

Then register a device as {
                          "device_type": "ios",
                          "device_name": "USer device",
                          "device_sub_type": "sas",
                          "device_id": "sas",
                          "device_token": "sas",
                          "device_os": "sas"
                          }


Given an expert
When login with expert1
And get expert profile
Then register a device as {
                          "device_type": "ios",
                          "device_name": "Expert device",
                          "device_sub_type": "sas",
                          "device_id": "sas",
                          "device_token": "sas",
                          "device_os": "sas"
                          }


Given an user
When login with user1
Then initiate a call of scheduled_duration 10

Given an expert
Then login with expert1
And accept the call

Then disconnect the call

Given an user
When login with user1
Then initiate a call of scheduled_duration 10

Given an expert
Then login with expert1
And accept the call

Then disconnect the call

Then provide review as {
                           "overall_rating": 1,
                           "knowledge_rating": null,
                           "communication_rating": null,
                           "professionalism_rating": null,
                           "text_review": ""
                       }

Then schedule a session as {
                               "title": "",
                               "details": "",
                               "scheduled_datetime": null,
                               "expert_profile": null,
                               "expert": null,
                               "user_device": null,
                               "scheduled_duration": null,
                               "card": null
                           }

Then cancel the session

Then extend an ongoing session by scheduled_duration of 20

Then get all the past session and verify

Then get all the future session and verify
