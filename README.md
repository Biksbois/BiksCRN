BiksCRN
=======
BiksCRN is a first order programming languege used to simulate Chemical Reaction Networks in real time using the Euler method. BiksCRN differece from other similiar languges, with the implementation of chemical protocols and titration. This compiler was made using the SableCC generator, and made in accordance to Aalborg Universitys 4th semesters project.

How get started
---------------
When opening the project in intellij ide, the first thing that should be done is to edit the **main** configurations. Edit the **Program arguments** to take the file _Biksintermidiat.sa_ as input. When writing a program in the _BiksCRN_ syntax it should be done in the _BiksCRN.sa_ file. Fianly to compile the written program **main** should get run, the resulting file of the compilation will be a file called _Python.py_.

Example
-------
Here is a small axample off some working code

Initialize {  
    Sample A {  
        Specie X=0, Y=100;  
        CRN {  
            Y->X, 1;  
            X+X -> Y, 2;  
        }  
        AddMol = X:0.1:while(X<3);  
    }  
    Sample B {  
        Specie C=0, D=10;  
        CRN {  
            C<->D, 1: 2;  
            2*C -> 2*D, 2;  
        }  
        RemMol = D:0.3;  
    }  
    Sample C {  
        Specie Q = 1;  
    }  
}  
Protocol {  
    Equilibrate A for 100 by 0.0005;  
    Sample C = Mix (A,B);  
    Sample A,B = C.Split (0.4,0.5);  
    A.Dispose();  
    B.Dispose(0.5);  
}  
