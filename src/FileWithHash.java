import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.regex.Matcher;

public class FileWithHash {

    private File file;
    private String hash;
    private ArrayList<FileWithHash> childs = new ArrayList<>();
    private String fileName;

    public void addFakeChild(FileWithHash child_fake) {
        childs.add(child_fake);
    }
    public void setChilds(ArrayList<FileWithHash> childs) {
        this.childs = childs;
    }

    public ArrayList<FileWithHash> getChilds_fake() {
        return childs_fake;
    }

    public void setChilds_fake(ArrayList<FileWithHash> childs_fake) {
        this.childs_fake = childs_fake;
    }

    private ArrayList<FileWithHash> childs_fake = new ArrayList<>();

    public TypeDifference getTypeDifference() {
        return typeDifference;
    }

    public void setTypeDifference(TypeDifference typeDifference) {
        this.typeDifference = typeDifference;
    }

    private TypeDifference typeDifference;

    private FileWithHash() {}

    private FileWithHash(File file, String hash) {
        this.file = file;
        this.hash = hash;
    }

    public FileWithHash(File file) {
        this.file = file;
        if (file.exists()) {
            fileName = file.getName();
        } else {
            fileName = "Такого файла нет";
        }
    }

    public void generateChilds() {
        if (file.isDirectory()) {
            /*if (file.listFiles() == null) {
                System.out.println();
            }*/
            for (File file : file.listFiles()) {
                FileWithHash fileWithHash = new FileWithHash(file);
                fileWithHash.generateChilds();
                childs.add(fileWithHash);
            }
        } else {
            childs = null;
        }
    }

    public File getFile() {
        return file;
    }

    public String getHash() {
        return hash;
    }

    public ArrayList<FileWithHash> getChilds() {
        return childs;
    }

    public void generateHash() {
        if (childs != null) {
            hash = String.valueOf(childs.size()) + " files";
            for (FileWithHash fileWithHash : childs) {
                fileWithHash.generateHash();
            }
        } else {
            hash = generateHashFromFile();
        }
    }

    private String generateHashFromFile() {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            String filepath = file.getAbsolutePath();

            // DigestInputStream is better, but you also can hash file like this.
            try (InputStream fis = new FileInputStream(filepath)) {
                byte[] buffer = new byte[1024];
                int nread;
                while ((nread = fis.read(buffer)) != -1) {
                    md.update(buffer, 0, nread);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // bytes to hex
            StringBuilder result = new StringBuilder();
            for (byte b : md.digest()) {
                result.append(String.format("%02x", b));
            }
            return result.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "something went wrong";
    }

    public void printHash(String begin) {
        System.out.println(begin + hash + " !(" + file.getName() + ")!");
        if (childs != null) {
            for (FileWithHash fileWithHash : childs) {
                fileWithHash.printHash(begin + "\t");
            }
        }
    }

    public boolean isFile() {
        return childs == null;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        FileWithHash equled = (FileWithHash) obj;
        if (equled.file.getName().equals(file.getName()) && equled.file.getAbsolutePath().equals(file.getAbsolutePath())) {
            return true;
        } else {
            return false;
        }
    }

    public void compare(FileWithHash fileWithHash_compared) {
        if (isFile()) {
            if (getHash().equals(fileWithHash_compared.hash)) {
                setTypeDifference(TypeDifference.Equal);
            } else {
                setTypeDifference(TypeDifference.Change);
            }
        } else {
            ArrayList<FileWithHash> childs_other = (ArrayList<FileWithHash>) fileWithHash_compared.childs.clone();
            ArrayList<FileWithHash> childs_clone = (ArrayList<FileWithHash>) childs.clone();
            for (FileWithHash fileWithHash : childs_clone) {
                int index = childs_other.indexOf(fileWithHash);
                if (index != -1) {
                    FileWithHash fileWithHash_other = childs_other.get(index);
                    fileWithHash.compare(fileWithHash_other);
                    childs_other.remove(fileWithHash_other);
                } else {
                    fileWithHash.setTypeDifference(TypeDifference.New);
                }
            }
            for (FileWithHash fileWithHash_other : childs_other) {
                addFakeChild(new FileWithHash(fileWithHash_other.getFileName()));
            }
        }
    } //в параметрах передаётся оригинал, все поулченные разнциы записываются в ЭТОТ объект

    public String getFileName () {
        return fileName;
    }

    public FileWithHash (String name) {
        this(new File(name));
        typeDifference = TypeDifference.NotExist;
    }

    public void printCompared(String begin) {
        String color = "";
        if (getTypeDifference() != null) {
            switch (typeDifference) {
                case New:
                    color = Main.ANSI_GREEN;
                    break;
                case Equal:
                    color = Main.ANSI_BLACK;
                    break;
                case Change:
                    color = Main.ANSI_RED;
                    break;
                case NotExist:
                    color = Main.ANSI_YELLOW;
                    break;
            }
        }

        System.out.println(begin + color + hash + "!(" + file.getName() + ")!" + Main.ANSI_RESET);
        if (childs != null) {
            for (FileWithHash fileWithHash : childs) {
                fileWithHash.printCompared(begin + "\t");
            }
        }
        if (childs_fake != null) {
            for (FileWithHash fileWithHash : childs_fake) {
                fileWithHash.printCompared(begin + "\t");
            }
        }

    }

}
