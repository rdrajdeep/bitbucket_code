Meta:

Narrative:
As a user
I want to perform an action
So that I can achieve a business goal
Scenario: scenario description

Given complete expert profile flow

Given an expert

When login with expert1
And get the profile

Given negative scenario
When try to create expert profile as
{
    "tags": [
       1,2,3,4
        ],
    "medias":[],
    "headline": "fdfd",
    "summary": "fdfd",
    "my_experience": "fdfdff",
    "year_of_experience":"2",
    "educational_background": "Bachelor of Technology (Computer Science and Information Engineering)"
}
Then should not allowed

Then update information on expert profile as
{
    "tags": [
       1,2,3,4
        ],
    "medias":[],
    "headline": "Java-8 Lambda",
    "summary": "Java Expert",
    "my_experience": "In test automation",
    "year_of_experience":"3",
    "educational_background": "Bachelor of Technology (Computer Science and Information Engineering)"
}

Then upload media as C:\Users\Kishor\Downloads\tv.mp4
