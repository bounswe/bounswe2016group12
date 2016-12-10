from django.shortcuts import render
from sys import platform as _platform
from MeancoApp.models import Topic, Comment, OfTopic, Relation, ViewedTopic

def get_page(request, id):

    topicId = id
    if request.user.is_authenticated:
        if(ViewedTopic.objects.filter(user_id=request.user.id,topic_id=topicId).exists()):
            vt=ViewedTopic.objects.get(user_id=request.user.id,topic_id=topicId)
            vt.visited()
            vt.save()
        else:
            vt=ViewedTopic(user_id=request.user.id,topic_id=topicId)
            vt.save()

    topic = Topic.objects.filter(id=topicId ).first()
    comments = Comment.objects.filter(topic=topicId)
    oftopic = OfTopic.objects.filter(topic=topicId)
    relationsFrom = Relation.objects.filter(topic_a=topicId)
    relationsTo = Relation.objects.filter(topic_b=topicId)

    if _platform=="win32":
        return render(request, 'MeancoApp\CommentList.html', {'topic':topic, 'relationsFrom':relationsFrom, 'relationsTo':relationsTo, 'comments':comments, 'oftopic':oftopic})
    else:
        return render(request, 'MeancoApp/CommentList.html', {'topic':topic, 'relationsFrom':relationsFrom, 'relationsTo':relationsTo, 'comments':comments, 'oftopic':oftopic})