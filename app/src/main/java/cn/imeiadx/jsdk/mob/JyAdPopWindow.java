/**
 * 
 */
package cn.imeiadx.jsdk.mob;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

/**
 * @author Neo(starneo@gmail.com)2014-10-27
 *
 */
public class JyAdPopWindow
{
	private PopupWindow popwin = null;
	private Activity act;
	private JyAdListener listener = null;

	/**
	 * @param listener the listener to set
	 */
	public void setListener(JyAdListener listener)
	{
		this.listener = listener;
	}

	public JyAdPopWindow(Activity act, String placeid, int width, int height)
	{
		this(act,placeid,width,height,null,null);
	}
	
	public JyAdPopWindow(Activity act, String placeid, int width, int height, JyJS jse)
	{
		this(act,  placeid,  width,  height, jse,null);
	}
	
	/**
	 * @return the act
	 */
	public Activity getActivity()
	{
		return act;
	}

	public JyAdPopWindow(Activity act, String placeid, int width, int height, JyJS jse, Drawable background)
	{
		this.act = act;
		RelativeLayout tp = new RelativeLayout(act);

		popwin = new PopupWindow(tp);
		
		if (jse == null)
		{
			jse = new JyJS();
		}
		jse.setJyAdPopwin(this);
		
		JyAdView wv = new JyAdView(act,act, placeid,JyAd.POP_AD, width, height,jse);
		RelativeLayout.LayoutParams wvl = new RelativeLayout.LayoutParams(width,height);
		wvl.addRule(RelativeLayout.CENTER_IN_PARENT);
        wv.setLayoutParams(wvl);
        
        RelativeLayout.LayoutParams tpl = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        tpl.addRule(RelativeLayout.CENTER_IN_PARENT);
        tp.setLayoutParams(tpl);
		
        tp.addView(wv);
        wv.loadAd();
        //wv.loadUrl("http://woso100.com/jstest.html");
        
		//popwin.setBackgroundDrawable(new ColorDrawable(Color.rgb(255,0,0)));

        if (background == null)
        {
            popwin.setBackgroundDrawable(new ColorDrawable(0));
            popwin.setFocusable(false);
    		popwin.setWidth(width);
    		popwin.setHeight(height);
        }
        else
        {
            popwin.setBackgroundDrawable(background);
            popwin.setFocusable(true);
    		popwin.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
    		popwin.setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
        }
        
		popwin.setTouchable(true);
		popwin.setOutsideTouchable(false); //false时点击外面不会被关闭

		popwin.showAtLocation(act.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
		
		popwin.update(); 
		
		popwin.setBackgroundDrawable(new ColorDrawable(Color.rgb(255,0,0)));
	}
	
	public void dismiss()
	{
		popwin.dismiss();
		if (listener != null)
		{
			listener.onClosed();
		}
	}
}