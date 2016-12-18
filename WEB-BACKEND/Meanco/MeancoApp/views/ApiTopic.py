from ..models import *
from django.http import request
from django.http import HttpResponse
from django.views.decorators.csrf import csrf_exempt
import json
import django.core.serializers
from django.conf.urls import  url
from rest_framework import generics
from MeancoApp.serializers import *
from MeancoApp.functions.search import *
# Adds topic and add one tag to it. Also finds references to topic from wikidata.
# Example API request:
# topicName="Donald Trump"
# tag=Politician
# description= Occupation
# URL: www.wikimedia.com/politician
# TODO: Add threading for finding references from Wikidita
@csrf_exempt
def addTopic(request):
    if request.method== 'POST':
        topicName=request.POST.get('topicName').capitalize()
        tag = request.POST.get('tag')
        description =request.POST.get('description')
        URL = request.POST.get('URL')
        # creates topic
        try:
            t=Topic(label=topicName)
            t.save()
        except:
            return HttpResponse("Topic Couldn't be created:", status=400)
        # find references of topic
        try:
            getRefOfTopic(topicName,t.id)
        except:
            print("refError")
        # create and link tag to topic.
        if Tag.objects.filter(URL = URL).exists():
            try:
                tagModel=Tag.objects.get(URL = URL)
                if OfTopic.objects.filter(topic_id=t.id,tag_id=tagModel.id).exists():
                    print("Weird Stuff")
                else:
                    tt=OfTopic(topic_id=t.id,tag_id=tagModel.id)
                    tt.save()
                    tagModel.topic_tagged()
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
                tagModel.topic_tagged()
            except:
                return HttpResponse("Tag Linking error", status=400)
        return HttpResponse(json.dumps({
            "topicId": t.id}),
            status=200,
            content_type="application/json")
    else:
        return HttpResponse("Wrong Request", status=400)
# search topic by :
# 1) string match.
# 2) Mutual tags.
# 3) wikidata references.
# Example API request:
# search: Donald
#
# TODO: Add more semantic search, check parameters
def searchTopic(request):
    if request.method== 'GET':
        searchParam=request.GET.get("search")
        try:
            topics1 = findStringMatchedTopics(searchParam)
            topics2= findMutuallyTaggedTopics(searchParam)
            topics3 =findRefTopics(searchParam)
            topics=topics1
            for i in topics2:
                if i not in topics:
                    topics.append(i)
            for i in topics3:
                if i not in topics:
                    topics.append(i)
            topicData=Topic.objects.filter(id__in=topics).order_by("-view_count")
            return HttpResponse(django.core.serializers.serialize('json',topicData), content_type='json')
        except:
            return HttpResponse("Error",status=400)
    else:
        return HttpResponse("Wrong Request", status=400)
# Follows topic for given user.
# Example API request:
# (Android)UserId:1
# TopicId=5
@csrf_exempt
def followTopic(request):
    if (request.method=="POST"):
        TopicId = int(request.POST.get('TopicId'))

        if 'UserId' not in request.POST:
            UserId = request.user.id
        else:
            UserId = int(request.POST.get("UserId"))
        profileId=Profile.objects.get(user_id=UserId).id
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
# finds topics starting with given parameter. Used for autocomplete.
# Example API request:
# search= Donald
def topicListerGet(request):
    searchParam = request.GET.get("search").capitalize()
    topics = Topic.objects.filter(label__startswith=searchParam)
    return HttpResponse(django.core.serializers.serialize('json', topics), content_type='json')

# Gives users followed topics.
# Example API request:
# (Android)UserId:5
def getFollowedTopics(request):
    if (request.method=="GET"):
        if 'UserId' not in request.GET:
            UserId = request.user.id
        else:
            UserId = request.GET.get("UserId")
        if Profile.objects.filter(user_id=UserId).exists():
            ft = FollowedTopic.objects.filter(profile_id=Profile.objects.get(user_id=UserId).id).values_list("topic_id",flat=True).distinct()
            topics=list(Topic.objects.filter(id__in=ft))
            return HttpResponse(django.core.serializers.serialize('json',topics ), content_type='json',status=200)
        else:
            return HttpResponse("No user found",status=400)
# Gives users latest viewed topics.
# Example API request:
# (Android)UserId:5
# TopicCount:10
def getViewedTopics(request):
    TopicCount=int(request.GET.get("TopicCount"))
    if (request.method=="GET"):
        if 'UserId' not in request.GET:
            UserId = request.user.id
        else:
            UserId = request.GET.get("UserId")
        if Profile.objects.filter(user_id=UserId).exists():
            vt = ViewedTopic.objects.filter(profile_id=Profile.objects.get(user_id=UserId).id).values_list("topic_id",flat=True).distinct()
            topics=list(Topic.objects.filter(id__in=vt))
            topics = sorted(topics, key=lambda topic: ViewedTopic.objects.get(profile_id=Profile.objects.get(user_id=UserId).id, topic_id=topic.id).visited_last, reverse=True)[:TopicCount]
            return HttpResponse(django.core.serializers.serialize('json',topics ), content_type='json',status=200)
        else:
            return HttpResponse("No user found",status=400)
# Gives users latest commented topics.
# Example API request:
# (Android)UserId:5
# TopicCount:10
def getCommentedTopics(request):
    TopicCount=int(request.GET.get("TopicCount"))
    if (request.method=="GET"):
        if 'UserId' not in request.GET:
            UserId = request.user.id
        else:
            UserId = request.GET.get("UserId")
        if Profile.objects.filter(user_id=UserId).exists():
            ct = CommentedTopic.objects.filter(profile_id=Profile.objects.get(user_id=UserId).id).values_list("topic_id",flat=True).distinct()
            topics=list(Topic.objects.filter(id__in=ct))
            print(topics)
            topics= sorted(topics,key=lambda topic:CommentedTopic.objects.get(profile_id=Profile.objects.get(user_id=UserId).id,topic_id=topic.id).commented_last ,reverse=True)
            return HttpResponse(django.core.serializers.serialize('json',topics[:TopicCount] ), content_type='json',status=200)
        else:
            return HttpResponse("No user found",status=400)
# Rest Api for all topic list.
class TopicList(generics.ListCreateAPIView):
    queryset = Topic.objects.all()
    serializer_class= TopicListSerializer

# Rest Api for single topic
class TopicDetail(generics.RetrieveUpdateDestroyAPIView):


    queryset = Topic.objects.all()
    serializer_class= TopicSerializer

