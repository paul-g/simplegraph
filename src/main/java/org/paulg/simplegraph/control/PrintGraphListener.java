package org.paulg.simplegraph.control;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import org.paulg.simplegraph.ui.FunctionGrapher;


public class PrintGraphListener implements Printable, ActionListener {

	private FunctionGrapher mjp;

	public PrintGraphListener(FunctionGrapher mjp) {
		this.mjp = mjp;
	}

	public int print(Graphics g, PageFormat pf, int page) throws PrinterException {
		Dimension d = mjp.getSize();
		double panelWidth = d.width;
		double pageWidth = pf.getImageableWidth();

		double scale = pageWidth / panelWidth;

		if (page > 0) {
			return NO_SUCH_PAGE;
		}
		Graphics2D g2d = (Graphics2D) g;
		g2d.translate(pf.getImageableX(), pf.getImageableY());
		g2d.scale(scale, scale);
		mjp.paint(g2d);
		return PAGE_EXISTS;
	}

	public void actionPerformed(ActionEvent e) {
		PrinterJob job = PrinterJob.getPrinterJob();
		job.setPrintable(this);
		boolean ok = job.printDialog();
		if (ok) {
			try {
				job.print();
			} catch (PrinterException ex) {
			}

		}
	}
}
