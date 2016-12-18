from django.shortcuts import render
from sys import platform as _platform
from MeancoApp.models import *

def get_page(request):

    id = request.user.id
    profileId = Profile.objects.get(user_id=id).id
    followed = FollowedTopic.objects.filter(profile_id=profileId)
    commented = CommentedTopic.objects.filter(profile_id=profileId)
    viewed = ViewedTopic.objects.filter(profile_id=profileId)

    if _platform == "win32":
        return render(request, 'MeancoApp\Profile.html', {'followed': followed, 'commented':commented, 'viewed':viewed})
    else:
        return render(request, 'MeancoApp/Profile.html', {'followed': followed, 'commented':commented, 'viewed':viewed})
