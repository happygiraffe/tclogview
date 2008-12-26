package net.happygiraffe.tclogview;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * A JavaBeans compliant wrapper around {@link File}.
 * @author dom
 */
public class FileBean {

    private final File file;
    private final SimpleDateFormat sdf =
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    public FileBean(File file) {
        this.file = file;
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    public FileBean(String base, String path) {
        this(new File(base, path));
    }

    public File getFile() {
        return file;
    }

    public Date getLastModified() {
        return new Date(file.lastModified());
    }

    public String getLastModifiedIso8601() {
        return sdf.format(getLastModified());
    }

    public long getLength() {
        return file.length();
    }

    public String getName() {
        return file.getName();
    }

    public List<FileBean> listFiles() {
        File[] files = this.file.listFiles();
        List<FileBean> beans = new ArrayList<FileBean>(files.length);
        for (File f : files) {
            beans.add(new FileBean(f));
        }
        return beans;
    }

    @Override
    public String toString() {
        return file.toString();
    }
}
