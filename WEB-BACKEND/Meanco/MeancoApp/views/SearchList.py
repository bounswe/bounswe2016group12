from django.shortcuts import render
from sys import platform as _platform
from ..models import Topic, Tag

def get_page(request):

    param = request.GET.get('param')
    action = request.GET.get('action')
    if action == "topic":
        results = Topic.objects.filter(label__startswith=param).order_by('-comment_count', '-timestamp_last', '-view_count')
    else:   # Tag
        results = Tag.objects.filter(label__startswith=param).order_by('-view_count', '-topic_count')

    if _platform=="win32":
        return render(request, 'MeancoApp\SearchList.html', {'results': results, 'action': action})
    else:
        return render(request, 'MeancoApp/SearchList.html' , {'results': results, 'action': action})