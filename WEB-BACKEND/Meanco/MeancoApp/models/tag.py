from django.db.models import Model, IntegerField, ForeignKey, CharField, URLField, CASCADE, PROTECT, SET_NULL
from django.db.models.signals import post_init, pre_delete
from django.contrib.auth.models import User

from .profile import Profile
from .topic import Topic

### tag.Tag

class Tag(Model):
    label = CharField(max_length=20)
    description = CharField(max_length=20)
    URL = URLField(default=None,blank=True,null=True)

    view_count = IntegerField(default=0)
    topic_count = IntegerField(default=0)

    def __unicode__(self):
        return str(self.pk)

    def __str__(self):
        return str(self.pk)

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
        unique_together = (('URL', ), ('label', 'description'))

### tag.OfTopic

class OfTopic(Model):
    topic = ForeignKey(Topic, on_delete=CASCADE, related_name='tags')
    tag = ForeignKey(Tag, on_delete=PROTECT, related_name='topics')
    def __unicode__(self):
        return str(self.pk)

    def __str__(self):
        return str(self.pk)

    class Meta:
        unique_together = ('topic', 'tag')
