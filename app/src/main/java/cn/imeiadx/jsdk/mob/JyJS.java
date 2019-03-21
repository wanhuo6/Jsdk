package cn.imeiadx.jsdk.mob;

import android.webkit.JavascriptInterface;

public class JyJS 
{
	private JyAdPopWindow popwin;
	
	@JavascriptInterface
	public void adcallback()
	{
		WLog.d("adcallback");
	}
	
	@JavascriptInterface
	public void closepop()
	{
		WLog.d("closepop"+popwin);
		
		if (popwin != null && popwin.getActivity()!=null)
		{
			popwin.getActivity().runOnUiThread(new Runnable()
			{
				@Override
				public void run()
				{
					popwin.dismiss();
				}
			});
		}
	}

	/**
	 * @param popwin the popwin to set
	 */
	public void setJyAdPopwin(JyAdPopWindow popwin)
	{
		this.popwin = popwin;
	}
}
