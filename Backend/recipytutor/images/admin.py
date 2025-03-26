from django.contrib import admin
from .models import ImagePost

@admin.register(ImagePost)
class ImagePostAdmin(admin.ModelAdmin):
    list_display = ('title', 'uploaded_by', 'uploaded_at')
    search_fields = ('title', 'uploaded_by__username')
