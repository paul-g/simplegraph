package ui;

import interpreter.Function;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JWindow;

import control.PrintGraphListener;



/*
 * @author Paul Grigoras
 */

public class FunctionGrapher extends JPanel implements MouseMotionListener,
		MouseListener, MouseWheelListener {

	private static final long serialVersionUID = 1L;
	
	BufferedImage img=null;
	ApplicationPanel appanel;
	Graphics2D g2;
	MainFrame mainframe;
	JPopupMenu popup;
	JMenuItem fullscreen,zoomIn,zoomOut,save,print,closeTab;
	JWindow jwp;
	Dimension d;
	double zoom=(double)100;
	boolean displayAxisValues=true,displayGrid=true, showAsymp=true;
	String increasedAcc="auto";
	int divX=0,Ox,Oy,divY=0;
	int mPx,mPy;
	int thick=1,accuracy=1;
	boolean AutoAccuracy=true;
	
	Function []functions;
	int numFunctions;
	
	Color []Colours;
	int numColours;
	
	FunctionGrapher(){
		popup=new JPopupMenu();
		fullscreen = new JMenuItem("Fullscreen");
		print=new JMenuItem("Print");
		zoomIn=new JMenuItem("Zoom in");
		zoomOut=new JMenuItem("Zoom out");
		save=new JMenuItem("Save");
		closeTab=new JMenuItem("Close this tab");
		PopupMenuHandler pmh=new PopupMenuHandler(this);
		fullscreen.addActionListener(pmh);
		zoomIn.addActionListener(pmh);
		zoomOut.addActionListener(pmh);
		save.addActionListener(pmh);
		closeTab.addActionListener(pmh);
		print.addActionListener(new PrintGraphListener(this));
		popup.add(fullscreen);
		popup.add(zoomIn);
		popup.add(zoomOut);
		popup.add(save);
		popup.add(print);
		popup.add(closeTab);
	
		MouseListener poplistener=new PopupListener(this);
		addMouseListener(poplistener);
		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);
		
		functions=new Function[100];
		Colours=new Color[100];
	}
	
	
	public void drawGrid(Graphics g){
		int i=Ox;
		g.setColor(Color.lightGray);
		while (i<d.height){	
			g.drawLine(0,i,d.width,i);
			i+=zoom;
		}
		i=Ox;
		while (i>0){	
			g.drawLine(0,i,d.width,i);
			i-=zoom;
		}
		i=Oy;
		while (i<d.width){
			g.drawLine(i,0,i,d.height);
			i+=zoom;
		}
		i=Oy;
		while (i>0){
			g.drawLine(i,0,i,d.height);
			i-=zoom;
		}
		g.setColor(Color.white);
	}

	public void displayAxisValues(Graphics g){
		g.setColor(Color.darkGray);
		int i=Ox;
		int nr=0;
		g.setColor(Color.black);
		while (i<d.height){	
			
			g.drawString(Integer.toString(-nr),Oy,i);
			i+=zoom;
			nr++;
		}
		i=Ox;
		nr=0;
		while (i>0){	
			g.drawString(Integer.toString(nr),Oy,i);
			nr++;
			i-=zoom;
		}
		i=Oy;
		nr=0;
		while (i<d.width){
			g.drawString(Integer.toString(nr),i,Ox);
			i+=zoom;
			nr++;
		}
		i=Oy;
		nr=0;
		while (i>0){
			g.drawString(Integer.toString(-nr),i,Ox);
			nr++;
			i-=zoom;
		}
		g.setColor(Color.white);
			
	}
	public void drawAxis(Graphics g){
			g2.setColor(Color.black);
			g.drawLine(0,Ox,d.width,Ox);
			g.drawLine(d.width,Ox,d.width-5,Ox-5);
			g.drawLine(d.width,Ox,d.width-5,Ox+5);
			
			g.drawLine(Oy,0,Oy,d.height);
			g.drawLine(Oy,0,Oy-5,5);
			g.drawLine(Oy,0,Oy+5,5);
	}

	public void paintComponent(Graphics g){
	
		super.paintComponent(g);
			
		d=this.getSize();
		Ox=d.height/2+divY;
		Oy=d.width/2+divX;
		
		img=new BufferedImage(d.width,d.height,BufferedImage.TYPE_INT_ARGB);
		g2=img.createGraphics();
		g2.setColor(Color.WHITE);
		g2.fillRect(0, 0, d.width, d.height); 
		Graphics2D aux_g=(Graphics2D)g;
		g=g2;
			
		if (displayGrid) drawGrid(g);
		if (displayAxisValues) displayAxisValues(g);
		drawAxis(g);
		g.setColor(Color.black);

		BasicStroke wideStroke = new BasicStroke(thick);	
		g2.setStroke(wideStroke);	
		

		int i;	
		String s;
		i=0;
		boolean AutoincreasedAcc=true;
		if (increasedAcc.equals("auto")){
			for (i=0;i<numFunctions;i++){
				s=functions[i].law;
				if (s.indexOf("ln")!=-1||s.indexOf("[")!=-1||s.indexOf("arctg")!=-1||s.indexOf("arcsin")!=-1||s.indexOf("arccos")!=-1||s.indexOf("tg")!=-1 || s.indexOf("tan")!=-1 || s.indexOf("floor")!=-1) {AutoincreasedAcc=false; if (AutoAccuracy) accuracy=2;}
			}
		};
		
		double incUnit=1/Math.pow(10,accuracy-1);
		double rez,rez2;
		
		for (i=0;i<numFunctions;i++){
			g.setColor(Colours[i]);
			rez2=0;
			rez=0;
			for (double j=-Oy;j<=d.width-Oy;j+=incUnit){
				rez=functions[i].evaluate(j/zoom)*zoom;
				if (increasedAcc.equals("false") || !AutoincreasedAcc) {rez2=rez;}
				if (!Double.isNaN(rez) && !Double.isNaN(rez2)){
					if (!Double.isInfinite(rez) && !Double.isInfinite(rez2)){
					if (j!=-Oy)g2.draw(new Line2D.Double((int)(Oy+j),(int)(Ox-rez),(int)(Oy+j-1),(int)(Ox-rez2)));
					else g2.draw(new Line2D.Double((int)(Oy+j),(int)(Ox-rez),(int)(Oy+j),(int)(Ox-rez)));
					} else if (this.showAsymp==true) {Color c=g2.getColor(); g2.setColor(Color.magenta); g2.draw(new Line2D.Double((int)(Oy+j),0,(int)(Oy+(int)j),(int)(d.height)));g2.setColor(c);}
					
				}
				rez2=rez;
			}
			g.drawString(functions[i].law,2,10+15*i);
		}
		
		aux_g.drawImage(img,0,0,null);
	}
	
	public void getFunctions(String s){
		String []newFunctions;
		newFunctions=new String[100];
		int numnewFunctions=0;
		
		int i=0,poz;
		if (s.charAt(s.length()-1)!=';') s+=";";
		while (i<s.length() && i!=-1){
			poz=s.indexOf(';',i);
			if (poz!=-1) newFunctions[numnewFunctions++]=s.substring(i,poz);
			i=poz+1;
		}
		for (i=0;i<numnewFunctions;i++) 
			functions[i]=new Function(newFunctions[i]);
		numFunctions=numnewFunctions;
	}
	public void getColours(String s){
		if (!s.equals("")){
		String []newColours=new String[100];
		System.out.println(s);
		int i=0,poz,numnewColours=0;
		if (s.charAt(s.length()-1)!=';') s+=";";
		while (i<s.length()){
			poz=s.indexOf(";",i);
			if (poz!=-1) newColours[numnewColours++]=s.substring(i,poz);
			i=poz+1;
		}
		for(i=0;i<numnewColours;i++)
			if (newColours[i].equals("red"))	Colours[i]=new Color(Color.red.getRGB());
			else if (newColours[i].equals("blue")) Colours[i]=new Color(Color.blue.getRGB());
			else if (newColours[i].equals("green")) Colours[i]=new Color(Color.green.getRGB());
			else if (newColours[i].equals("yellow")) Colours[i]=new Color(Color.yellow.getRGB());
			else if (newColours[i].equals("orange")) Colours[i]=new Color(Color.orange.getRGB());
			else if (newColours[i].equals("pink")) Colours[i]=new Color(Color.pink.getRGB());
			else if (newColours[i].equals("magenta")) Colours[i]=new Color(Color.magenta.getRGB());
			else if (newColours[i].equals("cyan")) Colours[i]=new Color(Color.cyan.getRGB());
			
		numColours=numnewColours;
		}	
	}
	
	public FunctionGrapher cloneGrapher(){
		FunctionGrapher clonedPanel=new FunctionGrapher();
		clonedPanel.zoom=zoom;
		clonedPanel.jwp=jwp;
		clonedPanel.numFunctions=numFunctions;
		clonedPanel.functions=functions;
		clonedPanel.mainframe=mainframe;
		clonedPanel.Colours=Colours;
		clonedPanel.numColours=numColours;
		clonedPanel.increasedAcc=increasedAcc;
		clonedPanel.thick=thick;
		clonedPanel.accuracy=accuracy;
		clonedPanel.displayAxisValues=displayAxisValues;
		clonedPanel.displayGrid=displayGrid;
		clonedPanel.AutoAccuracy=AutoAccuracy;
		return clonedPanel;
	}
	
	public void update(Graphics g){
		paintComponent(g);
	}

	public void mousePressed(MouseEvent me){
		mPx=me.getX(); mPy=me.getY();
	
	}
	public void mouseDragged(MouseEvent me){
		divX-=(mPx-me.getX())/5;
		divY-=(mPy-me.getY())/5;

		repaint();
	}
	public void mouseExited( MouseEvent me)  {}
	public void mouseMoved   (MouseEvent e) {} 
   	public void mouseEntered (MouseEvent e) {}  
   	public void mouseClicked (MouseEvent e) {}  
   	public void mouseReleased(MouseEvent e) {}  
	public void mouseWheelMoved(MouseWheelEvent mwe){
	int inc=mwe.getWheelRotation();
		if (inc<0) zoom+=zoom/10;
		if (inc>0) zoom-=zoom/10;
		divY+=divY/zoom*inc;
		divX+=divX/zoom*inc;
		repaint();
	}
}
