from MeancoApp.models import *
from django.http import request
from django.http import HttpResponse
from django.views.decorators.csrf import csrf_exempt

#
# profile : 1
# topic1 : 1
# topic2 : 2
# label : "asd"
#
#
@csrf_exempt
def addRelation(request):
    topicStartPoint=request.POST.get("topic1")
    topicEndPoint = request.POST.get("topic2")
    label = request.POST.get("label")
    profileId=request.POST.get("profile")
    try:
        if(Relation.objects.filter(topic_a_id=topicStartPoint,topic_b_id=topicEndPoint).exists()):
            relation=Relation.objects.get(topic_a_id=topicStartPoint,topic_b_id=topicEndPoint)
        else:
            relation = Relation(topic_a_id=topicStartPoint, topic_b_id=topicEndPoint)
            relation.save()
    except:
        return HttpResponse("Relation Couldn't be created")
    try:
        relationLabel= Label(relation_id=relation.id,text=label,profile_id=profileId)
        relationLabel.save()
    except:
        return HttpResponse("RelationLabel Couldn't be created")
    return HttpResponse("Relation created")

#
# relationId : 1
# profile : 1
# label : "dsa"
@csrf_exempt
def editRelation(request):
    relationId = request.POST.get("relationId")
    label = request.POST.get("label")
    profileId=request.POST.get("profile")
    try:
        relationLabel= Label(relation_id=relationId,text=label,profile_id=profileId)
        relationLabel.save()
    except:
        return HttpResponse("RelationLabel Couldn't be created")
    return HttpResponse("Relation created")

def deleteRelation():
    return

def rateRelation():
    return