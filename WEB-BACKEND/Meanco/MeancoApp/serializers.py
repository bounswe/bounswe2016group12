from rest_framework import serializers
from MeancoApp.models import *


class NameSerializer(serializers.ModelSerializer):
    class Meta:
        model = Name
        fields= ('id','label','vote_count')

class CommentVersionSerializer(serializers.ModelSerializer):
    class Meta:
        model = Version
        fields = ('id','content','timestamp')
class CommentSerializer(serializers.ModelSerializer):
    versions= CommentVersionSerializer(many=True,read_only=True)
    class Meta:
        model = Comment
        fields = ('id','profile','versions')
class TagSerializer(serializers.ModelSerializer):
    class Meta:
        model= Tag
        fields= ('id','label','description','url')
class TagOfTopicSerializer(serializers.ModelSerializer):
    tag= TagSerializer(read_only=True)
    class Meta:
        model =OfTopic
        fields = ('id','tag')
class RelationLabelSerializer(serializers.ModelSerializer):
    class Meta:
        model = Label
        fields = ('id','text')

class RelationSerializer(serializers.ModelSerializer):
    labels = RelationLabelSerializer(many=True,read_only=True)
    class Meta:
        model = Relation
        fields = ('topic_a','topic_b','labels')

class TopicSerializer(serializers.ModelSerializer):
    names= NameSerializer(many=True,read_only=True)
    relations_a= RelationSerializer(many=True,read_only=True)
    relations_b= RelationSerializer(many=True,read_only=True)
    comments = CommentSerializer(many=True, read_only=True)
    tags = TagOfTopicSerializer(many=True, read_only=True)
    class Meta:
        model = Topic
        fields = ('id','names','relations_a','relations_b','comments','tags' )

class TopicListSerializer(serializers.ModelSerializer):
    name= NameSerializer(read_only=True)
    relations_a= RelationSerializer(many=True,read_only=True)
    relations_b= RelationSerializer(many=True,read_only=True)
    tags = TagOfTopicSerializer(many=True, read_only=True)
    class Meta:
        model = Topic
        fields = ('id','name','relations_a','relations_b','tags' )
