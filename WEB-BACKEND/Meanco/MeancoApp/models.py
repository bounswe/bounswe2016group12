from django.db import models
from django.contrib.auth.models import User
from django.utils import timezone
from django.db.models.signals import post_init, pre_delete
import datetime

# Create your models here.

# This is an example from djangogirls tutorial
#
# class Post(models.Model):
#     author = models.ForeignKey('auth.User')
#     title = models.CharField(max_length=200)
#     text = models.TextField()
#     created_date = models.DateTimeField(
#             default=timezone.now)
#     published_date = models.DateTimeField(
#             blank=True, null=True)

#     def publish(self):
#         self.published_date = timezone.now()
#         self.save()

#     def __str__(self):
#         return self.title


#####
# PROFILE
#####

class Profile(models.Model):
    user = models.OneToOneField(User, on_delete = models.CASCADE)

#####
# TOPIC, Name, NameVoter, 
#####

class Topic(models.Model):
    comment_count = models.IntegerField(default = 0)
    view_count = models.IntegerField(default = 1)
    timestamp_last = models.DateTimeField(auto_now = True)
    timestamp_first = models.DateTimeField(auto_now_add = True)

    def name(self):
        return self.names.first()

    def viewed(self):
        self.view_count.value += 1
        self.save()

    def commented(self):
        self.comment_count.value += 1
        self.save()

    def comment_removed(self):
        self.comment_count.value -= 1
        self.save()

### TopicName

class TopicName(models.Model):
    topic = models.ForeignKey(Topic, on_delete = models.CASCADE, related_name = 'names')
    name = models.CharField(max_length = 40)
    vote_count = models.IntegerField()

    def vote(self, votecaster, skipifhasvoted = False):
        topicnamevoters = self.topic.topicnamevoter_set.all()

        for tnv in topicnamevoters:
            if tnv.profile == votecaster:
                if not skipifhasvoted:
                    tnv.topic_name.value = self
                    tnv.save()
                return

        tnv = TopicNameVoter(
            profile = votecaster,
            topic = self.topic,
            topic_name = self
        )
        tc.save()

    class Meta:
        ordering = ['-vote_count']
        unique_together = ('topic', 'name')

class TopicNameVoter(models.Model):
    def get_current_name(self):
        return self.topic.name()
    
    profile = models.ForeignKey(Profile, on_delete = models.CASCADE)
    topic = models.ForeignKey(Topic, on_delete = models.CASCADE)
    topic_name = models.ForeignKey(TopicName, on_delete = models.SET_DEFAULT, default = get_current_name)

    class Meta:
        unique_together = (('topic', 'profile'), ('topic', 'topic_name'), ('profile', 'topic_name'))

#####
# TAG, ofTopic
#####

class Tag(models.Model):
    label = models.CharField(max_length = 20)
    context = models.CharField(max_length = 20)
    url = models.URLField()

    view_count = models.IntegerField(default = 0)
    topic_count = models.IntegerField(default = 0)

    def viewed(self):
        self.view_count.value += 1
        self.save()

    def topic_tagged(self):
        self.topic_count.value += 1
        self.save()

    def topic_tag_removed(self):
        self.topic_count.value -= 1
        self.save()

    class Meta:
        unique_together = (('url', ), ('label', 'context'))

### TopicTag

class TopicTag(models.Model):
    topic = models.ForeignKey(Topic, on_delete = models.CASCADE, related_name = 'tags')
    tag = models.ForeignKey(Tag, on_delete = models.PROTECT, related_name = 'topics')
    profile = models.ForeignKey(Profile, on_delete = models.SET_NULL, null = True, related_name = 'topictags')

    class Meta:
        unique_together = ('topic', 'tag')

def topictag_post_init(**kwargs):
    topictag = kwargs.get('instance')
    
    topictag.topic.name().vote(comment.profile, skipifhasvoted = True)
    topictag.tag.topic_tagged()

def topictag_pre_delete(**kwargs):
    topictag = kwargs.get('instance')
    
    topictag.tag.topic_tag_removed()

post_init.connect(topictag_post_init, TopicTag)
pre_delete.connect(topictag_pre_delete, TopicTag)

#####
# COMMENT, Version, Voter
#####

class Comment(models.Model):
    topic = models.ForeignKey(Topic, on_delete = models.CASCADE, related_name = 'comments')
    profile = models.ForeignKey(Profile, on_delete = models.CASCADE, related_name = 'comments') # LATER: make it get set to default = AnonymousUser
    vote_count = models.IntegerField(default = 0)
    boost_count = models.IntegerField(default = 0)
    boost_timestamp = models.DateTimeField()

    def current(self):
        return self.versions.first()

    def content(self):
        return self.current().content

    def edit(self, newcontent):
        cv = CommentVersion(
            comment = self,
            content = newcontent
        )
        cv.save()

    def toggle_vote(self, votecaster):
        commentvoters = self.commentvoter_set.all()

        for cv in commentvoters:
            if cv.profile == votecaster:
                cv.toggle()
                return

        cv = CommentVoter(
            profile = votecaster,
            comment = self,
            comment_version = self.current()
        )
        cv.save()

    def boost(self, amount = 1):
        self.boost_count += amount
        self.boost_timestamp = timezone.now
        self.save()

    def prune_boost(self, fresh_hours = 0, fresh_minutes = 10, fresh_seconds = 0):
        if self.boost_count.value > 0:
            fresh_duration = datetime.timedelta(hours = fresh_hours, minutes = fresh_minutes, seconds = fresh_seconds)
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

