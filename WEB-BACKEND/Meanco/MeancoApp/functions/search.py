"""@package functions
View documentation

"""
import requests
import re
from  MeancoApp.models import *
## Gets reference from wikidata and create reference models.
def getRefOfTopic(topicName,TopicId):
    payload = {
        'action': 'wbsearchentities',
        'language': 'en',
        'search': topicName,
        'format': 'json',
    }
    r= requests.get('https://www.wikidata.org/w/api.php', params= payload)
    idList=list()
    for i in r.json()['search']:
        idList.append(i['id'])
    link="https://query.wikidata.org/bigdata/namespace/wdq/sparql"
    query='SELECT ?item ?itemLabel WHERE { {} '
    for i in idList:
        query+="UNION "
        query+= "{wd:"+i+" wdt:P279 ?item.}"
        query+="UNION"
        query+="{wd:"+i +" wdt:P31 ?item.}"
    query+=' SERVICE wikibase:label { bd:serviceParam wikibase:language "en" } } '
    r2=requests.get(link,params={'query':query,'format':'json' })
    for i in r2.json()['results']['bindings']:
        if 'item' in i :
            if(i['itemLabel']['value']!="Wikimedia disambiguation page"):
                q =re.search("(Q.+)",(i['item']['value']))
                tr =TopicRef(topic_id=TopicId,qId=q.group(0))
                tr.save()
## Finds reference from wikidata and create reference models.
def findRefTopics(search):
    payload = {
        'action': 'wbsearchentities',
        'language': 'en',
        'search': search,
        'format': 'json',
    }
    r= requests.get('https://www.wikidata.org/w/api.php', params= payload)
    idList=list()
    for i in r.json()['search']:
        idList.append(i['id'])
    link="https://query.wikidata.org/bigdata/namespace/wdq/sparql"
    query='SELECT ?item WHERE { {} '
    for i in idList:
        query+="UNION "
        query+= "{wd:"+i+" wdt:P279 ?item.}"
        query+="UNION"
        query+="{wd:"+i +" wdt:P31 ?item.}"
    query+=' SERVICE wikibase:label { bd:serviceParam wikibase:language "en" } } '
    r2=requests.get(link,params={'query':query,'format':'json' })
    topicDict = {}
    for i in r2.json()['results']['bindings']:
        if 'item' in i:
            q = re.search("(Q.+)", (i['item']['value']))
            if TopicRef.objects.filter(qId=q.group(0)).exists():
                tr = TopicRef.objects.filter(qId=q.group(0)).first()
                print(q.group(0))
                print(tr.topic_id)
                if not tr.topic_id in topicDict:
                    topicDict[tr.topic_id] = 1
                else :
                    topicDict[tr.topic_id]+=1
    topicData = list(topicDict.keys())
    topicData = sorted(topicData, key=lambda obj: topicDict[obj], reverse=True)
    return topicData
## Find mutually tagged topics.
def findMutuallyTaggedTopics(search):
    searchParam = search.capitalize()
    topic = Topic.objects.filter(label__startswith=searchParam)
    if(not topic.exists()):
        return list()
    TopicTags = OfTopic.objects.filter(topic_id=topic.first().id)
    tagsOfTopic = list(TopicTags.values_list('tag_id'))
    tagsOfTopic = list(topicTag[0] for topicTag in tagsOfTopic)
    topicsWithCountOfTags = {}
    for t in OfTopic.objects.all():
        if not t.topic_id in topicsWithCountOfTags:
            topicsWithCountOfTags[t.topic_id] = {
                'count': 0
            }
        if t.tag_id in tagsOfTopic:
            topicsWithCountOfTags[t.topic_id]['count'] += 1
    topicsToRemove = list()
    for t in topicsWithCountOfTags.keys():
        if topicsWithCountOfTags[t]['count'] == 0:
            topicsToRemove.append(t)
    for t in topicsToRemove:
        topicsWithCountOfTags.pop(t)
    topicData = list(topicsWithCountOfTags.keys())
    topicData = sorted(topicData, key=lambda obj: topicsWithCountOfTags[obj]['count'], reverse=True)
    return topicData
## Finds string matched topics.
def findStringMatchedTopics(search):
    searchParam = search.capitalize()
    topicsData = Topic.objects.filter(label__startswith=searchParam)
    topics=list()
    for i in topicsData:
        topics.append(i.id)
    return topics