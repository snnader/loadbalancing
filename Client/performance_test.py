import uniform_pattern_client, tide_pattern_client, spike_pattern_client


if __name__ == "__main__":
    input("Start Load Balancer")
    uniform_pattern_client.start()
    input("Restart Load Balancer")
    tide_pattern_client.start()
    input("Restart Load Balancer")
    spike_pattern_client.start()
    # input("Restart Load Balancer")
    # uniform_pattern_client.start(freq=50, cpu=(6, 10), mem=(6, 10), io=(1, 1))
    # input("Restart Load Balancer")
    # uniform_pattern_client.start(freq=50, cpu=(6, 10), mem=(1, 1), io=(6, 10))
    # input("Restart Load Balancer")
    # uniform_pattern_client.start(freq=50, cpu=(1, 1), mem=(6, 10), io=(6, 10))
    print("All Tests Done")