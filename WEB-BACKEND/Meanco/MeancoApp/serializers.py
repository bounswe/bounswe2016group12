from rest_framework import serializers
from MeancoApp.models import *

class CommentVersionSerializer(serializers.ModelSerializer):
    class Meta:
        model = Version
        fields = ('id','content','timestamp')
class CommentSerializer(serializers.ModelSerializer):
    versions= CommentVersionSerializer(many=True,read_only=True)
    class Meta:
        model = Comment
        fields = ('id','profile','vote_count','versions')
class TagSerializer(serializers.ModelSerializer):
    class Meta:
        model= Tag
        fields= ('id','label','description','URL')
class TagOfTopicSerializer(serializers.ModelSerializer):
    tag= TagSerializer(read_only=True)
    class Meta:
        model =OfTopic
        fields = ('tag',)
class RelationSerializer(serializers.ModelSerializer):
    class Meta:
        model = Relation
        fields = ('id','topic_a','topic_b','vote_count','label','isBidirectional')

class TopicSerializer(serializers.ModelSerializer):
    relations_a= RelationSerializer(many=True,read_only=True)
    comments = CommentSerializer(many=True, read_only=True)
    tags = TagOfTopicSerializer(many=True, read_only=True)
    class Meta:
        model = Topic
        fields = ('id','label','relations_a','comments','tags' )

class TopicListSerializer(serializers.ModelSerializer):
    relations_a= RelationSerializer(many=True,read_only=True)
    tags = TagOfTopicSerializer(many=True, read_only=True)
    class Meta:
        model = Topic
        fields = ('id','label','relations_a','tags' )
