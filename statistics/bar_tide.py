import os

import numpy
import matplotlib.pyplot as plt


def file_response_time(path, response_time):
    with open(f'./logs/{path}/ResponseTime.log', 'r') as file:
        last = file.readlines()[-1]
        response_time.append(int(last.split("avg: ")[1].split()[0]))


def file_failure_rate(path, failure_rate):
    with open(f'./logs/{path}/FailureRate.log', 'r') as file:
        last = file.readlines()[-1].split()
        request = int(last[2])
        response = int(last[4])
        failure_rate.append((1 - response/request) * 100)


def bar():
    labels = ['Consistent Hash', 'Estimated Finish Time', 'Least Recently Used', 'Least Response Time',
              'Least Usage', 'Random', 'Round Robin']
    labels_temp = [''.join(item.split()) for item in labels]
    response_time = []
    failure_rate = []
    directory_temp = os.listdir('./logs')
    directory = []
    for item in directory_temp:
        if item.split('_')[0] in labels_temp:
            directory.append(item)
    directory.sort()
    for i in range(1, len(directory), 3):
        file_response_time(directory[i], response_time)
        file_failure_rate(directory[i], failure_rate)
    response_time_labels = []
    failure_rate_labels = []
    for i in range(len(labels)):
        response_time_labels.append(str(response_time[i]))
        failure_rate_labels.append(str(round(failure_rate[i], 2)))

    x = numpy.arange(len(labels))
    width = 0.35
    fig, axis = plt.subplots(figsize=(10,6))

    y1 = axis.bar(x - width / 2, response_time, width, label='Average Response Time', color='b')
    axis2 = axis.twinx()
    y2 = axis2.bar(x + width / 2, failure_rate, width, label='Average Failure Rate', color='r')
    axis.set_xticks(x, labels, size=12, rotation=10)
    axis.tick_params(axis='y', labelcolor = 'b')
    axis2.tick_params(axis='y', labelcolor = 'r')
    axis.set_ylabel('Average Response Time(ms)', fontdict={'size':15}, color='b')
    axis2.set_ylabel('Average Failure Rate(%)', fontdict={'size':15}, color='r')
    plt.legend(handles = [y1, y2], loc = 'upper right')
    axis.bar_label(y1, labels=response_time_labels, padding=3, fontsize=10, color='b')
    axis2.bar_label(y2, labels=failure_rate_labels, padding=3, fontsize=10, color='r')
    
    plt.tight_layout()
    plt.show()


if __name__ == '__main__':
    bar()
