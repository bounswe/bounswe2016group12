"""@package Models
View documentation

"""
from django.db.models import Model, IntegerField, DateTimeField, ForeignKey, CharField, CASCADE, SET_DEFAULT
from django.contrib.auth.models import User

## Topic Model
class Topic(Model):
    comment_count = IntegerField(default=0)
    view_count = IntegerField(default=1)
    label = CharField(max_length=40)
    timestamp_last = DateTimeField(auto_now=True)
    timestamp_first = DateTimeField(auto_now_add=True)

    def __unicode__(self):
        return  str(self.pk)

    def __str__(self):
        return str(self.pk)

    def name(self):
        return self.names.first()
    ## Increases view count.
    def viewed(self):
        self.view_count += 1
        self.save()
    ## Increases comment count.
    def commented(self):
        self.comment_count += 1
        self.save()
    ## Decreases comment count.
    def comment_removed(self):
        self.comment_count -= 1
        self.save()

##Hidden Model object for finding semantic references of topic.
class TopicRef(Model):
    topic= ForeignKey(Topic, on_delete=CASCADE, related_name='references')
    qId = CharField(max_length=20)