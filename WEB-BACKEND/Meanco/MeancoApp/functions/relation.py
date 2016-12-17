from MeancoApp.models import *
from django.db.models import Q

def getRelations(count):
    topicIdQuery=list(Topic.objects.all().values('id'))
    topicIds=list()
    for i in topicIdQuery:
        topicIds.append(i['id'])
    topicIds.sort()
    relations=list()

    for firstId in range(0,len(topicIdQuery)+1):
        for secondId in range(firstId+1,len(topicIdQuery)+1):
            relation = list(Relation.objects.filter(
                Q(topic_a_id=firstId, topic_b_id=secondId) | Q(topic_a_id=secondId, topic_b_id=firstId,
                                                              isBidirectional=False)).order_by("-vote_count"))
            #Make it possible to set more than 1 relation
            relationCount=count;
            if relationCount>len(relation):
                relationCount=len(relation)
            for i in range(relationCount):
                relations.append(relation[i])
    return relations


