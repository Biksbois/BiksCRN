import numpy as np 
from scipy.integrate import odeint
import matplotlib.pyplot as plt
from matplotlib.animation import FuncAnimation
from itertools import count
from threading import Timer
import math
import warnings
def split( SplitSample,ResultingSampleList, Distribution):
    rSample = {} #resulting sample list

    for key in SplitSample.keys():
        for i in range(0,len(ResultingSampleList)):
            if key in ResultingSampleList[i]:
                ResultingSampleList[i][key] = [SplitSample.get(key)[-1]*Distribution[i]+ResultingSampleList[i][key][-1]]
            else:
                ResultingSampleList[i][key] = [SplitSample.get(key)[-1]*Distribution[i]]

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

    
    return rSample

def equilibrate(sample, stepsize, times, timeInterval, bitesize):
    sample.h = stepsize
    sample.steps = times
    sample.bitesize = bitesize
    plt.figure(figsize=(12, 7),dpi=80, num='BiksCRN')
    ani = FuncAnimation(plt.gcf(), sample.Animate, interval=timeInterval)
    plt.show()

def GetPercent(i, steps):
    result = (i/steps)*100
    if result > 100:
        return "100"
    else:
        return "{:.2f}".format(result)

def SaveGraph(Sample, name, taken):
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

def AddAtrribute(obj, atr):
    for str in atr:
        setattr(obj,str,0)
class SampleA():
    sample = {
        "a":[10],
        "ab":[0],
        "b":[10],
        "ac":[0],
        "bd":[0],
        "c":[10]
    }

    def AccTitration(self, act, time):
        time += self.h
        if(act <= time):
            result = math.floor(time/act)
            time = time - act * result
            return result, time
        else:
            return 0, time

    stepList = []
    index = count()
    steps = 100
    h = 0.0025
    bitesize = 1


    AddMol0=0
    AddMol1=0
    AddMol2=0

    RemMol0=0

    def Euler(self, i) :
        if(i < self.steps):
            r0=1*(self.sample.get("a")[-1]**1)*(self.sample.get("b")[-1]**1)
            r1=1*(self.sample.get("ab")[-1]**1)
            r2=1*(self.sample.get("ab")[-1]**1)*(self.sample.get("c")[-1]**1)
            r3=1*(self.sample.get("ab")[-1]**1)*(self.sample.get("ac")[-1]**1)

            self.sample["a"].append(self.sample.get("a")[-1]+(r0*(-1)+r1*(1))*self.h)
            self.sample["ab"].append(self.sample.get("ab")[-1]+(r0*(1)+r1*(-1)+r2*(-1)+r3*(-1))*self.h)
            self.sample["b"].append(self.sample.get("b")[-1]+(r0*(-1)+r1*(1)+r2*(1))*self.h)
            self.sample["ac"].append(self.sample.get("ac")[-1]+(r2*(1)+r3*(0)+r3*(0))*self.h)
            self.sample["bd"].append(self.sample.get("bd")[-1]+(r3*(1))*self.h)
            self.sample["c"].append(self.sample.get("c")[-1]+(r2*(-1))*self.h)

    def ApplyTitration(self,i):
        if(i < self.steps):
            if self.sample.get("a")[-1]<1.0:
                Result, self.AddMol0 = self.AccTitration(self, 0.1, self.AddMol0)
                self.sample["a"][-1] = self.sample.get("a")[-1]+Result*1
            if (self.sample.get("ab")[-1])*(-1)+self.sample.get("b")[-1]<0.1:
                Result, self.AddMol1 = self.AccTitration(self, 0.5, self.AddMol1)
                self.sample["c"][-1] = self.sample.get("c")[-1]+Result*1
            if self.sample.get("c")[-1]>3.0:
                Result, self.AddMol2 = self.AccTitration(self, 0.2, self.AddMol2)
                self.sample["ab"][-1] = self.sample.get("ab")[-1]+Result*1
            if self.sample.get("bd")[-1]>=7.0 and self.sample.get("ac")[-1]>-1.0+self.sample.get("bd")[-1]:
                Result, self.RemMol0 = self.AccTitration(self, 0.2, self.RemMol0)
                if Result > 0 and self.sample.get("bd")[-1]-1 <= 0:
                    self.sample.get("bd")[-1] = 0
                elif Result > 0:
                    self.sample["bd"][-1] = self.sample.get("bd")[-1]-Result*1


    @staticmethod
    def Animate(i) :
        plt.cla()
        for i in range(SampleA.bitesize):
            index = next(SampleA.index)
            if len(SampleA.stepList) == 0:
                SampleA.stepList.append(index*SampleA.h)
            else:
                SampleA.stepList.append(index*SampleA.h)
                SampleA.Euler(SampleA, index)
                SampleA.ApplyTitration(SampleA,index)
                if(index >= SampleA.steps):
                    SampleA.stepList.pop()
                    break

        DrawGraph(SampleA.sample, SampleA.stepList, "A", index, SampleA.steps)
