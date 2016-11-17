from django.shortcuts import render
from sys import platform as _platform

from MeancoApp.models import Topic, Comment, OfTopic, Relation

def get_page(request, id):

    userId = id
    topic = Topic.objects.filter(id=userId).first()
    topicId = topic.id
    comments = Comment.objects.filter(topic=topicId)
    oftopic = OfTopic.objects.filter(topic=topicId)
    relationsFrom = Relation.objects.filter(topic_a=topicId)
    relationsTo = Relation.objects.filter(topic_b=topicId)

    if _platform=="win32":
        return render(request, 'MeancoApp\CommentList.html', {'topic':topic, 'relationsFrom':relationsFrom, 'relationsTo':relationsTo, 'comments':comments, 'oftopic':oftopic})
    else:
        return render(request, 'MeancoApp/CommentList.html', {'topic':topic, 'relationsFrom':relationsFrom, 'relationsTo':relationsTo, 'comments':comments, 'oftopic':oftopic})