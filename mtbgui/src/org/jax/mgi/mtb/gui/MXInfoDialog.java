/**
 * Header: $Header: /mgi/cvsroot/mgi/mtb/mtbgui/src/org/jax/mgi/mtb/gui/MXInfoDialog.java,v 1.1 2008/08/01 12:23:19 sbn Exp $
 * Author: $Author: sbn $
 */
package org.jax.mgi.mtb.gui;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.ResourceBundle;
import java.util.logging.Level;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.TransferHandler;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicHTML;

/**
 * <p>Common Error Dialog, suitable for representing information about
 * errors and exceptions happened in application. The common usage of the
 * <code>MXInfoDialog</code> is to show collected data about the incident and
 * probably ask customer for a feedback. The data about the incident consists
 * from the title which will be displayed in the dialog header, short
 * description of the problem that will be immediately seen after dialog is
 * became visible, full description of the problem which will be visible after
 * user clicks "Details" button and Throwable that contains stack trace and
 * another usable information that may be displayed in the dialog.</p>
 *
 * <p>There are two basic ways to use the MXInfoDialog. The first is to call one
 * of the standard static methods for displaying the error dialog. Here is a
 * basic example:<br/>
 * <pre><code>
 *      try {
 *          //do some work
 *          //some exception is thrown
 *      } catch (Exception e) {
 *          MXInfoDialog.showDialog(this, "Critical Error",
 *                  "<html><body>Some <b>critical error</b> has occured." +
 *                  " You may need to restart this application. Message #SC21." +
 *                  "</body></html>", e);
 *      }
 * </code></pre></p>
 *
 * <p>For the vast majority of use cases, the static methods should be sufficient and
 * are recommended. However, for those few use cases requireing customizations, you
 * can create and display a MXInfoDialog manually.</p>
 *
 * <p>To ask user for feedback extend abstract class <code>ErrorReporter</code> and
 * set your reporter using <code>setReporter</code> method. Report button will
 * be added to the dialog automatically.<br>
 * See {@link MailErrorReporter MailErrorReporter} documentation for the
 * example of error reporting usage.</p>
 *
 * <p>For example, to show simple <code>MXInfoDialog</code> call <br>
 * <code>MXInfoDialog.showDialog(null, "Application Error",
 *   "The application encountered the unexpected error,
 *    please contact developers")</code></p>
 *
 * <p>Internationalization is handled via a resource bundle or via the UIManager
 * bidi orientation (usefull for right to left languages) is determined in the
 * same way as the JOptionPane where the orientation of the parent component is
 * picked. So when showDialog(Component cmp, ...) is invoked the component
 * orientation of the error dialog will match the component orientation of cmp.</p>
 *
 * @author $Author: sbn $
 * @version $Revision: 1.1 $
 * @cvsheader $Header: /mgi/cvsroot/mgi/mtb/mtbgui/src/org/jax/mgi/mtb/gui/MXInfoDialog.java,v 1.1 2008/08/01 12:23:19 sbn Exp $
 * @date $Date: 2008/08/01 12:23:19 $
 */
public class MXInfoDialog extends JDialog {

    // -------------------------------------------------------------- Constants

    /**
     * Used as a prefix when pulling data out of UIManager for i18n
     */
    private static String CLASS_NAME;
    /**
     * Icon for the error dialog (stop sign, etc)
     */
    private static Icon DEFAULT_ERROR_ICON = UIManager.getIcon("OptionPane.errorIcon");
    /**
     * Icon for the error dialog (stop sign, etc)
     */
    private static Icon DEFAULT_WARNING_ICON = UIManager.getIcon("OptionPane.warningIcon");


    // ----------------------------------------------------- Instance Variables

    /**
     * Error message text area
     */
    private JEditorPane errorMessage;

    /**
     * details text area
     */
    private JEditorPane details;

    /**
     * detail button
     */
    private EqualSizeJButton detailButton;

    /**
     * details panel
     */
    private JPanel detailsPanel;

    /**
     * label used to display the warning/error icon
     */
    private JLabel iconLabel;

    /**
     * report an error button
     */
    private EqualSizeJButton reportButton;

