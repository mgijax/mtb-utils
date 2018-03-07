package us.jawsoft.apps.RDBMSJavaGenerator;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Properties;
import java.util.StringTokenizer;

public class Main {
    
    private static Properties prop;
    
    public static void main(String argv[]) {
        // Check for required argument
        if(argv == null || argv.length < 1) {
            System.err.println("Usage: " + Main.class.toString() + " <properties filename>");
            System.exit(1);
        }
        
        prop = new Properties();
        
        try {
            prop.load(new FileInputStream(argv[0]));
            Database db = new Database();
            db.setDriver(getProperty("jdbc.driver"));
            db.setURL(getProperty("jdbc.url"));
            db.setUsername(getProperty("jdbc.username"));
            db.setPassword(getProperty("jdbc.password"));
            db.setCatalog(getProperty("jdbc.catalog"));
            db.setSchema(getProperty("jdbc.schema"));
            db.setTableNamePattern(getProperty("jdbc.tablenamepattern"));
            
            String tt = getProperty("jdbc.tabletypes", "TABLE");
            StringTokenizer st = new StringTokenizer(tt, ",");
            ArrayList al = new ArrayList();
            
            while(st.hasMoreTokens()) {
                al.add(((String)st.nextToken()).trim());
            }
            
            db.setTableTypes((String[])al.toArray(new String[al.size()]));
            
            db.load();
            
            PreparedManagerWriter writer = new PreparedManagerWriter();
            writer.setDatabase(db);
            writer.setProperties(prop);
            writer.process();
        } catch(Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    public static String getProperty(String key) {
        String s = prop.getProperty(key);
        return s==null?s:s.trim();
    }
    
    public static String getProperty(String key, String default_val) {
        return prop.getProperty(key, default_val);
    }
}