"""@package API
API documentation

"""
from MeancoApp.models import *
from django.http import request
from django.http import HttpResponse
from django.views.decorators.csrf import csrf_exempt
import django.core.serializers
import json

## Comment creation API Call
# Example Comment Creation Request:
# topicId: 1
# userId: 1
# text: HELP
@csrf_exempt
def addComment(request):
    if request.method == 'POST':
        topicId=request.POST.get('topicId')
        # check users session
        if 'userId' not in request.POST:
            userId = request.user.id
        else:
            userId= request.POST.get("userId")
        text="";
        if 'text' in request.POST:
            text= request.POST.get('text')
        profileId=Profile.objects.get(user_id=userId).id
        #Creates a comment and add its content
        try:
            print(topicId,profile)
            Com=Comment(topic_id=topicId,profile_id=profileId)
            Com.save()
            Com.edit(text)
        except:
            return HttpResponse("Comment Creation Error", status=400)
        # increases topics comment count.
        topic=Topic.objects.get(id=topicId)
        topic.commented()
        topic.save()
        # create a model for users latest commented topics.
        try:
            if(CommentedTopic.objects.filter(topic_id=topicId, profile_id=profileId).exists()):
                ct=CommentedTopic.objects.get(topic_id=topicId, profile_id=profileId)
                ct.visited()
                ct.save()
            else:
                ct=CommentedTopic(topic_id=topicId, profile_id=profileId)
                ct.save()
        except:
            return HttpResponse("Comment linking Error", status=400)
        return HttpResponse(json.dumps({
            "commentId": Com.id}),
            status=200,
            content_type="application/json")
## Editing Comment API call.
# Example Edit Request:
# commentId: 1
# text: HELP
@csrf_exempt
def editComment(request):
    if request.method == 'POST':
        commentId = request.POST.get('commentId')
        text = request.POST.get('text')
        Com = Comment.objects.get(pk=commentId)
        try:
            Com.edit(text)
        except:
            HttpResponse("Comment Edit Error", status=400)
        # if user edits a comment, we update users latest commented topics.
        try:
            ct = CommentedTopic.objects.get(topic_id=Com.topic,profile_id=Com.profile )
            ct.visited()
            ct.save()
        except:
            return HttpResponse("Comment linking Error", status=400)
        return HttpResponse("Comment Edited", status=200)

## API request to send users votes on comments for a page
# Example API request:
# TopicId=5
# [Android] UserId=1
@csrf_exempt
def getUsersVotes(request):
    if 'UserId' not in request.GET:
        userId = request.user.id
    else:
        userId = request.GET.get("UserId")
    profileId=Profile.objects.get(user_id=userId).id
    TopicId=int(request.GET.get('TopicId'))
    Comments=Comment.objects.filter(topic_id=TopicId).values_list('id')
    votes= list(Voter.objects.filter(comment_id__in=Comments,profile_id=profileId))
    return HttpResponse(django.core.serializers.serialize('json',votes ),
        status=200,
        content_type="application/json")

## API request for rating comments.
# Example API request:
# userId: 1
# comment : 5
# direction : "upvote"
@csrf_exempt
def rateComment(request):
    comment = request.POST.get("comment")

    if 'userId' not in request.POST:
        userId = request.user.id
    else:
        userId = request.POST.get("userId")
    profileId=Profile.objects.get(user_id=userId).id
    direction = request.POST.get("direction")
    # Check if voter voted on comment before.
    if Voter.objects.filter(comment_id=comment, profile_id=profileId):
        voter = Voter.objects.get(comment_id=comment, profile_id=profileId)
        voter.toggle(direction)
        voter.save()
    else:
        voter = Voter(comment_id=comment, profile_id=profileId)
        voter.toggle(direction)
        voter.save()
    return HttpResponse("Rated", status=200);
