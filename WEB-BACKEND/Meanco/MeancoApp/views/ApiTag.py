from MeancoApp.models import *
from django.http import request
from django.http import HttpResponse
from django.views.decorators.csrf import csrf_exempt

#
# topicId : 1
# label : "asd"
# context : "dsa"
# profileId : 1
#
#
@csrf_exempt
def addTag(request):
    topicId=request.POST.get("topicId")
    label = request.POST.get("label")
    context = request.POST.get("context")
    profileId=request.user.id
    if Tag.objects.filter(label=label, context=context).exists():
        try:
            tagModel = Tag.objects.get(label=label, context=context)
            print("asd")
            if OfTopic.objects.filter(topic_id=topicId, tag_id=tagModel.id).exists():
                return HttpResponse("Already Tagged")
            else:
                tt = OfTopic(topic_id=topicId, tag_id=tagModel.id, profile_id=profileId)
                tt.save()

            #tagModel.topic_tagged()
        except:
            return HttpResponse("Tag Linking Error:")
    else:
        try:
            tagModel = Tag(label=label, context=context)
            tagModel.save()
        except:
            return HttpResponse("Tag creation error")
        try:
            tt = OfTopic(topic_id=topicId, tag_id=tagModel.id, profile_id=profileId)
            tt.save()
            #tagModel.topic_tagged()
        except:
            return HttpResponse("Tag Linking error")
    return HttpResponse("Tag Added")
