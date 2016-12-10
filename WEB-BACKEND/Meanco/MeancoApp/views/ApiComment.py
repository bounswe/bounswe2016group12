from MeancoApp.models import *
from django.http import request
from django.http import HttpResponse
from django.views.decorators.csrf import csrf_exempt
import json
# example:
# topicId: 1
# userId: 1
# text: HELP
#
@csrf_exempt
def addComment(request):
    if request.method == 'POST':
        topicId=request.POST.get('topicId')

        if 'userId' not in request.POST:
            userId = request.user.id
        else:
            userId= request.POST.get("userId")

        text= request.POST.get('text')
        profileId=Profile.objects.get(user_id=userId).id
        try:
            print(topicId,profile)
            Com=Comment(topic_id=topicId,profile_id=profileId)
            Com.save()
            Com.edit(text)
        except:
            return HttpResponse("Comment Creation Error", status=400)
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
# example:
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

        try:
            ct = CommentedTopic.objects.get(topic_id=Com.topic,profile_id=Com.profile )
            ct.visited()
            ct.save()
        except:
            return HttpResponse("Comment linking Error", status=400)
        return HttpResponse("Comment Edited", status=200)


def deleteComment():
    return


#
#   userId: 1
#   comment : 5
#   direction : "upvote"
@csrf_exempt
def rateComment(request):
    comment = request.POST.get("comment")

    if 'userId' not in request.POST:
        userId = request.user.id
    else:
        userId = request.POST.get("userId")
    profileId=Profile.objects.get(user_id=userId).id
    direction = request.POST.get("direction")
    if Voter.objects.filter(comment_id=comment, profile_id=profileId):
        voter = Voter.objects.get(comment_id=comment, profile_id=profileId)
        voter.toggle(direction)
        voter.save()
    else:
        voter = Voter(comment_id=comment, profile_id=profileId)
        voter.toggle(direction)
        voter.save()
    return HttpResponse("Rated", status=200);