class SampleB():
    sample = {
        "Br":[10],
        "bd":[0],
        "H":[10],
        "gd":[10]
    }

    def AccTitration(self, act, time):
        time += self.h
        if(act <= time):
            result = math.floor(time/act)
            time = time - act * result
            return result, time
        else:
            return 0, time

    stepList = []
    index = count()
    steps = 100
    h = 0.0025
    bitesize = 1


    RemMol0=0
    RemMol1=0
    RemMol2=0

    def Euler(self, i) :
        if(i < self.steps):
            r0=0.1 *(self.sample.get("Br")[-1]**1)
            r1=1.0 *(self.sample.get("Br")[-1]**2)
            r2=0.1*(self.sample.get("Br")[-1]**1)*(self.sample.get("H")[-1]**1)
            r3=0.3 *(self.sample.get("H")[-1]**1)*(self.sample.get("Br")[-1]**1)
            r4=0.005*(self.sample.get("H")[-1]**2)*(self.sample.get("gd")[-1]**1)

            self.sample["Br"].append(self.sample.get("Br")[-1]+(r0*(1)+r0*(1)+r1*(1*(-1))+r1*(1*(-1))+r2*(-1)+r3*(0)+r3*(0)+r4*(1))*self.h)
            self.sample["bd"].append(self.sample.get("bd")[-1]+(r2*(1)+r3*(1))*self.h)
            self.sample["H"].append(self.sample.get("H")[-1]+(r2*(0)+r2*(0)+r3*(-1)+r4*(1)+r4*(1))*self.h)
            self.sample["gd"].append(self.sample.get("gd")[-1]+(r4*(-1))*self.h)

    def ApplyTitration(self,i):
        if(i < self.steps):
            if self.sample.get("H")[-1]>15.0:
                Result, self.RemMol0 = self.AccTitration(self, 0.1, self.RemMol0)
                if Result > 0 and self.sample.get("H")[-1]-1 <= 0:
                    self.sample.get("H")[-1] = 0
                elif Result > 0:
                    self.sample["H"][-1] = self.sample.get("H")[-1]-Result*1
            if self.sample.get("H")[-1]>20.0:
                Result, self.RemMol1 = self.AccTitration(self, 0.08, self.RemMol1)
                if Result > 0 and self.sample.get("H")[-1]-1 <= 0:
                    self.sample.get("H")[-1] = 0
                elif Result > 0:
                    self.sample["H"][-1] = self.sample.get("H")[-1]-Result*1
            if self.sample.get("H")[-1]>25.0:
                Result, self.RemMol2 = self.AccTitration(self, 0.1, self.RemMol2)
                if Result > 0 and self.sample.get("H")[-1]-1 <= 0:
                    self.sample.get("H")[-1] = 0
                elif Result > 0:
                    self.sample["H"][-1] = self.sample.get("H")[-1]-Result*1


    @staticmethod
    def Animate(i) :
        plt.cla()
        for i in range(SampleB.bitesize):
            index = next(SampleB.index)
            if len(SampleB.stepList) == 0:
                SampleB.stepList.append(index*SampleB.h)
            else:
                SampleB.stepList.append(index*SampleB.h)
                SampleB.Euler(SampleB, index)
                SampleB.ApplyTitration(SampleB,index)
                if(index >= SampleB.steps):
                    SampleB.stepList.pop()
                    break

        DrawGraph(SampleB.sample, SampleB.stepList, "B", index, SampleB.steps)