### CommentVersion

class CommentVersion(models.Model):
    comment = models.ForeignKey(Comment, on_delete = models.CASCADE, related_name = 'versions')
    content = models.TextField()

    timestamp = models.DateTimeField(auto_now_add = True)

    class Meta:
        ordering = ['-timestamp']
        unique_together = ('comment', 'timestamp')

### CommentVoter

class CommentVoter(models.Model):
    profile = models.ForeignKey(Profile, on_delete = models.CASCADE)
    comment = models.ForeignKey(Comment, on_delete = models.CASCADE)
    comment_version = models.ForeignKey(CommentVersion, on_delete = models.CASCADE)
    active = models.BooleanField(default = False)

    def toggle(self):
        if self.active:
            self.comment.vote_count.value -= 1
            self.active = False
        else:
            self.comment.vote_count.value += 1
            self.active = True
        
        self.save()

    class Meta:
        unique_together = (('profile', 'comment'), ('profile', 'comment_version'))

def comment_voter_post_init(**kwargs):
    cv = kwargs.get('instance')
    cv.toggle()

    cv.comment.boost()
    

def comment_voter_pre_delete(**kwargs):
    cv = kwargs.get('instance')
    
    if cv.active:
        cv.toggle()

post_init.connect(comment_voter_post_init, CommentVoter)
pre_delete.connect(comment_voter_pre_delete, CommentVoter)

#####
# RELATION, Label, LabelVoter
#####

class Relation(models.Model):
    topic_a = models.ForeignKey(Topic, on_delete = models.CASCADE, related_name = 'relations_a')
    topic_b = models.ForeignKey(Topic, on_delete = models.CASCADE, related_name = 'relations_b')

    def label(self):
        return self.labels.first()

    class Meta:
        unique_together = ('topic_a', 'topic_b')

### RelationLabel

class RelationLabel(models.Model):
    relation = models.ForeignKey(Relation, on_delete = models.CASCADE, related_name = 'labels')
    direction = models.NullBooleanField(default = None)
    text = models.CharField(max_length = 20, default = 'related')

    profile = models.ForeignKey(Profile, on_delete = models.SET_NULL, null = True, related_name = 'relations')
    vote_count = models.IntegerField(default = 0)
    boost_count = models.IntegerField(default = 0)
    boost_timestamp = models.DateTimeField()

    def toggle_vote(self, votecaster):
        voters = self.voters.all()

        for rlv in voters:
            if rlv.profile == votecaster:
                rlv.toggle()
                return

        rlv = RelationLabelVoter(
            profile = votecaster,
            relation_label = self
        )
        rlv.save()

    def boost(self, amount = 1):
        self.boost_count += amount
        self.boost_timestamp = timezone.now
        self.save()

    def prune_boost(self, fresh_hours = 0, fresh_minutes = 30, fresh_seconds = 0):
        if self.boost_count.value > 0:
            fresh_duration = datetime.timedelta(hours = fresh_hours, minutes = fresh_minutes, seconds = fresh_seconds)
            boost_duration = timezone.now - self.boost_timestamp.value

            boost_cut = boost_duration // fresh_duration
            if boost_cut:
                self.boost_count.value //= 2 ** boost_cut
                if self.boost_count.value:
                    self.boost_timestamp.value += fresh_duration * boost_cut

                self.save()

    class Meta:
        unique_together = ('relation', 'direction', 'text')

### RelationLabelVoter

class RelationLabelVoter(models.Model):
    profile = models.ForeignKey(Profile, on_delete = models.CASCADE)
    relation_label = models.ForeignKey(RelationLabel, on_delete = models.CASCADE, related_name = 'voters')
    active = models.BooleanField(default = False)

    def toggle(self):
        if self.active:
            self.relation_label.vote_count.value -= 1
            self.active = False
        else:
            self.relation_label.vote_count.value += 1
            self.active = True
        
        self.save()

    class Meta:
        unique_together = ('profile', 'relation_label')

def rl_voter_post_init(**kwargs):
    rlv = kwargs.get('instance')
    rlv.toggle()

    rlv.relation_label.boost()

def rl_voter_pre_delete(**kwargs):
    rlv = kwargs.get('instance')
    
    if rlv.active:
        rlv.toggle()

post_init.connect(rl_voter_post_init, RelationLabelVoter)
pre_delete.connect(rl_voter_pre_delete, RelationLabelVoter)