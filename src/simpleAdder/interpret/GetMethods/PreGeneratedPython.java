package simpleAdder.interpret.GetMethods;

public class PreGeneratedPython {
    /***
     * Returns the premade code
     * @return
     */
    public String GetInitialCode()
    {
        return GetPackages() + "\n" + GetProtocols();
    }

    /***
     * Returns all the packages we use in our python code
     * @return
     */
    private String GetPackages()
    {
        return  "import matplotlib.pyplot as plt\n" +
                "from matplotlib.animation import FuncAnimation\n" +
                "from itertools import count\n" +
                "import math";
    }

    /***
     * Returns all methods used for the four different protocols, Split, Dispose, Mix and Equilibrate
     * @return
     */
    private String GetProtocols()
    {
        return  GetpSplit() + "\n"+
                GetpDispose() + "\n"+
                GetpMix() + "\n"+
                GetpEquilibrste() + "\n"+
                GetPercent() + "\n" +
                GetSaveGraph() + "\n" +
                GetPringGraph() + "\n" +
                GetSetAttributes() +"\n";
    }

    /***
     * Returns python code for the SPlit method
     * @return
     */
    private String GetpSplit()
    {
        return  "def split( SplitSample,ResultingSampleList, Distribution):\n" +
                "    rSample = {} #resulting sample list\n" +
                "\n" +
                "    for key in SplitSample.keys():\n" +
                "        for i in range(0,len(ResultingSampleList)):\n" +
                "            if key in ResultingSampleList[i]:\n" +
                "                ResultingSampleList[i][key] = [SplitSample.get(key)[-1]*Distribution[i]+ResultingSampleList[i][key][-1]]\n" +
                "            else:\n" +
                "                ResultingSampleList[i][key] = [SplitSample.get(key)[-1]*Distribution[i]]";
    }

    /***
     * Returns the method for the protocol Dispose
     * @return
     */
    private String GetpDispose()
    {
        return "\n" +
                "def disposePercent(sample, percent):\n" +
                "    for key in sample.keys():\n" +
                "        sample[key] = [math.ceil(sample.get(key)[-1] * (1 - percent))]\n" +
                "    return sample\n" +
                "\n" +
                "\n" +
                "def dispose(sample):\n" +
                "    for key in sample.keys():\n" +
                "        sample[key] = [0]\n" +
                "    return sample";
    }

    /***
     * Returns the python code for the protocol Mix
     * @return
     */
    private String GetpMix()
    {
         return " \n" +
                "def mix(sampleList):\n" +
                "    rSample = {}\n" +
                "\n" +
                "    for sample in sampleList:\n" +
                "        for key in sample.keys():\n" +
                "            if key in rSample:\n" +
                "                rSample[key][-1] += sample.get(key)[-1]\n" +
                "            else :\n" +
                "                rSample[key] = [sample.get(key)[-1]]\n" +
                "\n" +
                "    \n" +
                "    return rSample";
    }

    /***
     * Returns the python code for the protocol Equilibrate
     * @return
     */
    private String GetpEquilibrste()
    {
        return "\ndef equilibrate(sample, stepsize, times, timeInterval, bitesize):\n" +
                "    sample.h = stepsize\n" +
                "    sample.steps = times\n"+
                "    sample.bitesize = bitesize\n" +
                "    plt.figure(figsize=(12, 7),dpi=80, num='BiksCRN')\n" +
                "    ani = FuncAnimation(plt.gcf(), sample.Animate, interval=timeInterval)\n" +
                "    plt.show()\n";
    }

    private String GetPercent(){
        return  "def GetPercent(i, steps):\n" +
                "    result = (i/steps)*100\n" +
                "    if result > 100:\n" +
                "        return \"100\"\n" +
                "    else:\n" +
                "        return \"{:.2f}\".format(result)\n";
    }

    private String GetSaveGraph(){
        return "def SaveGraph(Sample, name, taken):\n" +
                "    rSpecies = Sample.sample.copy()\n" +
                "    rSteps = Sample.stepList\n" +
                "    for key in Sample.sample:\n" +
                "        Sample.sample[key] = [Sample.sample.get(key)[-1]]\n" +
                "    Sample.stepList = []\n" +
                "    return rSpecies, rSteps, name, taken\n";
    }

    private String GetPringGraph(){
        return "def DrawGraph(Species, Steps, name, i, s):\n" +
                "        for key, value in Species.items():\n" +
                "            plt.plot(Steps, value, label=key + \" = {:.2f} mol/L\".format(Species.get(key)[-1]))\n" +
                "        plt.xlabel('Time (t)')\n" +
                "        plt.suptitle(\"Equilibrating sample \" + name + \" for \" + str(len(Steps)) + \" cycles (\" + GetPercent(i, s) + \"%)\", fontsize=12)\n" +
                "        plt.ylabel('Concentration (mol/L)')\n" +
                "        \n" +
                "        plt.legend()\n";
    }

    private String GetSetAttributes()
    {
        return "def AddAtrribute(obj, atr):\n" +
                "    for str in atr:\n" +
                "        setattr(obj,str,0)";
    }
}
