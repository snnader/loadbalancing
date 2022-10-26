import time
from concurrent.futures import ThreadPoolExecutor
import numpy as np
from scipy.stats import norm

import grequests


def pattern(count):
    ## requests = [grequests.get('http://127.0.0.1:8001', params={'cpu': '5', 'io': '5', 'dur': '2'}) for i in range(count)]
    requests = [grequests.get("url") for i in range(count)]
    responses = grequests.map(requests)
    print(responses)


def start():
    seconds = 60
    i = 0
    pool = ThreadPoolExecutor(max_workers=seconds)
    thread_list = []
    gaussion_scale = 7.5
    x = np.arange(0, 60, 1)
    arr = (norm.pdf(x, loc=20, scale=gaussion_scale) + norm.pdf(x, loc=40, scale=gaussion_scale)) * 350
    while i < seconds:
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
