BiksCRN
=======
BiksCRN is a first order programming languege used to simulate Chemical Reaction Networks in real time using the Euler method. BiksCRN differece from other similiar languges, with the implementation of chemical protocols and titration. This compiler was made using the SableCC generator, and made in accordance to Aalborg Universitys 4th semesters project.

How get started
---------------
When using BiksCrn some preparations are necessary, these will be covered briefly here
#### Build Project
Firstly when building the .jar file. Click on the **file**->**Project Structure**->**artifacts**. Then click on the **plus(+)**, then clik on the **JAR** and select the **From modules with dependencies**. When this is done the **main class** should be selected in this case it is called **com.company.Main**. When this is done click **ok**, and **apply** the changes and click in the **project structure menu**. Now click on the **build menue** and **click build**.

#### Python Packages
Since the compiled program will be a **.py file** it is requirement to install [python](https://www.python.org/downloads/).
When installing python, note that it is important that the boxes for **enviorment variables** and the **pip installation**.

After succesful installation some pakages that are required to run the target code these are:
* numpy
* matplotlib
* scipy
* itertools
* threading
* math
* warnings

To install the pakages trough pip a comman like this should in the terminal "pip install numpy"

#### Executing the .jar
There is a couple diffrent ways to give arguments to the .jar file to tell it which program should be compiled. 
1. Giving the path to the program trough the terminal as a program argument like so "java -jar SableCC_V3.jar [path]"
1. Dont give it an argument, and follow the intruction given by the compiler.

The path can either be the absolute path or a path relative to the compiler folder.

Example
-------
Here is a small example off some working code  
\/\* Single line comment \*\/

/\*  
&nbsp;&nbsp;&nbsp;&nbsp;Multi line  
&nbsp;&nbsp;&nbsp;&nbsp;comment  
\*/  

&nbsp;&nbsp;&nbsp;&nbsp;Initialize {  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Sample A {  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Specie X=0, Y=100;  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;CRN {  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Y->X, 1;  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;X+X -> Y, 2;  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;AddMol = X:0.1:while(X<3);  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Sample B {  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Specie C=0, D=10;  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;CRN {  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;C<->D, 1: 2;  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;2*C -> 2*D, 2;  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;RemMol = D:0.3;  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Sample C {  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Specie Q = 1;  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}  
&nbsp;&nbsp;&nbsp;&nbsp;}  
&nbsp;&nbsp;&nbsp;&nbsp;Protocol {  
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Equilibrate A for 100 by 0.0005;  
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Sample C = Mix (A,B);  
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Sample A,B = C.Split (0.4,0.5);  
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;A.Dispose();  
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;B.Dispose(0.5);  
&nbsp;&nbsp;&nbsp;&nbsp;}  
