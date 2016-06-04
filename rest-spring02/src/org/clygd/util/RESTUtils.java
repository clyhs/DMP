/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.clygd.util;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipFile;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.restlet.data.MediaType;
import org.restlet.data.Reference;
import org.restlet.data.Request;

import com.noelios.restlet.ext.servlet.ServletCall;
import com.noelios.restlet.http.HttpRequest;

/**
 * Utility class for Restlets.
 * 
 * @author David Winslow, OpenGeo
 * @author Simone Giannecchini, GeoSolutions
 * @author Justin Deoliveira, OpenGeo
 *
 */
public class RESTUtils {

    /**
     * Returns the underlying HttpServletRequest from a Restlet Request object.
     * <p>
     * Note that this only returns a value in the case where the Restlet 
     * request/call is originating from a servlet.
     * </p>
     * @return The HttpServletRequest, or null.
     */
    public static HttpServletRequest getServletRequest( Request request ) {
        if ( request instanceof HttpRequest ) {
            HttpRequest httpRequest = (HttpRequest) request;
            if ( httpRequest.getHttpCall() instanceof ServletCall ) {
                ServletCall call = (ServletCall) httpRequest.getHttpCall();
                return call.getRequest();
            }
        }
        
        return null;
    }
    
    /**
     * Returns the base url of a request.
     */
    public static String getBaseURL( Request request ) {
        Reference ref = request.getResourceRef();
        HttpServletRequest servletRequest = getServletRequest(request);
        if ( servletRequest != null ) {
            String baseURL = ref.getIdentifier();
            return baseURL.substring(0, baseURL.length()-servletRequest.getPathInfo().length());
        } else {
            return ref.getParentRef().getIdentifier();
        }
    }
    
    
    
    /**
     * Reads content from the body of a request and writes it to a file.
     * 
     * @param fileName The name of the file to write out.
     * @param directory The directory to write the file to.
     * @param request The request.
     * 
     * @return The file object representing the newly written file.
     * 
     * @throws IOException Any I/O errors that occur.
     * 
     * TODO: move this to IOUtils.
     */
    public static File handleBinUpload(String fileName, File directory, Request request ) 
        throws IOException {
        
        final File newFile = new File(directory, fileName);
        if (newFile.exists()) {
        	FileUtils.cleanDirectory(directory);
        }
        
        final ReadableByteChannel source =request.getEntity().getChannel();
        final FileChannel outputChannel = IOUtils.getOuputChannel(newFile);
        IOUtils.copyChannel(1024*1024, source,outputChannel );
        IOUtils.closeQuietly(source);
        IOUtils.closeQuietly(outputChannel);
        return newFile;
    }
    
    
    
    
    
    static Set<String> ZIP_MIME_TYPES = new HashSet();
    static {
        ZIP_MIME_TYPES.add( "application/zip" );
        ZIP_MIME_TYPES.add( "multipart/x-zip" );
        ZIP_MIME_TYPES.add( "application/x-zip-compressed" );
    }
    /**
     * Determines if the specified media type represents a zip stream.
     */
    public static boolean isZipMediaType( MediaType mediaType ) {
        return ZIP_MIME_TYPES.contains( mediaType.toString() );
    }
    
    /**
     * Unzips a zip a file to a specified directory, deleting the zip file after unpacking.
     * 
     * @param zipFile The zip file.
     * @param outputDirectory The directory to unpack the contents to.
     * 
     * @throws IOException Any I/O errors that occur.
     * 
     * TODO: move this to IOUtils
     */
    public static void unzipFile( File zipFile, File outputDirectory ) throws IOException {
        if ( outputDirectory == null ) {
            outputDirectory = zipFile.getParentFile();
        }
        if ( outputDirectory != null && !outputDirectory.exists() ) {
            outputDirectory.mkdir();
        }
        ZipFile archive = new ZipFile(zipFile);
        IOUtils.inflate(archive, outputDirectory, null);
        IOUtils.deleteFile(zipFile);
    }
    
    
    /**
     * Fetch a request attribute as a String, accounting for URL-encoding.
     *
     * @param request the Restlet Request object that might contain the attribute
     * @param name the name of the attribute to retrieve
     *
     * @return the attribute, URL-decoded, if it exists and is a valid URL-encoded string, or null
     *     otherwise
     */
    public static String getAttribute(Request request, String name) {
        Object o = request.getAttributes().get(name);
        return decode(o);
    }
    
    public static String getQueryStringValue(Request request, String key) {
        String value = request.getResourceRef().getQueryAsForm().getFirstValue(key);
        return decode(value);
    }
    
    static String decode(Object value) {
        if (value == null) {
            return null;
        }
        
        try {
            return URLDecoder.decode(value.toString(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }
}
