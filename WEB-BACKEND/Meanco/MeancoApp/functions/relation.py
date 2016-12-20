from MeancoApp.models import *
from django.db.models import Q

def getRelations(count):
    topicIdQuery=list(Topic.objects.all().values('id'))
    topicIds=list()
    for i in topicIdQuery:
        topicIds.append(i['id'])
    topicIds.sort()
    relations=list()

    for firstId in range(0,len(topicIdQuery)):
        for secondId in range(firstId+1,len(topicIdQuery)):
            relation = list(Relation.objects.filter(
                Q(topic_a_id=topicIds[firstId], topic_b_id=topicIds[secondId]) | Q(topic_a_id=topicIds[secondId], topic_b_id=topicIds[firstId],
                                                              isBidirectional=False)).order_by("-vote_count"))
            relationCount=count;
            if relationCount>len(relation):
                relationCount=len(relation)
            for i in range(relationCount):
                relations.append(relation[i])
    return relations


