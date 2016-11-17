from django.shortcuts import render
from sys import platform as _platform

from MeancoApp.models import Topic, Comment, OfTopic

def get_page(request, id):

    userId = id
    topic = Topic.objects.filter(id=userId).first()
    topicId = topic.id
    comments = Comment.objects.filter(topic=topicId)
    oftopic = OfTopic.objects.filter(topic=topicId)

    if _platform=="win32":
        return render(request, 'MeancoApp\CommentList.html', {})
    else:
        return render(request, 'MeancoApp/CommentList.html', {'topic':topic, 'comments':comments, 'oftopic':oftopic})