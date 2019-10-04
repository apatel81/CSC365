import json 
from pprint import pprint

with open('games_info.json') as file:
  data1 = json.load(file)

file1 = open("games.sql", "w")
for i in range(len(data1)):
    file1.write("INSERT INTO Game (game_id, date, home_team_id, away_team_id, score_home, score_away) VALUES (")
    file1.write(str(data1[i]["game_id"]))
    file1.write(", ")
    file1.write("'")
    #file1.write(str(data1[i]["date"].replace('-', '')))
    file1.write(str(data1[i]["date"]))
    file1.write("'")
    file1.write(", ")
    file1.write(str(data1[i]["home_team_id"]))
    file1.write(", ")
    file1.write(str(data1[i]["away_team_id"]))
    file1.write(", ")
    file1.write(data1[i]["score_home"])    
    file1.write(", ")
    file1.write(data1[i]["score_away"])
    file1.write(");")
    file1.write("\n")
file1.close()



with open('events_info.json') as file:
   data2 = json.load(file)

file2 = open("events.sql", "w")
for i in range(len(data2)):
    file2.write("INSERT INTO Event (id, game_id, type, count, player_id) VALUES (")
    file2.write(str(data2[i]["id"]))
    file2.write(", ")
    file2.write(str(data2[i]["game_id"]))
    file2.write(", ")
    file2.write("'")
    file2.write(data2[i]["type"])
    file2.write("'")
    file2.write(", ")
    file2.write(data2[i]["count"])
    file2.write(", ")
    file2.write(str(data2[i]["player_id"]))
    file2.write(");")
    file2.write("\n")
file2.close()
