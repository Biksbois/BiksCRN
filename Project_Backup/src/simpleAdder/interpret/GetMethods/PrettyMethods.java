package simpleAdder.interpret.GetMethods;

public class PrettyMethods {
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
        return  "import numpy as np \n" +
                "from scipy.integrate import odeint\n" +
                "import matplotlib.pyplot as plt\n" +
                "from matplotlib.animation import FuncAnimation\n" +
                "from itertools import count\n" +
                "from threading import Timer\n" +
                "import math\n"+
                "import warnings";
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
                GetpEquilibrste() + "\n";
    }

    /***
     * Returns python code for the SPlit method
     * @return
     */
    private String GetpSplit()
    {
        return  "\ndef combinedPercent(percentList):\n" +
        "    result = 0\n" +
        "    for number in percentList:\n" +
        "        result += number\n" +
        "    return result\n" +
        "\n" +
        "def split(sample, sampleList, percentList):\n" +
        "\n" +
        "    for key, value in sample.items():\n" +
        "        for i in range(0, len(sampleList)):\n" +
        "            sampleList[i][key] = [math.floor(value[-1] * percentList[i])]\n" +
        "\n" +
        "    return sample, sampleList";
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
                "    for sample in sampleList:\n" +
                "        sample = dispose(sample)\n" +
                "    \n" +
                "    return rSample";
    }

    /***
     * Returns the python code for the protocol Equilibrate
     * @return
     */
    private String GetpEquilibrste()
    {
        return "\ndef equilibrate(sample, stepsize, times):\n" +
                "    sample.h = stepsize\n" +
                "    ani = FuncAnimation(plt.gcf(), sample.Animate, interval=1000)\n" +
                "    plt.show()\n";
    }
}
