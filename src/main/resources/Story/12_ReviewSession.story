Meta:

Narrative:
As a user
Once I completed a call session, I will send a review / rating of the call session
So that I can verify review API  is working fine

Scenario: API -"http://connect.qa.experchat.com/v1/sessions/8777/review/"


Given an user

When I completed a call
Then get session details

Given negative scenario
When I send rating greater than 5 as {
                                                  "overall_rating": 6,
                                                  "knowledge_rating": 3,
                                                  "communication_rating": 3,
                                                  "professionalism_rating": 3,
                                                  "text_review": "Good Experience"
                                              }

Then Verify that review sending is unsuccessfull

Given negative scenario
When I send negative rating  as {
                                                  "overall_rating": -1,
                                                  "knowledge_rating": -1,
                                                  "communication_rating": -5,
                                                  "professionalism_rating": 3,
                                                  "text_review": "Good Experience"
                                              }

Then Verify that review sending is unsuccessfull


Given negative scenario
When I send rating 0 as {
                                                  "overall_rating": 0,
                                                  "knowledge_rating": 0,
                                                  "communication_rating": 0,
                                                  "professionalism_rating": 0,
                                                  "text_review": "Bad"
                                              }

Then Verify that review sending is unsuccessfull


Given negative scenario
When I send null overall rating as {
                                                  "overall_rating": 0,
                                                  "knowledge_rating": 0,
                                                  "communication_rating": 0,
                                                  "professionalism_rating": 3,
                                                  "text_review": "Bad"
                                              }

Then Verify that review sending is unsuccessfull


Then Send a valid review as {
                          "overall_rating": 3,
                          "knowledge_rating": 3,
                          "communication_rating": 3,
                          "professionalism_rating": 3,
                          "text_review": "Good Experience"
                      }

Then Verify that review sending is successfull

Given negative scenario
When I send review for same session once again as {
                                                                            "overall_rating": 3,
                                                                            "knowledge_rating": 3,
                                                                            "communication_rating": 3,
                                                                            "professionalism_rating": 3,
                                                                            "text_review": "Good Experience"
                                                                        }

Then Verify that review sending is unsuccessfull