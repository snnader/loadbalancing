import time
from concurrent.futures import ThreadPoolExecutor
import numpy as np
from scipy.stats import norm
import matplotlib.pyplot as plt
from request_gen import generate_requests

def start(peak_freq=55, cpu=(1,10), mem=(1,10), io=(1,10)):
    quantum = 0.5
    seconds = 60
    pool = ThreadPoolExecutor(max_workers=int(seconds/quantum))
    thread_list = []
    gaussion_scale = 7.5
    x = np.arange(0, seconds, quantum)
    arr = (norm.pdf(x, loc=20, scale=gaussion_scale) + norm.pdf(x, loc=40, scale=gaussion_scale)) * 18.25 * int(peak_freq * quantum)

    # print(arr)
    # plt.plot(x, arr)
    # plt.show()
    # return

    for i in range(int(seconds/quantum)):
        thread_list.append(pool.submit(generate_requests, int(arr[i]) + 1, cpu, mem, io))
        print(f'thread {i+1} started')
        time.sleep(quantum)

    while True:
        is_done = True
        for i in range(int(seconds/quantum)):
            if not thread_list[i].done():
                is_done = False
        if is_done:
            print("done")
            break
        else: time.sleep(1)
    pool.shutdown()


if __name__ == '__main__':
    start()