class SampleC():
    sample = {
        "a":[0],
        "bd":[0]
    }

    def AccTitration(self, act, time):
        time += self.h
        if(act <= time):
            result = math.floor(time/act)
            time = time - act * result
            return result, time
        else:
            return 0, time

    stepList = []
    index = count()
    steps = 100
    h = 0.0025
    bitesize = 1


    def Euler(self, i) :
        if(i < self.steps):
            r0=i*0.001*(self.sample.get("bd")[-1]**1)

            self.sample["a"].append(self.sample.get("a")[-1]+(r0*(1))*self.h)
            self.sample["bd"].append(self.sample.get("bd")[-1]+(r0*(-1))*self.h)

    def ApplyTitration(self,i):
        pass

    @staticmethod
    def Animate(i) :
        plt.cla()
        for i in range(SampleC.bitesize):
            index = next(SampleC.index)
            if len(SampleC.stepList) == 0:
                SampleC.stepList.append(index*SampleC.h)
            else:
                SampleC.stepList.append(index*SampleC.h)
                SampleC.Euler(SampleC, index)
                SampleC.ApplyTitration(SampleC,index)
                if(index >= SampleC.steps):
                    SampleC.stepList.pop()
                    break

        DrawGraph(SampleC.sample, SampleC.stepList, "C", index, SampleC.steps)
sample = {
    "bd":[0],
    "gd":[10]
}
SampleA.index = count()
equilibrate(SampleA, 0.005, 1800.0, 1, 1800 )
Species0, Steps0, name0, taken0 = SaveGraph(SampleA, "A", SampleA.steps )
SampleB.sample["bd"] = sample["bd"]
SampleB.sample["gd"] = sample["gd"]
SampleB.index = count()
equilibrate(SampleB, 5.0E-4, 11000, 1, 11000 )
sample["bd"] = [SampleB.sample.get("bd")[-1]]
sample["gd"] = [SampleB.sample.get("gd")[-1]]
Species1, Steps1, name1, taken1 = SaveGraph(SampleB, "B", SampleB.steps )
SampleC.sample["bd"] = sample["bd"]
SampleC.index = count()
equilibrate(SampleC, 0.0025, 1600, 1, 1600 )
sample["bd"] = [SampleC.sample.get("bd")[-1]]
Species2, Steps2, name2, taken2 = SaveGraph(SampleC, "C", SampleC.steps )
SampleC.sample = mix([SampleA.sample, SampleB.sample,SampleC.sample])
def Euler0(self, i) :
    if(i < self.steps):
        r0=1*(self.sample.get("a")[-1]**1)*(self.sample.get("b")[-1]**1)
        r1=1*(self.sample.get("ab")[-1]**1)
        r2=1*(self.sample.get("ab")[-1]**1)*(self.sample.get("c")[-1]**1)
        r3=1*(self.sample.get("ab")[-1]**1)*(self.sample.get("ac")[-1]**1)
        r4=0.1 *(self.sample.get("Br")[-1]**1)
        r5=1.0 *(self.sample.get("Br")[-1]**2)
        r6=0.1*(self.sample.get("Br")[-1]**1)*(self.sample.get("H")[-1]**1)
        r7=0.3 *(self.sample.get("H")[-1]**1)*(self.sample.get("Br")[-1]**1)
        r8=0.005*(self.sample.get("H")[-1]**2)*(self.sample.get("gd")[-1]**1)

        self.sample["Br"].append(self.sample.get("Br")[-1]+(r4*(1)+r4*(1)+r5*(1*(-1))+r5*(1*(-1))+r6*(-1)+r7*(0)+r7*(0)+r8*(1))*self.h)
        self.sample["a"].append(self.sample.get("a")[-1]+(r0*(-1)+r1*(1))*self.h)
        self.sample["ab"].append(self.sample.get("ab")[-1]+(r0*(1)+r1*(-1)+r2*(-1)+r3*(-1))*self.h)
        self.sample["b"].append(self.sample.get("b")[-1]+(r0*(-1)+r1*(1)+r2*(1))*self.h)
        self.sample["ac"].append(self.sample.get("ac")[-1]+(r2*(1)+r3*(0)+r3*(0))*self.h)
        self.sample["bd"].append(self.sample.get("bd")[-1]+(r3*(1)+r6*(1)+r7*(1))*self.h)
        self.sample["c"].append(self.sample.get("c")[-1]+(r2*(-1))*self.h)
        self.sample["H"].append(self.sample.get("H")[-1]+(r6*(0)+r6*(0)+r7*(-1)+r8*(1)+r8*(1))*self.h)
        self.sample["gd"].append(self.sample.get("gd")[-1]+(r8*(-1))*self.h)

