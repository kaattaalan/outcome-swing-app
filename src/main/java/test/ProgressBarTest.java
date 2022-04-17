package test;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class ProgressBarTest extends JFrame {


    private JPanel contentPane;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    ProgressBarTest frame = new ProgressBarTest();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public ProgressBarTest() throws ExecutionException, InterruptedException {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        JProgressBar progress = new JProgressBar();
        progress.setStringPainted(true);
        contentPane.add(new JLabel("Loop progress is: "), BorderLayout.NORTH);
        contentPane.add(progress, BorderLayout.EAST);
        JTextArea textArea = new JTextArea();
        contentPane.add(textArea,BorderLayout.SOUTH);
        setContentPane(contentPane);
        ProgressWorker worker = new ProgressWorker(progress,textArea,"a",10);
        worker.execute();
    }

    private static class ProgressWorker extends SwingWorker<String, String> {
        private final JProgressBar progress;
        private final JTextArea textArea;
        String s;
        int count;

        public ProgressWorker(JProgressBar progress,JTextArea textArea,String s, int count) {
            this.textArea = textArea;
            this.progress = progress;
            this.s=s;
            this.count=count;
        }

        @Override
        protected String doInBackground() throws Exception {
            for(int i=1;i<count;i++){
                s += s;
                publish(s);
                TimeUnit.MILLISECONDS.sleep(1000);
            }
            return s;
        }

        @Override
        protected void process(List<String> chunks) {
            textArea.append(chunks.get(chunks.size() - 1));
            progress.setValue(progress.getValue() + 1);
            super.process(chunks);
        }

        @Override
        protected void done() {
            progress.setValue(100);
            String val = "";
            try {
                val = get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            textArea.setEditable(false);
            textArea.setText(val);
            super.done();
        }
    }
}