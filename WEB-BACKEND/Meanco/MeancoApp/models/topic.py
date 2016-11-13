from django.db.models import Model, IntegerField, DateTimeField, ForeignKey, CharField, CASCADE, SET_DEFAULT
from django.contrib.auth.models import User
from .profile import Profile

### topic.Topic

class Topic(Model):
    comment_count = IntegerField(default=0)
    view_count = IntegerField(default=1)
    timestamp_last = DateTimeField(auto_now=True)
    timestamp_first = DateTimeField(auto_now_add=True)

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

### topic.Name

class Name(Model):
    topic = ForeignKey(Topic, on_delete=CASCADE, related_name='names')
    label = CharField(max_length=40)
    vote_count = IntegerField()

    def vote(self, votecaster, skipifhasvoted=False):
        namevoters = self.topic.namevoter_set.all()

        for voter in namevoters:
            if voter.profile == votecaster:
                if not skipifhasvoted:
                    voter.name.value = self
                    voter.save()
                return

        voter = NameVoter(
            profile=votecaster,
            topic=self.topic,
            name=self
        )
        tc.save()

    class Meta:
        ordering = ['-vote_count']
        unique_together = ('topic', 'label')

### topic.NameVoter

class NameVoter(Model):
    def get_current_name(self):
        return self.topic.name()
    
    profile = ForeignKey(Profile, on_delete=CASCADE)
    topic = ForeignKey(Topic, on_delete=CASCADE)
    name = ForeignKey(Name, on_delete=SET_DEFAULT, default=get_current_name)

    class Meta:
        unique_together = (('topic', 'profile'), ('topic', 'name'), ('profile', 'name'))