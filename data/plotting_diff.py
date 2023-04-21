import math

import matplotlib.pyplot as plt
import statistics
import csv
import os


PATH_TO_PLOT_FOLDER = os.getcwd() + "/plots/"
PATH_TO_CSVS = os.path.dirname(os.getcwd()) + "/src/main/resources/diff/"
PATH_TO_CSVS_OLD = os.path.dirname(os.getcwd()) + "/src/main/resources/evolution/"


def stringToFloat(strrr):
    return float(strrr)


def getMeanFitnessPerGeneration(path):
    result = []
    with open(path) as file:
        reader = csv.reader(file, delimiter=' ')
        for row in reader:
            mean_fitness = statistics.mean(map(stringToFloat, row))
            result.append(mean_fitness)

    return result


def getMinPerGeneration(path):
    result = []
    with open(path) as file:
        reader = csv.reader(file, delimiter=' ')
        for row in reader:
            min_value = min(map(stringToFloat, row))
            result.append(min_value)

    return result

x = [*range(1, 1001)]
# Ackley Vergleich mit GA und HC (Mean-Fitness)
y1 = getMeanFitnessPerGeneration(PATH_TO_CSVS + "diff_ackley.csv")
y2 = getMeanFitnessPerGeneration(PATH_TO_CSVS_OLD + "2_ACK_mean_low.csv")
y3 = getMeanFitnessPerGeneration(PATH_TO_CSVS_OLD + "2_ACK_HC.csv")

fig, ax = plt.subplots()
ax.plot(x, y1, label="Differential")
ax.plot(x, y2, label="EA with Elitism")
ax.plot(x, y3, label="Hillclimbing")
ax.set(xlabel="Generationen", ylabel="Mean Ackley-Value", title="Ackley (Mean per Generation)")
ax.legend()
plt.xscale('log')
plt.show()

# Ackley Vergleich mit GA und HC (Min-Fitness)
y1 = getMinPerGeneration(PATH_TO_CSVS + "diff_ackley.csv")
y2 = getMinPerGeneration(PATH_TO_CSVS_OLD + "2_ACK_mean_low.csv")
y3 = getMinPerGeneration(PATH_TO_CSVS_OLD + "2_ACK_HC.csv")

fig, ax = plt.subplots()
ax.plot(x, y1, label="Differential")
ax.plot(x, y2, label="EA with Elitism")
ax.plot(x, y3, label="Hillclimbing")
ax.set(xlabel="Generationen", ylabel="Mean Ackley-Value", title="Ackley (Min per Generation)")
ax.legend()
plt.xscale('log')
plt.show()


def getYForSinusParameters(a, f, phi, d):
    result = []
    for t in x:
        new = a * math.sin(2*math.pi * f * t + phi) + d
        result.append(new)
    return result


# Plot found Sinus Curve and Values
PATH_TO_MEASUREMENTS = "/home/mirjam/Nextcloud/Uni/S7/Evolutionäre Algorithmen/Praktikum/code/EAFramework_SoSe23/src/main/resources/sensordata.csv"
measurements = []

with open(PATH_TO_MEASUREMENTS) as file:
    reader = csv.reader(file, delimiter=';')
    count = 1
    for row in reader:
        if count > 1001:
            break
        measurements.append(row[4].replace(',', '.'))
        count = count + 1

measurements = list(map(stringToFloat, measurements[1:]))


fig, ax = plt.subplots()
ax.scatter(x, measurements, s=1)
curve = getYForSinusParameters(-3.2769184, -5962.0195, 177671.69, 9.86444)
ax.plot(x, curve, color="red")
plt.show()