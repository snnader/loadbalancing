import time
from concurrent.futures import ThreadPoolExecutor
import numpy as np
from scipy.stats import chi2
import matplotlib.pyplot as plt
from request_gen import generate_requests

def start(cpu=(1,10), mem=(1,10), io=(1,10)):
    quantum = 0.5
    seconds = 60
    peak_freq = 50 #try again
    pool = ThreadPoolExecutor(max_workers=int(seconds/quantum))
    thread_list = []
    time_arr = np.arange(0,seconds,quantum)
    chi2_freq = (chi2.pdf(time_arr, df=14) + 0.01) * 11.074 * peak_freq
    print(chi2_freq)

    # plt.plot(time_arr, chi2_freq)
    # plt.show()
    # return

    for i in range(int(seconds/quantum)):
        thread_list.append(pool.submit(generate_requests, int(chi2_freq[i]) + 1, cpu, mem, io))
        print(f'thread {i+1} started')
        time.sleep(quantum)

    while True:
        is_done = True
        for i in range(int(seconds/quantum)):
            if not thread_list[i].done():
                is_done = False
        if is_done:
            print('done')
            break
        else: time.sleep(1)
    pool.shutdown()


if __name__ == '__main__':
    start()


