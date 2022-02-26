package com.CL;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

public class CommandLineApplication {

    static int filecount;
    static int fileProgress = 1;

    public CommandLineApplication(){
        startApplication();
    }


    public void startApplication(){
        System.out.println("Welcome to the Structure Installer");
        System.out.println("Please enter a minecraft Dir");
        BufferedReader reader = new BufferedReader(
            new InputStreamReader(System.in));
        Path path;
        FileInputStream srcStream = null;
        FileOutputStream destStream = null;
        try {
            String location;
            do {

                location = reader.readLine();
                path = Paths.get(location);
                if (!Files.exists(path)){
                    System.out.println("Please Input a valid file Path to a folder");
                }
            } while (!Files.exists(path) || !Files.isDirectory(path) );

            File folder = path.resolve("test").toFile();
            URL jarlocation = getClass().getProtectionDomain().getCodeSource().getLocation();
            File jarFile = new File(jarlocation.getPath());
            Path StructurePath = Paths.get( jarFile.getParent() + "/structures");
            System.out.println(StructurePath.toAbsolutePath());
            File structureFiles = StructurePath.toFile();
            File[] files = structureFiles.listFiles();
            printFiles(files);
            filecount = countFiles(files);
            System.out.println("there are " + filecount + " files scanned ready to be installed");
            System.out.println("Would you like to continue, Y/N");
            String continuing = reader.readLine();
            if (continuing.equals("y"))
            {
                System.out.println(folder);
                System.out.println(structureFiles);

                copyDirectoryJavaNIO(structureFiles.toPath(),folder.toPath());
                System.out.println("Copy files successfully");
            }
            else if (continuing.equals("n"))
            {
                System.exit(0);
            }

            System.out.println("Press anything to exit");
            String string = reader.readLine();
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Oops you inputted something that broke this, please try again");
        }




    }



    private File getFile(String fileName) throws IOException
    {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource(fileName);

        if (resource == null) {
            System.out.println("File Not Found");
        } else {
            return new File(resource.getFile());
        }
        return null;
    }


    void printFiles(File[] files){
        for (File file: files){
            if (file.isDirectory())
            {
                System.out.println(file);
                printFiles(file.listFiles());
            }else
            {
            System.out.println(file);
        }
        }
    }


    int countFiles(File[] files)
    {
        int count = 0;
        for (File file: files){
            if (file.isDirectory())
            {
                System.out.println(file);
                count += countFiles(file.listFiles());
            }else
            {
                count++;
            }
        }
        return count;
    }

    public static void copyDirectoryJavaNIO(Path source, Path target)
        throws IOException {

        // is this a directory?
        if (Files.isDirectory(source)) {

            //if target directory exist?
            if (Files.notExists(target)) {
                // create it
                Files.createDirectories(target);
                System.out.println("Directory created : " + target);
            }

            // list all files or folders from the source, Java 1.8, returns a stream
            // doc said need try-with-resources, auto-close stream
            try (Stream<Path> paths = Files.list(source)) {

                // recursive loop
                paths.forEach(p -> {
                    copyDirectoryJavaNIOWrapper(
                        p, target.resolve(source.relativize(p)));
                    try {

                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });

            }

        } else {
            // if file exists, replace it
            Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
            System.out.println(
                String.format("Copied File \t%s  \t%d / %d", source.toFile().getName(), fileProgress, filecount)
            );
            fileProgress++;
        }
    }
    // extract method to handle exception in lambda
    public static void copyDirectoryJavaNIOWrapper(Path source, Path target) {

        try {
            copyDirectoryJavaNIO(source, target);
        } catch (IOException e) {
            System.err.println("IO errors : " + e.getMessage());
        }

    }
}