from django.db.models import Model, IntegerField, DateTimeField, ForeignKey, TextField, BooleanField, CASCADE
from django.db.models.signals import post_init, pre_delete
from django.contrib.auth.models import User
from django.utils import timezone
import datetime

from .profile import Profile
from .topic import Topic

### comment.Comment

class Comment(Model):
    topic = ForeignKey(Topic, on_delete=CASCADE, related_name='comments')
    profile = ForeignKey(Profile, on_delete=CASCADE, related_name='comments') # LATER: make it get set to default = AnonymousUser
    vote_count = IntegerField(default=0)
    boost_count = IntegerField(default=0)
    boost_timestamp = DateTimeField()

    def current(self):
        return self.versions.first()

    def content(self):
        return self.current().content

    def edit(self, newcontent):
        version = Version(
            comment=self,
            content=newcontent
        )
        version.save()

    def toggle_vote(self, votecaster):
        voters = self.voters.all()

        for voter in voters:
            if voter.profile == votecaster:
                voter.toggle()
                return

        voter = Voter(
            profile=votecaster,
            comment=self,
            version=self.current()
        )
        voter.save()

    def boost(self, amount=1):
        self.boost_count += amount
        self.boost_timestamp = timezone.now
        self.save()

    def prune_boost(self, fresh_hours=0, fresh_minutes=10, fresh_seconds=0):
        if self.boost_count.value > 0:
            fresh_duration = datetime.timedelta(hours=fresh_hours, minutes=fresh_minutes, seconds=fresh_seconds)
            boost_duration = timezone.now - self.boost_timestamp.value

            boost_cut = boost_duration // fresh_duration
            if boost_cut:
                self.boost_count.value //= 2 ** boost_cut
                if self.boost_count.value:
                    self.boost_timestamp.value += fresh_duration * boost_cut

                self.save()

def comment_post_init(**kwargs):
    comment = kwargs.get('instance')
    comment.topic.commented()

    comment.topic.name().vote(comment.profile, skipifhasvoted = True)

def comment_pre_delete(**kwargs):
    comment = kwargs.get('instance')
    comment.topic.comment_removed()

post_init.connect(comment_post_init, Comment)
pre_delete.connect(comment_pre_delete, Comment)

### comment.Version

class Version(Model):
    comment = ForeignKey(Comment, on_delete=CASCADE, related_name='versions')
    content = TextField()

    timestamp = DateTimeField(auto_now_add=True)

    class Meta:
        ordering = ['-timestamp']
        unique_together = (('timestamp', ), )

### comment.Voter

class Voter(Model):
    profile = ForeignKey(Profile, on_delete=CASCADE, related_name='commentvoters')
    comment = ForeignKey(Comment, on_delete=CASCADE, related_name='voters')
    version = ForeignKey(Version, on_delete=CASCADE, related_name='voters')
    active = BooleanField(default=False)

    def toggle(self):
        if self.active:
            self.comment.vote_count.value -= 1
            self.active = False
        else:
            self.comment.vote_count.value += 1
            self.active = True
        
        self.save()

    class Meta:
        unique_together = (('profile', 'comment'), ('profile', 'version'))

def voter_post_init(**kwargs):
    voter = kwargs.get('instance')
    voter.toggle()

    voter.comment.boost()

def voter_pre_delete(**kwargs):
    voter = kwargs.get('instance')
    
    if voter.active:
        voter.toggle()

post_init.connect(voter_post_init, Voter)
pre_delete.connect(voter_pre_delete, Voter)