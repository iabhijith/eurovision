#!/bin/bash

# List of countries
countries=(
'Belgium'
'Netherlands'
'Germany'
'Italy'
'Belarus'
'Hungary'
'Portugal'
'Ireland'
'Denmark'
'Switzerland'
'Austria'
)

num_countries=11

for i in `seq 1 1000000`; do
 echo "{\"country\":\"${countries[$RANDOM % $num_countries]}\",\"votedFor\":\"${countries[$RANDOM % $num_countries]}\"}"
done