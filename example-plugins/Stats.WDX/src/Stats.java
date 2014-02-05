import java.io.IOException;

import java.nio.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

import plugins.*;
import plugins.wdx.*;
import plugins.wdx.ContentPlugin;


public class Stats extends ContentPlugin {

    public static enum EntryType {
        Folder,
        File,
        Symlink,
        Other;
        
        public static abstract class Field extends plugins.wdx.Field.ENUM<EntryType> {
            public Field(String name) {
                super(name, EntryType.class);
            }
        }

        public static EntryType which(Path path) throws IOException {
            BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class);
            if (attr.isOther()) {
                return Other;
            }
            if (attr.isSymbolicLink()) {
                return Symlink;
            }
            if (attr.isDirectory()) {
                return Folder;
            }
            return File;
        }
    }

    java.nio.file.attribute.FileTime getMaxWriteTime(Path path) throws IOException {
        if (Files.isDirectory(path)) {
            java.nio.file.attribute.FileTime max = java.nio.file.attribute.FileTime.fromMillis(Long.MIN_VALUE);
            try (DirectoryStream<Path> folder = Files.newDirectoryStream(path)) {
               for (Path entry: folder) {
                   java.nio.file.attribute.FileTime t = getMaxWriteTime(entry);
                   if (t.compareTo(max) > 0) {
                        max = t;
                   }
               }
               return max;
            } catch (DirectoryIteratorException ex) {
               // I/O error encounted during the iteration, the cause is an IOException
               throw ex.getCause();
            }
        } else {
            return Files.getLastModifiedTime(path);
        }
    }

    int getEntryCount(Path path) throws IOException {
        if (Files.isDirectory(path)) {
            int result = 0;
            try (DirectoryStream<Path> folder = Files.newDirectoryStream(path)) {
               for (Path entry: folder) {
                   result += Files.isDirectory(entry) ? getEntryCount(entry) : 1;
               }
               return result;
            } catch (DirectoryIteratorException ex) {
               // I/O error encounted during the iteration, the cause is an IOException
               throw ex.getCause();
            }
        } else {
            return -1;
        }
    }

    int getFolderCount(Path path) throws IOException {
        if (Files.isDirectory(path)) {
            int result = 0;
            try (DirectoryStream<Path> folder = Files.newDirectoryStream(path)) {
               for (Path entry: folder) {
                   if (Files.isDirectory(entry)) {
                       result +=  getFolderCount(entry) + 1;
                   }
               }
               return result;
            } catch (DirectoryIteratorException ex) {
               // I/O error encounted during the iteration, the cause is an IOException
               throw ex.getCause();
            }
        } else {
            return -1;
        }
    }

    protected void initFields() {
        define(new Field.FILETIME("maxWriteTime") { public FileTime getValue(String fileName) throws IOException {
            Path path = Paths.get(fileName);
            FileTime result = new FileTime();
            java.nio.file.attribute.FileTime t = getMaxWriteTime(path);
            result.setDate(t.toMillis());
            log.info(t);
            return result;
        }});

        define(new Field.INT("entryCount") { public int getValue(String fileName) throws IOException {
            Path path = Paths.get(fileName);
            return getEntryCount(path);
        }});

        define(new Field.INT("folderCount") { public int getValue(String fileName) throws IOException {
            Path path = Paths.get(fileName);
            return getFolderCount(path);
        }});
/*
        define(new EntryType.Field("type") { public EntryType getValue(String fileName) throws IOException {
            Path path = Paths.get(fileName);
            return EntryType.which(path);
        }});
*/

    }

    public static void main(String... args) throws IOException {
        new Stats().runTests(args);
    }

}