    /**
     * MXInfo that contains all the information prepared for
     * reporting.
     */
    private MXInfo incidentInfo;

    /**
     * The Action that will be executed to report an error/warning. If null,
     * then a default ReportAction will be used.
     */
    private Action reportAction;

    /**
     * The Icon to use if the error message is indeed an Error, as specified
     * by MXInfo.getErrorLevel == Level.SEVERE
     */
    private Icon errorIcon = DEFAULT_ERROR_ICON;

    /**
     * The Icon to use if the error message is not an Error
     * (MXInfo.getErrorLevel != Level.SEVERE)
     */
    private Icon warningIcon = DEFAULT_WARNING_ICON;

    /**
     * The height of the window when collapsed. This value is stashed when the
     * dialog is expanded
     */
    private int collapsedHeight = 0;

    /**
     * The height of the window when last expanded. This value is stashed when
     * the dialog is collapsed
     */
    private int expandedHeight = 0;

    /**
     * Creates initialize the UIManager with localized strings
     */
    static {
        // Popuplate UIDefaults with the localizable Strings we will use
        // in the Login panel.
        CLASS_NAME = MXInfoDialog.class.getCanonicalName();
        String lookup;
        ResourceBundle res = ResourceBundle.getBundle("org.jax.mgi.mtb.gui.resources.MXInfoDialog");
        Enumeration<String> keys = res.getKeys();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            lookup = CLASS_NAME + "." + key;
            if (UIManager.getString(lookup) == null) {
                UIManager.put(lookup, res.getString(key));
            }
        }
    }

    // ----------------------------------------------------------- Constructors

    /**
     * Create a new ErrorDialog with the given Frame as the owner
     * @param owner Owner of this error dialog.
     */
    public MXInfoDialog(Frame owner) {
        super(owner, true);
        initGUI();
    }

    /**
     * Create a new ErrorDialog with the given Dialog as the owner
     * @param owner Owner of this error dialog.
     */
    public MXInfoDialog(Dialog owner) {
        super(owner, true);
        initGUI();
    }


    // --------------------------------------------------------- Public Methods

    /**
     * Sets the MXInfo for this dialog
     *
     * @param info IMXErrorInfothat incorporates all the details about the error
     */
    public void setIncidentInfo(MXInfo info) {
        MXInfo old = this.incidentInfo;
        this.incidentInfo = info;
        firePropertyChange("incidentInfo", old, this.incidentInfo);
        reinit();
    }

    /**
     * Get curent dialog's MXInfo
     *
     *
     * @return <code>IMXErrorInfo/code> assigned to this dialog
     */
    public MXInfo getIncidentInfo() {
        return incidentInfo;
    }

    /**
     * Specifies the icon to use if the MXInfo is Level.SEVERE
     *
     *
     * @param icon the Icon to use. If null, the default error icon will be used
     */
    public void setErrorIcon(Icon icon) {
        Icon old = this.errorIcon;
        this.errorIcon = icon == null ? DEFAULT_ERROR_ICON : icon;
        firePropertyChange("errorIcon", old, this.errorIcon);
        reinit();
    }

    /**
     * Returns the Icon in use if the MXInfo is Level.SEVERE
     *
     * @return the Icon
     */
    public Icon getErrorIcon() {
        return errorIcon;
    }

    /**
     * Specifies the icon to use if the MXInfo is not Level.SEVERE
     *
     * @param icon the Icon to use. If null, the default warning icon will be used
     */
    public void setWarningIcon(Icon icon) {
        Icon old = this.warningIcon;
        this.warningIcon = icon == null ? DEFAULT_WARNING_ICON : icon;
        firePropertyChange("warningIcon", old, this.warningIcon);
        reinit();
    }

    /**
     * Returns the Icon in use if the MXInfo is not Level.SEVERE
     *
     * @return the Icon
     */
    public Icon getWarningIcon() {
        return warningIcon;
    }

    /**
     * Specify the Action that will be executed to report an error/warning. If null,
     * then a default ReportAction will be used.
     *
     * @param action The Action to execute if the user attempts to report a problem
     */
    public void setReportAction(Action action) {
        Action old = this.reportAction;
        this.reportAction = action == null ? new ReportAction() : action;
        firePropertyChange("reportAction", old, this.reportAction);
        reportButton.setAction(this.reportAction);
    }

    /**
     * @return the Action that is executed if the user attempts to report a problem
     */
    public Action getReportAction() {
        return reportAction;
    }

    // ------------------------------------------------------ Protected Methods
    // none

    // -------------------------------------------------------- Private Methods
    // none

    /**
     * initialize the gui.
     */
    private void initGUI() {
        //initialize the gui
        GridBagLayout layout = new GridBagLayout();
        this.getContentPane().setLayout(layout);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridheight = 1;
        gbc.insets = new Insets(22, 12, 11, 17);
        iconLabel = new JLabel(DEFAULT_ERROR_ICON);
        this.getContentPane().add(iconLabel, gbc);

        errorMessage = new JEditorPane();
        errorMessage.setEditable( false );
        errorMessage.setContentType("text/html");
        errorMessage.setOpaque( false );
        //errorMessage.putClientProperty(JXEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridheight = 1;
        gbc.gridwidth = 3;
        gbc.gridx = 1;
        gbc.weightx = 0.0;
        gbc.weighty = 0.00001; //ensures that when details is hidden, it get all
        //the extra space, but none when details is shown
        //(unless you have a REALLY BIG MONITOR
        gbc.insets = new Insets(24, 0, 0, 11);
        this.getContentPane().add(errorMessage, gbc);

        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.insets = new Insets(12, 0, 11, 5);
        EqualSizeJButton okButton = new EqualSizeJButton(UIManager.getString(CLASS_NAME + ".ok_button_text"));
        this.getContentPane().add(okButton, gbc);

        reportAction = new ReportAction();
        reportButton = new EqualSizeJButton(reportAction);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.weightx = 0.0;
        gbc.insets = new Insets(12, 0, 11, 5);
        this.getContentPane().add(reportButton, gbc);
        reportButton.setVisible(false); // not visible by default

        detailButton = new EqualSizeJButton(UIManager.getString(CLASS_NAME + ".details_expand_text"));
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.weightx = 0.0;
        gbc.insets = new Insets(12, 0, 11, 11);
        this.getContentPane().add(detailButton, gbc);

        details = new JEditorPane();
        details.setContentType("text/html");
        details.setTransferHandler(new DetailsTransferHandler());
        JScrollPane detailsScrollPane = new JScrollPane(details);
        detailsScrollPane.setPreferredSize(new Dimension(10, 250));
        details.setEditable(false);
        detailsPanel = new JPanel(new GridBagLayout());
        detailsPanel.add(detailsScrollPane, new GridBagConstraints(0,0,1,1,1.0,1.0,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(6,11,11,11),0,0));
        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridwidth = 4;
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weighty = 1.0;
        this.getContentPane().add(detailsPanel, gbc);

        JButton button = new JButton(UIManager.getString(CLASS_NAME + ".copy_to_clipboard_button_text"));
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                details.copy();
            }
        });
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weighty = 0.0;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(6, 11, 11, 11);
        detailsPanel.add(button, gbc);


        //make the buttons the same size
        EqualSizeJButton[] buttons = new EqualSizeJButton[] {
            detailButton, okButton, reportButton };
        okButton.setGroup(buttons);
        reportButton.setGroup(buttons);
        detailButton.setGroup(buttons);

        okButton.setMinimumSize(okButton.getPreferredSize());
        reportButton.setMinimumSize(reportButton.getPreferredSize());
        detailButton.setMinimumSize(detailButton.getPreferredSize());

        //set the event handling
        okButton.addActionListener(new OkClickEvent());
        detailButton.addActionListener(new DetailsClickEvent());
    }

    /**
     * Set the details section of the error dialog.  If the details are either
     * null or an empty string, then hide the details button and hide the detail
     * scroll pane.  Otherwise, just set the details section.
     * @param details  Details to be shown in the detail section of the dialog.
     * This can be null if you do not want to display the details section of the
     * dialog.
     */
    private void setDetails(String details) {
        if ("".equals(details)) {
            setDetailsVisible(false);
            detailButton.setVisible(false);
        } else {
            this.details.setText(details);
            setDetailsVisible(false);
            detailButton.setVisible(true);
        }
    }

    /**
     * Set the details section to be either visible or invisible.  Set the
     * text of the Details button accordingly.
     * @param b if true details section will be visible
     */
    private void setDetailsVisible(boolean b) {
        if (b) {
            collapsedHeight = getHeight();
            setSize(getWidth(), expandedHeight == 0 ? collapsedHeight + 300 : expandedHeight);
            detailsPanel.setVisible(true);
            detailButton.setText(UIManager.getString(CLASS_NAME + ".details_contract_text"));
            detailsPanel.applyComponentOrientation(detailButton.getComponentOrientation());

            // workaround for bidi bug, if the text is not set "again" and the component orientation has changed
            // then the text won't be aligned correctly. To reproduce this (in JDK 1.5) show two dialogs in one
            // use LTOR orientation and in the second use RTOL orientation and press "details" in both.
            // Text in the text box should be aligned to right/left respectively, without this line this doesn't
            // occure I assume because bidi properties are tested when the text is set and are not updated later
            // on when setComponentOrientation is invoked.
            details.setText(details.getText());
            details.setCaretPosition(0);
        } else {
            expandedHeight = getHeight();
            detailsPanel.setVisible(false);
            detailButton.setText(UIManager.getString(CLASS_NAME + ".details_expand_text"));
            // Trick to force errorMessage JTextArea to resize according
            // to its columns property.
            errorMessage.setSize( 0, 0 );
            errorMessage.setSize( errorMessage.getPreferredSize() );
            setSize(getWidth(), collapsedHeight);
        }

        repaint();
    }

    /**
     * Set the error message for the dialog box
     * @param errorMessage Message for the error dialog
     */
    private void setErrorMessage(String errorMessage) {
        if(BasicHTML.isHTMLString(errorMessage)) {
            this.errorMessage.setContentType("text/html");
        } else {
            this.errorMessage.setContentType("text/plain");
        }
        this.errorMessage.setText(errorMessage);
    }

    /**
     * Reconfigures the dialog if settings have changed, such as the
     * MXInfo, errorIcon, warningIcon, etc
     */
    private void reinit() {
        //reportButton.setVisible(getErrorReporter() != null);
        if (incidentInfo == null) {
            iconLabel.setIcon(DEFAULT_ERROR_ICON);
            setTitle("");
            setErrorMessage("");
            setDetails("");
        } else {
            iconLabel.setIcon(incidentInfo.getErrorLevel() == Level.SEVERE ?
                errorIcon : warningIcon);
            setTitle(incidentInfo.getHeader());
            setErrorMessage(incidentInfo.getBasicErrorMessage());
            String details = incidentInfo.getDetailedErrorMessage();
            if(details == null) {
                if(incidentInfo.getErrorException() == null) {
                    details = "";
                } else {
                    //convert the stacktrace into a more pleasent bit of HTML
                    StringBuffer html = new StringBuffer(400);
                    html.append("<html><h2>").append(escapeXml(incidentInfo.getHeader())).append("</h2>");
                    html.append("<HR size='1' noshade><div></div><b>Message:</b>");
                    html.append("<pre>").append(escapeXml(incidentInfo.getErrorException().toString()));
                    html.append("</pre><b>Level:</b>");
                    html.append("<pre>").append(incidentInfo.getErrorLevel());
                    html.append("</pre><b>Stack Trace:</b>");
                    html.append("<pre>");
                    for (StackTraceElement el : incidentInfo.getErrorException().getStackTrace()) {
                        html.append("    ").append(el.toString().replace("<init>", "<init>")).append('\n');
                    }
                    html.append("</pre></html>");
                    details = html.toString();
                }
            }
            setDetails(details);
        }

        //set the preferred width of the message area
        //the preferred width should not exceed 500 pixels, or be less than 300 pixels
        Dimension prefSize = errorMessage.getPreferredSize();
        prefSize.width = Math.min(500, prefSize.width);
        prefSize.width = Math.max(300, prefSize.width);
        errorMessage.setSize(prefSize);
        prefSize.height = errorMessage.getPreferredSize().height;
        errorMessage.setPreferredSize(prefSize);
    }

    //------------------------------------------------------- static methods

    /**
     * Constructs and shows the error dialog for the given exception.  The exceptions message will be the
     * errorMessage, and the stacktrace will be the details.
     * @param owner Owner of this error dialog. Determines the Window in which the dialog
     *        is displayed; if the <code>owner</code> has
     *        no <code>Window</code>, a default <code>Frame</code> is used
     * @param title Title of the error dialog
     * @param e Exception that contains information about the error cause and stack trace
     */
    public static void showDialog(Component owner, String title, Throwable e) {
        MXInfo ii = new MXInfo(title, null, null, e);
        showDialog(owner, ii);
    }

    /**
     * Constructs and shows the error dialog for the given exception.  The exceptions message is specified,
     * and the stacktrace will be the details.
     * @param owner Owner of this error dialog. Determines the Window in which the dialog
     *        is displayed; if the <code>owner</code> has
     *        no <code>Window</code>, a default <code>Frame</code> is used
     * @param title Title of the error dialog
     * @param errorMessage Message for the error dialog
     * @param e Exception that contains information about the error cause and stack trace
     */
    public static void showDialog(Component owner, String title, String errorMessage, Throwable e) {
        MXInfo ii = new MXInfo(title, errorMessage, null, e);
        showDialog(owner, ii);
    }

    /**
     * Show the error dialog.
     * @param owner Owner of this error dialog. Determines the Window in which the dialog
     *        is displayed; if the <code>owner</code> has
     *        no <code>Window</code>, a default <code>Frame</code> is used
     * @param title Title of the error dialog
     * @param errorMessage Message for the error dialog
     * @param details Details to be shown in the detail section of the dialog.  This can be null
     * if you do not want to display the details section of the dialog.
     */
    public static void showDialog(Component owner, String title, String errorMessage, String details) {
        MXInfo ii = new MXInfo(title, errorMessage, details);
        showDialog(owner, ii);
    }

    /**
     * Show the error dialog.
     * @param owner Owner of this error dialog. Determines the Window in which the dialog
     *        is displayed; if the <code>owner</code> has
     *        no <code>Window</code>, a default <code>Frame</code> is used
     * @param title Title of the error dialog
     * @param errorMessage Message for the error dialog
     */
    public static void showDialog(Component owner, String title, String errorMessage) {
        MXInfo ii = new MXInfo(title, errorMessage, (String)null);
        showDialog(owner, ii);
    }

    /**
     * Show the error dialog.
     *
     * @param owner Owner of this error dialog. Determines the Window in which the dialog
     *         is displayed; if the <code>owner</code> has
     *         no <code>Window</code>, a default <code>Frame</code> is used
     * @param info <code>IMXErrorInfo/code> that incorporates all the information about the error
     */
    public static void showDialog(Component owner, MXInfo info) {
        MXInfoDialog dlg;

        Window window = findWindow(owner);

        if (window instanceof Dialog) {
            dlg = new MXInfoDialog((Dialog)window);
        } else {
            dlg = new MXInfoDialog((Frame)window);
        }
        dlg.setIncidentInfo(info);
        // If the owner is null applies orientation of the shared
        // hidden window used as owner.
        if(owner == null) {
            dlg.applyComponentOrientation(window.getComponentOrientation());
        } else {
            dlg.applyComponentOrientation(owner.getComponentOrientation());
        }
        dlg.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dlg.pack();
        dlg.setLocationRelativeTo(owner);
        dlg.setVisible(true);
    }

    public static Window findWindow(Component c) {
        if (c == null) {
            return JOptionPane.getRootFrame();
        } else if (c instanceof Window) {
            return (Window) c;
        } else {
            return findWindow(c.getParent());
        }
    }

    /**
     * Set the Icon to use as the default error icon for MXInfoDialog
     * instances. This icon is used whenever the MXInfo for a MXInfoDialog
     * has an errorLevel of Level.SEVERE
     *
     * @param icon the Icon to use as the default error icon
     */
    public static void setDefaultErrorIcon(Icon icon) {
        DEFAULT_ERROR_ICON = icon;
    }

    /**
     * @return the default error icon
     */
    public static Icon getDefaultErrorIcon() {
        return DEFAULT_ERROR_ICON;
    }

    /**
     * Set the Icon to use as the default warning icon for MXInfoDialog
     * instances. This icon is used whenever the MXInfo for a MXInfoDialog
     * has an errorLevel that is not Level.SEVERE
     *
     * @param icon the Icon to use as the default warning icon
     */
    public static void setDefaultWarningIcon(Icon icon) {
        DEFAULT_WARNING_ICON = icon;
    }

    /**
     * @return the default warning icon
     */
    public static Icon getDefaultWarningIcon() {
        return DEFAULT_WARNING_ICON;
    }

    //------------------------------------------------ actions/inner classes

    /**
     * Listener for Ok button click events
     * @author Richard Bair
     */
    private final class OkClickEvent implements ActionListener {

        /* (non-Javadoc)
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent e) {
            //close the window
            setVisible(false);
            dispose();
        }
    }

    /**
     * Listener for Details click events.  Alternates whether the details section
     * is visible or not.
     * @author Richard Bair
     */
    private final class DetailsClickEvent implements ActionListener {

        /* (non-Javadoc)
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent e) {
            setDetailsVisible(!detailsPanel.isVisible());
        }
    }

    /**
     * Action for report button
     */
    public class ReportAction extends AbstractAction {

        public boolean isEnabled() {
                return false;
            //return (getErrorReporter() != null);
        }

        public void actionPerformed(ActionEvent e) {
          //  getErrorReporter().reportIncident(getIncidentInfo());
        }
    }

    /**
     * This is a button that maintains the size of the largest button in the button
     * group by returning the largest size from the getPreferredSize method.
     * This is better than using setPreferredSize since this will work regardless
     * of changes to the text of the button and its language.
     */
    private static class EqualSizeJButton extends JButton {
        public EqualSizeJButton() {
        }

        public EqualSizeJButton(String text) {
            super(text);
        }

        public EqualSizeJButton(Action a) {
            super(a);
        }

        /**
         * Buttons whose size should be taken into consideration
         */
        private EqualSizeJButton[] group;

        public void setGroup(EqualSizeJButton[] group) {
            this.group = group;
        }

        /**
         * Returns the actual preferred size on a different instance of this button
         */
        private Dimension getRealPreferredSize() {
            return super.getPreferredSize();
        }

        /**
         * If the <code>preferredSize</code> has been set to a
         * non-<code>null</code> value just returns it.
         * If the UI delegate's <code>getPreferredSize</code>
         * method returns a non <code>null</code> value then return that;
         * otherwise defer to the component's layout manager.
         *
         * @return the value of the <code>preferredSize</code> property
         * @see #setPreferredSize
         * @see ComponentUI
         */
        public Dimension getPreferredSize() {
            int width = 0;
            int height = 0;
            for(int iter = 0 ; iter < group.length ; iter++) {
                Dimension size = group[iter].getRealPreferredSize();
                width = Math.max(size.width, width);
                height = Math.max(size.height, height);
            }

            return new Dimension(width, height);
        }

    }

    /**
     * Returns the text as non-HTML in a COPY operation, and disabled CUT/PASTE
     * operations for the Details pane.
     */
    private final class DetailsTransferHandler extends TransferHandler {
        protected Transferable createTransferable(JComponent c) {
            String text = details.getSelectedText();
            if ("".equals(text)) {
                details.selectAll();
                text = details.getSelectedText();
                details.select(-1, -1);
            }
            return new StringSelection(text);
        }

        public int getSourceActions(JComponent c) {
            return TransferHandler.COPY;
        }

    }

    /**
     * Converts the incoming string to an escaped output string. This method
     * is far from perfect, only escaping <, > and &amp; characters
     */
    private static String escapeXml(String input) {
        String s = input.replace("&", "&amp;");
        s = s.replace("<", "<");
        return s = s.replace(">", ">");
    }

    public static void main(String[] args) {
        JFrame f = new JFrame();
        try {
            int a = 1 / 0;
        } catch (Exception e) {
           MXInfoDialog.showDialog(f, "Critical Error",
                   "<html><body>Some <b>critical error</b> has occured." +
                   " You may need to restart this application. Message #SC21." +
                   "</body></html>", e);
        }

    }
}
