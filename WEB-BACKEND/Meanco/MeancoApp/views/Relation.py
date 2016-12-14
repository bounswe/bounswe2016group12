from django.shortcuts import render
from sys import platform as _platform
from MeancoApp.models import *
from django.db.models import Q

def get_page(request,id):
    print(id)
    Userid = request.user.id
    relation=Relation.objects.get(id=id)
    topic_a=relation.topic_a
    topic_b=relation.topic_b
    relations= Relation.objects.filter(Q(topic_a_id=topic_a.id) | Q(topic_a_id=topic_b.id)).order_by("-vote_count")
    if _platform == "win32":
        return render(request, 'MeancoApp\RelationView.html' , {'relations': relations, 'id': id,'topic_a':topic_a,'topic_b':topic_b})
    else:
        return render(request, 'MeancoApp/RelationView.html', {'relations': relations, 'id': Userid,'topic_a':topic_a,'topic_b':topic_b})