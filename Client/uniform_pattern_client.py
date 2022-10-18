import time
from concurrent.futures import ThreadPoolExecutor

import grequests


def uniform_pattern(count):
    requests = [grequests.get("url") for i in range(count)]
    responses = grequests.map(requests)
    print(responses)


def start():
    seconds = 60
    i = 0
    pool = ThreadPoolExecutor(max_workers=seconds)
    thread_list = []
    while i < seconds:
        thread_list.append(pool.submit(uniform_pattern, 50))
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
