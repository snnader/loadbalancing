import matplotlib.pyplot as plt
from scipy.ndimage import gaussian_filter
import numpy as np
from scipy.stats import chi2
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

    line, = plt.plot(time_hash, gaussian_filter(throughput_hash, sigma=1))
    return line

def plot():
    fig, axis = plt.subplots(figsize=(10,6))
    algos = ['Consistent Hashing', 'Estimated Finish Time',
                        'Least Recently Used', 'Least Response Time',
                        'Least Usage', 'Random', 'Round Robin']
    dirnames = [''.join(name.split()) for name in algos]
    dirlist = os.listdir('./logs')
    dirlist = [name for name in dirlist if name.split('_')[0] in dirnames]
    dirlist.sort()
    handles = []
    for i in range(2, len(dirlist), 3):
        handles.append(plot_algo(dirlist[i]))

    time_arr = np.arange(0, 60, 0.5)
    chi2_freq = (chi2.pdf(time_arr, df=14) + 0.01) * 11.074 * int(70 * 0.5) * 400
    line, = plt.plot(time_arr, chi2_freq)
    handles.append(line)
    algos.append('Request Pattern')

    plt.legend(handles=handles, labels=algos, loc='best', fontsize=15)
    plt.ylim(bottom=0)
    plt.xlim(left=0)
    plt.xlabel('Time (s)', fontsize=22)
    plt.ylabel('Throughput (B/s)', fontsize=22)
    plt.xticks(fontsize=15)
    plt.yticks(fontsize=15)
    plt.tight_layout()
    plt.savefig("line_spike")
    plt.clf()

if __name__ == '__main__':
    plot()
