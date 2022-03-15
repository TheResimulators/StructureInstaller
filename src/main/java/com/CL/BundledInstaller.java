package com.CL;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;


public class BundledInstaller {


    ArrayList<String> StructureNameList = new ArrayList<>();
    ArrayList<Structure> StructuresFiles = new ArrayList<>();
    File jarPath;
    public BundledInstaller()
    {
        BufferedReader reader = new BufferedReader(
            new InputStreamReader(System.in));
        ReadStructureList();
        try {
           jarPath =new File( getClass()
                .getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .toURI());

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        File targetPath =  jarPath;
        String input = "";
        do
        {
            try {

            targetPath = targetPath.getParentFile();
            System.out.println(targetPath.getAbsolutePath());
            System.out.println("Is this the correct path to you minecraft folder y/n");
            input = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }while (!input.equals("y"));


        String installationPath = targetPath.getAbsolutePath() + "/resource/simukraft/";
        try {
            File installationFile = new File(installationPath);
            Files.createDirectories(installationFile.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (String StructureName : StructureNameList)
        {
            StructuresFiles.add(new Structure(StructureName));
        }
        System.out.println("Found " + StructuresFiles.size() + " to be installed");

        for (Structure structure: StructuresFiles)
        {
            InputStream sourceFile = BundledInstaller.class.getResourceAsStream(structure.getFile());

            File locationFile = new File(installationPath + "/" + structure.category + "/" + structure.name);

            try {
                if (Files.notExists(locationFile.toPath())){
            Files.createDirectories(locationFile.getParentFile().toPath());}
            //locationFile.createNewFile();
            Files.copy(sourceFile, locationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println(String.format("Copied File \t%s", structure.name));
           } catch (IOException e) {
               e.printStackTrace();
           }

        }
    }


    public void ReadStructureList()
    {
        InputStream stream= BundledInstaller.class.getResourceAsStream("Generated/StructureList.txt");
       // System.out.println(stream);
        if (stream != null){
            try (InputStreamReader isr = new InputStreamReader(stream);
                 BufferedReader br = new BufferedReader(isr);)
            {
                String line;
                while ((line = br.readLine()) != null) {
                    StructureNameList.add(line);
                }
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static class Structure {

        private String file;
        public String category;
        public String name;
        public Structure(String string) {
            String[] StringList = string.split(",");
            file = "Structures/" + StringList[0] + "/" + StringList[1].strip();
            category = StringList[0];
            name = StringList[1].strip();

        }
        public String getFile() {
            return file;
        }
    }
}