SampleC.Euler = Euler0
def ApplyTitration0(self,i):
    if(i < self.steps):
        if self.sample.get("a")[-1]<1.0:
            Result, self.AddMol0 = self.AccTitration(self, 0.1, self.AddMol0)
            self.sample["a"][-1] = self.sample.get("a")[-1]+Result*1
        if (self.sample.get("ab")[-1])*(-1)+self.sample.get("b")[-1]<0.1:
            Result, self.AddMol1 = self.AccTitration(self, 0.5, self.AddMol1)
            self.sample["c"][-1] = self.sample.get("c")[-1]+Result*1
        if self.sample.get("c")[-1]>3.0:
            Result, self.AddMol2 = self.AccTitration(self, 0.2, self.AddMol2)
            self.sample["ab"][-1] = self.sample.get("ab")[-1]+Result*1
        if self.sample.get("bd")[-1]>=7.0 and self.sample.get("ac")[-1]>-1.0+self.sample.get("bd")[-1]:
            Result, self.RemMol0 = self.AccTitration(self, 0.2, self.RemMol0)
            if Result > 0 and self.sample.get("bd")[-1]-1 <= 0:
                self.sample.get("bd")[-1] = 0
            elif Result > 0:
                self.sample["bd"][-1] = self.sample.get("bd")[-1]-Result*1
        if self.sample.get("H")[-1]>15.0:
            Result, self.RemMol1 = self.AccTitration(self, 0.1, self.RemMol1)
            if Result > 0 and self.sample.get("H")[-1]-1 <= 0:
                self.sample.get("H")[-1] = 0
            elif Result > 0:
                self.sample["H"][-1] = self.sample.get("H")[-1]-Result*1
        if self.sample.get("H")[-1]>20.0:
            Result, self.RemMol2 = self.AccTitration(self, 0.08, self.RemMol2)
            if Result > 0 and self.sample.get("H")[-1]-1 <= 0:
                self.sample.get("H")[-1] = 0
            elif Result > 0:
                self.sample["H"][-1] = self.sample.get("H")[-1]-Result*1
        if self.sample.get("H")[-1]>25.0:
            Result, self.RemMol3 = self.AccTitration(self, 0.1, self.RemMol3)
            if Result > 0 and self.sample.get("H")[-1]-1 <= 0:
                self.sample.get("H")[-1] = 0
            elif Result > 0:
                self.sample["H"][-1] = self.sample.get("H")[-1]-Result*1
