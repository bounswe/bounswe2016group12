from ..models import *
from django.http import request
from django.http import HttpResponse
from django.views.decorators.csrf import csrf_exempt
import json
from django.core import serializers
from django.conf.urls import  url
from rest_framework import generics
from MeancoApp.serializers import *
# Example Post Request to addTopic
#
# userId= 15
# topicName="Donald Trump"
# tag=Politician
# context= Occupation
# TODO:Add better control
@csrf_exempt
def addTopic(request):
    if request.method== 'POST':
        userId =request.POST.get('userId')
        topicName=request.POST.get('topicName')
        tag = request.POST.get('tag')
        context =request.POST.get('context')
        print(userId)
        try:
            t=Topic()
            t.save()
        except:
            return HttpResponse(("Topic Couldn't be created:"))
        try:
            tName = Name(topic_id=t.id,label=topicName)
            tName.save()
        except:
            return HttpResponse(("TopicName Creation Error: " ,topicName))
        if Tag.objects.filter(label=tag,context=context).exists():
            try:
                tagModel=Tag.objects.get(label=tag,context=context)
                if OfTopic.objects.filter(topic_id=t.id,tag_id=tagModel.id).exists():
                    print("Weird Stuff")
                else:
                    tt=OfTopic(topic_id=t.id,tag_id=tagModel.id,profile_id=userId)
                    tt.save()
                #tagModel.topic_tagged()
            except:
                return HttpResponse("Tag Linking Error:")
        else :
            try:
                tagModel=Tag(label=tag,context=context)
                tagModel.save()
            except:
                return HttpResponse("Tag creation error")
            try:
                tt = OfTopic(topic_id=t.id, tag_id=tagModel.id, profile_id=userId)
                tt.save()
                #tagModel.topic_tagged()
            except:
                return HttpResponse("Tag Linking error")
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
        topics= Name.objects.filter(label=searchParam)
        return HttpResponse(serializers.serialize('json',topics),content_type='json')
    else:
        return HttpResponse("Wrong Request")
# Example Post Request to addTopicName
#
# topicId:25 TopicName= President
@csrf_exempt
def addTopicName(request):
    if request.method== 'POST':
        id=request.POST.get("topicId")
        name = request.POST.get("topicName")
        try:
            tn = Name(topic_id=id,label=name)
            tn.save()
        except:
            return HttpResponse(("TopicName creation error: ",name))
        return HttpResponse("TopicName created succesfully")
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
class TopicList(generics.ListCreateAPIView):
    queryset = Topic.objects.all()
    serializer_class= TopicListSerializer

class TopicDetail(generics.RetrieveUpdateDestroyAPIView):
    queryset = Topic.objects.all()
    serializer_class= TopicSerializer