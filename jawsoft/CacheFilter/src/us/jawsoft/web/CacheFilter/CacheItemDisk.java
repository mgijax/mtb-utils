package us.jawsoft.web.CacheFilter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import org.apache.log4j.Logger;


/**
 * A CacheItemDisk extends <code>CacheItem</code> for specific use of
 * serializing the <code>Cache</code> contents to disk.
 *
 * @version 1.0
 * @author mjv@jawsoft.us
 */
public class CacheItemDisk extends CacheItem implements Serializable {

    // -------------------------------------------------------------- Constants

    /**
     * The logger for this class.
     */
    private static Logger log = Logger.getLogger(CacheItemDisk.class.getName());

    /**
     * Default file name length.
     */
    private final int FILE_NAME_LENGTH = 64;

    /**
     * Default filename extension.
     */
    private final String FILE_NAME_EXTENSION = ".cache";

    /**
     * Array of <code>char</code>s that are the only allowed values in the
     * file name.
     */
    private final char chars[] = {
        'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
        'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
        'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
        '1', '2', '3', '4', '5', '6', '7', '8', '9', '0'
    };

    // ----------------------------------------------------- Instance Variables

    /**
     * The path to the serialized file.
     */
    private String path = null;

    /**
     * The serialized file name.
     */
    private String fileName = null;


    // ----------------------------------------------------------- Constructors

    /**
     * Construct a new CacheItemDisk using the key provided.
     *
     * @param key The key for this CacheItemDisk
     * @param path The path to where the contents will be stored on disk.
     */
    public CacheItemDisk(String key, String path) {
        super(key);
        this.path = path;
        this.fileName = createFileName();
    }


    // --------------------------------------------------------- Public Methods

    /**
     * Get the cached content from this CacheItemDisk which is stored on disk.
     *
     * @return The content of this CacheItemDisk.
     */
    public Object getContent() {
        return readFileSerialized();
    }

    /**
     * Sets the content that is being cached.
     *
     * @param content The content to store in this CacheItemDisk.
     */
    public synchronized void setContent(Object content) {
        writeFileSerialized(content);
        touch();
    }

    /**
     * Get the size of the cache entry in bytes (roughly).
     *
     * @return The size of the serialized file in bytes, or -1 if the size
     * could not be determined.
     */
    public long getSize() {
        try {
            File file = new File(path + File.separator + fileName);
            return file.length();
        } catch (Exception e) {
            return -1;
        }
    }

    // ------------------------------------------------------ Protected Methods
    // none

    // -------------------------------------------------------- Private Methods

    /**
     * Create a random file name.
     *
     * @return A ranfdomly created file name.
     */
    private String createFileName() {
        int preExtensionLength = FILE_NAME_LENGTH;

        if (FILE_NAME_EXTENSION != null) {
            preExtensionLength = FILE_NAME_LENGTH -
                                 FILE_NAME_EXTENSION.length();
        }

        char file_char[] = new char[preExtensionLength];

        for(int i = 0; i < preExtensionLength; i++) {
            int location = (int) (Math.random() * 1000D) % chars.length;
            file_char[i] = chars[location];
        }

        String name = new String(file_char);

        if (FILE_NAME_EXTENSION != null) {
            name = name + FILE_NAME_EXTENSION;
        }

        return name;
    }

    /**
     * Read the serialized file.
     *
     * @return The content of the file, <code>null</code> if the file could
     * not be read.
     */
    private Object readFileSerialized() {
        if (log.isDebugEnabled()) {
            log.debug("{CacheItemDisk.readFileSerialized() : File = " + fileName);
        }

        try {
            // This is a fast and easy way to read the file into memory
            File file = new File(path + File.separator + fileName);
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
            // Deserialize the object
            Object o = in.readObject();
            in.close();

            return o;
        } catch (Exception e) {
            log.error("Unable to read serialized file!", e);
        }

        return null;
    }

    /**
     * Write the serialized file.
     *
     * @param content The content of the file.
     */
    private void writeFileSerialized(Object content) {
        if (log.isDebugEnabled()) {
            log.debug("{CacheItemDisk.writeFileSerialized() : File = " + fileName);
        }

        try {
            // Serialize to a file
            ObjectOutput out = new ObjectOutputStream(new FileOutputStream(path + File.separator + fileName));
            out.writeObject(content);
            out.close();
        } catch (Exception e) {
            log.error("Unable to write serialized file!", e);
        }
    }
}

