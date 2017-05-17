Given an expert

When register with {"email": "kishor+test23@atlogys.com","password": "qwerty12" } as expert1

Then Verify Email

Then login with expert1

Then create a calender as    {
                             "title": "Experchatesting",
                                "start_time": "8:00:00",
                                "end_time": "11:00:00",
                                "timezone": "Asia/Kolkata",
                                "week_days": [
                                  1,2,3,4,5,6,7
                                ]
                            }


Then get the avilable slot of expert1
Given an user

When register with {"email": "kishor+test23@atlogys.com","password": "qwerty12" } as user1

Then Verify Email

Then login with user1

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
Then schedule a session as {
                               "title": "a test call",
                               "details": "test",
                               "scheduled_datetime":"2017-05-29T02:40:00Z",
                               "expert_profile":7,
                               "expert":12,
                               "user_device":11,
                               "scheduled_duration": 20,
                               "card":5,
                               "promo_code": ""
                           }

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