from ..models import *
from django.http import request
from django.http import HttpResponse
from django.views.decorators.csrf import csrf_exempt
import json
from django.core import serializers

# Example Post Request to addTopic
#
# userId= 15
# topicName="Donald Trump"
# tags=Politician:Occupation,English:Native Language
#
# TODO:Add better control
@csrf_exempt
def addTopic(request):
    if request.method== 'POST':
        userId =request.POST.get('userId')
        topicName=request.POST.get('topicName')
        tags = request.POST.get('tags')
        print(userId)
        try:
            t=Topic()
            t.save()
        except:
            return HttpResponse(("Topic Couldn't be created:"))
        print(tags)
        tagsList=tags.split(",")
        try:
            tName = Name(topic_id=t.id,label=topicName)
            tName.save()
        except:
            return HttpResponse(("TopicName Creation Error: " ,topicName))
        print (len(tagsList))
        for tagWithContext in tagsList:
            tag=tagWithContext.split(":")
            print(len(tag))
            if Tag.objects.filter(label=tag[0],context=tag[1]).exists():
                print(tag)
                try:
                    tagModel=Tag.objects.get(label=tag[0],context=tag[1])
                    print (userId,t.id,tagModel.id)
                    tt=OfTopic(topic_id=t.id,tag_id=tagModel.id,profile_id=userId)
                    tt.save()
                    print(tt)
                    #tagModel.topic_tagged()
                except:
                    return HttpResponse(("Tag Linking Error:",tag[0]," ",tag[1]))
            else :
                try:
                    tagModel=Tag(label=tag[0],context=tag[1])
                    tagModel.save()
                except:
                    return HttpResponse(("Tag creation error: ",tag[0]," ",tag[1]))
                try:
                    OfTopic(profile_id=userId, topic=t.id,tag=tagModel.id).save()
                    #tagModel.topic_tagged()
                except:
                    return HttpResponse(("Tag creation error: ",tag[0]," ",tag[1]))
        return HttpResponse("Topic created succesfully")
    else:
        return HttpResponse("Wrong Request")
# Example Get Request to searchTopic
#
# search: Donald
#
# TODO: Add more semantic search, check parameters
def searchTopic(request):
    if request.method== 'GET':
        searchParam=request.GET.get("search")
        topics= Name.objects.filter(name=searchParam)
        return HttpResponse(serializers.serialize('json',topics),content_type='json')
    else:
        return HttpResponse("Wrong Request")
# Example Post Request to addTopicName
#
# topicId:25 TopicName= President
def addTopicName(request):
    if request.method== 'POST':
        id=request.POST.get("topicId")
        name = request.POST.get("topicName")
        try:
            tn =Name(topic=id,name=name)
            tn.save()
        except:
            return HttpResponse(("TopicName creation error: ",name))
        return HttpResponse("Topic created succesfully")
    else:
        return HttpResponse("Wrong Request")

# TODO: Learn is this necessary
def deleteTopic(request):
    return
# Example Post Request to rateTopicName
#
# TopicNameId=15, upvote=1
# TODO: Ask how voting is done in models.

def rateTopicName(request):

    return
