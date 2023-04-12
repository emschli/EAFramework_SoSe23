import matplotlib.pyplot as plt
import statistics
import csv
import os

PATH_TO_PLOT_FOLDER = os.getcwd() + "/plots/"
PATH_TO_CSVS = os.path.dirname(os.getcwd()) + "/src/main/resources/evolution/"


def stringToFloat(strrr):
    return float(strrr)


def getMeanFitnessPerGeneration(fileName):
    result = []
    with open(str(PATH_TO_CSVS)+fileName) as file:
        reader = csv.reader(file, delimiter=' ')
        for row in reader:
            mean_fitness = statistics.mean(map(stringToFloat, row))
            result.append(mean_fitness)

    return result


def getMinPerGeneration(fileName):
    result = []
    with open(str(PATH_TO_CSVS) + fileName) as file:
        reader = csv.reader(file, delimiter=' ')
        for row in reader:
            min_value = min(map(stringToFloat, row))
            result.append(min_value)

    return result


x = [*range(1, 1001)]
# 1     Ohne Elitismus
# 1.1   2DSphere
# 1.1.1 Mean
y1 = getMeanFitnessPerGeneration("1_2D_singlePoint_low.csv")
y2 = getMeanFitnessPerGeneration("1_2D_singlePoint_high.csv")
y3 = getMeanFitnessPerGeneration("1_2D_mean_low.csv")
y4 = getMeanFitnessPerGeneration("1_2D_mean_high.csv")
y5 = getMeanFitnessPerGeneration("1_2D_HC.csv")

fig, ax = plt.subplots()
ax.plot(x, y1, label="Single Point / Low")
ax.plot(x, y2, label="Single Point / High")
ax.plot(x, y3, label="Mean / Low")
ax.plot(x, y4, label="Mean / High")
ax.plot(x, y5, label="Hillclimbing")
ax.set(xlabel="Generationen", ylabel="Mean Sphere2D value", title="Sphere2D (Mean per Generation)")
ax.legend()
plt.show()


# 1.1.2 Min
y1 = getMinPerGeneration("1_2D_singlePoint_low.csv")
y2 = getMinPerGeneration("1_2D_singlePoint_high.csv")
y3 = getMinPerGeneration("1_2D_mean_low.csv")
y4 = getMinPerGeneration("1_2D_mean_high.csv")
y5 = getMinPerGeneration("1_2D_HC.csv")

fig, ax = plt.subplots()
ax.plot(x, y1, label="Single Point / Low")
ax.plot(x, y2, label="Single Point / High")
ax.plot(x, y3, label="Mean / Low")
ax.plot(x, y4, label="Mean / High")
ax.plot(x, y5, label="Hillclimbing")
ax.set(xlabel="Generationen", ylabel="Min Sphere2D value", title="Sphere2D (Min per Generation)")
ax.legend()
plt.show()

# 1.2   Ackley
# 1.2.1 Mean
y1 = getMeanFitnessPerGeneration("1_ACK_singlePoint_low.csv")
y2 = getMeanFitnessPerGeneration("1_ACK_singlePoint_high.csv")
y3 = getMeanFitnessPerGeneration("1_ACK_mean_low.csv")
y4 = getMeanFitnessPerGeneration("1_ACK_mean_high.csv")
y5 = getMeanFitnessPerGeneration("1_ACK_HC.csv")

fig, ax = plt.subplots()
ax.plot(x, y1, label="Single Point / Low")
ax.plot(x, y2, label="Single Point / High")
ax.plot(x, y3, label="Mean / Low")
ax.plot(x, y4, label="Mean / High")
ax.plot(x, y5, label="Hillclimbing")
ax.set(xlabel="Generationen", ylabel="Mean Ackley value", title="Ackley (Mean per Generation)")
ax.legend()
plt.show()


# 1.2.1 Min
y1 = getMinPerGeneration("1_ACK_singlePoint_low.csv")
y2 = getMinPerGeneration("1_ACK_singlePoint_high.csv")
y3 = getMinPerGeneration("1_ACK_mean_low.csv")
y4 = getMinPerGeneration("1_ACK_mean_high.csv")
y5 = getMinPerGeneration("1_ACK_HC.csv")

