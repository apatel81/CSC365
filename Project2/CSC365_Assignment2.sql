{\rtf1\ansi\ansicpg1252\cocoartf1561\cocoasubrtf600
{\fonttbl\f0\fswiss\fcharset0 Helvetica;\f1\fnil\fcharset0 Menlo-Regular;}
{\colortbl;\red255\green255\blue255;\red0\green0\blue0;\red255\green255\blue255;}
{\*\expandedcolortbl;;\csgray\c0;\csgray\c100000;}
\margl1440\margr1440\vieww13860\viewh17220\viewkind0
\pard\tx720\tx1440\tx2160\tx2880\tx3600\tx4320\tx5040\tx5760\tx6480\tx7200\tx7920\tx8640\pardirnatural\partightenfactor0

\f0\fs24 \cf0 Ajay Patel\
CSC 365-05\
Kuboi Assignment 2 SQL Queries\
\
1. 
\f1\fs22 \cf2 \cb3 \CocoaLigature0 \
\pard\tx560\tx1120\tx1680\tx2240\tx2800\tx3360\tx3920\tx4480\tx5040\tx5600\tx6160\tx6720\pardirnatural\partightenfactor0
select first, last, player_id, Goals from Player JOIN (select * from (select player_id, SUM(count) as Goals from Event where type = "Goals scored" group by player_id order by Goals DESC) as A where Goals = (select max(Goals) from (select player_id, SUM(count) as Goals from Event where type = "Goals scored" group by player_id order by Goals DESC) as B)) as max_goals ON Player.id = max_goals.player_id;\
\
\
\
2. \
select first, last, position, goals, game_id, player_id from Player JOIN(select * from(select game_id, player_id, SUM(count) as goals from Event where type = "Goals scored" group by game_id, player_id) as A where goals = (select MAX(goals) from (select game_id, player_id, SUM(count) as goals from Event where type = "Goals scored" group by game_id, player_id) as B)) as C ON Player.id = C.player_id;\
\
\
\
3. \
select name, A.id, Total_Goals from Team JOIN (select * from (select A.id, A.goals + B.goals as Total_Goals from (select home_team_id as id, SUM(score_home) as goals from Game group by home_team_id) as A join (select away_team_id as id, SUM(score_away) as goals from Game group by away_team_id ) as B on A.id = B.id order by Total_Goals DESC) as A where Total_Goals >= ALL (select A.goals + B.goals as Total_Goals from (select home_team_id as id, SUM(score_home) as goals from Game group by home_team_id) as A join (select away_team_id as id, SUM(score_away) as goals from Game group by away_team_id ) as B on A.id = B.id order by Total_Goals DESC)) as A ON Team.id = A.id;\
\
\
4. \
select game_id, name, A.id, goals from Team JOIN (select * from (select game_id, home_team_id as id, score_home as goals from Game where score_home > score_away UNION select game_id, away_team_id as id, score_away as goals from Game where score_away > score_home ORDER BY game_id) as A where goals >= ALL (select goals from (select game_id, home_team_id as id, score_home as goals from Game where score_home > score_away UNION select game_id, away_team_id as id, score_away as goals from Game where score_away > score_home ORDER BY game_id) as A)) as A ON Team.id = A.id; \
\
\
\
\
5. \
select name, Team.id from Team JOIN (select * from (select distinct home_team_id as id from Game UNION select distinct away_team_id as id from Game) as C where id NOT IN (select distinct id from (select home_team_id as id, count(home_team_id) as wins from Game where score_home > score_away group by id UNION select away_team_id as id, count(away_team_id) as wins from Game where score_away  > score_home group by id) as A)) as B ON Team.id = B.id;\
}