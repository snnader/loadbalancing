import bar_spike, bar_tide, bar_uniform, line_spike, line_tide, line_uniform
import matplotlib as plt

if __name__ == '__main__':
    plt.rcParams.update({'font.family':'serif'})
    plt.rcParams.update({'font.serif':'Times New Roman'})
    bar_spike.bar()
    bar_tide.bar()
    bar_uniform.bar()
    line_spike.plot()
    line_tide.plot()
    line_uniform.plot()