import time
from concurrent.futures import ThreadPoolExecutor
import numpy as np
from scipy.stats import chi2
from scipy.stats import norm

import grequests


def pattern(count):
    requests = [grequests.get("url") for i in range(count)]
    responses = grequests.map(requests)
    print(responses)


def start():
    seconds = 60
    i = 0
    pool = ThreadPoolExecutor(max_workers=seconds)
    thread_list = []
    x = np.arange(0, 60, 1)
    arr = (chi2.pdf(x, df=14) + 0.01)*400
    while i < seconds:
        if i < 5:
            thread_list.append(pool.submit(pattern, 4))
        else:
            thread_list.append(pool.submit(pattern, int(arr[i])))
        print(f'thread {i + 1} started')
        time.sleep(1)
        i += 1
    while True:
        is_done = True
        for i in range(seconds):
            if not thread_list[i].done():
                is_done = False
        if is_done:
            print("done")
            break
    pool.shutdown()


if __name__ == '__main__':
    start()
