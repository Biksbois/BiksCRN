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

class SampleA():
    sample = {
        "X":[0],
        "Y":[100]
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
            r0=1*(self.sample.get("Y")[-1]**2)
            r1=2*(self.sample.get("X")[-1]**2)

            self.sample["X"].append(self.sample.get("X")[-1]+(r0*(1)+r1*(-2))*self.h)
            self.sample["Y"].append(self.sample.get("Y")[-1]+(r0*(-2)+r1*(1))*self.h)

    def ApplyTitration(self,i):
        if self.sample.get("X")[-1]>3:
            Result, self.AddMol0 = self.AccTitration(self, 0.01, self.AddMol0)
            self.sample["X"][-1] = self.sample.get("X")[-1]+Result*1


    @staticmethod
    def Animate(i) :
        plt.cla()

        if(i <= SampleA.steps):
            SampleA.stepList.append(next(SampleA.index)*SampleA.h)

        for key, value in SampleA.sample.items():
            plt.plot(SampleA.stepList, value, label=key + " = {:.2f} ".format(SampleA.sample.get(key)[-1]))

        plt.xlabel('Time (t)')
        plt.ylabel('Concentration (mol/L)')
        plt.legend()

        if(i <= SampleA.steps):
            SampleA.Euler(SampleA, i)
            SampleA.ApplyTitration(SampleA, i+1)


class SampleB():
    sample = {
        "C":[0],
        "D":[10]
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

    RemMol0=0

    def Euler(self, i) :
        if(i < self.steps):
            r0=1*(self.sample.get("C")[-1]**1)
            r0=2*(self.sample.get("D")[-1]**1)
            r1=2*(self.sample.get("C")[-1]**2)

            self.sample["C"].append(self.sample.get("C")[-1]+(r0*(-1)+r0*(-1)+r1*(-2))*self.h)
            self.sample["D"].append(self.sample.get("D")[-1]+(r0*(1)+r0*(1)+r1*(2))*self.h)

    def ApplyTitration(self,i):
        Result, self.RemMol0 = self.AccTitration(self, 0.3, self.RemMol0)
        if Result > 0 and self.sample.get("D")[-1]-1 <= 0:
            self.sample.get("D")[-1] = 0
        elif Result > 0:
            self.sample["D"][-1] = self.sample.get("D")[-1]-Result*1


    @staticmethod
    def Animate(i) :
        plt.cla()

        if(i <= SampleB.steps):
            SampleB.stepList.append(next(SampleB.index)*SampleB.h)

        for key, value in SampleB.sample.items():
            plt.plot(SampleB.stepList, value, label=key + " = {:.2f} ".format(SampleB.sample.get(key)[-1]))

        plt.xlabel('Time (t)')
        plt.ylabel('Concentration (mol/L)')
        plt.legend()

        if(i <= SampleB.steps):
            SampleB.Euler(SampleB, i)
            SampleB.ApplyTitration(SampleB, i+1)


class SampleC():
    sample = {
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

    def Euler(self, i) :
        pass

    @staticmethod
    def Animate(i) :
        plt.cla()

        if(i <= SampleC.steps):
            SampleC.stepList.append(next(SampleC.index)*SampleC.h)

        for key, value in SampleC.sample.items():
            plt.plot(SampleC.stepList, value, label=key + " = {:.2f} ".format(SampleC.sample.get(key)[-1]))

        plt.xlabel('Time (t)')
        plt.ylabel('Concentration (mol/L)')
        plt.legend()

        if(i <= SampleC.steps):
            SampleC.Euler(SampleC, i)



equilibrate(SampleA, 5.0E-4, 100)
SampleC.sample = mix([SampleA.sample, SampleB.sample])
def Euler0(self, i) :
    if(i < self.steps):
        r0=1*(self.sample.get("Y")[-1]**2)
        r1=2*(self.sample.get("X")[-1]**2)
        r2=1*(self.sample.get("C")[-1]**1)
        r2=2*(self.sample.get("D")[-1]**1)
        r3=2*(self.sample.get("C")[-1]**2)

        self.sample["C"].append(self.sample.get("C")[-1]+(r2*(-1)+r2*(-1)+r3*(-2))*self.h)
        self.sample["D"].append(self.sample.get("D")[-1]+(r2*(1)+r2*(1)+r3*(2))*self.h)
        self.sample["X"].append(self.sample.get("X")[-1]+(r0*(1)+r1*(-2))*self.h)
        self.sample["Y"].append(self.sample.get("Y")[-1]+(r0*(-2)+r1*(1))*self.h)

SampleC.Euler = Euler0
def ApplyTitration0(self,i):
    if self.sample.get("X")[-1]>3:
        Result, self.AddMol0 = self.AccTitration(self, 0.01, self.AddMol0)
        self.sample["X"][-1] = self.sample.get("X")[-1]+Result*1
    Result, self.RemMol0 = self.AccTitration(self, 0.3, self.RemMol0)
    if Result > 0 and self.sample.get("D")[-1]-1 <= 0:
        self.sample.get("D")[-1] = 0
    elif Result > 0:
        self.sample["D"][-1] = self.sample.get("D")[-1]-Result*1

SampleC.ApplyTitration = ApplyTitration0
split(SampleC.sample,[SampleA.sample, SampleB.sample], [0.4, 0.5])
disposePercent(SampleA.sample,1)
disposePercent(SampleB.sample,0.5)
