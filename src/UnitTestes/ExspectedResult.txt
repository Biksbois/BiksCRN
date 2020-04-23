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
    ani = FuncAnimation(plt.gcf(), sample.Animate, interval=1000)
    plt.show()

class SampleQ():
    sample = {
        "A":[100],
        "B":[90],
        "C":[80],
        "D":[70],
        "E":[60]
    }

    stepList = []
    index = count()
    ft = 100
    t = np.arange(0,ft + 0.5,1)
    n = len(t)
    h = 0.0025
    def Euler(self, i) :
        r1=2*i**(1)+1/2*self.sample.get("A")[-1]
        r2=1*self.sample.get("B")[-1]
        r3=1*self.sample.get("A")[-1]*self.sample.get("C")[-1]
        r4=1*self.sample.get("D")[-1]
        r5=1*self.sample.get("B")[-1]*self.sample.get("E")[-1]
        r6=1*self.sample.get("B")[-1]*self.sample.get("E")[-1]
        r7=1*self.sample.get("E")[-1]

        self.sample["A"].append((-r1+r2-r3+r6)*self.h+self.sample.get("A")[-1])
        self.sample["B"].append((r1-r2+r4-r5-r6)*self.h+self.sample.get("B")[-1])
        self.sample["C"].append((-r3+r6+r7)*self.h+self.sample.get("C")[-1])
        self.sample["D"].append((r3-r4+r5)*self.h+self.sample.get("D")[-1])
        self.sample["E"].append((r4-r5-r6-r7)*self.h+self.sample.get("E")[-1])


    @staticmethod
    def Animate(i) :
        plt.cla()

        SampleQ.stepList.append(next(SampleQ.index))

        for key, value in SampleQ.sample.items():
            plt.plot(SampleQ.stepList, value, label=key)
            plt.legend()

        SampleQ.Euler(SampleQ, i)



class SampleL():
    sample = {
        "A":[100],
        "C":[80],
        "D":[70],
        "U":[1],
        "E":[60]
    }

    stepList = []
    index = count()
    ft = 100
    t = np.arange(0,ft + 0.5,1)
    n = len(t)
    h = 0.0025
    def Euler(self, i) :
        r1=1*self.sample.get("A")[-1]
        r2=1*self.sample.get("A")[-1]*self.sample.get("C")[-1]
        r3=2*self.sample.get("D")[-1]
        r4=2*self.sample.get("D")[-1]
        r5=1*self.sample.get("U")[-1]*self.sample.get("E")[-1]
        r6=1*self.sample.get("U")[-1]*self.sample.get("E")[-1]
        r7=3*self.sample.get("A")[-1]*self.sample.get("C")[-1]
        r8=3*self.sample.get("E")[-1]

        self.sample["A"].append((-r1-r2+r3+r6-r7)*self.h+self.sample.get("A")[-1])
        self.sample["C"].append((-r2+r3+r6-r7+r8)*self.h+self.sample.get("C")[-1])
        self.sample["D"].append((r2-r3-r4+r5)*self.h+self.sample.get("D")[-1])
        self.sample["U"].append((r1+r4-r5-r6+r7)*self.h+self.sample.get("U")[-1])
        self.sample["E"].append((r4-r5-r6+r7-r8)*self.h+self.sample.get("E")[-1])

    def ApplyTitration(self,i):
        if i % 20 == 0 :
            self.sample["U"][-1] = self.sample.get("U")[-1]+1
        if i % 45 == 0  and self.sample.get("U")[-1]<5 and 6<7 or 1==1:
            self.sample["U"][-1] = self.sample.get("U")[-1]+1
        if i % 1 == 0 :
            if self.sample.get("U")[-1]-1 <= 0:
                self.sample.get("U")[-1] = 0
            else:
                self.sample["U"][-1] = self.sample.get("U")[-1]-1
        if i % 2 == 0 :
            if self.sample.get("U")[-1]-1 <= 0:
                self.sample.get("U")[-1] = 0
            else:
                self.sample["U"][-1] = self.sample.get("U")[-1]-1
        if i % 3 == 0  and self.sample.get("U")[-1]<5 and 6<7 or 1==1:
            if self.sample.get("U")[-1]-1 <= 0:
                self.sample.get("U")[-1] = 0
            else:
                self.sample["U"][-1] = self.sample.get("U")[-1]-1

    @staticmethod
    def Animate(i) :
        plt.cla()

        SampleL.stepList.append(next(SampleL.index))

        for key, value in SampleL.sample.items():
            plt.plot(SampleL.stepList, value, label=key)
            plt.legend()

        SampleL.Euler(SampleL, i)
        SampleL.ApplyTitration(SampleL, i+1)


sample = {
    "II":[1],
    "A":[100],
    "B":[12]
}
equilibrate(SampleQ, 0.0025, 1)
sample["A"] = [SampleQ.sample.get("A")[-1]]
equilibrate(SampleL, 0.0025, 1)
SampleQ.sample = mix([SampleQ.sample])
def Euler0(self, i) :
    r1=2*i**(1)+1/2*self.sample.get("A")[-1]
    r2=1*self.sample.get("B")[-1]
    r3=1*self.sample.get("A")[-1]*self.sample.get("C")[-1]
    r4=1*self.sample.get("D")[-1]
    r5=1*self.sample.get("B")[-1]*self.sample.get("E")[-1]
    r6=1*self.sample.get("B")[-1]*self.sample.get("E")[-1]
    r7=1*self.sample.get("E")[-1]
    r8=2*i**(1)+1/2*self.sample.get("A")[-1]
    r9=1*self.sample.get("B")[-1]
    r10=1*self.sample.get("A")[-1]*self.sample.get("C")[-1]
    r11=1*self.sample.get("D")[-1]
    r12=1*self.sample.get("B")[-1]*self.sample.get("E")[-1]
    r13=1*self.sample.get("B")[-1]*self.sample.get("E")[-1]
    r14=1*self.sample.get("E")[-1]

    self.sample["A"].append((-r8+r9-r10+r13-r8+r9-r10+r13)*self.h+self.sample.get("A")[-1])
    self.sample["B"].append((r8-r9+r11-r12-r13+r8-r9+r11-r12-r13)*self.h+self.sample.get("B")[-1])
    self.sample["C"].append((-r10+r13+r14-r10+r13+r14)*self.h+self.sample.get("C")[-1])
    self.sample["D"].append((r10-r11+r12+r10-r11+r12)*self.h+self.sample.get("D")[-1])
    self.sample["E"].append((r11-r12-r13-r14+r11-r12-r13-r14)*self.h+self.sample.get("E")[-1])
SampleQ.Euler = Euler0
split(SampleQ.sample,[SampleQ.sample, SampleL.sample], [0.5, 0.5])
disposePercent(SampleQ.sample,1)
