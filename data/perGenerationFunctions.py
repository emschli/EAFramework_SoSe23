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