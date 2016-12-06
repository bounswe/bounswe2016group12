from django.db.models import Model, IntegerField, DateTimeField, ForeignKey, CharField, BooleanField, NullBooleanField, CASCADE, SET_NULL
from django.db.models.signals import post_init, pre_delete
from django.contrib.auth.models import User
from django.utils import timezone
import datetime

from .profile import Profile
from .topic import Topic

### relation.Relation

class Relation(Model):
    topic_a = ForeignKey(Topic, on_delete=CASCADE, related_name='relations_a')
    topic_b = ForeignKey(Topic, on_delete=CASCADE, related_name='relations_b')
    isBidirectional=BooleanField(default=False)
    label = CharField(max_length=20)
    vote_count = IntegerField(default=0)
    def __unicode__(self):
        return str(self.pk)

    def __str__(self):
        return str(self.pk)

### relation.LabelVoter

class RelationVoter(Model):
    profile = ForeignKey(Profile, on_delete=CASCADE)
    relation = ForeignKey(Relation, on_delete=CASCADE, related_name='voters')
    active = BooleanField(default=False)
    upvoted= BooleanField(default=False)
    downvoted = BooleanField(default=False)
    def __unicode__(self):
        return str(self.pk)

    def __str__(self):
        return str(self.pk)

    def toggle(self,direction):
        relation=Relation.objects.get(id=self.relation_id)
        if self.active and self.upvoted and direction =='upvote' :
            relation.vote_count-= 1
            self.upvoted=False
            self.active = False
        elif self.active and self.downvoted and direction =='upvote' :
            relation.vote_count+= 2
            self.upvoted=True
            self.downvoted=False

        elif self.active and self.upvoted and direction =='downvote' :
            relation.vote_count-= 2
            self.upvoted=False
            self.downvoted = True

        elif self.active and self.downvoted and direction =='downvote' :
            relation.vote_count+= 1
            self.downvoted=False
            self.active=False
        elif  not self.active and direction=='upvote':
            relation.vote_count+= 1
            self.upvoted=True
            self.active = True
        elif not self.active and direction == 'downvote':
            relation.vote_count-= 1
            self.downvoted = True
            self.active = True
        relation.save()
        self.save()

    class Meta:
        unique_together = ('profile', 'relation')
