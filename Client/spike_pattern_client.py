import time
from concurrent.futures import ThreadPoolExecutor
import numpy as np
from scipy.stats import chi2
import matplotlib.pyplot as plt
import grequests

def pattern(count):
    requests = [grequests.get('http://127.0.0.1:8001', params={'cpu': '5', 'mem': '5', 'io': '5', 'dur': '2'}) for _ in range(count)]
    responses = grequests.map(requests)
    print(count)

def start():
    seconds = 60
    peak_freq = 40
    pool = ThreadPoolExecutor(max_workers=seconds)
    thread_list = []
    time_arr = np.arange(0,seconds,1)
    chi2_freq = (chi2.pdf(time_arr, df=14) + 0.01) * 11.074 * peak_freq
    print(chi2_freq)
    # plt.plot(time_arr, chi2_freq)
    # plt.show()
    for i in range(seconds):
        thread_list.append(pool.submit(pattern, int(chi2_freq[i])))
        print(f'thread {i+1} started')
        time.sleep(1)
    while True:
        is_done = True
        for i in range(seconds):
            if not thread_list[i].done():
                is_done = False
        if is_done:
            print('done')
            break
    pool.shutdown()

if __name__ == '__main__':
    start()


