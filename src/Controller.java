import java.awt.Canvas;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Controller extends JFrame{
	
	int width=0,height=0,length=200;
	
	private GridLayout layout;
	private Container container;
	private JPanel panel;
	private JScrollPane scrollPane;
	
	//private Color min_color,max_color;
	//DrawingCanvas min_canvas,max_canvas;
	//JSlider smooth_slider,threshold_slider,canny_slider;
	//JSlider h_slider,s_slider,v_slider,hx_slider,sx_slider,vx_slider;
	//JSlider min_rad_slider,max_rad_slider;
	JSlider rect_x_slider,rect_y_slider,rect_length_slider;
	
	public Controller(int x,int y){
		super("Controller");
		
		width=x;
		height=y;

		layout = new GridLayout(10,2,10,5);
		container = getContentPane();
		panel=new JPanel();
		setLayout( layout );
		scrollPane=new JScrollPane();
		
		/*minimum color panel
		min_color=Color.getHSBColor(0, 0, 0);
		min_canvas=new DrawingCanvas();
		
		//maximum color panel
		max_color=Color.getHSBColor(0, 0, 0);
		max_canvas=new DrawingCanvas();
		
		//minimum hsv start
		h_slider=getSlider(0,360,83,60,10);
		s_slider=getSlider(0,255,124,50,10);
		v_slider=getSlider(0,255,0,50,10);
		//minimum hsv end
		
		//maximum hsv start
		hx_slider=getSlider(0,360,179,60,10);
		sx_slider=getSlider(0,255,255,50,10);
		vx_slider=getSlider(0,255,255,50,10);
		//maximum hsv end
		
		//Radius
		min_rad_slider=getSlider(0, 1000, 15, 200, 50);
		max_rad_slider=getSlider(0, 2000, 1000, 500, 50);*/
		
		//canny threshold
		//canny_slider=getSlider(0, 255, 255, 50, 5);
		
		//Rect Properties
		rect_x_slider=getSlider(0, width, 0, 30, 10);
		rect_y_slider=getSlider(0, height, 0, 30, 10);
		rect_length_slider=getSlider(0, length, 50, 50, 10);
		
		/*add(new JLabel("Minimum HSV"));
		add(new JLabel("Maximum HSV"));
		add(h_slider);
		add(hx_slider);
		add(s_slider);
		add(sx_slider);
		add(v_slider);
		add(vx_slider);
		add(min_canvas);
		add(max_canvas);
		add(new JLabel("Minimum Radius"));
		add(min_rad_slider);
		add(new JLabel("Maximum Radius"));
		add(max_rad_slider);
		add(new JLabel("Canny Threshold"));
		add(canny_slider);*/
		add(new JLabel("Rectangle X Position"));
		add(rect_x_slider);
		add(new JLabel("Rectangle Y Position"));
		add(rect_y_slider);
		add(new JLabel("Rectangle Length"));
		add(rect_length_slider);
		
	}
	
	public JSlider getSlider(int min, int max, int initVal, int majorTikSpace, int minorTikSpace) {
	    JSlider slider = new JSlider(JSlider.HORIZONTAL, min, max, initVal);
	    slider.setPaintTicks(true);
	    slider.setMajorTickSpacing(majorTikSpace);
	    slider.setMinorTickSpacing(minorTikSpace);
	    slider.setPaintLabels(true);
	    slider.addChangeListener(new SliderListener());
	    
	    return slider;
	    }

	class SliderListener implements ChangeListener {
	    public void stateChanged(ChangeEvent e) {
	      JSlider slider = (JSlider) e.getSource();

	      if(slider == rect_x_slider){
	    	  Main.rect_x=slider.getValue();
	      } else if (slider == rect_y_slider) {
	    	  Main.rect_y=slider.getValue();
	      } else if (slider == rect_length_slider) {
	    	  Main.rect_length=slider.getValue();
	      }/*else if (slider == h_slider) {
	    	  ColorFilter.minc_h=slider.getValue();
	      } else if (slider == s_slider) {
	    	  ColorFilter.minc_s=slider.getValue();
	      } else if (slider == v_slider) {
	    	  ColorFilter.minc_v=slider.getValue();
	      } else if (slider == hx_slider) {
	    	  ColorFilter.maxc_h=slider.getValue();
	      } else if (slider == sx_slider) {
	    	  ColorFilter.maxc_s=slider.getValue();
	      } else if (slider == vx_slider) {
	    	  ColorFilter.maxc_v=slider.getValue();
	      }else if (slider == min_rad_slider) {
	    	  Main.imgProcs.min_radius=slider.getValue();
	      }else if (slider == max_rad_slider) {
	    	  Main.imgProcs.max_radius=slider.getValue();
	      }else if (slider == canny_slider) {
	    	  Main.imageProcessing.canny_threshold=slider.getValue();
	      }

		  min_canvas.setBackgroundColor();
	      max_canvas.setBackgroundColor();
	      min_canvas.repaint();
	      max_canvas.repaint();*/
	    }
	}
	
	/*class DrawingCanvas extends Canvas {
	    Color color;
	    int h_value=0, s_value=0, v_value=0, color_val=0;
	    int r_value=0, g_value=0, b_value=0,alpha_value=255;
	    
	    public DrawingCanvas() {
	      setSize(100, 30);
	      color = Color.getHSBColor(h_value, s_value, v_value);
	      setBackgroundColor();
	    }

	    public void setBackgroundColor() {
	    	color_val=Color.HSBtoRGB(h_value, s_value, v_value);
	    	r_value = (color_val>>16)&0xFF;
	    	g_value = (color_val>>8)&0xFF;
	    	b_value = color_val&0xFF;
		    setBackground(new Color(r_value, g_value, b_value));
		    //System.out.println(r_value+" "+g_value+" "+b_value);
	    }
	  }*/

}