fig, ax = plt.subplots()
ax.plot(x, y1, label="Single Point / Low")
ax.plot(x, y2, label="Single Point / High")
ax.plot(x, y3, label="Mean / Low")
ax.plot(x, y4, label="Mean / High")
ax.plot(x, y5, label="Hillclimbing")
ax.set(xlabel="Generationen", ylabel="Min Ackley value", title="Ackley (Min per Generation)")
ax.legend()
plt.show()

# 2     Mit Elitismus
# 2.1   2DSphere
# 2.1.1 Mean
y1 = getMeanFitnessPerGeneration("2_2D_singlePoint_low.csv")
y2 = getMeanFitnessPerGeneration("2_2D_singlePoint_high.csv")
y3 = getMeanFitnessPerGeneration("2_2D_mean_low.csv")
y4 = getMeanFitnessPerGeneration("2_2D_mean_high.csv")
y5 = getMeanFitnessPerGeneration("2_2D_HC.csv")

fig, ax = plt.subplots()
ax.plot(x, y1, label="Single Point / Low")
ax.plot(x, y2, label="Single Point / High")
ax.plot(x, y3, label="Mean / Low")
ax.plot(x, y4, label="Mean / High")
ax.plot(x, y5, label="Hillclimbing")
ax.set(xlabel="Generationen", ylabel="Mean Sphere2D value", title="Sphere2D (Mean per Generation) with Elitism (N=1)")
ax.legend()
plt.show()

# 2.1.2 Min
y1 = getMinPerGeneration("2_2D_singlePoint_low.csv")
y2 = getMinPerGeneration("2_2D_singlePoint_high.csv")
y3 = getMinPerGeneration("2_2D_mean_low.csv")
y4 = getMinPerGeneration("2_2D_mean_high.csv")
y5 = getMinPerGeneration("2_2D_HC.csv")

fig, ax = plt.subplots()
ax.plot(x, y1, label="Single Point / Low")
ax.plot(x, y2, label="Single Point / High")
ax.plot(x, y3, label="Mean / Low")
ax.plot(x, y4, label="Mean / High")
ax.plot(x, y5, label="Hillclimbing")
ax.set(xlabel="Generationen", ylabel="Min Sphere2D value", title="Sphere2D (Min per Generation) with Elitism (N=1)")
ax.legend()
plt.show()

# 2.2   Ackley
# 2.2.1 Mean
y1 = getMeanFitnessPerGeneration("2_ACK_singlePoint_low.csv")
y2 = getMeanFitnessPerGeneration("2_ACK_singlePoint_high.csv")
y3 = getMeanFitnessPerGeneration("2_ACK_mean_low.csv")
y4 = getMeanFitnessPerGeneration("2_ACK_mean_high.csv")
y5 = getMeanFitnessPerGeneration("2_ACK_HC.csv")

fig, ax = plt.subplots()
ax.plot(x, y1, label="Single Point / Low")
ax.plot(x, y2, label="Single Point / High")
ax.plot(x, y3, label="Mean / Low")
ax.plot(x, y4, label="Mean / High")
ax.plot(x, y5, label="Hillclimbing")
ax.set(xlabel="Generationen", ylabel="Mean Ackley value", title="Ackley (Mean per Generation) with Elitism (N=1)")
ax.legend()
plt.show()

# 2.2.2 Min
y1 = getMinPerGeneration("2_ACK_singlePoint_low.csv")
y2 = getMinPerGeneration("2_ACK_singlePoint_high.csv")
y3 = getMinPerGeneration("2_ACK_mean_low.csv")
y4 = getMinPerGeneration("2_ACK_mean_high.csv")
y5 = getMinPerGeneration("2_ACK_HC.csv")

fig, ax = plt.subplots()
ax.plot(x, y1, label="Single Point / Low")
ax.plot(x, y2, label="Single Point / High")
ax.plot(x, y3, label="Mean / Low")
ax.plot(x, y4, label="Mean / High")
ax.plot(x, y5, label="Hillclimbing")
ax.set(xlabel="Generationen", ylabel="Min Ackley value", title="Ackley (Min per Generation) with Elitism (N=1)")
ax.legend()
plt.show()

