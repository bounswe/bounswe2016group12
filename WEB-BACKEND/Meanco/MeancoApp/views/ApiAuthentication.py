from django.contrib.auth import login, authenticate
from django.http import HttpResponse, JsonResponse
from django.views.decorators.csrf import csrf_exempt
import json
from .forms import UserCreateForm
from MeancoApp.models import Profile
@csrf_exempt
def register(request):
    if request.method == "POST":
        form = UserCreateForm(request.POST)
        if form.is_valid():
            try:
                form.save(True)
                new_user = authenticate(username=form.cleaned_data['username'],
                                        password=form.cleaned_data['password1'],
                                        )
                profile=Profile(user_id=new_user.id)
                profile.save()
                return HttpResponse(json.dumps({
                    "UserId": new_user.id}),
                    status=200,
                    content_type="application/json")
            except:
                return HttpResponse("User Creation Error")
        return JsonResponse(form.errors)
    return HttpResponse("Wrong Request!")