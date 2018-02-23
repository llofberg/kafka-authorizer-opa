package kafka.authz

# Kafka authorize request

clusters = {"Cluster:kafka-cluster": ["t1"]}

import input as kafka_request

# kafka_request = {
#   "Principal"     : "User:ANONYMOUS",
#   "Resource"      : "Topic:t1" or "Cluster:kafka-cluster",
#   "Operation"     : "Describe" or "Create" or "Write",
#   "ClientAddress" : "/172.20.0.3"
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
  kafka_request.Resource = "Cluster:kafka-cluster"
#  kafka_request.Operation = "ClusterAction"
#  kafka_request.Operation = "Create"
#  kafka_request.Operation = "Describe"
  kafka_request.Principal = "User:ANONYMOUS"
}

allow {
  kafka_request.Resource = "Topic:t1"
#  kafka_request.Operation = "Read"
#  kafka_request.Operation = "Write"
#  kafka_request.Operation = "Describe"
  kafka_request.Principal = "User:ANONYMOUS"
}