AddAtrribute(SampleC,["AddMol0","AddMol1","AddMol2","RemMol0","RemMol1","RemMol2","RemMol3"])
SampleC.ApplyTitration = ApplyTitration0
SampleC.sample["gd"] = sample["gd"]
SampleC.index = count()
equilibrate(SampleC, 5.0E-4, 7000, 1, 7000 )
sample["gd"] = [SampleC.sample.get("gd")[-1]]
Species3, Steps3, name3, taken3 = SaveGraph(SampleC, "C", SampleC.steps )
split(SampleC.sample, [SampleB.sample, SampleC.sample],[0.9,0.1])
def Euler1(self, i) :
    if(i < self.steps):
        r0=1*(self.sample.get("a")[-1]**1)*(self.sample.get("b")[-1]**1)
        r1=1*(self.sample.get("ab")[-1]**1)
        r2=1*(self.sample.get("ab")[-1]**1)*(self.sample.get("c")[-1]**1)
        r3=1*(self.sample.get("ab")[-1]**1)*(self.sample.get("ac")[-1]**1)
        r4=0.1 *(self.sample.get("Br")[-1]**1)
        r5=1.0 *(self.sample.get("Br")[-1]**2)
        r6=0.1*(self.sample.get("Br")[-1]**1)*(self.sample.get("H")[-1]**1)
        r7=0.3 *(self.sample.get("H")[-1]**1)*(self.sample.get("Br")[-1]**1)
        r8=0.005*(self.sample.get("H")[-1]**2)*(self.sample.get("gd")[-1]**1)
        r9=1*(self.sample.get("a")[-1]**1)*(self.sample.get("b")[-1]**1)
        r10=1*(self.sample.get("ab")[-1]**1)
        r11=1*(self.sample.get("ab")[-1]**1)*(self.sample.get("c")[-1]**1)
        r12=1*(self.sample.get("ab")[-1]**1)*(self.sample.get("ac")[-1]**1)
        r13=0.1 *(self.sample.get("Br")[-1]**1)
        r14=1.0 *(self.sample.get("Br")[-1]**2)
        r15=0.1*(self.sample.get("Br")[-1]**1)*(self.sample.get("H")[-1]**1)
        r16=0.3 *(self.sample.get("H")[-1]**1)*(self.sample.get("Br")[-1]**1)
        r17=0.005*(self.sample.get("H")[-1]**2)*(self.sample.get("gd")[-1]**1)

        self.sample["Br"].append(self.sample.get("Br")[-1]+(r13*(1)+r13*(1)+r14*(1*(-1))+r14*(1*(-1))+r15*(-1)+r16*(0)+r16*(0)+r17*(1)+r13*(1)+r13*(1)+r14*(1*(-1))+r14*(1*(-1))+r15*(-1)+r16*(0)+r16*(0)+r17*(1))*self.h)
        self.sample["a"].append(self.sample.get("a")[-1]+(r9*(-1)+r10*(1)+r9*(-1)+r10*(1))*self.h)
        self.sample["ab"].append(self.sample.get("ab")[-1]+(r9*(1)+r10*(-1)+r11*(-1)+r12*(-1)+r9*(1)+r10*(-1)+r11*(-1)+r12*(-1))*self.h)
        self.sample["b"].append(self.sample.get("b")[-1]+(r9*(-1)+r10*(1)+r11*(1)+r9*(-1)+r10*(1)+r11*(1))*self.h)
        self.sample["ac"].append(self.sample.get("ac")[-1]+(r11*(1)+r12*(0)+r12*(0)+r11*(1)+r12*(0)+r12*(0))*self.h)
        self.sample["bd"].append(self.sample.get("bd")[-1]+(r12*(1)+r15*(1)+r16*(1)+r12*(1)+r15*(1)+r16*(1))*self.h)
        self.sample["c"].append(self.sample.get("c")[-1]+(r11*(-1)+r11*(-1))*self.h)
        self.sample["H"].append(self.sample.get("H")[-1]+(r15*(0)+r15*(0)+r16*(-1)+r17*(1)+r17*(1)+r15*(0)+r15*(0)+r16*(-1)+r17*(1)+r17*(1))*self.h)
        self.sample["gd"].append(self.sample.get("gd")[-1]+(r17*(-1)+r17*(-1))*self.h)

