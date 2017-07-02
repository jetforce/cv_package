package cv_package.testui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.activation.MimetypesFileTypeMap;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import cv_package.testgen.Segmenter;
import cv_package.testgen.Segmenter2;

public class TestUI {
	
	JFrame frame;
	JPanel bgPanel, fileChooserPanel, lowerPanel, progressPanel;
	JButton fileChooserBtn, startBtn;
	JLabel fileName;
	JScrollPane scrollPane;
	int fWidth, fHeight;
	boolean ready = false;
//	String filepath = "C:/Users/Hannah/Desktop/yo2.png";
//	String filepath = "C:/Users/Hannah/Desktop/test-yo2.jpg";
//	String filepath = "C:/Users/Hannah/Desktop/yox2.png";
//	String filepath = "Eto na talaga/yox2.png";
//	String filepath = "C:/Users/Hannah/Desktop/here we go/form-1.png";
	String filepath = "C:/Users/Hannah/Desktop/ft.jpg";

	private static PanelBuilder pb = PanelBuilder.getInstance();
	
	public static void main(String args[]) {
		TestUI ui = new TestUI();
		ui.init();
	}
	
	public void init() {
		frame = new JFrame("#Segmenter");
		bgPanel = new JPanel(new FlowLayout());
		fileChooserPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 6));
		lowerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		fileChooserBtn = new JButton("Choose File");
		startBtn = new JButton("PROCESS");
		fileName = new JLabel("No file chosen");
		
		buildFrame(.75, .75);
		buildPanel();
		buildPanel(fileChooserPanel, 1, .065);
		buildButton(fileChooserPanel, startBtn);
		buildButton(fileChooserPanel, fileChooserBtn);
		fileChooserPanel.add(fileName);
		startBtn.setVisible(false);

		buildPanel(lowerPanel, 1, .90);
		buildScroll(scrollPane, .98, .84);
		
		fileChooserPanel.add(fileName);
		
		fileChooserBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				String s = chooseFile();
				if(s != null) {
					fileName.setText(s);
					startBtn.setVisible(true);
					filepath = s;
				}
				else{
					fileName.setText("Not an image file. Please choose again..");
					startBtn.setVisible(false);
				}
			}
		});
		
		startBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				startImageProcess();
			}
		});
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		autoInput();
	}
	
	public void startImageProcess() {
		progressPanel.removeAll();
		pb.init(progressPanel, frame);
		Segmenter2 s = new Segmenter2();
		try {
			s.segment(filepath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void autoInput() {
		startImageProcess();
	}
	
	public String chooseFile(){
		JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(new File("C:/Users/Hannah/Desktop"));
		int ret = fc.showOpenDialog(null);
		if(ret == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			if(isImage2(file)) {
				String filename = file.getAbsolutePath();
				return filename;
			}
			else
				return null;
		}
		else {
			return null;
		}
	}
	
	public boolean isImage2(File f) {
		try {
		    Image image = ImageIO.read(f);
		    if (image == null) {
	            System.out.println(f.getName() + " -> NON IMAGE");
	            return false;
		    }
		    else {
	            System.out.println(f.getName() + " -> IMAGE");
	            return true;
		    }
		} catch(IOException ex) {
            System.out.println(f.getName() + " -> NON IMAGE");
            return false;
		}
	}
	
	public boolean isImage(File f) {
		String mimetype= new MimetypesFileTypeMap().getContentType(f);
        String type = mimetype.split("/")[0];
        if(type.equals("image")) {
            System.out.println(f.getName() + " -> IMAGE");
            return true;
        }
        else {
            System.out.println(f.getName() + " -> NON IMAGE");
            return false;
        }
	}
	
	public void buildFrame(double wPerc, double hPerc) {
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		fWidth = (int) (dim.width*wPerc);
		fHeight= (int) (dim.height*hPerc);
		frame.setSize(fWidth, fHeight);
		frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.setBackground(Color.GRAY);
		frame.setVisible(true);
	}
	
	public void buildPanel() {
		bgPanel.setSize(fWidth, fHeight);
		bgPanel.setBackground(Color.WHITE);
		frame.add(bgPanel);
		refreshUI();
	}
	
	public void buildPanel(JPanel panel, double wPerc, double hPerc) {
		panel.setPreferredSize(new Dimension((int)(fWidth*wPerc), (int)(fHeight*hPerc)));
		panel.setBackground(Color.WHITE);
//		System.out.println("> " + (int)(fWidth*wPerc) + ", " + (int)(fHeight*hPerc));
		bgPanel.add(panel);
		refreshUI();
	}
	
	public void buildScroll(JScrollPane scroll, double wPerc, double hPerc) {
		
		progressPanel = new JPanel(new GridLayout(1, 0, 0, 0));
//		progressPanel.setPreferredSize(new Dimension((int)(fWidth*.98), (int)(fHeight*.84)));
		progressPanel.setBackground(Color.WHITE);
		
		scrollPane = new JScrollPane(progressPanel);
		scrollPane.setPreferredSize(new Dimension((int)(fWidth*.98), (int)(fHeight*.84)));
//		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		scrollPane.setFocusable(false);
		scrollPane.getVerticalScrollBar().setUnitIncrement(20);
		
		lowerPanel.add(scrollPane);
		
		refreshUI();
	}
	
	public void buildButton(JPanel panel, JButton btn) {
		btn.setPreferredSize(new Dimension(100, 40));
		btn.setEnabled(true);
		btn.setBackground(Color.WHITE);
		btn.setFocusable(false);
//		btn.setVerticalAlignment();
		panel.add(btn);
		refreshUI();
	}
	
	public void refreshUI() {
		frame.repaint();
		frame.revalidate();
	}
	
}
