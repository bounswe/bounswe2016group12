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

    def label(self):

        first=Label.objects.filter(relation=self.id).first()
        return first

    class Meta:
        unique_together = ('topic_a', 'topic_b')

    def __unicode__(self):
        return str(self.pk)

    def __str__(self):
        return str(self.pk)


### relation.Label

class Label(Model):
    relation = ForeignKey(Relation, on_delete=CASCADE, related_name='labels')
    #direction = NullBooleanField(default=None)
    text = CharField(max_length=20, default='related')

    profile = ForeignKey(Profile, on_delete=SET_NULL, null=True, related_name='relations')
    vote_count = IntegerField(default=0)
    #boost_count = IntegerField(default=0)
    #boost_timestamp = DateTimeField()

    def __unicode__(self):
        return str(self.pk)

    def __str__(self):
        return str(self.pk)

    # def toggle_vote(self, votecaster):
    #     voters = self.voters.all()
    #
    #     for voter in voters:
    #         if voter.profile == votecaster:
    #             voter.toggle()
    #             return
    #
    #     voter = LabelVoter(
    #         profile=votecaster,
    #         relation_label=self
    #     )
    #     voter.save()
    #
    # def boost(self, amount=1):
    #     self.boost_count += amount
    #     self.boost_timestamp = timezone.now
    #     self.save()

    # def prune_boost(self, fresh_hours=0, fresh_minutes=30, fresh_seconds=0):
    #     if self.boost_count.value > 0:
    #         fresh_duration = datetime.timedelta(hours=fresh_hours, minutes=fresh_minutes, seconds=fresh_seconds)
    #         boost_duration = timezone.now - self.boost_timestamp.value
    #
    #         boost_cut = boost_duration // fresh_duration
    #         if boost_cut:
    #             self.boost_count.value //= 2 ** boost_cut
    #             if self.boost_count.value:
    #                 self.boost_timestamp.value += fresh_duration * boost_cut
    #
    #             self.save()

    class Meta:
        unique_together = ('relation', 'text')

### relation.LabelVoter

class LabelVoter(Model):
    profile = ForeignKey(Profile, on_delete=CASCADE)
    label = ForeignKey(Label, on_delete=CASCADE, related_name='voters')
    active = BooleanField(default=False)

    def __unicode__(self):
        return str(self.pk)

    def __str__(self):
        return str(self.pk)

    def toggle(self):
        if self.active:
            self.label.vote_count.value -= 1
            self.active = False
        else:
            self.label.vote_count.value += 1
            self.active = True
        
        self.save()

    class Meta:
        unique_together = ('profile', 'label')

# def label_voter_post_init(**kwargs):
#     voter = kwargs.get('instance')
#     voter.toggle()
#
#     voter.label.boost()
#
# def label_voter_pre_delete(**kwargs):
#     voter = kwargs.get('instance')
#
#     if voter.active:
#         voter.toggle()
#
# post_init.connect(label_voter_post_init, LabelVoter)
# pre_delete.connect(label_voter_pre_delete, LabelVoter)