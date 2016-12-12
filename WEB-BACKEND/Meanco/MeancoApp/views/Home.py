from django.shortcuts import render
from sys import platform as _platform

from MeancoApp.models import Topic
from .forms import UserCreateForm

def get_page(request):
    topics = Topic.objects.all()
    id = request.user.id

    if _platform == "win32":
        return render(request, 'MeancoApp\TopicMap.html' , {'topics': topics, 'id': id})
    else:
        return render(request, 'MeancoApp/TopicMap.html', {'topics': topics, 'id': id})

def get_signup_page(request):
    form = UserCreateForm()
    return render(request, 'signup.html', {'form': form})