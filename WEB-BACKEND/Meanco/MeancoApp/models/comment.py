"""@package Models
View documentation

"""
from django.db.models import Model, IntegerField, DateTimeField, ForeignKey, TextField, BooleanField, CASCADE
from django.db.models.signals import post_init, pre_delete
from django.contrib.auth.models import User
from django.utils import timezone
import datetime

from .profile import Profile
from .topic import Topic

### Comment Model
class Comment(Model):
    topic = ForeignKey(Topic, on_delete=CASCADE, related_name='comments')
    profile = ForeignKey(Profile, on_delete=CASCADE, related_name='comments') # LATER: make it get set to default = AnonymousUser
    vote_count = IntegerField(default=0)

    def __unicode__(self):
        return str(self.pk)

    def __str__(self):
        return str(self.pk)

    ##Get latest version of users comment
    def current(self):
        return self.versions.first()
    ##Get latest version of users comments content.
    def content(self):
        return self.current().content

    #Edits comment.
    def edit(self, newcontent):
        version = Version(
            comment=self,
            content=newcontent
        )
        version.save()

## Versions of Comment
#
class Version(Model):
    comment = ForeignKey(Comment, on_delete=CASCADE, related_name='versions')
    content = TextField()

    timestamp = DateTimeField(auto_now_add=True)

    def __unicode__(self):
        return str(self.pk)

    def __str__(self):
        return str(self.pk)

    class Meta:
        ordering = ['-timestamp']
        unique_together = (('timestamp', ), )

## Voters of comment
class Voter(Model):
    profile = ForeignKey(Profile, on_delete=CASCADE, related_name='commentvoters')
    comment = ForeignKey(Comment, on_delete=CASCADE, related_name='voters')
    active = BooleanField(default=False)
    upvoted = BooleanField(default=False)
    downvoted = BooleanField(default=False)

    def __unicode__(self):
        return str(self.pk)

    def __str__(self):
        return str(self.pk)

    ## All in one function for user to upvote,downvote.
    def toggle(self,direction):
        comment = Comment.objects.get(id=self.comment_id)

        if self.active and self.upvoted and direction =='upvote' :
            comment.vote_count -= 1
            self.upvoted=False
            self.active = False
        elif self.active and self.downvoted and direction =='upvote' :
            comment.vote_count+= 2
            self.upvoted=True
            self.downvoted=False

        elif self.active and self.upvoted and direction =='downvote' :
            comment.vote_count -= 2
            self.upvoted=False
            self.downvoted = True

        elif self.active and self.downvoted and direction =='downvote' :
            comment.vote_count += 1
            self.downvoted=False
            self.active=False
        elif  not self.active and direction=='upvote':
            comment.vote_count += 1
            self.upvoted=True
            self.active = True
        elif not self.active and direction == 'downvote':
            comment.vote_count-= 1
            self.downvoted = True
            self.active = True
        comment.save()
        self.save()

    class Meta:
        unique_together = (('profile', 'comment'))
