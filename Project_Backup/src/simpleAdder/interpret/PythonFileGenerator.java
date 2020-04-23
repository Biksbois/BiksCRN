package simpleAdder.interpret;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;

public class PythonFileGenerator {

    String absPath;
    String FileName;

    public PythonFileGenerator(String fileName) throws IOException {
        this.FileName = fileName;
        this.absPath = Paths.get("").toAbsolutePath().toString() + "\\";
        CreateFile();
    }

    public void WriteInputfile(String code) throws IOException {
        try{
            FileWriter fw = new FileWriter(absPath+FileName);
            fw.write(code);
            fw.close();
        }catch (IOException e)
        {
            System.err.println("File path did not exist: "+ absPath);
            System.exit(1);
        }
    }

    private void CreateFile() throws IOException {
        File file = new File(absPath+FileName);
        if(!file.exists())
        {
            file.createNewFile();
        }
    }
}
