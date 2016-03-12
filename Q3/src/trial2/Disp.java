package trial2;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.RenderingHints;
import java.awt.Graphics2D;
import java.awt.Transparency;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.awt.image.BufferedImage;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
//TODO repaint revalidate
//serialise the images
public class Disp extends JFrame implements Serializable{

	private transient JPanel panel;
	private transient List<JLabel> labels= new ArrayList<JLabel>();
	private List<String> names= new ArrayList<String>();
	private List<ImageIcon> images=new ArrayList<ImageIcon>();
	private List<String> anots=new ArrayList<String>();
	private transient JLabel label;
	private transient int index;
	
	/**
	 * Launch the application.
	 */
	//600*800
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Disp t = new Disp();
					t.setVisible(false);
					t.myread();
					int x=t.askArea();
					
					t.work(x);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Disp() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setBounds(100,100,900, 640);
		//panel = new JPanel();
		//panel.setBorder(new EmptyBorder(5, 5, 5, 5));
		//setContentPane(panel);
		//panel.setLayout(new BorderLayout(0, 0));
		
	}
	public int askArea(){
		Object[] options={"Load Image","View Images","Exit"};
        int choice=-1;
        choice=JOptionPane.showOptionDialog(null, 
                "What do you want to do?", "Choose", 
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);
        if(choice == -1 || choice==2)
            System.exit(0);
        return choice;
	}
	
	public void work(int x){
		if(x==0){
			load_image();
		}
		else if(x==1){
			display_image();
		}
		else if(x==2);
		mywrite();
	}
	
	@SuppressWarnings("unchecked")
	public void myread(){
    	File f= new File("14CS10045_A2_Q3.ser");
        if(f.exists() && !f.isDirectory()){
        	try{
            	FileInputStream fin = new FileInputStream(f);
            	ObjectInputStream in = new ObjectInputStream(fin);
            	images=(ArrayList<ImageIcon>)in.readObject();
            	names=(ArrayList<String>)in.readObject();
            	anots=(ArrayList<String>)in.readObject();
            	for(ImageIcon image:images){
            		JLabel t=new JLabel(image);
            		t.setBounds(140, 70, 600, 300);
        			labels.add(t);
            	}
            	in.close();
            	fin.close();
        	}catch(IOException e){
            	e.printStackTrace();
        	}catch(ClassNotFoundException c){
            	c.printStackTrace();
        	}
        }
    }
    public void mywrite(){
    	try{
            FileOutputStream fout = new FileOutputStream("14CS10045_A2_Q3.ser");
            ObjectOutputStream out = new ObjectOutputStream(fout);
            out.writeObject(images);    
            out.writeObject(names);    
            out.writeObject(anots);
            out.flush();
            out.close();
            fout.close();
        }    
        catch(IOException ex){
            System.out.println("images not  saved");
            ex.printStackTrace();
        }
    }
	public void load_image(){
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "jpg", "png", "gif", "jpeg");

		JFileChooser choice=new JFileChooser();
		choice.setFileFilter(filter);

		int option=choice.showOpenDialog(this);
		File f=null;
		if(option==JFileChooser.APPROVE_OPTION){
				f=choice.getSelectedFile();
		}
		String name=JOptionPane.showInputDialog(null,"Enter NAME of Image");
		
		try {
			BufferedImage myPic=ImageIO.read(f);
			BufferedImage scaled = getScaledInstance(
		            myPic,600, 300, RenderingHints.VALUE_INTERPOLATION_BILINEAR, true);
			//label=new JLabel();
			//label.setHorizontalAlignment(JLabel.CENTER);
			//label.setVerticalAlignment(JLabel.CENTER);
			JLabel label1=new JLabel(new ImageIcon(scaled));
			images.add(new ImageIcon(scaled));
			label1.setHorizontalAlignment(JLabel.CENTER);
			label1.setVerticalAlignment(JLabel.CENTER);
			label1.setBounds(140, 70, 600, 300);
			labels.add(label1);
			names.add(name);
			//mywrite();02
			askAnot();
			//work(askArea());
		} catch (IOException e) {
			work(askArea());
			//e.printStackTrace();
		}
	}
	public void askAnot(){
		setSize(450,300);
		JTextArea ip=new JTextArea(15,12);
		
		ip.setWrapStyleWord(true);
		ip.setLineWrap(true);
		ip.setFont(new Font("Centaur", Font.PLAIN, 20));
		JButton b=new JButton("DONE !!");
		JLabel l=new JLabel("Enter annotation");
		panel= new JPanel();
		panel.add(l,BorderLayout.NORTH);
		panel.add(ip);
		panel.add(b,BorderLayout.SOUTH);
		setContentPane(panel);
		setVisible(true);
		b.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String k= ip.getText();
				anots.add(k);
				mywrite();
				//myread();
				setVisible(false);
				
				work(askArea());
			}
		});
		
	}
	public static BufferedImage getScaledInstance(
        BufferedImage img, int targetWidth,
        int targetHeight, Object hint, 
        boolean higherQuality){
        int type =
            (img.getTransparency() == Transparency.OPAQUE)
            ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
        BufferedImage ret = (BufferedImage) img;
        double w=img.getWidth(), h=img.getHeight();
        double d1=(double)w/targetWidth;
        double d2=(double)h/targetHeight;
        double d=(double)w/h;
        if(d1>d2 && w>=targetWidth){
        	w = targetWidth;
        	h=	targetWidth/d;
        }	
        if(d2>=d1 && h>=targetHeight){
        	h = targetHeight;
        	w=targetWidth*d;
		}	
        int W=(int)w;
        int H=(int)h;
        BufferedImage tmp = new BufferedImage(W, H, type);
        Graphics2D g2 = tmp.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, hint);
        g2.drawImage(ret, 0, 0, W, H, null);
        g2.dispose();

        ret = tmp;
        return ret;
    }
	public int choose_image(){
		JComboBox combo=new JComboBox(names.toArray());
		String[] options={"OK","Cancel"};
		int selection=JOptionPane.showOptionDialog(null, 
                combo,
                "Choose Photo to view", 
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,options[0]);
		if(selection==0){
			return (int)combo.getSelectedIndex();	
		}
		else{
			return -1;
		}
	}
	public void display_image(){
		setSize(900,640);
		int x=choose_image();
		index=x;
		if(x==-1){
			work(askArea());
		}
		else{
			panel=new JPanel();
			panel.setBorder(new EmptyBorder(5, 5, 5, 5));
			JButton left=new JButton("<");
			left.setBounds(30, 210, 70, 25);
			JButton right=new JButton(">");
			right.setBounds(760, 210, 70, 25);
			JButton exit=new JButton("Exit");
			exit.setBounds(400, 530, 100, 25);
			
			panel.setLayout(null);
			
			JTextArea text=new JTextArea();
			text.setBounds(220, 430, 450, 90);
			text.setText(anots.get(index));
			text.setWrapStyleWord(true);
			text.setLineWrap(true);
			text.setFont(new Font("Centaur", Font.PLAIN, 20));
			panel.add(text);
			
			
			panel.add(exit);
			
			panel.add(labels.get(index));
			
			JLabel title = new JLabel(names.get(index));
			title.setFont(new Font("AR JULIAN", Font.PLAIN, 28));
			title.setBounds(370, 13, 175, 35);
			
			panel.add(title);
			panel.add(left);
			panel.add(right);
			
			setVisible(true);
			setContentPane(panel);
			left.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					panel.remove(labels.get(index));
					if(index>0)
						index--;
					else
						index=labels.size() - 1;
						
					panel.revalidate();
					panel.repaint();
					panel.add(labels.get(index));
					text.setText(anots.get(index));
					title.setText(names.get(index));
						
				}
			});
			//TODO- outofbounds exception
			right.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					panel.remove(labels.get(index));
					if(index<(labels.size() - 1))
						index++;
					else
						index=0;
						
					panel.revalidate();
					panel.repaint();
					
					//System.out.println(index +"  mc \t" + labels.size());
					panel.add(labels.get(index));
					text.setText(anots.get(index));
					title.setText(names.get(index));	
				}
			});
			exit.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					setVisible(false);
					work(askArea());
				}
			});	
		}
	}
}