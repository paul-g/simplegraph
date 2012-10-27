package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JTabbedPane;


/*
 * @author Paul Grigoras
 */

public class MyHandler implements ActionListener {

	JFileChooser fc;
	
	MainFrame mainframe;
	JTabbedPane jt;
	String msg;
	public MyHandler(MainFrame mainframe){
		this.fc=mainframe.fc;
		this.mainframe=mainframe;
		jt=mainframe.jtb;
	}
	public void actionPerformed(ActionEvent ae){
		String arg=(String)ae.getActionCommand();
		if (arg.equals("New tab")) {
			ApplicationPanel newPanel=new ApplicationPanel("New tab",mainframe);
			jt.addTab("New tab",newPanel);
			mainframe.add(jt);
		}else if (arg.equals("Exit")){
			System.exit(0);
		}else if (arg.equals("Close selected tab")){
			int i=jt.getSelectedIndex();
			if (i!=-1) jt.remove(i);
		}else if (arg.equals("Save selected tab") && jt.getTabCount()>0){
			int i=jt.getSelectedIndex();
			ApplicationPanel ap=(ApplicationPanel)jt.getComponentAt(i);
			File file = fc.getSelectedFile();
			fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			if (file!=null){
			try {
				ImageIO.write(ap.jp.img, "png", file);
			} catch (IOException e){}}
			file=null;
			
		}else if (arg.equals("Save all tabs") && jt.getTabCount()>0){
			int tabn=jt.getTabCount();
			int i;
			
			File filePath = fc.getSelectedFile();
			String filePathString=filePath.toString();
			String name="",extension="";
			for(i=0;i<filePathString.length();i++)
			 if (filePathString.charAt(i)!='.') name+=filePathString.charAt(i);
			 else break;
			for(;i<filePathString.length();i++) extension+=filePathString.charAt(i);
			
			
			fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			if (filePath!=null){
			for(i=0;i<tabn;i++){
				ApplicationPanel ap=(ApplicationPanel)jt.getComponentAt(i);			
				try {
					File file=new File(name+" "+i+extension);
					ImageIO.write(ap.jp.img, "png", file);
				} catch (IOException e){}}
			
			}
			filePath=null;
		}else if (arg.equals("Supported functions")) {
			mainframe.SupportedFunctionsFrame.setVisible(true);
		}
		else if (arg.equals("Default accuracy") || arg.equals("about")){
			mainframe.ComingSoonFrame.setVisible(true);
		}
		
		mainframe.validate();
	}

}
