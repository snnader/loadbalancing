from random import Random
import grequests

rand = Random()

LoadBalancerIP = '127.0.0.1'

def generate_requests(count, cpu, mem, io):
    requests = [
        grequests.get(
            'http://' + LoadBalancerIP + ':8001', 
            params={
                'cpu': rand.randint(cpu[0], cpu[1]), 
                'mem': rand.randint(mem[0], mem[1]), 
                'io': rand.randint(io[0], io[1])
            }
        ) 
        for _ in range(count)
    ]
    grequests.map(requests, gtimeout=10)
    print(count)
