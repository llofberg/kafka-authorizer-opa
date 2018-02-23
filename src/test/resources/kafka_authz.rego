package kafka.authz

# Kafka authorize request

clusters = {"Cluster:kafka-cluster": ["t1"]}

import input as kafka_request

# {"input":
#   {
#      "principal" : {"principalType":"User","name":"ANONYMOUS"},
#      "operation" : {"name":"ClusterAction"},
#      "resource"  : {"resourceType":{"name":"Cluster","errorCode":31},"name":"kafka-cluster"},
#      "session"   : {"clientAddress":"172.20.0.4","sanitizedUser":"ANONYMOUS",
#                      {"principal":{"principalType":"User","name":"ANONYMOUS"}}
#                    }
#    }
# }
#
# Resource types: Cluster, Group, Topic
#
# Operations
# - Cluster: ClusterAction, Create, Describe
# - Group: Describe, Read
# - Topic: Alter, Delete, Describe, Read, Write
#

default allow = false

allow {
  kafka_request.resource.resourceType.name = "Cluster"
  kafka_request.resource.name = "kafka-cluster"
  kafka_request.session.principal.principalType = "User"
  kafka_request.session.principal.name = "ANONYMOUS"
#  kafka_request.Operation = "ClusterAction"
#  kafka_request.Operation = "Create"
#  kafka_request.Operation = "Describe"
}

allow {
  kafka_request.resource.resourceType.name = "Topic"
  kafka_request.resource.name = "t1"
  kafka_request.session.principal.principalType = "User"
  kafka_request.session.principal.name = "ANONYMOUS"
#  kafka_request.Operation = "Read"
#  kafka_request.Operation = "Write"
#  kafka_request.Operation = "Describe"
}
