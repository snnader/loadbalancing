import uniform_pattern_client, tide_pattern_client, spike_pattern_client


if __name__ == "__main__":
    input("Start Load Balancer")
    uniform_pattern_client.start()
    input("Restart Load Balancer")
    tide_pattern_client.start()
    input("Restart Load Balancer")
    spike_pattern_client.start()
    # input("Restart Load Balancer")
    # input("Restart Load Balancer")
    # input("Restart Load Balancer")
    # input("Restart Load Balancer")
    print("All Tests Done")