SampleC.Euler = Euler1
def ApplyTitration2(self,i):
    if(i < self.steps):
        if self.sample.get("a")[-1]<1.0:
            Result, self.AddMol0 = self.AccTitration(self, 0.1, self.AddMol0)
            self.sample["a"][-1] = self.sample.get("a")[-1]+Result*1
        if (self.sample.get("ab")[-1])*(-1)+self.sample.get("b")[-1]<0.1:
            Result, self.AddMol1 = self.AccTitration(self, 0.5, self.AddMol1)
            self.sample["c"][-1] = self.sample.get("c")[-1]+Result*1
        if self.sample.get("c")[-1]>3.0:
            Result, self.AddMol2 = self.AccTitration(self, 0.2, self.AddMol2)
            self.sample["ab"][-1] = self.sample.get("ab")[-1]+Result*1
        if self.sample.get("bd")[-1]>=7.0 and self.sample.get("ac")[-1]>-1.0+self.sample.get("bd")[-1]:
            Result, self.AddMol3 = self.AccTitration(self, 0.2, self.AddMol3)
            self.sample["bd"][-1] = self.sample.get("bd")[-1]+Result*1
        if self.sample.get("H")[-1]>15.0:
            Result, self.AddMol4 = self.AccTitration(self, 0.1, self.AddMol4)
            self.sample["H"][-1] = self.sample.get("H")[-1]+Result*1
        if self.sample.get("H")[-1]>20.0:
            Result, self.AddMol5 = self.AccTitration(self, 0.08, self.AddMol5)
            self.sample["H"][-1] = self.sample.get("H")[-1]+Result*1
        if self.sample.get("H")[-1]>25.0:
            Result, self.AddMol6 = self.AccTitration(self, 0.1, self.AddMol6)
            self.sample["H"][-1] = self.sample.get("H")[-1]+Result*1
        if self.sample.get("bd")[-1]>=7.0 and self.sample.get("ac")[-1]>-1.0+self.sample.get("bd")[-1]:
            Result, self.RemMol0 = self.AccTitration(self, 0.2, self.RemMol0)
            if Result > 0 and self.sample.get("bd")[-1]-1 <= 0:
                self.sample.get("bd")[-1] = 0
            elif Result > 0:
                self.sample["bd"][-1] = self.sample.get("bd")[-1]-Result*1
        if self.sample.get("H")[-1]>15.0:
            Result, self.RemMol1 = self.AccTitration(self, 0.1, self.RemMol1)
            if Result > 0 and self.sample.get("H")[-1]-1 <= 0:
                self.sample.get("H")[-1] = 0
            elif Result > 0:
                self.sample["H"][-1] = self.sample.get("H")[-1]-Result*1
        if self.sample.get("H")[-1]>20.0:
            Result, self.RemMol2 = self.AccTitration(self, 0.08, self.RemMol2)
            if Result > 0 and self.sample.get("H")[-1]-1 <= 0:
                self.sample.get("H")[-1] = 0
            elif Result > 0:
                self.sample["H"][-1] = self.sample.get("H")[-1]-Result*1
        if self.sample.get("H")[-1]>25.0:
            Result, self.RemMol3 = self.AccTitration(self, 0.1, self.RemMol3)
            if Result > 0 and self.sample.get("H")[-1]-1 <= 0:
                self.sample.get("H")[-1] = 0
            elif Result > 0:
                self.sample["H"][-1] = self.sample.get("H")[-1]-Result*1
        if self.sample.get("a")[-1]<1.0:
            Result, self.RemMol4 = self.AccTitration(self, 0.1, self.RemMol4)
            if Result > 0 and self.sample.get("a")[-1]-1 <= 0:
                self.sample.get("a")[-1] = 0
            elif Result > 0:
                self.sample["a"][-1] = self.sample.get("a")[-1]-Result*1
        if (self.sample.get("ab")[-1])*(-1)+self.sample.get("b")[-1]<0.1:
            Result, self.RemMol5 = self.AccTitration(self, 0.5, self.RemMol5)
            if Result > 0 and self.sample.get("c")[-1]-1 <= 0:
                self.sample.get("c")[-1] = 0
            elif Result > 0:
                self.sample["c"][-1] = self.sample.get("c")[-1]-Result*1
        if self.sample.get("c")[-1]>3.0:
            Result, self.RemMol6 = self.AccTitration(self, 0.2, self.RemMol6)
            if Result > 0 and self.sample.get("ab")[-1]-1 <= 0:
                self.sample.get("ab")[-1] = 0
            elif Result > 0:
                self.sample["ab"][-1] = self.sample.get("ab")[-1]-Result*1
