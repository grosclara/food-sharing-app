from rest_framework.response import Response
from rest_framework.status import (
    HTTP_200_OK
)

class DestroyWithPayloadMixin(object):
     def destroy(self, *args, **kwargs):
         serializer = self.get_serializer(self.get_object())
         super().destroy(*args, **kwargs)
         return Response(serializer.data, status=HTTP_200_OK)