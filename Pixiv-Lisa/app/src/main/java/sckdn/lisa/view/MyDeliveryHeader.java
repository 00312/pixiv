package sckdn.lisa.view;

import android.content.Context;
import com.scwang.smartrefresh.header.DeliveryHeader;
import sckdn.lisa.R;
import sckdn.lisa.activities.Lisa;

public class MyDeliveryHeader extends DeliveryHeader{


    // run one time to change default color
    static{
        cloudColors[0] = Lisa.getContext().getResources().getColor(R.color.delivery_header_cloud);
    }


    public static void changeCloudColor(Context context){
        cloudColors[0] = context.getResources().getColor(R.color.delivery_header_cloud);
    }

    public MyDeliveryHeader(Context context) {
        super(context);
    }
}
