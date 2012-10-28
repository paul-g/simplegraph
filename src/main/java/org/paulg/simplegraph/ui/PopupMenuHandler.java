package org.paulg.simplegraph.ui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.JWindow;

public class PopupMenuHandler implements ActionListener {

	private FunctionGrapher mjp;
	private Dimension d;

	public PopupMenuHandler(FunctionGrapher mjp) {
		this.mjp = mjp;
	}

	public void actionPerformed(ActionEvent ae) {
		String arg = (String) ae.getActionCommand();
		if (arg.equals("Fullscreen")) {
			d = Toolkit.getDefaultToolkit().getScreenSize();
			FunctionGrapher jp2 = mjp.cloneGrapher();
			JWindow jw = new JWindow();
			jw.getContentPane().add(jp2);
			jw.setSize(d.width, d.height);
			jw.setAlwaysOnTop(true);
			jp2.jwp = jw;
			jp2.fullscreen.setText("Normal");
			jw.setVisible(true);
			jp2.popup.remove(jp2.closeTab);

		} else if (arg.equals("Normal")) {
			mjp.jwp.dispose();
			mjp.jwp = null;
			JMenuItem close = new JMenuItem("Close this tab");
			mjp.popup.add(close);
			close.addActionListener(new PopupMenuHandler(mjp));

		} else if (arg.equals("Zoom in")) {
			mjp.zoom += mjp.zoom / 2;
			mjp.repaint();
		} else if (arg.equals("Zoom out")) {
			mjp.zoom -= mjp.zoom / 2;
			mjp.repaint();
		} else if (arg.equals("Save")) {
			JFileChooser fc = mjp.mainframe.fc;
			if (mjp.jwp != null)
				mjp.jwp.setAlwaysOnTop(false);
			if (mjp.jwp != null)
				mjp.jwp.setAlwaysOnTop(true);
			File file = fc.getSelectedFile();
			fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			if (file != null) {
				try {

					ImageIO.write(mjp.img, "png", file);
				} catch (IOException e) {
				}
			}
			file = null;
		}
		else if (arg.equals("Close this tab")) {
			if (mjp.jwp != null) {
				mjp.mainframe.setVisible(true);
				mjp.jwp.dispose();
				mjp.jwp = null;
			} else {
				JTabbedPane pane = mjp.mainframe.jtb;
				int i = pane.indexOfComponent(mjp.appanel);
				if (i != -1)
					pane.remove(i);
			}
		}
	}

}