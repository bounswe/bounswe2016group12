from django.db.models import Model, IntegerField, ForeignKey, CharField, URLField, CASCADE, PROTECT, SET_NULL,DateTimeField, OneToOneField
from django.contrib.auth.models import User
from .topic import Topic
from django.utils import timezone
### profile.Profile

class Profile(Model):
    user = OneToOneField(User, on_delete=CASCADE)

    def __unicode__(self):
        return str(self.pk)

    def __str__(self):
        return str(self.pk)

#To find out users followed topics
class FollowedTopic(Model):
    profile =  ForeignKey(Profile, on_delete=CASCADE, related_name='followedTopics')
    topic = ForeignKey(Topic, on_delete=CASCADE, related_name='followers')
    timestamp_last = DateTimeField(auto_now=True)
    def __unicode__(self):
        return str(self.pk)
    def __str__(self):
        return str(self.pk)

#To find out users viewed topics
class ViewedTopic(Model):
    profile =  ForeignKey(Profile, on_delete=CASCADE, related_name='viewedTopics')
    topic = ForeignKey(Topic, on_delete=CASCADE, related_name='viewers')
    visited_last = DateTimeField(default=timezone.now())
    def visited(self):
        self.visited_last = timezone.now()
        self.save()
    def __unicode__(self):
        return str(self.pk)
    def __str__(self):
        return str(self.pk)

#To find out users Commented topics.
class CommentedTopic(Model):
    profile =  ForeignKey(Profile, on_delete=CASCADE, related_name='commentedTopics')
    topic = ForeignKey(Topic, on_delete=CASCADE, related_name='commenters')
    commented_last = DateTimeField(default=timezone.now())
    def visited(self):
        self.commented_last = timezone.now()
        self.save()
    def __unicode__(self):
        return str(self.pk)
    def __str__(self):
        return str(self.pk)