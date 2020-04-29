import numpy as np 
from scipy.integrate import odeint
import matplotlib.pyplot as plt
from matplotlib.animation import FuncAnimation
from itertools import count
from threading import Timer
import math
import warnings

def combinedPercent(percentList):
    result = 0
    for number in percentList:
        result += number
    return result

def split(sample, sampleList, percentList):

    for key, value in sample.items():
        for i in range(0, len(sampleList)):
            sampleList[i][key] = [math.floor(value[-1] * percentList[i])]

    return sample, sampleList

def disposePercent(sample, percent):
    for key in sample.keys():
        sample[key] = [math.ceil(sample.get(key)[-1] * (1 - percent))]
    return sample


def dispose(sample):
    for key in sample.keys():
        sample[key] = [0]
    return sample
 
def mix(sampleList):
    rSample = {}

    for sample in sampleList:
        for key in sample.keys():
            if key in rSample:
                rSample[key][-1] += sample.get(key)[-1]
            else :
                rSample[key] = [sample.get(key)[-1]]

    for sample in sampleList:
        sample = dispose(sample)
    
    return rSample

def equilibrate(sample, stepsize, times):
    sample.h = stepsize
    sample.steps = times
    ani = FuncAnimation(plt.gcf(), sample.Animate, interval=50)
    plt.show()

class SampleQ():
    sample = {
        "A":[0],
        "X":[10]
    }

    def AccTitration(self, act, titra):
        titra += self.h
        if(act <= titra):
            result = math.floor(titra/act)
            titra = titra - act * result
            return result, titra
        else:
            return 0, titra

    stepList = []
    index = count()
    steps = 100
    h = 0.0025

    AddMol0=0

    def Euler(self, i) :
        if(i < self.steps):
            r0=1*(self.sample.get("A")[-1]**1)
            r1=2*(self.sample.get("X")[-1]**3)

            self.sample["A"].append(self.sample.get("A")[-1]+(r0*(-1)+r1*(1))*self.h)
            self.sample["X"].append(self.sample.get("X")[-1]+(r0*(1)+r1*(-3))*self.h)

    def ApplyTitration(self,i):
        if self.sample.get("X")[-1]<2:
            Result, self.AddMol0 = self.AccTitration(self, 0.1, self.AddMol0)
            self.sample["X"][-1] = self.sample.get("X")[-1]+Result*1


    @staticmethod
    def Animate(i) :
        plt.cla()

        if(i <= SampleQ.steps):
            SampleQ.stepList.append(next(SampleQ.index)*SampleQ.h)

        for key, value in SampleQ.sample.items():
            plt.plot(SampleQ.stepList, value, label=key + " = {:.2f} ".format(SampleQ.sample.get(key)[-1]))

        plt.xlabel('Time (t)')
        plt.ylabel('Concentration (mol/L)')
        plt.legend()

        if(i <= SampleQ.steps):
            SampleQ.Euler(SampleQ, i)
            SampleQ.ApplyTitration(SampleQ, i+1)


equilibrate(SampleQ, 5.0E-4, 20000)
