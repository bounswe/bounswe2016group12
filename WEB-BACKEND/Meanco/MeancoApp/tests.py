from django.test import TestCase
from MeancoApp.models import *
from MeancoApp.functions.search import *
# Create your tests here.
class ModelTestCases(TestCase):

    def setUp(self):
        self.u = User.objects.create_user(username="TestUser",email="test@test.com",password="test")
        self.p = Profile.objects.create(user=self.u)
        self.t1 = Topic.objects.create(label="Test")
        self.t2 = Topic.objects.create(label="Test2")
        self.c = Comment.objects.create(topic=self.t1,profile=self.p)
        self.v = Version.objects.create(comment=self.c,content="test")
        self.r = Relation.objects.create(topic_a=self.t1,topic_b=self.t2,label="test")
        self.tag = Tag.objects.create(label="test",description="test",URL="test.com")
        self.ot =OfTopic.objects.create(tag=self.tag,topic=self.t1)

        # Model Tests
    def testModel(self):
        self.assertEquals(self.u.id,User.objects.get(username="TestUser").id)
        self.assertEquals(self.p.id,Profile.objects.get(user_id=self.u.id).id)
        self.assertEquals(self.t1.label,Topic.objects.get(label="Test").label)
        self.assertEquals(self.t2.id, Topic.objects.get(label="Test2").id)
        self.assertEqual(self.c.id,Comment.objects.get(topic_id=self.t1.id,profile_id=self.p.id).id)
        self.assertEquals(self.v.id,Version.objects.get(comment_id=self.c.id,content__contains="test").id)
        self.assertEquals(self.r.label,Relation.objects.get(id=self.r.id).label)
        self.assertEquals(self.tag.label,Tag.objects.get(URL__contains="test").label)
        self.assertEquals(self.ot.id,OfTopic.objects.get(tag_id=self.tag.id,topic_id=self.t1.id).id)

        #Vote testing
    def testVote(self):
        com= Comment.objects.get(id=self.c.id)
        voter = Voter(comment_id=com.id,profile_id=self.p.id)
        voter.save()
        v_count= com.vote_count
        voter.toggle("upvote")
        self.assertEquals(v_count+1,Comment.objects.get(id=com.id).vote_count)
        voter.toggle("downvote")
        self.assertEquals(v_count - 1, Comment.objects.get(id=com.id).vote_count)
        voter.toggle("downvote")
        self.assertEquals(v_count, com.vote_count)
        #topic tests
    def testTopic(self):
        t_view_count=self.t1.view_count
        t_comment_count=self.t1.comment_count
        self.t1.viewed()
        self.t1.commented()
        self.assertEquals(t_view_count+1,self.t1.view_count)
        self.assertEquals(t_comment_count+1,self.t1.comment_count)

        #search Test
    def testSearch(self):
        t= Topic(label="Fenerbahce")
        t.save()
        getRefOfTopic("Fenerbahce",t.id)
        self.assertEquals(t.id,findStringMatchedTopics("Fener")[0])
        self.assertEquals(t.id, findRefTopics("Galatasaray")[0])







