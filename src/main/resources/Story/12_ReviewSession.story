Meta:

Narrative:
As a user
I want to perform an action
So that I can achieve a business goal


Given an user

When I completed a call
Then get session details
Then Send a review as {
                          "overall_rating": 3,
                          "knowledge_rating": 3,
                          "communication_rating": 3,
                          "professionalism_rating": 3,
                          "text_review": "Good Experience"
                      }
Then Verify review is successfully send

Given negative scenario
When I send review for same session once again as {
                                                                            "overall_rating": 3,
                                                                            "knowledge_rating": 3,
                                                                            "communication_rating": 3,
                                                                            "professionalism_rating": 3,
                                                                            "text_review": "Good Experience"
                                                                        }
Then I should not allowed to send review



