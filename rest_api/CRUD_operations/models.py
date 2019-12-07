from django.db import models

# Create your models here.

class TestQuestion(models.Model):
    question_text = models.CharField(max_length=200)
    pub_date = models.DateTimeField('date published')

class TestChoice(models.Model):
    question = models.ForeignKey(TestQuestion, on_delete=models.CASCADE)
    choice_text = models.CharField(max_length=200)
    votes = models.IntegerField(default=0)
