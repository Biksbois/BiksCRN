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
    ani = FuncAnimation(plt.gcf(), sample.Animate, interval=2)
    plt.show()

class SampleQ():
    sample = {
        "A":[10],
        "U":[0]
    }

    temp = [
        10
    ]

    temp2 = [
        0
    ]

    temp3 = [
        10
    ]

    temp5 = [
        0
    ]

    def TitAccumilationA(self, act, titra):
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
        if(i < self.steps):
            # r1=1*(self.sample.get("A")[-1]**2)*(self.sample.get("O")[-1])
            
            # #O+2A -> U +2*O
            # #self.temp3.append(self.temp[-1])
            # #self.temp5.append(self.temp2[-1])

            # self.temp.append(self.temp[-1]+(r1)*self.h)
            # self.temp2.append(self.temp2[-1]+(r2)*self.h)

            # self.sample["A"].append(self.temp[-1]*-2)
            # self.sample["U"].append(self.temp2[-1]*1)
            # self.sample["O"].append(self.temp2[-1]*-1)

            lhs1=1*(self.sample.get("A")[-1]**3)
            

            #O+2A -> U +2*O
            #self.temp3.append(self.temp[-1])
            #self.temp5.append(self.temp2[-1])

            #self.temp.append(self.temp[-1]+(r1)*self.h)
            #self.temp2.append(self.temp2[-1]+(r1)*self.h)

            self.sample["A"].append(self.sample.get("A")[-1]+lhs1*-3*self.h)
            self.sample["U"].append(self.sample.get("U")[-1]+lhs1*1*self.h)


    @staticmethod
    def Animate(i) :
        plt.cla()

        if(i <= SampleQ.steps):
            SampleQ.stepList.append(next(SampleQ.index)*SampleQ.h)

        for key, value in SampleQ.sample.items():
            plt.plot(SampleQ.stepList, value,'bo', label=key +"  = " +str(SampleQ.sample.get(key)[-1]))
            plt.legend()

        if(i <= SampleQ.steps):
            SampleQ.Euler(SampleQ, i)

class SampleL():
    sample = {
        "A":[100],
        "C":[80],
        "D":[70],
        "U":[1],
        "E":[60]
    }

    def TitAccumilationA(self, act, titra):
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
    AddMol1=0
    AddMol2=0
    AddMol3=0
    AddMol4=0

    RemMol0=0
    RemMol1=0
    RemMol2=0
    RemMol3=0
    RemMol4=0

    def Euler(self, i) :
        if(i < self.steps):
            r1=1*self.sample.get("A")[-1]
            r1=1*self.sample.get("A")[-1]*self.sample.get("C")[-1]
            r1=2*self.sample.get("D")[-1]
            r1=2*self.sample.get("D")[-1]
            r1=1*self.sample.get("U")[-1]*self.sample.get("E")[-1]
            r1=1*self.sample.get("U")[-1]*self.sample.get("E")[-1]
            r1=3*self.sample.get("A")[-1]*self.sample.get("C")[-1]
            r1=3*self.sample.get("E")[-1]

            self.sample["A"].append((-r1-r1+r1+r1-r1)*self.h+self.sample.get("A")[-1])
            self.sample["C"].append((-r1+r1+r1-r1+r1)*self.h+self.sample.get("C")[-1])
            self.sample["D"].append((r1-r1-r1+r1)*self.h+self.sample.get("D")[-1])
            self.sample["U"].append((r1+r1-r1-r1+r1)*self.h+self.sample.get("U")[-1])
            self.sample["E"].append((r1-r1-r1+r1-r1)*self.h+self.sample.get("E")[-1])

    def ApplyTitration(self,i):
        Result, self.AddMol0 = self.TitAccumilationA(self, 1, self.AddMol0)
        self.sample["U"][-1] = self.sample.get("U")[-1]+Result*1
        Result, self.AddMol1 = self.TitAccumilationA(self, 1, self.AddMol1)
        self.sample["U"][-1] = self.sample.get("U")[-1]+Result*1
        Result, self.AddMol2 = self.TitAccumilationA(self, 1, self.AddMol2)
        self.sample["U"][-1] = self.sample.get("U")[-1]+Result*1
        Result, self.AddMol3 = self.TitAccumilationA(self, 1, self.AddMol3)
        self.sample["U"][-1] = self.sample.get("U")[-1]+Result*1
        Result, self.AddMol4 = self.TitAccumilationA(self, 1, self.AddMol4)
        self.sample["U"][-1] = self.sample.get("U")[-1]+Result*1
        if self.sample.get("U")[-1]-1 <= 0:
            self.sample.get("U")[-1] = 0
        else:
            Result, self.RemMol0 = self.TitAccumilationA(self, 1, self.RemMol0)
            self.sample["U"][-1] = self.sample.get("U")[-1]-Result*1
        if self.sample.get("U")[-1]-1 <= 0:
            self.sample.get("U")[-1] = 0
        else:
            Result, self.RemMol1 = self.TitAccumilationA(self, 1, self.RemMol1)
            self.sample["U"][-1] = self.sample.get("U")[-1]-Result*1
        if self.sample.get("U")[-1]+1/self.sample.get("U")[-1]>9:
            if self.sample.get("U")[-1]-1 <= 0:
                self.sample.get("U")[-1] = 0
            else:
                Result, self.RemMol2 = self.TitAccumilationA(self, 1, self.RemMol2)
                self.sample["U"][-1] = self.sample.get("U")[-1]-Result*1
        if self.sample.get("U")[-1]-1 <= 0:
            self.sample.get("U")[-1] = 0
        else:
            Result, self.RemMol3 = self.TitAccumilationA(self, 1, self.RemMol3)
            self.sample["U"][-1] = self.sample.get("U")[-1]-Result*1
        if self.sample.get("U")[-1]<6:
            if self.sample.get("U")[-1]-1 <= 0:
                self.sample.get("U")[-1] = 0
            else:
                Result, self.RemMol4 = self.TitAccumilationA(self, 1, self.RemMol4)
                self.sample["U"][-1] = self.sample.get("U")[-1]-Result*1


    @staticmethod
    def Animate(i) :
        plt.cla()

        if(i <= SampleL.steps):
            SampleL.stepList.append(next(SampleL.index)*SampleL.h)

        for key, value in SampleL.sample.items():
            plt.plot(SampleL.stepList, value,'b:', label=key)
            plt.legend()

        if(i <= SampleL.steps):
            SampleL.Euler(SampleL, i)
            SampleL.ApplyTitration(SampleL, i+1)


class Sampleempty():
    sample = {
    }

    def TitAccumilationA(self, act, titra):
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

        if(i <= Sampleempty.steps):
            Sampleempty.stepList.append(next(Sampleempty.index)*Sampleempty.h)

        for key, value in Sampleempty.sample.items():
            plt.plot(Sampleempty.stepList, value,'bo', label=key)
            plt.legend()

        if(i <= Sampleempty.steps):
            Sampleempty.Euler(Sampleempty, i)



sample = {
    "II":[1],
    "A":[100],
    "B":[12]
}
equilibrate(SampleQ, 5.0E-4, 1000)
