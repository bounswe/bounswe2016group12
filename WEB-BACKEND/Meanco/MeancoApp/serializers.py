"""@package Serializers
View documentation

"""
from rest_framework import serializers
from MeancoApp.models import *

## Comment Version Serializer
#
class CommentVersionSerializer(serializers.ModelSerializer):
    class Meta:
        model = Version
        fields = ('id','content','timestamp')


## Comment Serializer
#
class CommentSerializer(serializers.ModelSerializer):
    versions= CommentVersionSerializer(many=True,read_only=True)
    profile  = serializers.ReadOnlyField(source='profile.user.username')
    class Meta:
        model = Comment
        fields = ('id','profile','vote_count','versions')


## Tag Serializer
#
class TagSerializer(serializers.ModelSerializer):
    class Meta:
        model= Tag
        fields= ('id','label','description','URL')

## Tag Of topic Serializer
#
class TagOfTopicSerializer(serializers.ModelSerializer):
    tag= TagSerializer(read_only=True)
    class Meta:
        model =OfTopic
        fields = ('tag',)

## Relation Serializer
#
class RelationSerializer(serializers.ModelSerializer):
    class Meta:
        model = Relation
        fields = ('id','topic_a','topic_b','vote_count','label','isBidirectional')

## Topic Detail Serializer
#
class TopicSerializer(serializers.ModelSerializer):
    relations_a= RelationSerializer(many=True,read_only=True)
    comments = CommentSerializer(many=True, read_only=True)
    tags = TagOfTopicSerializer(many=True, read_only=True)
    class Meta:
        model = Topic
        fields = ('id','label','relations_a','comments','tags' )

## Topic list Serializer
#
class TopicListSerializer(serializers.ModelSerializer):
    relations_a= RelationSerializer(many=True,read_only=True)
    tags = TagOfTopicSerializer(many=True, read_only=True)
    class Meta:
        model = Topic
        fields = ('id','label','relations_a','tags' )