AddAtrribute(SampleC,["AddMol0","AddMol1","AddMol2","AddMol3","AddMol4","AddMol5","AddMol6","RemMol0","RemMol1","RemMol2","RemMol3","RemMol4","RemMol5","RemMol6"])
SampleC.ApplyTitration = ApplyTitration2
def Euler3(self, i) :
    if(i < self.steps):
        r0=0.1 *(self.sample.get("Br")[-1]**1)
        r1=1.0 *(self.sample.get("Br")[-1]**2)
        r2=0.1*(self.sample.get("Br")[-1]**1)*(self.sample.get("H")[-1]**1)
        r3=0.3 *(self.sample.get("H")[-1]**1)*(self.sample.get("Br")[-1]**1)
        r4=0.005*(self.sample.get("H")[-1]**2)*(self.sample.get("gd")[-1]**1)
        r5=1*(self.sample.get("a")[-1]**1)*(self.sample.get("b")[-1]**1)
        r6=1*(self.sample.get("ab")[-1]**1)
        r7=1*(self.sample.get("ab")[-1]**1)*(self.sample.get("c")[-1]**1)
        r8=1*(self.sample.get("ab")[-1]**1)*(self.sample.get("ac")[-1]**1)
        r9=0.1 *(self.sample.get("Br")[-1]**1)
        r10=1.0 *(self.sample.get("Br")[-1]**2)
        r11=0.1*(self.sample.get("Br")[-1]**1)*(self.sample.get("H")[-1]**1)
        r12=0.3 *(self.sample.get("H")[-1]**1)*(self.sample.get("Br")[-1]**1)
        r13=0.005*(self.sample.get("H")[-1]**2)*(self.sample.get("gd")[-1]**1)

        self.sample["Br"].append(self.sample.get("Br")[-1]+(r9*(1)+r9*(1)+r10*(1*(-1))+r10*(1*(-1))+r11*(-1)+r12*(0)+r12*(0)+r13*(1)+r9*(1)+r9*(1)+r10*(1*(-1))+r10*(1*(-1))+r11*(-1)+r12*(0)+r12*(0)+r13*(1))*self.h)
        self.sample["a"].append(self.sample.get("a")[-1]+(r5*(-1)+r6*(1))*self.h)
        self.sample["ab"].append(self.sample.get("ab")[-1]+(r5*(1)+r6*(-1)+r7*(-1)+r8*(-1))*self.h)
        self.sample["bd"].append(self.sample.get("bd")[-1]+(r11*(1)+r12*(1)+r8*(1)+r11*(1)+r12*(1))*self.h)
        self.sample["b"].append(self.sample.get("b")[-1]+(r5*(-1)+r6*(1)+r7*(1))*self.h)
        self.sample["ac"].append(self.sample.get("ac")[-1]+(r7*(1)+r8*(0)+r8*(0))*self.h)
        self.sample["c"].append(self.sample.get("c")[-1]+(r7*(-1))*self.h)
        self.sample["H"].append(self.sample.get("H")[-1]+(r11*(0)+r11*(0)+r12*(-1)+r13*(1)+r13*(1)+r11*(0)+r11*(0)+r12*(-1)+r13*(1)+r13*(1))*self.h)
        self.sample["gd"].append(self.sample.get("gd")[-1]+(r13*(-1)+r13*(-1))*self.h)

