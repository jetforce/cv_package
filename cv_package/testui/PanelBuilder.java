package cv_package.testui;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.opencv.core.Core;
import org.opencv.core.Mat;

public class PanelBuilder {

	JPanel panel;
	
	JFrame frame;
	
	public PanelBuilder(JPanel panel, JFrame frame) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		this.panel = panel;
		this.frame = frame;
	}
	
	public void addImage(String sLabel, String filepath) {
		ImageIcon img = new ImageIcon(filepath);
		JLabel label = new JLabel(sLabel, img, JLabel.CENTER);
		label.setHorizontalTextPosition(JLabel.CENTER);
		label.setVerticalTextPosition(JLabel.BOTTOM);
		panel.add(label);
		panel.repaint();
		panel.revalidate();
//		refreshUI();
	}
	
	public void refreshUI() {
		frame.repaint();
		frame.revalidate();
	}
	
}
