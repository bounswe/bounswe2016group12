from MeancoApp.models import *
from django.http import request
from django.http import HttpResponse
from django.views.decorators.csrf import csrf_exempt
# example:
# topicId: 1
# profile: 1
# text: HELP
#
@csrf_exempt
def addComment(request):
    if request.method == 'POST':
        topicId=request.POST.get('topicId')
        profileId = request.user.id
        text= request.POST.get('text')
        try:
            print(topicId,profile)
            Com=Comment(topic_id=topicId,profile_id=profileId)
            Com.save()
            print("asd")
            Com.edit(text)
        except:
            return HttpResponse("Comment Creation Error")
        return HttpResponse("Comment Created")
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
            HttpResponse("Comment Edit Error")
        return HttpResponse("Comment Edited")


def deleteComment():
    return


#
#
#   comment : 5
#   direction : "upvote"
@csrf_exempt
def rateComment(request):
    comment = request.POST.get("comment")
    profile = 1   # replace with auth
    direction = request.POST.get("direction")
    if Voter.objects.filter(comment_id=comment, profile_id=profile):
        voter = Voter.objects.get(comment_id=comment, profile_id=profile)
        voter.toggle(direction)
        voter.save()
    else:
        voter = Voter(comment_id=comment, profile_id=profile)
        voter.toggle(direction)
        voter.save()
    return HttpResponse("Rated", status=200);