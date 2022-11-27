#!/bin/bash

function printLine()
{
echo "|---------------------------"
}

printLine
echo "| 0. Stopping the all services"
echo "|"
echo "| I will clean all the trash :-)"

printLine
echo "| 1. Stopping services"
printLine
kubectl delete -f services/cassandra-service.yaml
kubectl delete -f services/user-service.yaml

printLine
echo "| 2. Deleting config"
printLine
kubectl delete -f configmap.yaml

printLine
echo "| 3. Deleting secret"
printLine
kubectl delete -f secret.yaml
