from django.shortcuts import render
from sys import platform as _platform

def get_page(request):
    if _platform=="win32":
        return render(request, 'MeancoApp\SearchList.html', {})
    else:
        return render(request, 'MeancoApp/SearchList.html' , {})