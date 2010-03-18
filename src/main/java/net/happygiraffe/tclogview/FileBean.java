/*
 * Copyright (C) 2008 by Dominic Mitchell
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY AUTHOR AND CONTRIBUTORS ``AS IS'' AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED.  IN NO EVENT SHALL AUTHOR OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
 * OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 */

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
