import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.event.*;
import javax.imageio.*;
import java.io.*;

/*
 * @author Paul Grigoras
 */

public class ApplicationPanel extends JPanel implements TableModelListener{
	
	private static final long serialVersionUID = 1L;
	
	int OPTION_PANEL_MAX_HEIGHT=200;
	int OPTION_PANEL_MIN_HEIGHT=50;
	FunctionGrapher jp;
	JTable tabel1;
	MainFrame mainframe;
	ApplicationPanel(String s,MainFrame mainframe){
		this.mainframe=mainframe;
		setLayout(new BorderLayout());
		Dimension d=Toolkit.getDefaultToolkit().getScreenSize();
		tabel1=new JTable(new MyTableModel());
	
		JScrollPane jsp=new JScrollPane(tabel1);
		jp=new FunctionGrapher();
		jp.mainframe=mainframe;
		jp.appanel=this;
	
		jp.setMinimumSize(new Dimension(0,d.height-OPTION_PANEL_MAX_HEIGHT));
		jp.setPreferredSize(new Dimension(0,d.height-OPTION_PANEL_MAX_HEIGHT));
				
		JSplitPane jsplit=new JSplitPane(JSplitPane.VERTICAL_SPLIT,jp,jsp);
		jsp.setMinimumSize(new Dimension(0,OPTION_PANEL_MIN_HEIGHT));
		jsp.setPreferredSize(new Dimension(d.width,OPTION_PANEL_MIN_HEIGHT));
		jsplit.setResizeWeight(1);
		
		add(jsplit,BorderLayout.CENTER);
		setMinimumSize(new Dimension(d.width,d.height));
		jsplit.setOneTouchExpandable(true);
		  
		// Set the combobox editor on the 1st visible column
		TableColumn col = tabel1.getColumnModel().getColumn(1);
		col.setPreferredWidth(600);

		tabel1.getModel().addTableModelListener(this);
	}

	

	public void tableChanged(TableModelEvent e){
		TableModel model=(TableModel)e.getSource();
		int row=(int)e.getFirstRow();
		int col=(int)e.getColumn();
		String info=(String) model.getValueAt(row,col);
		if(row==0) {String infoTitle;
					int i=this.mainframe.jtb.getSelectedIndex(); 
					if (info.length()>20) infoTitle=new String(info.substring(0,20)+"...");
					else infoTitle=info;
					this.mainframe.jtb.setTitleAt(i,infoTitle);  
					//this.jp.function_name=info;
					this.jp.getFunctions(info);
					}
		else if(row==1) this.jp.getColours(info);
		else if (row==2) {if (info.equals("true")) this.jp.displayGrid=true; else this.jp.displayGrid=false;}
		else if (row==3) {if (info.equals("true") )this.jp.displayAxisValues=true; else this.jp.displayAxisValues=false;}
		else if (row==4) {if (info.equals("true")) this.jp.showAsymp=true;else this.jp.showAsymp=false;}
		else if (row==5) {int thick=Integer.parseInt(info); if (thick<=10) this.jp.thick=thick; else {this.jp.thick=10; tabel1.setValueAt("10",5,1);}}
		else if (row==6) {if (info.equals("auto")) {this.jp.accuracy=1;this.jp.AutoAccuracy=true;} else {int acc=Integer.parseInt(info); if (acc<=4) this.jp.accuracy=acc; else {this.jp.accuracy=4;tabel1.setValueAt("4",6,1);}}}
		else if (row==7) {if (info!=null) this.jp.increasedAcc=info; }
		this.jp.repaint();
	}
}

class PMyHandler implements ActionListener{
	//final JFileChooser fc = new JFileChooser();
	FunctionGrapher mjp;
	Dimension d;
	PMyHandler(FunctionGrapher mjp){
		this.mjp=mjp;
	}
	public void actionPerformed(ActionEvent ae){
		String arg=(String)ae.getActionCommand();
		if (arg.equals("Fullscreen")){
			d=Toolkit.getDefaultToolkit().getScreenSize();
			FunctionGrapher jp2=mjp.cloneGrapher();
			JWindow jw=new JWindow();
			jw.getContentPane().add(jp2);
			jw.setSize(d.width,d.height);
			jw.setAlwaysOnTop(true);
			jp2.jwp=jw;
			jp2.fullscreen.setText("Normal");
			jw.setVisible(true);
			jp2.popup.remove(jp2.closeTab);
				
		}else if (arg.equals("Normal")){
			mjp.jwp.dispose();
			mjp.jwp=null;
			JMenuItem close=new JMenuItem("Close this tab");
			mjp.popup.add(close);
			close.addActionListener(new PMyHandler(mjp));
		
		}else if(arg.equals("Zoom in"))	{mjp.zoom+=mjp.zoom/2;mjp.repaint();}
		else if(arg.equals("Zoom out"))	{mjp.zoom-=mjp.zoom/2;mjp.repaint();}
		else if (arg.equals("Save")) { 	JFileChooser fc=mjp.mainframe.fc;
										if (mjp.jwp!=null)mjp.jwp.setAlwaysOnTop(false);
										if (mjp.jwp!=null)mjp.jwp.setAlwaysOnTop(true);
										File file = fc.getSelectedFile();
										fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
										if (file!=null){
										try {
												
												ImageIO.write(mjp.img, "png", file);
												} catch (IOException e){}}
										file=null;
										}
										
		else if(arg.equals("Close this tab")) {    
			if (mjp.jwp!=null) {	mjp.mainframe.setVisible(true);
									mjp.jwp.dispose();
									mjp.jwp=null;
								}
			else{
			JTabbedPane pane=mjp.mainframe.jtb;
			int i = pane.indexOfComponent(mjp.appanel);
			if (i != -1) pane.remove(i);
			}
		}
	}
}
