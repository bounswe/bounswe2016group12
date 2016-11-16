from django.shortcuts import render
from sys import platform as _platform
def get_page(request):
    return render(request, 'MeancoApp\TopicMap.html' , {})
