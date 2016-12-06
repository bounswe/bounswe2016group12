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
# userId= 15
# topicName="Donald Trump"
# tag=Politician
# description= Occupation
# URL: www.wikimedia.com/politician
# TODO:Add better control
@csrf_exempt
def addTopic(request):
    if request.method== 'POST':
        userId =request.POST.get('userId')
        topicName=request.POST.get('topicName')
        tag = request.POST.get('tag')
        description =request.POST.get('description')
        URL = request.POST.get('URL')
        try:
            t=Topic(label=topicName)
            t.save()
        except:
            return HttpResponse("Topic Couldn't be created:",status=400)
        if Tag.objects.filter(URL = URL).exists():
            try:
                tagModel=Tag.objects.get(URL = URL)
                if OfTopic.objects.filter(topic_id=t.id,tag_id=tagModel.id).exists():
                    print("Weird Stuff")
                else:
                    tt=OfTopic(topic_id=t.id,tag_id=tagModel.id,profile_id=userId)
                    tt.save()
                #tagModel.topic_tagged()
            except:
                return HttpResponse("Tag Linking Error:",status=400)
        else :
            try:
                tagModel=Tag(label=tag,description=description,URL=URL)
                tagModel.save()
            except:
                return HttpResponse("Tag creation error",status=400)
            try:
                tt = OfTopic(topic_id=t.id, tag_id=tagModel.id, profile_id=userId)
                tt.save()
                #tagModel.topic_tagged()
            except:
                return HttpResponse("Tag Linking error",status=400)
        return HttpResponse(json.dumps({
            "Topic": t}),
            status=200,
            content_type="application/json")
    else:
        return HttpResponse("Wrong Request",status=400)
# Example Get Request to searchTopic
#
# search: Donald
#
# TODO: Add more semantic search, check parameters
def searchTopic(request):
    if request.method== 'GET':
        searchParam=request.GET.get("search")
        topics= Topic.objects.filter(label__startswith=searchParam)

        if(topics.count()>1):
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
            topicData = list(Topic.objects.filter(id__in=topicsWithCountOfTags.keys()))
            print(topicData)
            topicData= sorted(topicData, key=lambda obj:topicsWithCountOfTags[obj.id]['count'],reverse=True)[0:10]
            print(topicData)
            return HttpResponse(django.core.serializers.serialize('json',topicData), content_type='json')
    else:
        return HttpResponse("Wrong Request")

class TopicList(generics.ListCreateAPIView):
    queryset = Topic.objects.all()
    serializer_class= TopicListSerializer

class TopicDetail(generics.RetrieveUpdateDestroyAPIView):
    queryset = Topic.objects.all()
    serializer_class= TopicSerializer