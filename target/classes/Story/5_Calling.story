Given an user
Then register a device as {
                          "device_type": "ios",
                          "device_name": "USer device",
                          "device_sub_type": "sas",
                          "device_id": "sas",
                          "device_token": "sas",
                          "device_os": "sas"
                          }


Given an expert
Then register a device as {
                          "device_type": "ios",
                          "device_name": "Expert device",
                          "device_sub_type": "sas",
                          "device_id": "sas",
                          "device_token": "sas",
                          "device_os": "sas"
                          }


Given an user
Then create a card with fake-masterpass-mastercard-nonce
Then schedule a session of 10 mint

Given an expert
Then accept the call

Then disconnect the call

Given an user
Then initiate a call of scheduled_duration 10

Given an expert
Then accept the call

Then disconnect the call

Then provide review as {
                           "overall_rating": 1,
                           "knowledge_rating": null,
                           "communication_rating": null,
                           "professionalism_rating": null,
                           "text_review": ""
                       }

Then cancel the session

Then extend an ongoing session by scheduled_duration of 20

Then get all the past session and verify

Then get all the future session and verify

Then create a session for 60 mint and the revenue should be 90 $

Then extend the session by 10 mint and check the revenue

Then check the session status

Then check the notification