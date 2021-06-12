import json
import time

Edges = []

Vertices = []

Degrees = []

Results = []

with open('hourDegree.json') as file:
    data = json.load(file)

    for view in data['views']:
        Edges.append(view['edges'])
        Vertices.append(view['vertices'])
        if (view['edges'] != 0):
            Degrees.append(view['degree'])

Results.append(sum(Vertices)/len(Vertices))
Results.append(sum(Edges)/len(Edges))
Results.append(sum(Degrees)/len(Degrees))
print(Results)

archivo=open('hourResults.txt','w')
archivo.write(','.join([str(result) for result in Results]))
archivo.close()