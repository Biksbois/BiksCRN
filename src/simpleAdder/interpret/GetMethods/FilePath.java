package simpleAdder.interpret.GetMethods;

import java.io.File;
import java.nio.file.Paths;
import java.util.Scanner;

public class FilePath {
    public String get(String[] args){

        if(args.length == 0 ){
            return RequestPath();
        }else{
            File arg = new File(args[0]);
            if(!arg.exists()){
                return RequestPath();
            }else{
                return args[0];
            }
        }
    }

    public String RequestPath(){
        String AbsPath = Paths.get("").toAbsolutePath().toString() + "\\";

        Scanner myObj = new Scanner(System.in);
        System.out.println("Input file not provided");
        System.out.println("Enter filepath:");
        String nextLine = myObj.nextLine();

        return IsValidPath(nextLine) ? nextLine : AbsPath + nextLine;
    }

    private Boolean IsValidPath(String path){
        File file = new File(path);
        return file.exists() ? true : false;
    }
}
