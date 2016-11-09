from django.shortcuts import render

def get_page(request):
    return render(request, 'MeancoApp/CommentList.html' , {})