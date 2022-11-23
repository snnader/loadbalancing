import matplotlib.pyplot as plt
from scipy.ndimage import gaussian_filter
import os

def plot_algo(filepath):
    file_hash = open(f'./logs/{filepath}/Throughput.log', 'r')
    time_hash = []
    throughput_hash = []
    for line in file_hash:
        lst = line.split()
        if lst[1] != '0.0':
            time_hash.append(int(lst[0][1:-1]))
            throughput_hash.append(float(lst[1]))
    file_hash.close()
    line, = plt.plot(time_hash, gaussian_filter(throughput_hash, sigma=5))
    return line

def plot():
    algos = ['Consistent Hash', 'Estimated Finish Time',
                        'Least Recently Used', 'Least Response Time',
                        'Least Usage', 'Random', 'Round Robin']
    dirnames = [''.join(name.split()) for name in algos]
    dirlist = os.listdir('./logs')
    dirlist = [name for name in dirlist if name.split('_')[0] in dirnames]
    dirlist.sort()
    handles = []
    for i in range(2, len(dirlist), 3):
        handles.append(plot_algo(dirlist[i]))

    plt.legend(handles=handles,
               labels=algos, loc='best')
    plt.ylim(bottom=0)
    plt.xlim(left=0)
    plt.xlabel('Time(s)', fontsize=12)
    plt.ylabel('Throughput(B/s)', fontsize=12)
    plt.show()

if __name__ == '__main__':
    plot()
