Meta:

Narrative:
As a user
I want to perform an action
So that I can achieve a business goal

Scenario: scenario description
Given an expert
When I get the session details

When The session is completed
Then I send a followup to user
And Followup should be successfully send

Given negative scenario
When I send another followup for same session
Then I should not allowed to send followup