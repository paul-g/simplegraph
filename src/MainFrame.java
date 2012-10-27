import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

/*
 * @author Paul Grigoras
 */

public class MainFrame extends JFrame{
	
	private static final long serialVersionUID = 1L;
	
	JMenu file,options,help;
	JMenuItem nou,saveTab,saveAllTabs,closeSelTab,exit,defAccuracy,defIncAccuracy,supFunctions,about;
	JTabbedPane jtb;
	final JFileChooser fc = new JFileChooser();
	final JFrame SupportedFunctionsFrame=new JFrame();
	final JFrame ComingSoonFrame=new JFrame("Coming soon!!");
	private String newline = "\n";
	
	
	public static void main(String args[]){
		MainFrame ApplicationFrame=new MainFrame();
		ApplicationFrame.setVisible(true);
	}
	
	MainFrame(){
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setExtendedState(MAXIMIZED_BOTH);
		setTitle("FGrafic");
		setMinimumSize(new Dimension(400,400));
		toFront();
		
		jtb=new JTabbedPane();
		JMenuBar menuBar=new JMenuBar();
		file=new JMenu("File");
		help=new JMenu("Help");
		options=new JMenu("Options");
		nou=new JMenuItem("New tab");
		MyHandler handler=new MyHandler(this);
		exit=new JMenuItem("Exit");
		saveTab=new JMenuItem("Save selected tab");
		saveAllTabs=new JMenuItem("Save all tabs");
		closeSelTab=new JMenuItem("Close selected tab");
		defAccuracy=new JMenuItem("Default accuracy");
		defIncAccuracy=new JMenuItem("Default increased accuracy");
		about=new JMenuItem("About");
		supFunctions=new JMenuItem("Supported functions");
		JMenuItem off=new JMenuItem("Off");
		
		file.add(nou);
		file.add(saveTab);
		file.add(saveAllTabs);
		file.add(closeSelTab);
		file.add(exit);
		help.add(supFunctions);
		help.add(about);
		options.add(defAccuracy);
		help.add(about);
		defIncAccuracy.add(off);
		menuBar.add(file);
		menuBar.add(options);
		menuBar.add(help);
	
		file.setMnemonic(KeyEvent.VK_F);
		nou.setMnemonic(KeyEvent.VK_N);
		nou.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.ALT_MASK));
		exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.ALT_MASK));
		saveTab.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		saveAllTabs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		saveAllTabs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
		closeSelTab.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
		
		help.setMnemonic(KeyEvent.VK_H);
		
		options.setMnemonic(KeyEvent.VK_O);
	
		getContentPane().add(jtb);
		nou.addActionListener(handler);
		exit.addActionListener(handler);
		saveTab.addActionListener(handler);
		saveAllTabs.addActionListener(handler);
		closeSelTab.addActionListener(handler);
		supFunctions.addActionListener(handler);
		about.addActionListener(handler);
		defAccuracy.addActionListener(handler);
		setJMenuBar(menuBar);
		
		SupportedFunctionsFrame.setSize(500,200);
		SupportedFunctionsFrame.setTitle("FGrafic Help - Supported functions");
		SupportedFunctionsFrame.setAlwaysOnTop(true);
		SupportedFunctionsFrame.setLocation(300,100);
		JTextPane textPane = new JTextPane();
		JTextPane textPane2=new JTextPane();
		JPanel textPanel=new JPanel(new GridLayout());
		JSplitPane textSplit=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,textPane,textPane2);
		textPanel.add(textSplit);
		textPane.setEditable(false);
		textPane2.setEditable(false);
		
		JScrollPane scrollPane=new JScrollPane(textPanel);
		SupportedFunctionsFrame.add(scrollPane);

		String[] functionForm =
                { "+ - * / ()","base^exponent","sqrt()","abs","[],floor","ln()","sin() ","cos()","tg(x),tan(x)","arcsin()","arccos()","arctg(),arctan()"
                 };

        String[] functionDescription =
                { " Plus, minus, multiplication, division, grouping symbol. Multiplication can be omitted.","Exponentiation baseexponent", "Square root of argument","Absolute value of argument","Rounds arg down","Natural logarithm of argument","Sine","Cosine","Tangent","Arc sine","Arc cosine","Arc tangent"
                };
		
		StyledDocument doc = textPane.getStyledDocument();
		StyledDocument doc2 = textPane2.getStyledDocument();
		Style def = StyleContext.getDefaultStyleContext().
                        getStyle(StyleContext.DEFAULT_STYLE);
		Style regular = doc2.addStyle("regular", def);
        StyleConstants.setFontFamily(def, "SansSerif");

        Style s = doc.addStyle("italic", regular);
        StyleConstants.setItalic(s, true);

        s = doc.addStyle("bold", regular);
        StyleConstants.setBold(s, true);

        s = doc.addStyle("small", regular);
        StyleConstants.setFontSize(s, 10);

        s = doc.addStyle("large", regular);
        StyleConstants.setFontSize(s, 16);		

        try {
            for (int i=0; i < functionForm.length; i++) {
				//while(functionForm[i].length()<100) functionForm[i]+=" ";
				doc.insertString(doc.getLength(), functionForm[i]+newline,
                                 doc.getStyle("bold"));
				doc2.insertString(doc2.getLength(),functionDescription[i]+newline,doc2.getStyle("regular"));
				
				
            }
        } catch (BadLocationException ble) {
            System.err.println("Couldn't insert initial text into text pane.");
        }
		
		ComingSoonFrame.setSize(500,500);
		ComingSoonFrame.setLocation(300,100);
		JTextPane CSPane=new JTextPane();
		CSPane.setText("Coming soon!!!!");
		CSPane.setEditable(true);
		ComingSoonFrame.add(CSPane);
		ComingSoonFrame.setAlwaysOnTop(true);
	}
	

	
}