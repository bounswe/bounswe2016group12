from django.shortcuts import render
from sys import platform as _platform
from MeancoApp.models import Topic, Comment, OfTopic, Relation, ViewedTopic,Profile

def get_page(request, id):

    topicId = id
    if request.user.is_authenticated:
        profileId=Profile.objects.get(user_id=request.user.id).id
        if(ViewedTopic.objects.filter(profile_id=profileId,topic_id=topicId).exists()):
            vt=ViewedTopic.objects.get(profile_id=profileId,topic_id=topicId)
            vt.visited()
            vt.save()
        else:
            vt=ViewedTopic(profile_id=profileId,topic_id=topicId)
            vt.save()

    topic = Topic.objects.get(id=topicId )
    topic.viewed()
    topic.save()
    comments = Comment.objects.filter(topic=topicId)
    oftopic = OfTopic.objects.filter(topic=topicId)
    relationsFrom = Relation.objects.filter(topic_a=topicId)
    relationsTo = Relation.objects.filter(topic_b=topicId)

    if _platform=="win32":
        return render(request, 'MeancoApp\CommentList.html', {'topic':topic, 'relationsFrom':relationsFrom, 'relationsTo':relationsTo, 'comments':comments, 'oftopic':oftopic})
    else:
        return render(request, 'MeancoApp/CommentList.html', {'topic':topic, 'relationsFrom':relationsFrom, 'relationsTo':relationsTo, 'comments':comments, 'oftopic':oftopic})