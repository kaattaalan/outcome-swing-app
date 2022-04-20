package ui;

import util.OutComeUtil;
import util.Progress;
import util.Props;
import worker.DownloadWorker;
import worker.OutComeWorker;
import worker.WorkerListener;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import static util.Props.APP_TITLE;

public class OutcomeFrame
        extends JFrame
        implements ActionListener {

    Map<String, String> urlMap;
    private Map<String, String> gameMap = new HashMap<>();
    // Components of the Form
    private Container c;
    private JLabel title;
    private JLabel fclabel;
    private JButton sub;
    private JTextArea tout;
    private JButton fbutton;
    private JFileChooser fileChooser;
    private JTextField password;
    private JTextField uname;
    private JLabel urlTitle;
    private JTextField artUrl;
    private JLabel unameTitle;
    private JLabel passLabel;
    private JButton reset;
    private JProgressBar progress;
    private JFileChooser saveFC;
    private JButton saveButton;
    private JButton downloadButton;
    private JFileChooser downFC;


    // constructor, to initialize the components
    // with default values.
    public OutcomeFrame() throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        setTitle(APP_TITLE);
        setBounds(300, 90, 900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        c = getContentPane();
        c.setLayout(null);

        title = new JLabel(APP_TITLE);
        title.setFont(new Font("Arial", Font.PLAIN, 30));
        title.setSize(300, 30);
        title.setLocation(300, 30);
        c.add(title);

        fclabel = new JLabel("Choose JenkinsFile");
        fclabel.setFont(new Font("Arial", Font.PLAIN, 15));
        fclabel.setSize(300, 20);
        fclabel.setLocation(100, 100);
        c.add(fclabel);

        fbutton = new JButton("Browse");
        fbutton.setFont(new Font("Arial", Font.PLAIN, 12));
        fbutton.setSize(80, 20);
        fbutton.setLocation(400, 100);
        fbutton.addActionListener(this);
        c.add(fbutton);

        UIManager.put("FileChooser.readOnly", Boolean.TRUE);
        fileChooser = new JFileChooser("./");
        fileChooser.setFont(new Font("Arial", Font.PLAIN, 15));
        fileChooser.addActionListener(this);
        c.add(fileChooser);

        urlTitle = new JLabel("Arifactory URL");
        urlTitle.setFont(new Font("Arial", Font.PLAIN, 12));
        urlTitle.setSize(100, 20);
        urlTitle.setLocation(100, 150);
        c.add(urlTitle);

        artUrl = new JTextField(Props.artifactory_url);
        artUrl.setFont(new Font("Arial", Font.PLAIN, 12));
        artUrl.setSize(300, 20);
        artUrl.setLocation(180, 150);
        artUrl.setEditable(false);
        c.add(artUrl);

        unameTitle = new JLabel("UserName");
        unameTitle.setFont(new Font("Arial", Font.PLAIN, 15));
        unameTitle.setSize(100, 20);
        unameTitle.setLocation(100, 200);
        c.add(unameTitle);

        uname = new JTextField();
        uname.setFont(new Font("Arial", Font.PLAIN, 15));
        uname.setSize(190, 20);
        uname.setLocation(200, 200);
        c.add(uname);

        passLabel = new JLabel("Password");
        passLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        passLabel.setSize(100, 20);
        passLabel.setLocation(100, 250);
        c.add(passLabel);

        password = new JPasswordField();
        password.setFont(new Font("Arial", Font.PLAIN, 15));
        password.setSize(190, 20);
        password.setLocation(200, 250);
        c.add(password);

        saveButton = new JButton("Save Results to file");
        saveButton.setFont(new Font("Arial", Font.PLAIN, 15));
        saveButton.setSize(150, 20);
        saveButton.setLocation(100, 300);
        saveButton.setToolTipText("Save Results to a CSV file.");
        saveButton.setVisible(false);
        saveButton.addActionListener(this);
        c.add(saveButton);

        downloadButton = new JButton("Download files");
        downloadButton.setFont(new Font("Arial", Font.PLAIN, 15));
        downloadButton.setToolTipText("Download files to the selected folder.");
        downloadButton.setSize(150, 20);
        downloadButton.setLocation(300, 300);
        downloadButton.setVisible(false);
        downloadButton.addActionListener(this);
        c.add(downloadButton);

        saveFC = new JFileChooser("./");
        saveFC.setDialogTitle("Save File..");
        saveFC.addActionListener(this);
        saveFC.setFileFilter(new FileNameExtensionFilter("CSV file", "csv"));

        downFC = new JFileChooser("./");
        downFC.setDialogTitle("Download files to..");
        downFC.addActionListener(this);
        downFC.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        downFC.setDialogType(JFileChooser.SAVE_DIALOG);
        downFC.setApproveButtonText("Select");

        progress = new JProgressBar();
        progress.setFont(new Font("Arial", Font.PLAIN, 12));
        progress.setStringPainted(true);
        progress.setSize(350, 20);
        progress.setLocation(100, 400);
        c.add(progress);

        sub = new JButton("Submit");
        sub.setFont(new Font("Arial", Font.PLAIN, 15));
        sub.setSize(100, 20);
        sub.setLocation(150, 450);
        sub.addActionListener(this);
        sub.setVisible(false);
        c.add(sub);

        reset = new JButton("Reset");
        reset.setFont(new Font("Arial", Font.PLAIN, 15));
        reset.setSize(100, 20);
        reset.setLocation(270, 450);
        reset.addActionListener(this);
        c.add(reset);

        tout = new JTextArea();
        tout.setFont(new Font("Arial", Font.PLAIN, 15));
        tout.setSize(300, 400);
        tout.setLocation(500, 100);
        tout.setLineWrap(true);
        tout.setEditable(false);
        tout.setAutoscrolls(true);
        c.add(tout);

        populateCreds();

        setVisible(true);
    }

    // method actionPerformed()
    // to get the action performed
    // by the user and act accordingly
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == sub) {
            tout.setText("");
            //Sends func.interfaces to perform while progress and done
            OutComeWorker worker = new OutComeWorker(artUrl.getText(), uname.getText(), password.getText(), gameMap, c -> {
                if (c.getValue() == null) {
                    tout.append(String.format("%s - %s\n", c.getKey(), "Not Found"));
                }
            }, c -> {
                progress.setValue(100);
                urlMap = c;
                setSave();
            });
            worker.addPropertyChangeListener(new WorkerListener(progress));
            worker.execute();
        } else if (e.getSource() == fbutton) {
            fileChooser.showOpenDialog(c);
        } else if (e.getSource() == fileChooser) {
            if (e.getActionCommand().equals(JFileChooser.APPROVE_SELECTION) && fileChooser.getSelectedFile() != null) {
                gameMap = OutComeUtil.getGameMap(fileChooser.getSelectedFile().getAbsolutePath());
                fclabel.setText(fileChooser.getSelectedFile().getAbsolutePath());
                tout.setText(String.format("Found %d games \n %s", gameMap.size(), gameMap));
                sub.setVisible(true);
            }
        } else if (e.getSource() == reset) {
            tout.setText("");
            password.setText("");
            uname.setText("");
            fclabel.setText("Choose JenkinsFile");
            artUrl.setText(Props.artifactory_url);
            populateCreds();
            sub.setVisible(false);
            saveButton.setVisible(false);
            downloadButton.setVisible(false);
        } else if (e.getSource() == saveFC) {
            if (saveFC.getSelectedFile() != null) {
                OutComeUtil.writeToFile(urlMap, saveFC.getSelectedFile());
            }
        } else if (e.getSource() == saveButton) {
            saveFC.showSaveDialog(c);
        } else if (e.getSource() == downloadButton) {
            progress.setValue(0);
            downFC.showSaveDialog(c);
        } else if (e.getSource() == downFC) {
            if (e.getActionCommand().equals(JFileChooser.APPROVE_SELECTION) && downFC.getSelectedFile() != null) {
                DownloadWorker worker = new DownloadWorker(urlMap, downFC.getSelectedFile(), uname.getText(), password.getText(),progress,tout);
                worker.addPropertyChangeListener(new WorkerListener(progress));
                worker.execute();
            }
        }

    }

    public void setSave() {
        saveButton.setVisible(true);
        downloadButton.setVisible(true);
    }

    public void populateCreds() {
        if (Props.credsExists()) {
            uname.setText(Props.artifactory_username);
            password.setText(Props.artifactory_password);
        }
    }

    public interface UIUpdate {
        void perform(Progress progress);
    }

    public interface FinishProcess {
        void perform(Map<String, String> urlMap);
    }

}
