"""@package API
API documentation

"""
from MeancoApp.models import *
from django.http import request
from django.http import HttpResponse
from django.views.decorators.csrf import csrf_exempt
import django.core.serializers
import json


## Add tags to a topic. API call.
#
# Example API request:
# topicId : 1
# label : "asd"
# description : "dsa"
# URL : "www.asd.com"

@csrf_exempt
def addTag(request):
    topicId=request.POST.get("topicId")
    label = request.POST.get("label")
    description = request.POST.get("description")
    URL = request.POST.get("URL")
    if Tag.objects.filter(URL=URL).exists():
        try:
            tagModel = Tag.objects.get(label=label, description=description,URL=URL)
            print("asd")
            if OfTopic.objects.filter(topic_id=topicId, tag_id=tagModel.id).exists():
                return HttpResponse("Already Tagged",status=200)
            else:
                tt = OfTopic(topic_id=topicId, tag_id=tagModel.id)
                tt.save()
                tagModel.topic_tagged()
        except:
            return HttpResponse("Tag Linking Error:", status=400)
    else:
        try:
            tagModel = Tag(label=label, description=description,URL=URL)
            tagModel.save()
        except:
            return HttpResponse("Tag creation error", status=400)
        try:
            tt = OfTopic(topic_id=topicId, tag_id=tagModel.id)
            tt.save()
            tagModel.topic_tagged()
        except:
            return HttpResponse("Tag Linking error", status=400)
    return HttpResponse(json.dumps({
            "tagId": tagModel.id}),
            status=200,
            content_type="application/json")

## Searches for a tag in db.
## Example API request:
## URL: www.asd.com
#
def searchTag(request):
    URL= request.GET.get('URL')
    print(URL)
    if (Tag.objects.filter(URL__icontains=URL).exists()):
        tag= Tag.objects.get(URL__icontains=URL)
        topics= Topic.objects.filter(tags=tag.id).order_by('view_count')
        print(topics)
        if (topics.count()<1):
            return HttpResponse("No topic tagged with that tag", status=400)
        return HttpResponse(django.core.serializers.serialize('json', topics), content_type='json',status=200)
    else:
        return HttpResponse("No topic tagged with that tag",status=400)