SampleB.Euler = Euler3
def ApplyTitration4(self,i):
    if(i < self.steps):
        if self.sample.get("bd")[-1]>=7.0 and self.sample.get("ac")[-1]>-1.0+self.sample.get("bd")[-1]:
            Result, self.AddMol0 = self.AccTitration(self, 0.2, self.AddMol0)
            self.sample["bd"][-1] = self.sample.get("bd")[-1]+Result*1
        if self.sample.get("H")[-1]>15.0:
            Result, self.AddMol1 = self.AccTitration(self, 0.1, self.AddMol1)
            self.sample["H"][-1] = self.sample.get("H")[-1]+Result*1
        if self.sample.get("H")[-1]>20.0:
            Result, self.AddMol2 = self.AccTitration(self, 0.08, self.AddMol2)
            self.sample["H"][-1] = self.sample.get("H")[-1]+Result*1
        if self.sample.get("H")[-1]>25.0:
            Result, self.AddMol3 = self.AccTitration(self, 0.1, self.AddMol3)
            self.sample["H"][-1] = self.sample.get("H")[-1]+Result*1
        if self.sample.get("H")[-1]>15.0:
            Result, self.RemMol0 = self.AccTitration(self, 0.1, self.RemMol0)
            if Result > 0 and self.sample.get("H")[-1]-1 <= 0:
                self.sample.get("H")[-1] = 0
            elif Result > 0:
                self.sample["H"][-1] = self.sample.get("H")[-1]-Result*1
        if self.sample.get("H")[-1]>20.0:
            Result, self.RemMol1 = self.AccTitration(self, 0.08, self.RemMol1)
            if Result > 0 and self.sample.get("H")[-1]-1 <= 0:
                self.sample.get("H")[-1] = 0
            elif Result > 0:
                self.sample["H"][-1] = self.sample.get("H")[-1]-Result*1
        if self.sample.get("H")[-1]>25.0:
            Result, self.RemMol2 = self.AccTitration(self, 0.1, self.RemMol2)
            if Result > 0 and self.sample.get("H")[-1]-1 <= 0:
                self.sample.get("H")[-1] = 0
            elif Result > 0:
                self.sample["H"][-1] = self.sample.get("H")[-1]-Result*1
        if self.sample.get("a")[-1]<1.0:
            Result, self.RemMol3 = self.AccTitration(self, 0.1, self.RemMol3)
            if Result > 0 and self.sample.get("a")[-1]-1 <= 0:
                self.sample.get("a")[-1] = 0
            elif Result > 0:
                self.sample["a"][-1] = self.sample.get("a")[-1]-Result*1
        if (self.sample.get("ab")[-1])*(-1)+self.sample.get("b")[-1]<0.1:
            Result, self.RemMol4 = self.AccTitration(self, 0.5, self.RemMol4)
            if Result > 0 and self.sample.get("c")[-1]-1 <= 0:
                self.sample.get("c")[-1] = 0
            elif Result > 0:
                self.sample["c"][-1] = self.sample.get("c")[-1]-Result*1
        if self.sample.get("c")[-1]>3.0:
            Result, self.RemMol5 = self.AccTitration(self, 0.2, self.RemMol5)
            if Result > 0 and self.sample.get("ab")[-1]-1 <= 0:
                self.sample.get("ab")[-1] = 0
            elif Result > 0:
                self.sample["ab"][-1] = self.sample.get("ab")[-1]-Result*1
AddAtrribute(SampleB,["AddMol0","AddMol1","AddMol2","AddMol3","RemMol0","RemMol1","RemMol2","RemMol3","RemMol4","RemMol5"])
SampleB.ApplyTitration = ApplyTitration4
SampleB.sample["gd"] = sample["gd"]
SampleB.index = count()
equilibrate(SampleB, 5.0E-4, 20000.0, 1, 20000 )
sample["gd"] = [SampleB.sample.get("gd")[-1]]
Species4, Steps4, name4, taken4 = SaveGraph(SampleB, "B", SampleB.steps )
disposePercent(SampleC.sample,0.5)
count = 1
def onclick(event):
    global count
    event.canvas.figure.clear()
    plt.clf()
    if count == 5:
        DrawGraph(Species0, Steps0, name0, len(Steps0), taken0)
    elif count == 2: 
        DrawGraph(Species1, Steps1, name1, len(Steps1), taken1)
    elif count == 3: 
        DrawGraph(Species2, Steps2, name2, len(Steps2), taken2)
    elif count == 4: 
        DrawGraph(Species3, Steps3, name3, len(Steps3), taken3)
    else:
        DrawGraph(Species4, Steps4, name4, len(Steps4), taken4)
    count += 1
    if count == 5+1:
        count -= 5
    event.canvas.draw()

fig = plt.figure()
fig.canvas.mpl_connect('button_press_event', onclick)

DrawGraph(Species0, Steps0, name0, len(Steps0), taken0)

plt.show()
