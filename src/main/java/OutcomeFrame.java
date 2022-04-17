import Util.Progress;
import Util.Props;
import test.WorkerListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

class OutcomeFrame
        extends JFrame
        implements ActionListener {

    Map<String, String> urlMap;
    private Map<String, String> gameMap = new HashMap<>();
    private OutComeWorker worker;
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


    // constructor, to initialize the components
    // with default values.
    public OutcomeFrame() throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        setTitle("Outcome finder");
        setBounds(300, 90, 900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        c = getContentPane();
        c.setLayout(null);

        title = new JLabel("Outcome finder");
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

        if(Props.credsExists()) {
            uname.setText(Props.artifactory_username);
            password.setText(Props.artifactory_password);
        }

        setVisible(true);
    }

    // method actionPerformed()
    // to get the action performed
    // by the user and act accordingly
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == sub) {
            tout.setText("");
            //Sends func.interfaces to perform while progress and done
            worker = new OutComeWorker(artUrl.getText(), uname.getText(), password.getText(), gameMap, c -> {
                tout.append(String.format("%s - %s\n", c.getKey(), c.getValue() == null ? "Not Found" : "Found"));
            }, c -> {
                progress.setValue(100);
                urlMap = c;
            });
            worker.addPropertyChangeListener(new WorkerListener(progress));
            worker.execute();
        } else if (e.getSource() == fbutton) {
            fileChooser.showOpenDialog(c);
        } else if (e.getSource() == fileChooser) {
            if (fileChooser.getSelectedFile() != null) {
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
            sub.setVisible(false);
        }

    }

    public interface UIUpdate {
        void perform(Progress progress);
    }

    public interface FinishProcess {
        void perform(Map<String, String> urlMap);
    }

}
