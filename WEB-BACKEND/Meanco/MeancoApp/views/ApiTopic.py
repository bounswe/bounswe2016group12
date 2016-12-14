from ..models import *
from django.http import request
from django.http import HttpResponse
from django.views.decorators.csrf import csrf_exempt
import json
import django.core.serializers
from django.conf.urls import  url
from rest_framework import generics
from MeancoApp.serializers import *
# Example Post Request to addTopic
#
# topicName="Donald Trump"
# tag=Politician
# description= Occupation
# URL: www.wikimedia.com/politician
# TODO:Add better control
@csrf_exempt
def addTopic(request):
    if request.method== 'POST':
        topicName=request.POST.get('topicName')
        tag = request.POST.get('tag')
        description =request.POST.get('description')
        URL = request.POST.get('URL')
        try:
            t=Topic(label=topicName)
            t.save()
        except:
            return HttpResponse("Topic Couldn't be created:", status=400)
        if Tag.objects.filter(URL = URL).exists():
            try:
                tagModel=Tag.objects.get(URL = URL)
                if OfTopic.objects.filter(topic_id=t.id,tag_id=tagModel.id).exists():
                    print("Weird Stuff")
                else:
                    tt=OfTopic(topic_id=t.id,tag_id=tagModel.id)
                    tt.save()
                #tagModel.topic_tagged()
            except:
                return HttpResponse("Tag Linking Error:", status=400)
        else :
            try:
                tagModel=Tag(label=tag,description=description,URL=URL)
                tagModel.save()
            except:
                return HttpResponse("Tag creation error", status=400)
            try:
                tt = OfTopic(topic_id=t.id, tag_id=tagModel.id)
                tt.save()
                #tagModel.topic_tagged()
            except:
                return HttpResponse("Tag Linking error", status=400)
        return HttpResponse(json.dumps({
            "topicId": t.id}),
            status=200,
            content_type="application/json")
    else:
        return HttpResponse("Wrong Request", status=400)
# Example Get Request to searchTopic
#
# search: Donald
#
# TODO: Add more semantic search, check parameters
def searchTopic(request):
    if request.method== 'GET':
        searchParam=request.GET.get("search")
        topics= Topic.objects.filter(label__startswith=searchParam)

        if(topics.count()!=1):
            return HttpResponse(django.core.serializers.serialize('json', topics), content_type='json')
        else:
            TopicTags=OfTopic.objects.filter(topic_id=topics.values('id'))
            tagsOfTopic=list(TopicTags.values_list('tag_id'))
            tagsOfTopic=list(topicTag[0] for topicTag in tagsOfTopic)
            topicsWithCountOfTags={}
            for t in OfTopic.objects.all():
                if not t.topic_id in topicsWithCountOfTags:
                    topicsWithCountOfTags[t.topic_id]={
                        'count':0
                    }
                print(t.tag_id)
                if t.tag_id in tagsOfTopic:
                    print("here")
                    topicsWithCountOfTags[t.topic_id]['count']+=1
            print(topicsWithCountOfTags)
            topicsToRemove=list()
            for t in topicsWithCountOfTags.keys():
                if topicsWithCountOfTags[t]['count']==0:
                    topicsToRemove.append(t)
            for t in topicsToRemove:
                topicsWithCountOfTags.pop(t)
            topicData = list(Topic.objects.filter(id__in=topicsWithCountOfTags.keys()))
            print(topicData)
            topicData= sorted(topicData, key=lambda obj:topicsWithCountOfTags[obj.id]['count'],reverse=True)[0:3]
            print(topicData)
            return HttpResponse(django.core.serializers.serialize('json',topicData), content_type='json')
    else:
        return HttpResponse("Wrong Request", status=400)

# (Android)UserId:1
# TopicId=5
@csrf_exempt
def followTopic(request):
    if (request.method=="POST"):
        TopicId = request.POST.get('TopicId')

        if 'UserId' not in request.POST:
            UserId = request.user.id
        else:
            UserId = request.POST.get("UserId")
        profileId=Profile.objects.get(user_id=UserId)

        try:
            if(FollowedTopic.objects.filter(profile_id=profileId, topic_id=TopicId).exists()):
                ft=FollowedTopic.objects.get(profile_id=profileId, topic_id=TopicId)
                ft.delete()
            else:
                ft = FollowedTopic(profile_id=profileId ,topic_id=TopicId)
                ft.save()
        except:
            return HttpResponse("Follow Topic Error", status=400)
        return HttpResponse("Success", status=200)
    else:
        return HttpResponse("Wrong Request", status=400)
# search= Donald

def topicListerGet(request):
    searchParam = request.GET.get("search")
    topics = Topic.objects.filter(label__startswith=searchParam)
    return HttpResponse(django.core.serializers.serialize('json', topics), content_type='json')

class TopicList(generics.ListCreateAPIView):
    queryset = Topic.objects.all()
    serializer_class= TopicListSerializer

class TopicDetail(generics.RetrieveUpdateDestroyAPIView):

    queryset = Topic.objects.all()
    serializer_class= TopicSerializer

