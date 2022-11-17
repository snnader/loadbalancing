import time
from concurrent.futures import ThreadPoolExecutor
from request_gen import generate_requests

def start(freq=40, cpu=(1,10), mem=(1,10), io=(1,10)):
    quantum = 0.5
    seconds = 60
    pool = ThreadPoolExecutor(max_workers=int(seconds/quantum))
    thread_list = []

    # print("Uniform frequency:", freq)
    # return

    for i in range(int(seconds/quantum)):
        thread_list.append(pool.submit(generate_requests, int(freq * quantum), cpu, mem, io))
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
