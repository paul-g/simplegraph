package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

public class ApplicationPanel extends JPanel implements TableModelListener{
	
	private static final long serialVersionUID = 1L;
	
	private static final int OPTION_PANEL_MAX_HEIGHT=200;
	private static final int OPTION_PANEL_MIN_HEIGHT=50;
	private FunctionGrapher functionGrapher;
	private JTable tabel1;
	private MainFrame mainframe;
	
	ApplicationPanel(String s,MainFrame mainframe){
		this.mainframe=mainframe;
		setLayout(new BorderLayout());
		Dimension d=Toolkit.getDefaultToolkit().getScreenSize();
		tabel1=new JTable(new SettingsTableModel());
	
		JScrollPane jsp=new JScrollPane(tabel1);
		functionGrapher=new FunctionGrapher();
		functionGrapher.mainframe=mainframe;
		functionGrapher.appanel=this;
	
		functionGrapher.setMinimumSize(new Dimension(0,d.height-OPTION_PANEL_MAX_HEIGHT));
		functionGrapher.setPreferredSize(new Dimension(0,d.height-OPTION_PANEL_MAX_HEIGHT));
				
		JSplitPane jsplit=new JSplitPane(JSplitPane.VERTICAL_SPLIT,functionGrapher,jsp);
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
					this.functionGrapher.getFunctions(info);
					}
		else if(row==1) this.functionGrapher.getColours(info);
		else if (row==2) {if (info.equals("true")) this.functionGrapher.displayGrid=true; else this.functionGrapher.displayGrid=false;}
		else if (row==3) {if (info.equals("true") )this.functionGrapher.displayAxisValues=true; else this.functionGrapher.displayAxisValues=false;}
		else if (row==4) {if (info.equals("true")) this.functionGrapher.showAsymp=true;else this.functionGrapher.showAsymp=false;}
		else if (row==5) {int thick=Integer.parseInt(info); if (thick<=10) this.functionGrapher.thick=thick; else {this.functionGrapher.thick=10; tabel1.setValueAt("10",5,1);}}
		else if (row==6) {if (info.equals("auto")) {this.functionGrapher.accuracy=1;this.functionGrapher.AutoAccuracy=true;} else {int acc=Integer.parseInt(info); if (acc<=4) this.functionGrapher.accuracy=acc; else {this.functionGrapher.accuracy=4;tabel1.setValueAt("4",6,1);}}}
		else if (row==7) {if (info!=null) this.functionGrapher.increasedAcc=info; }
		this.functionGrapher.repaint();
	}
	

	public FunctionGrapher getFunctionGrapher() {
		return functionGrapher;
	}

}
