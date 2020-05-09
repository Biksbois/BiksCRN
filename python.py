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
    plt.figure(figsize=(12, 7),dpi=80, num='BiksCRN')
    ani = FuncAnimation(plt.gcf(), sample.Animate, interval=50)
    plt.show()

def GetPercent(i, steps):
    result = (i/steps)*100
    if result > 100:
        return "100"
    else:
        return "{:.2f}".format(result)

def SaveGraph(Sample, name, taken):
    if len(next(iter(Sample.sample.values()))) != len(Sample.stepList):
        Sample.stepList.append(Sample.stepList[-1]+Sample.h)
    rSpecies = Sample.sample.copy()
    rSteps = Sample.stepList
    for key in Sample.sample:
        Sample.sample[key] = [Sample.sample.get(key)[-1]]
    Sample.stepList = []
    return rSpecies, rSteps, name, taken

def DrawGraph(Species, Steps, name, i, s):
        for key, value in Species.items():
            plt.plot(Steps, value, label=key + " = {:.2f} mol/L".format(Species.get(key)[-1]))
        plt.xlabel('Time (t)')
        plt.suptitle("Equilibrating sample " + name + " for " + str(len(Steps)) + " cycles (" + GetPercent(i, s) + "%)", fontsize=12)
        plt.ylabel('Concentration (mol/L)')
        
        plt.legend()

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
        if self.sample.get("X")[-1]<3:
            Result, self.AddMol0 = self.AccTitration(self, 0.001, self.AddMol0)
            self.sample["X"][-1] = self.sample.get("X")[-1]+Result*1


    @staticmethod
    def Animate(i) :
        plt.cla()

        if(i <= SampleA.steps):
            SampleA.stepList.append(next(SampleA.index)*SampleA.h)

        DrawGraph(SampleA.sample, SampleA.stepList, "A", i, SampleA.steps)

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

        DrawGraph(SampleB.sample, SampleB.stepList, "A", i, SampleB.steps)

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
    def ApplyTitration(self,i):
        pass

    @staticmethod
    def Animate(i) :
        plt.cla()

        if(i <= SampleC.steps):
            SampleC.stepList.append(next(SampleC.index)*SampleC.h)

        DrawGraph(SampleC.sample, SampleC.stepList, "A", i, SampleC.steps)

        if(i <= SampleC.steps):
            SampleC.Euler(SampleC, i)
            SampleC.ApplyTitration(SampleC, i+1)


equilibrate(SampleA, 5.0E-4, 100)
Species0, Steps0, name0, taken0 = SaveGraph(SampleA, "A", SampleA.steps )
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
    if self.sample.get("X")[-1]<3:
        Result, self.AddMol0 = self.AccTitration(self, 0.001, self.AddMol0)
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
DrawGraph(Species0, Steps0, name0)

plt.show()