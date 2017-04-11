Meta:
Narrative:
update the tag as well to verify getstream update via celery
Sprint-4: EX:121

Scenario: scenario description

Given complete super admin flow and celery task(EX:121 Sprint-4)

Given an user

When login with {"email": "anoop+super@atlogys.com", "password": "qwerty12"}

Then create a super admin content as {
                                         "content_id": "ddshg8799",
                                         "title": "test Automation",
                                         "image": "",
                                         "description": "Automation",
                                         "tags": [1,2]
                                     }

Then get the content

Then get the feeds from get stream using tag 1

Then verify the tags

Then get the feeds from get stream using tag 2

Then verify the tags again

Then update the content as           {
                                         "content_id": "6778sasas",
                                         "title": "test Automation",
                                         "image": "",
                                         "description": "Automation",
                                         "tags": [1]

                                     }


Given negative scenario

When update the content as           {
                                         "content_id": "",
                                         "title": "",
                                         "image": "",
                                         "description": "",
                                         "tags": [6]

                                     }
Then should not $allowed

Then get the content

Then get the feeds from get stream using tag 1

Then verify the tags

!--Given negative scenario
Then get the feeds from get stream using tag 2
And verify the tags

Then get all the content

Then check the count of content

Then delete the content

Then check the count of content again

Then unhide the content

Then check the count of content again

Then get the content again

Then get all the content